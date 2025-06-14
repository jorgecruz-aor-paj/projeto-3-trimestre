package aor.proj3t.exceptions.mapper;

import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.resource.ErrorResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class ForbiddenExceptionMapper implements ExceptionMapper<ForbiddenException> {
    private static final Logger logger = LogManager.getLogger(ForbiddenExceptionMapper.class);

    @Override
    public Response toResponse(ForbiddenException e) {
        logger.warn("Forbidden access: {}", e.getMessage()); // logs internal/dev message
        return Response.status(Response.Status.FORBIDDEN)
                .entity(new ErrorResponse(e.getUserMessage())) // safe for client response
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}

