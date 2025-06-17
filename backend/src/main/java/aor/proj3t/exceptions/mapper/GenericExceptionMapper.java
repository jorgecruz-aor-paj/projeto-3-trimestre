package aor.proj3t.exceptions.mapper;

import aor.proj3t.resource.ErrorResponse;
import aor.proj3t.resource.EvaluationResource;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.apache.logging.log4j.LogManager;

import java.util.logging.Logger;

@Provider
public class GenericExceptionMapper implements ExceptionMapper<Throwable> {
    private static final org.apache.logging.log4j.Logger logger = LogManager.getLogger(GenericExceptionMapper.class);
    @Override
    public Response toResponse(Throwable exception) {
       logger.error(exception.getMessage(), exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorResponse("Unexpected error occurred"))
                .type("application/json")
                .build();
    }
}
