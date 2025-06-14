package aor.proj3t.util.authutils;

import java.util.Arrays;

import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.service.impl.TokenService;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.ext.Provider;

@Provider
@Secured
public class JwtAuthFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Inject
    JwtUtil jwtUtil;

    @Inject
    TokenService tokenService;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        String authHeader = requestContext.getHeaderString(HttpHeaders.AUTHORIZATION);
        Role role = tokenService.getRoleFromAuthHeader(authHeader);
        Long userId = tokenService.getUserIdFromAuthHeader(authHeader);
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
