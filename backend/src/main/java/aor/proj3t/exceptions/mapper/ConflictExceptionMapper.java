package aor.proj3t.exceptions.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.APIResponseDTO;
import aor.proj3t.exceptions.ConflictException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConflictExceptionMapper implements ExceptionMapper<ConflictException> {
    private static final Logger logger = LogManager.getLogger(ConflictExceptionMapper.class);

    @Override
    public Response toResponse(ConflictException e) {
        logger.warn("Conflict detected: {}", e.getMessage()); // internal/dev message
        return Response.status(Response.Status.CONFLICT)
                .entity(new APIResponseDTO(e.getUserMessage())) // safe user-facing message
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
