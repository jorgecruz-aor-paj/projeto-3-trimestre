package aor.proj3t.resource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import aor.proj3t.dto.PersonalStatsDTO;
import aor.proj3t.dto.TrainingYearStatDTO;
import aor.proj3t.enums.Role;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.authutils.Secured;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.HeaderParam;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST endpoint for statistics-related operations.
 * <p>
 * This class provides administrative and personal insights regarding users and their training and evaluation activities.
 * It exposes endpoints to retrieve statistics such as manager assignments, evaluation completion rates,
 * yearly training participation, and individualized user stats.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Path("/stats")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatsResource {

    @Inject
    private ServiceFactory serviceFactory;

    /**
     * Retrieves statistics comparing total users vs users not assigned to a manager.
     *
     * @param authHeader the authorization header containing the bearer token
     * @return a map showing managed vs unmanaged user counts
     */
    @GET
    @Path("/managers")
    @Secured(Role.ADMIN)
    public Response getManagerAssignment(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        Map<String, Long> map = serviceFactory.forUser().getTotalVSUnmanagedUsers(requesterId);
        return Response.ok(map).build();
    }

    /**
     * Retrieves the number of completed evaluations for all users.
     *
     * @param authHeader the authorization header containing the bearer token
     * @return a map showing counts of completed evaluations
     */
    @GET
    @Path("/evaluations")
    @Secured(Role.ADMIN)
    public Response getCompleteEvaluationStats(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        Map<String, Long> map = serviceFactory.forEvaluation().getEvaluationsCompletedStats(requesterId);
        return Response.ok(map).build();
    }

    /**
     * Retrieves training participation time grouped by year for a specific user.
     *
     * @param authHeader the authorization header containing the bearer token
     * @param userId the ID of the user to retrieve participation stats for
     * @return a list of training year statistics
     */
    @GET
    @Path("/participation/{userId}")
    @Secured
    public Response getTrainingParticipationStats(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
                                                   @PathParam("userId") Long userId) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        LinkedHashMap<Integer, Long> statsMap = serviceFactory.forTrainingParticipation()
                .getTrainingParticipationTimeByYearByUser(requesterId, userId);
        List<TrainingYearStatDTO> statsList = statsMap.entrySet().stream()
                .map(e -> new TrainingYearStatDTO(e.getKey(), e.getValue()))
                .toList();
        return Response.ok(statsList).build();
    }

    /**
     * Retrieves personal statistics related to training and evaluation for a specific user.
     *
     * @param authHeader the authorization header containing the bearer token
     * @param userId the ID of the user to retrieve personal stats for
     * @return a DTO containing various personal statistics
     */
    @GET
    @Path("/personal/{userId}")
    @Secured
    public Response getUserPersonalStats(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
                                         @PathParam("userId") Long userId) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        PersonalStatsDTO stats = serviceFactory.forStats().getUserPersonalStats(requesterId, userId);
        return Response.ok(stats).build();
    }
}

