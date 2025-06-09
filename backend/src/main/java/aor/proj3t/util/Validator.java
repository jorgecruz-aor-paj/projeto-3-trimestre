package aor.proj3t.util;
import aor.proj3t.dto.LoginDTO;

import java.util.regex.Pattern;


public class Validator {

    private Validator() {}

    private Boolean isValidEmail(String email) {
        if (email == null || email.isEmpty()) {
            return false;
        }
        String emailRegex = "^(?=.{1,64}@.{1,255}$)([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,})$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    public Boolean isValidPassword(String password) {
        if (password == null || password.isEmpty()) {
            return false;
        }
        String emailRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*#?&])\\S{8,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(password).matches();
    }

    public Boolean isValidLoginDTO(LoginDTO loginDTO) {
        return isValidEmail(loginDTO.getEmail()) && isValidPassword(loginDTO.getPassword());
    }


}
