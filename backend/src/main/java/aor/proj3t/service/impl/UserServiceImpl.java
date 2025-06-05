package aor.proj3t.service.impl;

import aor.proj3t.daos.impl.PendingRegistrationDAOimpl;
import aor.proj3t.daos.impl.SettingsDAOimpl;
import aor.proj3t.dto.LoginDTO;
import aor.proj3t.entities.PendingRegistration;
import aor.proj3t.entities.Settings;
import aor.proj3t.util.JwtUtil;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

public class UserServiceImpl {
    private static final int DEFAULT_CONFIRMATION_WINDOW = 60;
    private static final Logger log = LogManager.getLogger(UserServiceImpl.class);
    @Inject
    JwtUtil jwtUtil;

    @Inject
    SettingsDAOimpl settingsDAO;

    @Inject
    PendingRegistrationDAOimpl pendingRegistrationDAO;

    public Boolean registerUser(LoginDTO loginDTO, String ipAddress) {
        try {
            PendingRegistration registration = new PendingRegistration(loginDTO.getEmail(), loginDTO.getPassword());
            registration.setConfirmationToken(jwtUtil.generateConfirmationToken(loginDTO.getEmail(), ipAddress, getConfirmationExpirationTime()));
            registration.setExpirationDate(Instant.now().plus(getConfirmationExpirationTime(), ChronoUnit.MINUTES));
            pendingRegistrationDAO.create(registration);
            return true;
        } catch (Exception e) {
            log.info("Error registering user with email {}. Message: {}", loginDTO.getEmail(), e.getMessage());
            //log.info(e.getMessage());
            return false;
        }
    }

    private Integer getConfirmationExpirationTime(){
        Optional<Settings> settingsOpt = settingsDAO.getLatestSettings();
        Settings settings;
        if (settingsOpt.isPresent()) {
            settings = settingsOpt.get();
            return settings.getConfirmationExpirationTime();
        } else {
            return DEFAULT_CONFIRMATION_WINDOW;
        }
    }
}
