package aor.proj3t.exceptions.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.MessageResponseDTO;
import aor.proj3t.exceptions.UnauthorizedException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {
    private static final Logger logger = LogManager.getLogger(UnauthorizedExceptionMapper.class);

    @Override
    public Response toResponse(UnauthorizedException e) {
        logger.warn("Unauthorized access attempt: {}", e.getMessage()); // Internal log
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity(new MessageResponseDTO(e.getUserMessage())) // Safe message for the user
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
