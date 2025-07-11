package aor.proj3t.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class AuthResourceFullIT {
    private static final String BASE_URI = "http://localhost:8080/jorgeosorio-proj3t/api";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/proj3t_db";
    private static final String DB_USER = "proj3t_user";
    private static final String DB_PASS = "proj3t_password";

    private static String testEmail;
    private static String testPassword;
    private static String confirmationToken;
    private static String sessionToken;
    private static Long id;

    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = 8080;
        testEmail = "testuser_" + UUID.randomUUID() + "@example.com";
        testPassword = "Password123!";
    }

    @Test
    @Order(1)
    @DisplayName("Register user")
    public void testRegisterUser() {
        given()
                .contentType(ContentType.JSON)
                .body("{" +
                        "\"email\": \"" + testEmail + "\"," +
                        "\"password\": \"" + testPassword + "\"}")
                .when()
                .post("/auth/register")
                .then()
                .statusCode(201)
                .body("message", equalTo("User registered successfully"));
    }

    @Test
    @Order(2)
    @DisplayName("Fetch confirmation token from DB")
    public void fetchConfirmationToken() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT pending_registration.confirmation_token FROM pending_registration WHERE email = ? ORDER BY created_at DESC LIMIT 1";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, testEmail);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        confirmationToken = rs.getString("confirmation_token");
                        Assertions.assertNotNull(confirmationToken, "Confirmation token should not be null");
                    } else {
                        Assertions.fail("No confirmation token found for test user");
                    }
                }
            }
        }
    }

    @Test
    @Order(3)
    @DisplayName("Confirm user registration")
    public void testConfirmUser() {
        String registrationBody = "{" +
                "\"firstName\": \"Testino\"," +
                "\"lastName\": \"John\"," +
                "\"phoneNumber\": \"111111111\"," +
                "\"address\": \"test address\"," +
                "\"birthdate\": [2000, 1, 1]," +
                "\"biography\": \"Just a dude for testing\"," +
                "\"workplaceId\": 1" +
                "}";
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .queryParam("confirmationToken", confirmationToken)
                .body(registrationBody)
                .when()
                .post("/auth/confirm")
                .then()
                .statusCode(200)
                .body("message", equalTo("User confirmed successfully"));
    }

    @Test
    @Order(4)
    @DisplayName("Fetch user ID from DB")
    public void fetchID() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "SELECT id FROM users WHERE email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, testEmail);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                       id = rs.getLong("id");
                        Assertions.assertNotNull(id, "ID should not be null");
                    } else {
                        Assertions.fail("No ID found for test user");
                    }
                }
            }
        }
    }


    @Test
    @Order(5)
    @DisplayName("Login with confirmed user")
    public void testLoginSuccess() {
        sessionToken =
                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body("{" +
                                "\"email\": \"" + testEmail + "\"," +
                                "\"password\": \"" + testPassword + "\"}")
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract().asString();
        Assertions.assertNotNull(sessionToken, "Session token should not be null");
    }

    @Test
    @Order(6)
    @DisplayName("Login failure with wrong password")
    public void testLoginFailure() {
        given()
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body("{" +
                        "\"email\": \"" + testEmail + "\"," +
                        "\"password\": \"WrongPassword!\"}")
                .when()
                .post("/auth/login")
                .then()
                .statusCode(400);
    }

    @Test
    @Order(7)
    @DisplayName("Logout user")
    public void testLogout() {
        given()
                .header("Authorization", "Bearer " + sessionToken)
                .contentType(ContentType.ANY) // clears implicit type
                .when()
                .post("/auth/logout")
                .then()
                .statusCode(200)
                .body("message", equalTo("Logout successful"));
    }

    @Test
    @Order(8)
    @DisplayName("Register user with missing password should fail")
    public void testRegisterMissingPassword() {
        given()
            .contentType(ContentType.JSON)
            .body("{" +
                "\"email\": \"failuser_" + UUID.randomUUID() + "@example.com\"}")
        .when()
            .post("/auth/register")
        .then()
            .statusCode(400);
    }

    @Test
    @Order(9)
    @DisplayName("Confirm user with invalid token should fail")
    public void testConfirmInvalidToken() {
        String registrationBody = "{" +
            "\"firstName\": \"Fail\"," +
            "\"lastName\": \"User\"," +
            "\"phoneNumber\": \"000000000\"," +
            "\"address\": \"fail address\"," +
            "\"birthdate\": [1999, 1, 1]," +
            "\"biography\": \"Should fail\"," +
            "\"workplaceId\": 1" +
        "}";
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .queryParam("confirmationToken", "invalidtoken")
            .body(registrationBody)
        .when()
            .post("/auth/confirm")
        .then()
            .statusCode(401);
    }

    @Test
    @Order(10)
    @DisplayName("Login with missing password should fail")
    public void testLoginMissingPassword() {
        given()
            .contentType(ContentType.JSON)
            .accept(ContentType.JSON)
            .body("{" +
                "\"email\": \"" + testEmail + "\"}")
        .when()
            .post("/auth/login")
        .then()
            .statusCode(400);
    }

    @Test
    @Order(11)
    @DisplayName("Logout with invalid token should fail")
    public void testLogoutInvalidToken() {
        given()
            .header("Authorization", "Bearer invalidtoken")
            .contentType(ContentType.ANY)
        .when()
            .post("/auth/logout")
        .then()
            .statusCode(401);
    }

    @Test
    @Order(99)
    @DisplayName("Cleanup: Delete test user from database")
    public void cleanupDeleteTestUser() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // Delete from sessions table
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM session WHERE user_id = ?")) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }
            // Delete from users table
            try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }

            // Add more deletes for related tables if needed
        }
    }

    // Add more tests for reset password, etc. as needed
}
