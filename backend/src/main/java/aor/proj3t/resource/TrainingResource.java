package aor.proj3t.resource;

import aor.proj3t.dto.NewTrainingDTO;
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
    public Response createTraining(@HeaderParam(HttpHeaders.AUTHORIZATION) String authHeader, NewTrainingDTO newTrainingDto) {
        validatorUtil.validateNewTrainingDTO(newTrainingDto);
        serviceFactory.forTraining().createTraining(newTrainingDto);
    }
}
