package aor.proj3t.exceptions.mapper;

import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.resource.MessageResponse;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    private static final Logger logger = LogManager.getLogger(NotFoundExceptionMapper.class);

    @Override
    public Response toResponse(NotFoundException e) {
        logger.warn("Resource not found: {}", e.getMessage()); // Developer message
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new MessageResponse(e.getUserMessage())) // Safe for client
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
