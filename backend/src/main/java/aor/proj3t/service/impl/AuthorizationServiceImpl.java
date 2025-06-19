package aor.proj3t.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.service.interfaces.AuthorizationService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Implementation of the AuthorizationService interface that provides role-based
 * access control (RBAC) throughout the application.
 * <p>
 * This service enforces authorization rules based on user roles:
 * <ul>
 * <li>ADMIN: Has full access to all resources in the system</li>
 * <li>USER: Has limited access to resources based on ownership and role</li>
 * <li>MANAGER: Has expanded access for resources related to their team
 * members</li>
 * </ul>
 * </p>
 * <p>
 * This implementation follows the Single Responsibility Principle by
 * centralizing
 * authorization logic separate from business logic, making it reusable across
 * different parts of the application. Additional authorization methods can be
 * added to handle different resource types and access patterns.
 * </p>
 * 
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 */
@ApplicationScoped
public class AuthorizationServiceImpl extends BaseService implements AuthorizationService {
    /**
     * Data Access Object Factory for database operations
     */
    @Inject
    DAOFactory daoFactory;

    /**
     * Authorizes a user's access to evaluations based on filter parameters and user
     * role.
     * <p>
     * Access rules:
     * <ul>
     * <li>ADMIN: Can access all evaluations regardless of parameters</li>
     * <li>USER: Can only access evaluations where they are the evaluated
     * person</li>
     * <li>MANAGER: Can access evaluations where they are the evaluator or
     * where they are the manager of the evaluated person</li>
     * </ul>
     * </p>
     *
     * @param requester   The user requesting access
     * @param evaluatedId The ID of the evaluated user in the filter (can be null)
     * @param evaluatorId The ID of the evaluator in the filter (can be null)
     * @throws ForbiddenException if the user does not have permission to access the
     *                            requested evaluations
     */
    @Override
    public void authorizeEvaluationAccess(User requester, Long evaluatedId, Long evaluatorId) {
        // Admin can see everything
        if (requester.getRole() == Role.ADMIN) {
            return;
        }

        // Regular users can only see evaluations where they are evaluated
        if (requester.getRole() == Role.USER) {
            if (evaluatedId != null && !evaluatedId.equals(requester.getId())) {
                throw new ForbiddenException("Permission denied",
                        "User with id " + requester.getId() + " cannot access evaluations for user " + evaluatedId);
            }

            if (evaluatorId != null && !isUserManagerOfRequester(evaluatorId, requester.getId())) {
                throw new ForbiddenException("Permission denied",
                        "User with id " + requester.getId() + " cannot access evaluations by evaluator " + evaluatorId);
            }
        }

        // Managers can see evaluations where they are evaluated or where they are the
        // evaluator
        if (requester.getRole() == Role.MANAGER) {
            // If evaluatedId specified, must be the requester or one of their direct
            // reports
            if (evaluatedId != null &&
                    !evaluatedId.equals(requester.getId()) &&
                    !isRequesterManagerOf(requester.getId(), evaluatedId)) {
                throw new ForbiddenException("Permission denied",
                        "Manager with id " + requester.getId() + " cannot access evaluations for user " + evaluatedId);
            }

            // If evaluatorId specified, must be the requester
            if (evaluatorId != null && !evaluatorId.equals(requester.getId())) {
                throw new ForbiddenException("Permission denied",
                        "Manager with id " + requester.getId() + " cannot access evaluations by evaluator "
                                + evaluatorId);
            }
        }
    }

    /**
     * Checks if a user has permission to view evaluations for a specific evaluated
     * person.
     * <p>
     * Access rules:
     * <ul>
     * <li>Any user can view their own evaluations</li>
     * <li>Managers can view evaluations of their direct reports</li>
     * <li>Admins can view evaluations of any user</li>
     * </ul>
     * </p>
     *
     * @param requester   The user requesting access
     * @param evaluatedId The ID of the evaluated user
     * @throws ForbiddenException if the user does not have permission to view the
     *                            evaluations
     */
    @Override
    public void canViewEvaluationsByEvaluated(User requester, Long evaluatedId) {
        // Admin can view any evaluation
        if (requester.getRole() == Role.ADMIN) {
            return;
        }

        // Anyone can view their own evaluations
        if (requester.getId().equals(evaluatedId)) {
            return;
        }

        // Managers can view evaluations of their direct reports
        if (requester.getRole() == Role.MANAGER && isRequesterManagerOf(requester.getId(), evaluatedId)) {
            return;
        }

        throw new ForbiddenException("Permission denied",
                "User with id " + requester.getId() + " cannot view evaluations of user " + evaluatedId);
    }

    /**
     * Checks if a user has permission to view evaluations created by a specific
     * evaluator.
     * <p>
     * Access rules:
     * <ul>
     * <li>Users can view evaluations they created</li>
     * <li>Admins can view evaluations created by any user</li>
     * <li>Other users cannot view evaluations by evaluators that are not
     * themselves</li>
     * </ul>
     * </p>
     *
     * @param requester   The user requesting access
     * @param evaluatorId The ID of the evaluator
     * @throws ForbiddenException if the user does not have permission to view the
     *                            evaluations
     */
    @Override
    public void canViewEvaluationsByEvaluator(User requester, Long evaluatorId) {
        // Admin can view any evaluation
        if (requester.getRole() == Role.ADMIN) {
            return;
        }

        // Users can view evaluations where they are the evaluator
        if (requester.getId().equals(evaluatorId)) {
            return;
        }

        throw new ForbiddenException("Permission denied",
                "User with id " + requester.getId() + " cannot view evaluations made by user " + evaluatorId);
    }

    /**
     * Checks if a user is the manager of another user.
     *
     * @param managerId The ID of the potential manager
     * @param userId    The ID of the user to check
     * @return true if the user with managerId is the manager of the user with
     *         userId
     */
    private boolean isRequesterManagerOf(Long managerId, Long userId) {
        User user = getEntity(daoFactory.forUser().findById(userId), userId, "User");
        return user.getManager() != null && user.getManager().getId().equals(managerId);
    }

    /**
     * Checks if a user is managed by another user.
     *
     * @param managerId   The ID of the potential manager
     * @param requesterId The ID of the user making the request
     * @return true if the user with requesterId has the user with managerId as
     *         their manager
     */
    private boolean isUserManagerOfRequester(Long managerId, Long requesterId) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        return requester.getManager() != null && requester.getManager().getId().equals(managerId);
    }
}
