package fr.epita.assistant.jws.presentation.rest.request;


import fr.epita.assistant.jws.domain.entity.PlayerEntity;
import fr.epita.assistant.jws.domain.service.GameService;
import fr.epita.assistant.jws.domain.service.PlayerService;
import fr.epita.assistant.jws.presentation.rest.response.GetGameResponse;

import javax.inject.Inject;
import javax.ws.rs.*;

@Produces("application/json")
@Consumes("application/json")
@Path("/games")
public class JoinGame {
    @Inject
    PlayerService playerService;

    @Inject
    GameService gameService;

    @POST
    @Path("/{gameId}")
    public GetGameResponse joinGame(PlayerEntity player, @PathParam("gameId") long id) {
        if (player.name == null)
            throw new WebApplicationException(404);
        playerService.addPlayer(player, id);
        return gameService.getGame(id);
    }
}
