package fr.epita.assistant.jws.presentation.rest.request;

import fr.epita.assistant.jws.domain.service.GameService;
import fr.epita.assistant.jws.presentation.rest.response.GetGameResponse;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

@Produces("application/json")
@Path("/games")
public class GetGame {
    @Inject
    GameService service;

    @GET
    @Path("/{gameId}")
    public GetGameResponse getGame(@PathParam("gameId") long id) {
        return service.getGame(id);
    }
}
