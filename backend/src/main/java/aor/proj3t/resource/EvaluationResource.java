package aor.proj3t.resource;

import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.filters.EvaluationFilter;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.authutils.Secured;
import aor.proj3t.util.validatorutils.fordto.TrainingValidator;
import aor.proj3t.util.validatorutils.forfilters.FilterValidatorFactory;
import jakarta.enterprise.context.RequestScoped;
import jakarta.faces.component.html.HtmlOutputText;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

/**
 * REST endpoint for evaluation-related operations.
 * This class is responsible for handling requests related to evaluations.
 * It includes operations such as creating, updating, and retrieving
 * evaluations.
 */
@Path("/evaluations")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvaluationResource {
    private static final Logger logger = LogManager.getLogger(EvaluationResource.class);

    @Inject
    ServiceFactory serviceFactory;

    @Inject
    TrainingValidator validator;

    @Inject
    FilterValidatorFactory validatorFactory;

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvaluations(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader, @BeanParam EvaluationFilter filters) {
        validatorFactory.validate(filters);
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        List<EvaluationDTO> evaluations = serviceFactory.forEvaluation().getEvaluationsByFilter(requesterId, filters);
        return Response.ok(evaluations).build();
    }



}
