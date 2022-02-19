package fr.epita.assistant.jws.domain.service;

import fr.epita.assistant.jws.Position;
import fr.epita.assistant.jws.RLE;
import fr.epita.assistant.jws.data.model.GameModel;

import fr.epita.assistant.jws.domain.entity.PlayerEntity;
import fr.epita.assistant.jws.presentation.rest.response.GetGameResponse;
import fr.epita.assistant.jws.presentation.rest.response.ListGamesResponse;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.springframework.scheduling.annotation.Scheduled;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import java.sql.Timestamp;

import java.util.HashSet;
import java.util.Set;

import java.util.stream.Collectors;


@ApplicationScoped
public class GameService {
    @ConfigProperty(name = "JWS_TICK_DURATION") long tick;
    @ConfigProperty(name = "JWS_DELAY_BOMB") long bombDelay;

    @Inject GameMapService mapService;
    @Inject PlayerService playerService;

    @Transactional
    public Set<ListGamesResponse> getSet() {
        return GameModel.<GameModel>findAll()
                .stream()
                .map(model -> new ListGamesResponse(model.id, model.players.size(), model.state))
                .collect(Collectors.toSet());
    }

    private GetGameResponse createResponse(GameModel gModel) {
        return new GetGameResponse(gModel.startTime, gModel.state, gModel.players.stream()
                .map(pl -> new GetGameResponse.Player(pl.id, pl.name, pl.lives, pl.posX, pl.posY))
                .collect(Collectors.toSet()),
                RLE.wrappEncode(gModel.gameMap.map), gModel.id);
    }

    @Transactional
    public GetGameResponse addGame(final PlayerEntity player) {

        var mModel = mapService.getMap(mapService.addMap());

        var gModel = new GameModel()
                .withStartTime(new Timestamp(System.currentTimeMillis()))
                .withState("STARTING")
                .withPlayers(new HashSet<>())
                .withGameMap(mModel);

        GameModel.persist(gModel);

        playerService.addPlayer(player, gModel.id);

        GameModel tmp = GameModel.findById(gModel.id);

        return createResponse(tmp);
    }

    @Transactional
    public GetGameResponse getGame(final long id) {
        GameModel gModel = GameModel.findById(id);

        if (gModel == null)
            throw new WebApplicationException(404);

        return createResponse(gModel);
    }

    @Transactional
    public GetGameResponse startGame(final long id) {
        GameModel gModel = GameModel.findById(id);

        if (gModel == null)
            throw new WebApplicationException(404);

        gModel.startTime = new Timestamp(System.currentTimeMillis());
        gModel.state = "RUNNING";

        return createResponse(gModel);
    }

    private boolean isValid(final Position position, final String map) {
        return map.charAt(position.posY * 17 + position.posX) == 'G';
    }

    @Transactional
    public GetGameResponse movePlayer(final long gameId, final long playerId, final Position pos) {
        GameModel gModel = GameModel.findById(gameId);

        if (gModel == null)
            throw new WebApplicationException(404);

        if (!gModel.state.equals("RUNNING"))
            throw new WebApplicationException(400);

        if (isValid(pos, gModel.gameMap.map)) {
            var pModel = gModel.players.stream().
                    filter(player -> player.id == playerId).
                    findFirst()
                    .orElseThrow(() -> new WebApplicationException(404));
            playerService.movePlayer(pModel, pos);
        }
        else
            throw new WebApplicationException(400);

        return createResponse(gModel);
    }

    @Transactional
    // is executed every 10ms
    @Scheduled(fixedRate = 1)
    public void scheduledExplosion() {

        GameModel.<GameModel>findAll().stream().forEach(gModel -> {
            for (var player : gModel.players) {
                var now = new Timestamp(System.currentTimeMillis());
                if (player.lastBomb != null && now.getTime() >= player.lastBomb.getTime() + tick * bombDelay) {
                    var pos = new Position(player.bombX, player.bombY);
                    mapService.exploseBomb(gModel.gameMap, pos);
                    playerService.hurtPlayer(gModel.players, pos);
                    player.lastBomb = null;
                }
            }

            if (gModel.state.equals("RUNNING") && gModel.players.stream().filter(p -> p.lives > 0).count() <= 1)
                gModel.state = "FINISHED";
        });
    }

    @Transactional
    public GetGameResponse addBomb(final long gameId, final long playerId, Position pos) {
        GameModel gModel = GameModel.findById(gameId);

        if (gModel == null) {
            throw new WebApplicationException(404);
        }

        if (!gModel.state.equals("RUNNING"))
            throw new WebApplicationException(400);

        if (isValid(pos, gModel.gameMap.map)) {
            var pModel = gModel.players.stream().
                    filter(player -> player.id == playerId).
                    findFirst()
                    .orElseThrow(() -> new WebApplicationException(404));

            playerService.poseBomb(pModel, pos);
            mapService.addBomb(gModel.gameMap, pos);

            if (gModel.players.stream().filter(p -> p.lives > 0).count() <= 1)
                gModel.state = "FINISHED";
        }
        else
            throw new WebApplicationException(400);

        return createResponse(gModel);
    }
}
