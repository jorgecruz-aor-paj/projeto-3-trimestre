package aor.proj3t.config;

public final class AppConstants {

    private AppConstants() {
        // Prevent instantiation
    }

    public static final class Jwt {
        private Jwt() {
            // Prevent instantiation
        }

        public static final int DEFAULT_TOKEN_EXPIRATION_TIME = 60; // value in minutes

        public static final int DEFAULT_CONFIRMATION_TOKEN_EXPIRATION_TIME = 60; // value in minutes
    }
}
