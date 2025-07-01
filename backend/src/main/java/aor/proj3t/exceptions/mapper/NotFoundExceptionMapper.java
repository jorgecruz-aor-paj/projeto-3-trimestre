package aor.proj3t.exceptions.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.APIResponseDTO;
import aor.proj3t.exceptions.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {
    private static final Logger logger = LogManager.getLogger(NotFoundExceptionMapper.class);

    @Override
    public Response toResponse(NotFoundException e) {
        logger.warn("Resource not found: {}", e.getMessage()); // Developer message
        return Response.status(Response.Status.NOT_FOUND)
                .entity(new APIResponseDTO(e.getUserMessage())) // Safe for client
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
