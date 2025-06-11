package aor.proj3t.util.authutils;

import aor.proj3t.util.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.inject.Inject;
import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerRequestFilter;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.Provider;

@Provider
@Secured
public class JwtAuthFilter implements ContainerRequestFilter {

    @Inject
    JwtUtil jwtUtil;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authHeader = requestContext.getHeaderString("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).entity("Missing or invalid token").build());
            return;
        }

        String token = authHeader.substring("Bearer ".length()).trim();
        try {
            Claims claims = jwtUtil.extractAllClaims(token);
            String role = claims.get("role", String.class);
            String userId = claims.get("userId").toString();

            // Attach role and user details to security context
            requestContext.setSecurityContext(new CustomSecurityContext(userId, role));

        } catch (Exception e) {
            requestContext.abortWith(
                    Response.status(Response.Status.UNAUTHORIZED).entity("Invalid or expired token").build());
        }
    }
}
