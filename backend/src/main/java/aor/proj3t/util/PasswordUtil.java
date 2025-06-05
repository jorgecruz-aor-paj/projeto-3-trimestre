package aor.proj3t.util;

import org.mindrot.jbcrypt.BCrypt;


public class PasswordUtil {

    public static String hashPassword(String plainText) {
        return BCrypt.hashpw(plainText, BCrypt.gensalt(10));
    }
}



