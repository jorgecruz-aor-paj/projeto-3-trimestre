package aor.proj3t.exceptions.mapper;

import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.resource.ErrorResponse;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {
    @Override
    public Response toResponse(ForbiddenException e) {
        return Response.status(Response.Status.FORBIDDEN)
                .entity(new ErrorResponse("Permission Denied"))
                .build();
    }
}
