package aor.proj3t.exceptions.mapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.APIResponseDTO;
import aor.proj3t.exceptions.BadRequestException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class BadRequestExceptionMapper implements ExceptionMapper<BadRequestException> {
    private static final Logger logger = LogManager.getLogger(BadRequestExceptionMapper.class);

    @Override
    public Response toResponse(BadRequestException e) {
        logger.warn("Bad request: {}", e.getMessage()); // internal/dev message
        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new APIResponseDTO(e.getUserMessage())) // safe user-facing message
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}