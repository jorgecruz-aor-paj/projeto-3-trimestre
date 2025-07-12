package aor.proj3t.exceptions.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.APIResponseDTO;
import aor.proj3t.exceptions.JwtConfigurationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

/**
 * Exception mapper for JwtConfigurationException.
 * Maps JWT configuration errors to HTTP 500 Internal Server Error responses.
 */
@Provider
public class JwtConfigurationExceptionMapper implements ExceptionMapper<JwtConfigurationException> {
    private static final Logger logger = LogManager.getLogger(JwtConfigurationExceptionMapper.class);

    @Override
    public Response toResponse(JwtConfigurationException exception) {
        logger.error("JWT configuration error: {}", exception.getMessage());

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new APIResponseDTO(exception.getUserMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
