package aor.proj3t.service.impl;

import java.util.Optional;

import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.NotFoundException;

/**
 * Abstract base service class providing common functionality for all service
 * implementations.
 * This class contains utility methods that are frequently used across different
 * service layers,
 * including entity validation and authorization checks.
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 2025
 */
public abstract class BaseService {

    /**
     * Retrieves an entity from an Optional or throws a NotFoundException if not
     * present.
     * This is a utility method to handle the common pattern of extracting entities
     * from repository Optional returns and providing meaningful error messages.
     * 
     * @param <T>       the type of the entity
     * @param entityOpt the Optional containing the entity, typically returned from
     *                  a DAO method
     * @param id        the ID of the entity being searched for (used in error
     *                  messages)
     * @param fieldName the name of the entity type (used in error messages)
     * @return the entity if present in the Optional
     * @throws NotFoundException if the Optional is empty
     */
    protected <T> T getEntity(Optional<T> entityOpt, Long id, String fieldName) {
        return entityOpt.orElseThrow(
                () -> new NotFoundException(fieldName + " not found with ID: " + id));
    }

    /**
     * Verifies that the requesting user has administrator privileges.
     * This method is used to enforce role-based access control for administrative
     * operations.
     * 
     * @param user the user making the request
     * @throws ForbiddenException if the user does not have ADMIN role
     */
    protected void checkIfRequesterIsAdmin(User user) {
        if (!user.getRole().equals(Role.ADMIN)) {
            throw new ForbiddenException(
                    "Permission denied",
                    "Non admin user with id " + user.getId() + " tried to start evaluation cycle");
        }
    }

}
