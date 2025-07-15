package aor.proj3t.resource;

import aor.proj3t.dto.WorkplaceDTO;
import aor.proj3t.service.ServiceFactory;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

/**
 * REST endpoint for workplace-related operations.
 * <p>
 * This class handles retrieval of workplace data,
 * providing endpoints that return workplace listings.
 * It delegates the retrieval logic to the service layer.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@Path("/workplaces")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class WorkplaceResource {

    @Inject
    private ServiceFactory serviceFactory;

    /**
     * Retrieves the list of all registered workplaces.
     *
     * @return a list of WorkplaceDTOs in JSON format
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getWorkplaces() {
        List<WorkplaceDTO> list = serviceFactory.forWorkplace().getAllWorkplaces();
        return Response.ok(list).build();
    }
}

