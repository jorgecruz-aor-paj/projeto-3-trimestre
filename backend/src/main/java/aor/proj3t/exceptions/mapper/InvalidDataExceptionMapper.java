package aor.proj3t.exceptions.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.APIResponseDTO;
import aor.proj3t.exceptions.InvalidDataException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class InvalidDataExceptionMapper implements ExceptionMapper<InvalidDataException> {
    private static final Logger logger = LogManager.getLogger(InvalidDataExceptionMapper.class);

    @Override
    public Response toResponse(InvalidDataException e) {
        logger.warn("Invalid data: {}", e.getMessage()); // Internal log
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new APIResponseDTO(e.getUserMessage())) // Safe message for client
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
