package fr.epita.assistant.jws.domain.service;


import fr.epita.assistant.jws.data.model.GameMapModel;
import fr.epita.assistant.jws.data.model.GameModel;
import fr.epita.assistant.jws.data.model.PlayerModel;
import fr.epita.assistant.jws.domain.entity.PlayerEntity;
import fr.epita.assistant.jws.presentation.rest.response.CreateGameResponse;
import fr.epita.assistant.jws.presentation.rest.response.ListGamesResponse;
import lombok.val;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@ApplicationScoped
public class GameService {
    private final Set<ListGamesResponse> gameSet = new HashSet<>();

    @Transactional
    public Set<ListGamesResponse> getSet() {
        return GameModel.<GameModel>findAll()
                .stream()
                .map(model -> new ListGamesResponse(model.id, model.players.size(), model.state))
                .collect(Collectors.toSet());
    }

    @Transactional
    public CreateGameResponse addGame(final PlayerEntity player) {
        var mModel = new GameMapModel();

        var gModel = new GameModel()
                .withStartTime(new Timestamp(System.currentTimeMillis()))
                .withState("STARTING")
                .withPlayers(new HashSet<PlayerModel>())
                .withGameMap(mModel);

        GameModel.persist(gModel);

        var tmp = GameModel.<GameModel>findAll()
                .stream()
                .filter(game -> game.id == gModel.id)
                .findFirst()
                .orElse(null);

        var pModel = new PlayerModel()
                .withLastBomb(player.lastBomb)
                .withLives(3)
                .withLastMovement(player.lastMovement)
                .withName(player.name)
                .withPosition(player.position)
                .withPosX(0)
                .withPosY(0)
                .withGame(tmp);

        PlayerModel.persist(pModel);

        tmp.players.add(pModel);

        return new CreateGameResponse(tmp.startTime, tmp.state, tmp.players.stream()
                .map(pl -> new CreateGameResponse.Player(pl.id, pl.name, pl.lives, pl.posX, pl.posY))
                .collect(Collectors.toSet()),
                tmp.gameMap.map, tmp.id);
        // TODO: RLE
    }
}
