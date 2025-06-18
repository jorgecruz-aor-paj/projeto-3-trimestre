package aor.proj3t.exceptions.mapper;

import aor.proj3t.resource.MessageResponse;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    private static final Logger logger = LogManager.getLogger(GenericExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {
        // Let JAX-RS handle NotFound and other standard exceptions
        if (exception instanceof jakarta.ws.rs.WebApplicationException) {
            return ((WebApplicationException) exception).getResponse();
        }

        logger.error("Unhandled exception", exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new MessageResponse("Unexpected error occurred"))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}

