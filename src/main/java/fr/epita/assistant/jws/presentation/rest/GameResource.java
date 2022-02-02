/*package fr.epita.assistant.jws.presentation.rest;

import fr.epita.assistant.jws.domain.service.GameService;
import lombok.Value;
import lombok.With;
import org.gradle.internal.impldep.javax.inject.Inject;

import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import java.sql.Timestamp;
import java.util.Objects;
import java.util.Set;

@ApplicationScoped

@Path("/games")
public class GameResource {



    @POST
    public Set<GameDTO> putGame(final GameDTO game) {
        service.addGame(game);
        return service.getSet();
    }

    @GET
    @Path("/{gameId}")
    public GameDTO getGame(@PathParam("gameId") final long id) {
        return service.getSet().stream()
                .filter(game -> Objects.equals(game.id, id))
                .findFirst()
                .orElse(null);
    }

    @Value
    @With
    public static class GameDTO {
        public long id;
        public Timestamp startTime;
        public String state;
    }
}*/
