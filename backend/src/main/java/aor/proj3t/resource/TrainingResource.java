package aor.proj3t.resource;

import aor.proj3t.dto.*;
import aor.proj3t.enums.Role;
import aor.proj3t.filters.TrainingFilter;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.authutils.Secured;
import aor.proj3t.util.validatorutils.fordto.TrainingValidator;
import aor.proj3t.util.validatorutils.forfilters.FilterValidatorFactory;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * REST endpoint for training-related operations.
 * This class is responsible for handling requests related to training sessions,
 * such as creating, updating, and retrieving training information.
 */
@Path("/trainings")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrainingResource {

    @Inject
    ServiceFactory serviceFactory;

    @Inject
    TrainingValidator validator;

    @Inject
    FilterValidatorFactory validatorFactory;

    @GET
    @Secured()
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrainings(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @BeanParam TrainingFilter filters) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        validatorFactory.validate(filters);
        List<TrainingDTO> trainings = serviceFactory.forTraining().getTrainingsByFilters(requesterId, filters);
        return Response.ok(trainings).build();
    }

    @POST
    @Secured(Role.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTraining(NewTrainingDTO newTrainingDto) {
        validator.validateNewTrainingDTO(newTrainingDto);
        TrainingDTO training = serviceFactory.forTraining().createTraining(newTrainingDto);
        return Response.status(Response.Status.CREATED).entity(training).build();
    }

    @PUT
    @Secured(Role.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateTraining(TrainingDTO trainingDto) {
        validator.validateTrainingDTO(trainingDto);
        TrainingDTO trainingDTO = serviceFactory.forTraining().updateTraining(trainingDto);
        return Response.status(Response.Status.OK).entity(trainingDTO).build();
    }

    @POST
    @Path("/area")
    @Secured(Role.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTrainingArea(TrainingAreaDTO trainingAreaDTO) {
        validator.validateTrainingAreaDTO(trainingAreaDTO);
        TrainingAreaDTO trainingAreaDto = serviceFactory.forTraining().addTrainingArea(trainingAreaDTO);
        return Response.status(Response.Status.OK).entity(trainingAreaDto).build();
    }

    @GET
    @Path("/unparticipated/{userId}")
    @Secured({ Role.MANAGER, Role.ADMIN })
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUnparticipatedTrainings(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathParam("userId") Long userId) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        List<TrainingDTO> trainings = serviceFactory.forTraining().getUnparticipatedTrainingsByUserId(requesterId,
                userId);
        return Response.ok(trainings).build();
    }

    @GET
    @Path("/area")
    @Secured
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrainingAreas() {
        List<TrainingAreaDTO> trainingAreas = serviceFactory.forTraining().getAllTrainingAreas();
        return Response.ok(trainingAreas).build();
    }

    @PATCH
    @Path("/{trainingId}")
    @Secured(Role.ADMIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response deactivateTraining(@PathParam("trainingId") Long id) {
        serviceFactory.forTraining().deactivateTraining(id);
        return Response.ok(new APIResponseDTO("Training deactivated successfully")).build();
    }

    @POST
    @Path("/participation")
    @Secured({ Role.MANAGER, Role.ADMIN })
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response registerTrainingParticipation(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            NewTrainingParticipationDTO newTrainingParticipationDto) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        serviceFactory.forTrainingParticipation().registerTrainingParticipation(requesterId,
                newTrainingParticipationDto);
        return Response.ok(new APIResponseDTO("Training participation successfully registered")).build();
    }

    @GET
    @Path("/participation/{userId}")
    @Secured
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrainingParticipation(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
            @PathParam("userId") Long userId) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        List<TrainingParticipationDTO> participations = serviceFactory.forTrainingParticipation()
                .findTrainingParticipationsByUser(requesterId, userId);
        return Response.ok(participations).build();
    }


}
