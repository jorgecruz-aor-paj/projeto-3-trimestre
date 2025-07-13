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

@Path("/stats")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StatsResource {

    @Inject
    ServiceFactory serviceFactory;

    @GET
    @Path("/managers")
    @Secured(Role.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getManagerAssignment(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        Map<String, Long> map = serviceFactory.forUser().getTotalVSUnmanagedUsers(requesterId);
        return Response.ok(map).build();
    }

    @GET
    @Path("/evaluations")
    @Secured(Role.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCompleteEvaluationStats(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        Map<String, Long> map = serviceFactory.forEvaluation().getEvaluationsCompletedStats(requesterId);
        return Response.ok(map).build();
    }

    @GET
    @Path("/participation/{userId}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
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

    @GET
    @Path("/personal/{userId}")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserPersonalStats(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathParam("userId") Long userId) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        PersonalStatsDTO stats = serviceFactory.forStats().getUserPersonalStats(requesterId, userId);
        return Response.ok(stats).build();
    }
}
