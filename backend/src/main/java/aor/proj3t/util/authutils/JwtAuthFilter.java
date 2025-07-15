package aor.proj3t.util.authutils;

import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.service.impl.TokenServiceImpl;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;

import java.util.Arrays;

/**
 * Authentication filter for secured endpoints using JWT validation.
 * <p>
 * This filter intercepts requests to resources annotated with {@link Secured}
 * and enforces role-based authorization based on the provided JWT.
 * It extracts user identity and role from the token and sets a custom security context.
 * If the user does not have the appropriate role, a {@link ForbiddenException} is thrown.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Provider
@Secured
public class JwtAuthFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Inject
    private JwtUtil jwtUtil;

    @Inject
    private TokenServiceImpl tokenServiceImpl;

    /**
     * Filters incoming requests by validating JWT tokens and checking role-based access.
     * Injects a {@link CustomSecurityContext} upon successful validation.
     *
     * @param requestContext the request context for the current HTTP call
     * @throws ForbiddenException if the user lacks required roles for the endpoint
     */
    @Override
    public void filter(ContainerRequestContext requestContext) {

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        Role role = tokenServiceImpl.getRoleFromAuthHeader(authHeader);
        Long userId = tokenServiceImpl.getUserIdFromAuthHeader(authHeader);
        requestContext.setSecurityContext(new CustomSecurityContext(userId.toString(), role.getName()));

        Secured secured = resourceInfo.getResourceMethod().getAnnotation(Secured.class);
        if (secured == null) {
            secured = resourceInfo.getResourceClass().getAnnotation(Secured.class);
        }

        if (secured != null && secured.value().length > 0) {
            Role[] allowedRoles = secured.value();
            boolean allowed = Arrays.stream(allowedRoles).anyMatch(r -> r == role);
            if (!allowed) {
                throw new ForbiddenException(
                        "Permission denied",
                        "Unauthorized access attempt: userId=" + userId +
                                ", method=" + requestContext.getMethod() +
                                ", path=" + requestContext.getUriInfo().getPath()
                );
            }
        }
    }
}

