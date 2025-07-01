package aor.proj3t.resource;

import java.util.List;

import aor.proj3t.dto.EvaluationCloseDTO;
import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.dto.MessageResponseDTO;
import aor.proj3t.enums.Role;
import aor.proj3t.filters.DateRange;
import aor.proj3t.filters.EvaluationFilter;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.authutils.Secured;
import aor.proj3t.util.validatorutils.fordto.EvaluationValidator;
import aor.proj3t.util.validatorutils.forfilters.FilterValidatorFactory;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.BeanParam;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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

    @Inject
    ServiceFactory serviceFactory;

    @Inject
    EvaluationValidator validator;

    @Inject
    FilterValidatorFactory validatorFactory;

    @GET
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEvaluationsByFilters(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @BeanParam EvaluationFilter filters) {
        validatorFactory.validate(filters);
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        List<EvaluationDTO> evaluations = serviceFactory.forEvaluation().getEvaluationsByFilter(requesterId, filters);
        return Response.ok(evaluations).build();
    }

    @PUT
    @Secured({ Role.MANAGER, Role.ADMIN })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateEvaluation(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            EvaluationDTO evaluationDTO) {
        validator.validateEvaluationDTO(evaluationDTO);
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        serviceFactory.forEvaluation().updateEvaluation(requesterId, evaluationDTO);
        return Response.ok(new MessageResponseDTO("Evaluation updated successfully")).build();
    }

    @PATCH
    @Secured(Role.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response closeEvaluation(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            EvaluationCloseDTO evaluationCloseDto) {
        validator.validateEvaluationCloseDTO(evaluationCloseDto);
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        serviceFactory.forEvaluation().closeEvaluationsBulk(requesterId, evaluationCloseDto);
        return Response.ok(new MessageResponseDTO("Evaluations closed successfully")).build();
    }

    @POST
    @Secured(Role.ADMIN)
    @Path("/cycle")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response startCycle(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader, DateRange dateRange) {
        validator.newCycleDateRangeValidation(dateRange);
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        serviceFactory.forEvaluationCycle().startCycle(requesterId, dateRange);
        return Response.ok(new MessageResponseDTO("Evaluation cycle initiated successfully")).build();
    }

    @GET
    @Path("/cycle/current")
    @Secured(Role.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCurrentCycle(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        EvaluationCycleDTO cycle = serviceFactory.forEvaluationCycle().getActiveCycle(requesterId);
        return Response.ok(cycle).build();
    }

    @GET
    @Path("/cycle/all")
    @Secured(Role.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCycles(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @QueryParam("complete") boolean complete) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        List<EvaluationCycleDTO> cycles = serviceFactory.forEvaluationCycle().getAllCycles(requesterId, complete);
        return Response.ok(cycles).build();
    }

}
