package aor.proj3t.exceptions.mapper;

import aor.proj3t.exceptions.UnauthorizedException;
import aor.proj3t.resource.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {
    @Override
    public Response toResponse(UnauthorizedException e) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new ErrorResponse(e.getMessage()))
                .type("application/json")
                .build();
    }
}
