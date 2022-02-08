package fr.epita.assistant.jws.presentation.rest.request;

import fr.epita.assistant.jws.domain.service.GameService;
import fr.epita.assistant.jws.presentation.rest.response.ListGamesResponse;


import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.Set;

@Produces("application/json")
@Path("/games")
public class ListGames {
    @Inject
    GameService service;

    @GET
    public Set<ListGamesResponse> findAll() {
        return service.getSet();
    }
}
