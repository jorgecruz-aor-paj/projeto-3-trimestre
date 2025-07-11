package aor.proj3t.service.interfaces;

import aor.proj3t.entities.User;
import aor.proj3t.exceptions.ForbiddenException;

/**
 * Service interface for role-based access control (RBAC) operations.
 * This interface defines the contract for authorization services that enforce
 * security policies and access controls throughout the application.
 * 
 * <p>
 * Key authorization services include:
 * <ul>
 * <li><b>Role-based Access:</b> Enforcing permissions based on user roles
 * (ADMIN, MANAGER, USER)</li>
 * <li><b>Resource Authorization:</b> Validating access to specific resources
 * and operations</li>
 * <li><b>Evaluation Access Control:</b> Managing who can view, edit, or approve
 * evaluations</li>
 * <li><b>Hierarchical Permissions:</b> Supporting manager-subordinate access
 * patterns</li>
 * <li><b>Centralized Security:</b> Single point for authorization logic across
 * the application</li>
 * </ul>
 * 
 * <p>
 * This service follows the Single Responsibility Principle by centralizing
 * authorization
 * logic separate from business logic, making it reusable across different
 * application components.
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 2025
 */
public interface AuthorizationService {

    /**
     * Authorizes access to evaluation data based on user roles and relationships.
     * Enforces role-based access control for evaluation viewing and management
     * operations.
     * 
     * <p>
     * Access rules:
     * <ul>
     * <li>ADMIN: Can access all evaluations regardless of parameters</li>
     * <li>MANAGER: Can access evaluations where they are the evaluator or manage
     * the evaluated user</li>
     * <li>USER: Can only access evaluations where they are the evaluated
     * person</li>
     * </ul>
     * 
     * @param requester   the User entity making the authorization request
     * @param evaluatedId the ID of the user being evaluated (can be null for
     *                    unrestricted access)
     * @param evaluatorId the ID of the evaluator (can be null for unrestricted
     *                    access)
     * @throws ForbiddenException if the requester lacks permission to access the
     *                            specified evaluation data
     */
    void authorizeEvaluationAccess(User requester, Long evaluatedId, Long evaluatorId);

}
