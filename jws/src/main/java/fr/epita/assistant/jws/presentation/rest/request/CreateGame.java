package fr.epita.assistant.jws.presentation.rest.request;

import fr.epita.assistant.jws.domain.entity.PlayerEntity;
import fr.epita.assistant.jws.domain.service.GameService;
import fr.epita.assistant.jws.presentation.rest.response.GetGameResponse;

import javax.inject.Inject;
import javax.ws.rs.*;

@Produces("application/json")
@Consumes("application/json")
@Path("/games")
public class CreateGame {
    @Inject
    GameService service;

    @POST
    public GetGameResponse putGame(PlayerEntity player) {
        if (player.name == null)
            throw new WebApplicationException(400);
        return service.addGame(player);
    }
}
