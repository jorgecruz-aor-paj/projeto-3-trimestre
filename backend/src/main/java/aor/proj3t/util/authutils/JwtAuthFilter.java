package aor.proj3t.util.authutils;

import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.service.interfaces.AuthService;
import aor.proj3t.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.container.ResourceInfo;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

import java.util.Arrays;


@Provider
@Secured
public class JwtAuthFilter implements ContainerRequestFilter {

    @Context
    private ResourceInfo resourceInfo;

    @Inject
    JwtUtil jwtUtil;

    @Inject
    AuthService authService;

    @Override
    public void filter(ContainerRequestContext requestContext) {

        String authHeader = requestContext.getHeaderString("Authorization");
        Role role = authService.getRoleFromAuthHeader(authHeader);
        Long userId = authService.getUserIdFromAuthHeader(authHeader);
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
