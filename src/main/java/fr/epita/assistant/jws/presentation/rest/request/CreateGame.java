package fr.epita.assistant.jws.presentation.rest.request;

import fr.epita.assistant.jws.domain.entity.PlayerEntity;
import fr.epita.assistant.jws.domain.service.GameService;
import fr.epita.assistant.jws.presentation.rest.response.CreateGameResponse;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Produces("application/json")
@Consumes("application/json")
@Path("/games")
public class CreateGame {
    @Inject
    GameService service;

    @POST
    public CreateGameResponse putGame(PlayerEntity player) {
        return service.addGame(player);
    }
}
