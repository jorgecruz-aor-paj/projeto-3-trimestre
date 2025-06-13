package aor.proj3t.resource;

import aor.proj3t.dto.NewTrainingDTO;
import aor.proj3t.dto.TrainingAreaDTO;
import aor.proj3t.dto.TrainingDTO;
import aor.proj3t.entities.User;
import aor.proj3t.enums.Role;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.util.ValidatorUtil;
import aor.proj3t.util.authutils.Secured;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

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
    ValidatorUtil validatorUtil;

    @POST
    @Secured(Role.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createTraining(NewTrainingDTO newTrainingDto) {
        validatorUtil.validateNewTrainingDTO(newTrainingDto);
        TrainingDTO training = serviceFactory.forTraining().createTraining(newTrainingDto);
        return Response.status(Response.Status.CREATED).entity(training).build();
    }

    @PUT
    @Secured(Role.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(TrainingDTO trainingDto){
        validatorUtil.validateTrainingDTO(trainingDto);
        TrainingDTO trainingDTO = serviceFactory.forTraining().updateTraining(trainingDto);
        return Response.status(Response.Status.OK).entity(trainingDTO).build();
    }

    @POST
    @Path("/area")
    @Secured(Role.ADMIN)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTrainings(TrainingAreaDTO trainingAreaDTO) {
        validatorUtil.validateTrainingAreaDTO(trainingAreaDTO);
        TrainingAreaDTO trainingAreaDto = serviceFactory.forTraining().addTrainingArea(trainingAreaDTO);
        return Response.status(Response.Status.OK).entity(trainingAreaDto).build();
    }
}
