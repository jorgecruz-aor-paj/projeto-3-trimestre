package aor.proj3t.util;

import java.util.regex.Pattern;

import jakarta.annotation.Resource;
import jakarta.mail.Session;

public class EmailUtil {

    @Resource(mappedName = "java:jboss/mail/Default")
    private Session mailSession;

    // Email validation pattern
    private static final Pattern EMAIL_PATTERN = Pattern
            .compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$");
}
