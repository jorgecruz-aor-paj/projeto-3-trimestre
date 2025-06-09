package aor.proj3t.resource;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

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

}
