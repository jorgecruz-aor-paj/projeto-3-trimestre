package aor.proj3t.service.impl;

import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.NotFoundException;

import java.util.Optional;

public abstract class BaseService {

    protected <T> T getEntity(Optional<T> entityOpt, Long id, String fieldName) {
        return entityOpt.orElseThrow(
                () -> new NotFoundException(fieldName + " not found with ID: " + id));
    }

    protected void checkIfRequesterIsAdmin(User user) {
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenException(
                    "Permission denied",
                    "Non admin user with id " + user.getId() + " tried to start evaluation cycle"
            );
        }
    }


}
