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
 * <p>
 * This class handles RESTful operations related to training sessions,
 * including creation, updates, area management, user participation,
 * and filtered queries for training data. Access controls are enforced
 * based on user roles.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Path("/trainings")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TrainingResource {

    @Inject
    private ServiceFactory serviceFactory;

    @Inject
    private TrainingValidator validator;

    @Inject
    private FilterValidatorFactory validatorFactory;

    /**
     * Retrieves trainings filtered by provided parameters.
     *
     * @param authHeader the bearer token from the request
     * @param filters the filter criteria to apply
     * @return list of training DTOs that match the filter
     */
    @GET
    @Secured
    public Response getTrainings(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
                                 @BeanParam TrainingFilter filters) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        validatorFactory.validate(filters);
        List<TrainingDTO> trainings = serviceFactory.forTraining().getTrainingsByFilters(requesterId, filters);
        return Response.ok(trainings).build();
    }

    /**
     * Creates a new training session based on provided data.
     *
     * @param newTrainingDto details of the training session to create
     * @return the created training DTO
     */
    @POST
    @Secured(Role.ADMIN)
    public Response createTraining(NewTrainingDTO newTrainingDto) {
        validator.validateNewTrainingDTO(newTrainingDto);
        TrainingDTO training = serviceFactory.forTraining().createTraining(newTrainingDto);
        return Response.status(Response.Status.CREATED).entity(training).build();
    }

    /**
     * Updates an existing training session with new information.
     *
     * @param trainingDto the updated training DTO
     * @return the updated training details
     */
    @PUT
    @Secured(Role.ADMIN)
    public Response updateTraining(TrainingDTO trainingDto) {
        validator.validateTrainingDTO(trainingDto);
        TrainingDTO trainingDTO = serviceFactory.forTraining().updateTraining(trainingDto);
        return Response.status(Response.Status.OK).entity(trainingDTO).build();
    }

    /**
     * Adds a new training area to the system.
     *
     * @param trainingAreaDTO the DTO containing the training area's name
     * @return the created training area DTO
     */
    @POST
    @Path("/area")
    @Secured(Role.ADMIN)
    public Response addTrainingArea(TrainingAreaDTO trainingAreaDTO) {
        validator.validateTrainingAreaDTO(trainingAreaDTO);
        TrainingAreaDTO trainingAreaDto = serviceFactory.forTraining().addTrainingArea(trainingAreaDTO);
        return Response.status(Response.Status.OK).entity(trainingAreaDto).build();
    }

    /**
     * Retrieves trainings a user has not yet participated in.
     *
     * @param authHeader the bearer token from the request
     * @param userId the user ID to query unparticipated trainings
     * @return list of trainings the user has not joined
     */
    @GET
    @Path("/unparticipated/{userId}")
    @Secured({ Role.MANAGER, Role.ADMIN })
    public Response getUnparticipatedTrainings(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
                                               @PathParam("userId") Long userId) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        List<TrainingDTO> trainings = serviceFactory.forTraining().getUnparticipatedTrainingsByUserId(requesterId, userId);
        return Response.ok(trainings).build();
    }

    /**
     * Retrieves all existing training areas.
     *
     * @return list of all training area DTOs
     */
    @GET
    @Path("/area")
    @Secured
    public Response getTrainingAreas() {
        List<TrainingAreaDTO> trainingAreas = serviceFactory.forTraining().getAllTrainingAreas();
        return Response.ok(trainingAreas).build();
    }

    /**
     * Deactivates a training session by ID.
     *
     * @param id the training ID to deactivate
     * @return confirmation response
     */
    @PATCH
    @Path("/{trainingId}")
    @Secured(Role.ADMIN)
    public Response deactivateTraining(@PathParam("trainingId") Long id) {
        serviceFactory.forTraining().deactivateTraining(id);
        return Response.ok(new APIResponseDTO("Training deactivated successfully")).build();
    }

    /**
     * Registers a training participation for a given user and training session.
     *
     * @param authHeader the bearer token from the request
     * @param newTrainingParticipationDto details of the participation record
     * @return confirmation response
     */
    @POST
    @Path("/participation")
    @Secured({ Role.MANAGER, Role.ADMIN })
    public Response registerTrainingParticipation(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
                                                  NewTrainingParticipationDTO newTrainingParticipationDto) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        serviceFactory.forTrainingParticipation().registerTrainingParticipation(requesterId, newTrainingParticipationDto);
        return Response.ok(new APIResponseDTO("Training participation successfully registered")).build();
    }

    /**
     * Retrieves a user's recorded training participations.
     *
     * @param authHeader the bearer token from the request
     * @param userId the user ID to query
     * @return list of participation DTOs
     */
    @GET
    @Path("/participation/{userId}")
    @Secured
    public Response getTrainingParticipation(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader,
                                             @PathParam("userId") Long userId) {
        Long requesterId = serviceFactory.forToken().getUserIdFromAuthHeader(authHeader);
        List<TrainingParticipationDTO> participations =
                serviceFactory.forTrainingParticipation().findTrainingParticipationsByUser(requesterId, userId);
        return Response.ok(participations).build();
    }
}

