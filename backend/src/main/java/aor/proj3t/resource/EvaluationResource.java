package aor.proj3t.resource;

import aor.proj3t.dto.APIResponseDTO;
import aor.proj3t.dto.EvaluationCloseDTO;
import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.enums.Role;
import aor.proj3t.filters.DateRange;
import aor.proj3t.filters.EvaluationFilter;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.authutils.Secured;
import aor.proj3t.util.validatorutils.fordto.EvaluationValidator;
import aor.proj3t.util.validatorutils.forfilters.FilterValidatorFactory;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
/**
 * REST endpoint for evaluation-related operations.
 * <p>
 * This class is responsible for handling requests related to evaluations,
 * including creation, updates, bulk closure, and cycle management. It ensures
 * that the incoming data is validated and routed to the appropriate service-layer
 * methods based on user roles and authorization context.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Path("/evaluations")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class EvaluationResource {

    @Inject
    private ServiceFactory serviceFactory;

    @Inject
    private EvaluationValidator validator;

    @Inject
    private FilterValidatorFactory validatorFactory;

    /**
     * Retrieves evaluations that match the given filter criteria.
     *
     * @param authHeader the authorization header containing the bearer token
     * @param filters the filter parameters for evaluations
     * @return a list of filtered evaluations
     */
    @GET
    @Secured
    public Response getEvaluationsByFilters(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
                                            @BeanParam EvaluationFilter filters) {
        validatorFactory.validate(filters);
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        List<EvaluationDTO> evaluations = serviceFactory.forEvaluation().getEvaluationsByFilter(requesterId, filters);
        return Response.ok(evaluations).build();
    }

    /**
     * Updates an existing evaluation.
     *
     * @param authHeader the authorization header containing the bearer token
     * @param evaluationDTO the updated evaluation data
     * @return the updated evaluation DTO
     */
    @PUT
    @Secured({ Role.MANAGER, Role.ADMIN })
    public Response updateEvaluation(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
                                     EvaluationDTO evaluationDTO) {
        validator.validateEvaluationDTO(evaluationDTO);
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        EvaluationDTO evaluation = serviceFactory.forEvaluation().updateEvaluation(requesterId, evaluationDTO);
        return Response.ok(evaluation).build();
    }

    /**
     * Closes multiple evaluations in bulk using the provided evaluation close DTO.
     *
     * @param authHeader the authorization header containing the bearer token
     * @param evaluationCloseDto DTO containing IDs of evaluations to close
     * @return a response indicating success
     */
    @PATCH
    @Secured(Role.ADMIN)
    public Response closeEvaluation(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
                                    EvaluationCloseDTO evaluationCloseDto) {
        validator.validateEvaluationCloseDTO(evaluationCloseDto);
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        serviceFactory.forEvaluation().closeEvaluationsBulk(requesterId, evaluationCloseDto);
        return Response.ok(new APIResponseDTO("Evaluations closed successfully")).build();
    }

    /**
     * Initiates a new evaluation cycle with the provided date range.
     *
     * @param authHeader the authorization header containing the bearer token
     * @param dateRange the start and end date range for the new cycle
     * @return a response indicating that the cycle was successfully started
     */
    @POST
    @Secured(Role.ADMIN)
    @Path("/cycle")
    public Response startCycle(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
                               DateRange dateRange) {
        validator.newCycleDateRangeValidation(dateRange);
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        serviceFactory.forEvaluationCycle().startCycle(requesterId, dateRange);
        return Response.ok(new APIResponseDTO("Evaluation cycle initiated successfully")).build();
    }

    /**
     * Retrieves the currently active evaluation cycle.
     *
     * @param authHeader the authorization header containing the bearer token
     * @return the DTO of the active evaluation cycle
     */
    @GET
    @Path("/cycle/current")
    @Secured(Role.ADMIN)
    public Response getCurrentCycle(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        EvaluationCycleDTO cycle = serviceFactory.forEvaluationCycle().getActiveCycle(requesterId);
        return Response.ok(cycle).build();
    }

    /**
     * Retrieves all evaluation cycles, optionally including completed ones.
     *
     * @param authHeader the authorization header containing the bearer token
     * @param complete whether to include completed cycles
     * @return a list of evaluation cycle DTOs
     */
    @GET
    @Path("/cycle/all")
    @Secured(Role.ADMIN)
    public Response getAllCycles(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
                                 @QueryParam("complete") boolean complete) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        List<EvaluationCycleDTO> cycles = serviceFactory.forEvaluationCycle().getAllCycles(requesterId, complete);
        return Response.ok(cycles).build();
    }
}
