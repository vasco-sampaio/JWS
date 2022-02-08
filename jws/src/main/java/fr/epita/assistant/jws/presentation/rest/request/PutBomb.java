package fr.epita.assistant.jws.presentation.rest.request;

import fr.epita.assistant.jws.Position;
import fr.epita.assistant.jws.domain.service.GameService;
import fr.epita.assistant.jws.presentation.rest.response.GetGameResponse;
import org.jboss.resteasy.annotations.jaxrs.PathParam;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Produces("application/json")
@Consumes("application/json")
@Path("/games")
public class PutBomb {

    @Inject
    GameService service;

    @POST
    @Path("/{gameId}/players/{playerId}/bomb")
    public GetGameResponse putBomb(@PathParam("gameId") long gameId, @PathParam("playerId") long playerId, Position pos) {
        return service.addBomb(gameId, playerId, pos);
    }
}
