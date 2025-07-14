

package aor.proj3t.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserResourceFullIt {
    private static final String BASE_URI = "http://localhost:8080/jorgeosorio-proj3t/api";
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/proj3t_db";
    private static final String DB_USER = "proj3t_user";
    private static final String DB_PASS = "proj3t_password";
    private static String testAdminToken;
    private static String testManagerToken;
    private static String testUserToken;
    private static String testPassword;
    private static String testAdminEmail;
    private static String testManagerEmail;
    private static String testUserEmail;
    private static String fullAdminInfo;
    private static String fullUserInfo;
    private static Long userId = 1002L;


    @BeforeAll
    public static void setup() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = 8080;
        testAdminEmail = "testinojohn@example.com";
        testPassword = "Password123!";
        testManagerEmail = "managerino@example.com";
        testUserEmail = "userinochuck@example.com";
    }


    @Test
    @Order(1)
    @DisplayName("Add test user to DB")
    public void addTestUser() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            String sql = "INSERT INTO public.users (id, address, biography, birth_date, created_at, email, firstname, lastname, password, phonenumber, photograph, role, manager_id, workplace_id) VALUES " +
                    "('1000', 'test address', 'Just an admin dude for testing', '2001-01-01', '2021-01-01 01:01:01.000001+00', 'testinojohn@example.com', 'Testino', 'John', '$2a$12$udPCK2gZFz7G/YUBkg8xzu1YEabnrv.3el.wGEv1D0tw4CqXekdfO', '111111111', '', 'ADMIN', null, '1')," +
                    "('1001', 'test address', 'Just a manager dude for testing', '2001-01-01', '2021-01-01 01:01:01.000001+00', 'managerino@example.com', 'Managerino', 'Jane', '$2a$12$udPCK2gZFz7G/YUBkg8xzu1YEabnrv.3el.wGEv1D0tw4CqXekdfO', '222222222', '', 'MANAGER', null, '1')," +
                    "('1002', 'test address', 'Just a regular dude for testing', '2001-01-01', '2021-01-01 01:01:01.000001+00', 'userinochuck@example.com', 'Userino', 'Chuck', '$2a$12$udPCK2gZFz7G/YUBkg8xzu1YEabnrv.3el.wGEv1D0tw4CqXekdfO', '333333333', '', 'USER', null, '1');";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.executeUpdate();
            }
        }
    }


    @Test
    @Order(2)
    @DisplayName("Login with test admin and token extraction")
    public void loginWithTestAdmin() {
        testAdminToken =
                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body("{" +
                                "\"email\": \"" + testAdminEmail + "\"," +
                                "\"password\": \"" + testPassword + "\"}")
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract().asString();
        Assertions.assertNotNull(testAdminToken, "Session token should not be null");
    }

    @Test
    @Order(2)
    @DisplayName("Login with test manager and token extraction")
    public void loginWithTestManager() {
        testManagerToken =
                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body("{" +
                                "\"email\": \"" + testManagerEmail + "\"," +
                                "\"password\": \"" + testPassword + "\"}")
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract().asString();
        Assertions.assertNotNull(testManagerToken, "Session token should not be null");

    }

    @Test
    @Order(2)
    @DisplayName("Login with test user and token extraction")
    public void loginWithTestUser() {
        testUserToken =
                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body("{" +
                                "\"email\": \"" + testUserEmail + "\"," +
                                "\"password\": \"" + testPassword + "\"}")
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract().asString();
        Assertions.assertNotNull(testUserToken, "Session token should not be null");
    }


    @Test
    @Order(3)
    @DisplayName("Get current user profile (GET /users/me)")
    public void testGetMe() {
        fullAdminInfo =
                given()
                        .header("Authorization", "Bearer " + testAdminToken)
                        .accept(ContentType.JSON)
                        .when()
                        .get("/users/me")
                        .then()
                        .statusCode(200)
                        .extract().asString();
    }

    @Test
    @Order(3)
    @DisplayName("Get current user profile")
    public void testGetUsersFilters() {
        fullUserInfo =
                given()
                        .header("Authorization", "Bearer " + testAdminToken)
                        .accept(ContentType.JSON)
                        .queryParam("id", userId)
                        .when()
                        .get("/users")
                        .then()
                        .statusCode(200)
                        .extract().asString();
    }

    @Test
    @Order(4)
    @DisplayName("Get users with filters (GET /users)")
    public void testGetUsers() {
        given()
                .header("Authorization", "Bearer " + testAdminToken)
                .accept(ContentType.JSON)
                .queryParam("role", "MANAGER")
                .when()
                .get("/users")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    @Order(5)
    @DisplayName("Admin update own profile")
    public void testUpdateUser() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode userNode = (ObjectNode) mapper.readTree(fullAdminInfo);
            userNode.put("firstName", "UpdatedName");
            String userUpdateJson = userNode.toString();
            given()
                    .header("Authorization", "Bearer " + testAdminToken)
                    .contentType(ContentType.JSON)
                    .body(userUpdateJson)
                    .when()
                    .put("/users")
                    .then()
                    .statusCode(200)
                    .body("firstName", equalTo("UpdatedName"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(6)
    @DisplayName("Assign manager in bulk")
    public void testAssignManagerBulk() {
        String assignJson = "{\"managerId\": 1001, \"userIds\": [1002]}";
        given()
                .header("Authorization", "Bearer " + testAdminToken)
                .contentType(ContentType.JSON)
                .body(assignJson)
                .when()
                .patch("/users")
                .then()
                .statusCode(200)
                .body("message", containsString("Managers assigned successfully"));
    }

    @Test
    @Order(7)
    @DisplayName("Get managers")
    public void testGetManagers() {
        given()
                .header("Authorization", "Bearer " + testAdminToken)
                .accept(ContentType.JSON)
                .when()
                .get("/users/managers")
                .then()
                .statusCode(200)
                .body("size()", greaterThanOrEqualTo(0));
    }

    @Test
    @Order(8)
    @DisplayName("Get compact users")
    public void testGetCompactUsers() {
        given()
                .accept(ContentType.JSON)
                .header("Authorization", "Bearer " + testAdminToken)
                .queryParam("includeAdmins", true)
                .when()
                .get("/users/compact")
                .then()
                .statusCode(200)
                .body("email", hasItem("testinojohn@example.com"));
    }


    @Test
    @Order(9)
    @DisplayName("Assign admin as someone's manager should fail")
    public void testAssignAdminAsManagerForbidden() {
        String assignJson = "{\"managerId\": 1000, \"userIds\": [1002]}";
        given()
                .header("Authorization", "Bearer " + testAdminToken)
                .contentType(ContentType.JSON)
                .body(assignJson)
                .when()
                .patch("/users")
                .then()
                .statusCode(403);
    }

    @Test
    @Order(10)
    @DisplayName("Assign user as their own manager should fail")
    public void testAssignSelfAsManagerForbidden() {
        String assignJson = "{\"managerId\": 1002, \"userIds\": [1002]}";
        given()
                .header("Authorization", "Bearer " + testAdminToken)
                .contentType(ContentType.JSON)
                .body(assignJson)
                .when()
                .patch("/users")
                .then()
                .statusCode(403);
    }

    @Test
    @Order(11)
    @DisplayName("Assign manager for non-existent user should fail")
    public void testAssignManagerNonExistentUser() {
        String assignJson = "{\"managerId\": 1001, \"userIds\": [9999]}";
        given()
                .header("Authorization", "Bearer " + testAdminToken)
                .contentType(ContentType.JSON)
                .body(assignJson)
                .when()
                .patch("/users")
                .then()
                .statusCode(404);
    }


    @Test
    @Order(12)
    @DisplayName("Regular user updating admin profile should fail")
    public void testUpdateOtherUserForbidden() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode userNode = (ObjectNode) mapper.readTree(fullAdminInfo);
            userNode.put("firstName", "UpdatedNameAgain");
            String userUpdateJson = userNode.toString();
            given()
                    .header("Authorization", "Bearer " + testUserToken)
                    .contentType(ContentType.JSON)
                    .body(userUpdateJson)
                    .when()
                    .put("/users")
                    .then()
                    .statusCode(403);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(13)
    @DisplayName("Admin updating regular user profile should succeed")
    public void testAdminUpdateOtherUserSucceeds() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode arrayNode = (ArrayNode) mapper.readTree(fullUserInfo);
            ObjectNode userNode = (ObjectNode) arrayNode.get(0);
            userNode.put("lastName", "UpdatedLastName");
            String userUpdateJson = userNode.toString();
            given()
                    .header("Authorization", "Bearer " + testAdminToken)
                    .contentType(ContentType.JSON)
                    .body(userUpdateJson)
                    .when()
                    .put("/users")
                    .then()
                    .statusCode(200)
                    .body("lastName", equalTo("UpdatedLastName"));

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(14)
    @DisplayName("Manager updating managed user profile should fail")
    public void testManagerUpdatingManagedUserFails() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            ArrayNode arrayNode = (ArrayNode) mapper.readTree(fullUserInfo);
            ObjectNode userNode = (ObjectNode) arrayNode.get(0);
            userNode.put("lastName", "UpdatedLastName");
            String userUpdateJson = userNode.toString();
            given()
                    .header("Authorization", "Bearer " + testManagerToken)
                    .contentType(ContentType.JSON)
                    .body(userUpdateJson)
                    .when()
                    .put("/users")
                    .then()
                    .statusCode(403);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @Order(15)
    @DisplayName("Assign manager as non-admin should fail")
    public void testAssignManagerForbidden() {
        // Try to assign manager as managerId=1001 (not admin)
        String sessionToken =
                given()
                        .contentType(ContentType.JSON)
                        .accept(ContentType.JSON)
                        .body("{" +
                                "\"email\": \"" + testManagerEmail + "\"," +
                                "\"password\": \"" + testPassword + "\"}")
                        .when()
                        .post("/auth/login")
                        .then()
                        .statusCode(200)
                        .extract().asString();
        String assignJson = "{\"managerId\": 1001, \"userIds\": [1002]}";
        given()
                .header("Authorization", "Bearer " + sessionToken)
                .contentType(ContentType.JSON)
                .body(assignJson)
                .when()
                .patch("/users")
                .then()
                .statusCode(403);
    }


    @Test
    @Order(99)
    @DisplayName("Cleanup: Delete test user from database")
    public void cleanupDeleteTestUser() throws Exception {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // Delete from sessions table

            for (int i = 1003; i >= 1000; i--) {
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM session WHERE user_id = ?")) {
                    stmt.setLong(1, i);
                    stmt.executeUpdate();
                }
            }

            for (int i = 1003; i >= 1000; i--) {
                try (PreparedStatement stmt = conn.prepareStatement("DELETE FROM users WHERE id = ?")) {
                    stmt.setLong(1, i);
                    stmt.executeUpdate();
                }
            }


            // Add more deletes for related tables if needed
        }
    }
}
