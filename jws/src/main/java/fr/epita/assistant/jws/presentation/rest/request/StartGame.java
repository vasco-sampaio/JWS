package fr.epita.assistant.jws.presentation.rest.request;

import fr.epita.assistant.jws.domain.service.GameService;
import fr.epita.assistant.jws.presentation.rest.response.GetGameResponse;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;

import javax.ws.rs.PATCH;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Produces("application/json")
@Path("/games")
public class StartGame {
    @Inject
    GameService service;

    @PATCH
    @Path("/{gameId}/start")
    public GetGameResponse startGame(@PathParam("gameId") long id) {
        return service.startGame(id);
    }
}
