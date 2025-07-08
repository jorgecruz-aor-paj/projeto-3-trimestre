package aor.proj3t.service.interfaces;

import aor.proj3t.entities.User;
import aor.proj3t.exceptions.ForbiddenException;

public interface AuthorizationService {

    /**
     * Checks if the requester can view evaluations with the specified filter
     * parameters
     * 
     * @param requester   The user making the request
     * @param evaluatedId The ID of the user being evaluated (can be null)
     * @param evaluatorId The ID of the evaluator (can be null)
     * @throws ForbiddenException if access is denied
     */
    void authorizeEvaluationAccess(User requester, Long evaluatedId, Long evaluatorId);

}
