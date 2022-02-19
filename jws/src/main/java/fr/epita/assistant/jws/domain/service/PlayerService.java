package fr.epita.assistant.jws.domain.service;


import fr.epita.assistant.jws.Position;
import fr.epita.assistant.jws.data.model.GameModel;
import fr.epita.assistant.jws.data.model.PlayerModel;

import fr.epita.assistant.jws.domain.entity.PlayerEntity;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.enterprise.context.ApplicationScoped;

import javax.transaction.Transactional;
import javax.ws.rs.WebApplicationException;
import java.sql.Timestamp;

import java.util.Set;

@ApplicationScoped
public class PlayerService {
    @ConfigProperty(name = "JWS_TICK_DURATION") long tick;
    @ConfigProperty(name = "JWS_DELAY_MOVEMENT") long moveDelay;
    @ConfigProperty(name = "JWS_DELAY_BOMB") long bombDelay;

    @Transactional
    public void addPlayer(final PlayerEntity player, final long id) {
        var tmp = GameModel.<GameModel>findAll()
                .stream()
                .filter(g -> id == g.id)
                .findFirst()
                .orElse(null);

        if (tmp.state.equals("RUNNING") || tmp.players.size() == 4)
            throw new WebApplicationException(400);

        Position pos = null;
        int position = tmp.players.size();
        switch (position) {
            case 0:
                pos = new Position(1, 1);
                break;
            case 1:
                pos = new Position(15, 1);
                break;
            case 2:
                pos = new Position(1, 13);
                break;
            case 3:
                pos = new Position(15, 13);
                break;
            default:
                throw new WebApplicationException(400);
        }
        var pModel = new PlayerModel()
                .withLastBomb(player.lastBomb)
                .withLives(3)
                .withLastMovement(player.lastMovement)
                .withName(player.name)
                .withPosition(position)
                .withPosX(pos.posX)
                .withPosY(pos.posY)
                .withGame(tmp);

        PlayerModel.persist(pModel);

        tmp.players.add(pModel);
    }

    @Transactional
    public void movePlayer(PlayerModel pModel, final Position pos) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (pModel.lastMovement == null || now.getTime() >= pModel.lastMovement.getTime() + tick * moveDelay) {
            pModel.lastMovement = now;
        }
        else {
            throw new WebApplicationException(429);
        }
        if (pModel.lives > 0 && Math.abs((pModel.posX - pos.posX) + (pModel.posY - pos.posY)) == 1) {
            pModel.posX = pos.posX;
            pModel.posY = pos.posY;
        }
        else {
            throw new WebApplicationException(400);
        }
    }

    @Transactional
    public void hurtPlayer(Set<PlayerModel> players, final Position pos) {
        for (var play : players) {
             if (play.lives > 0) {
                 if ((play.posX == pos.posX && play.posY == pos.posY) ||
                         (play.posX == pos.posX - 1 && play.posY == pos.posY) ||
                         (play.posX == pos.posX + 1 && play.posY == pos.posY) ||
                         (play.posX == pos.posX && play.posY == pos.posY - 1) ||
                         (play.posX == pos.posX && play.posY == pos.posY + 1)) {
                     switch (play.position) {
                     case 0:
                         play.posX = 1;
                         play.posY = 1;
                         break;
                     case 1:
                         play.posX = 15;
                         play.posY = 1;
                         break;
                     case 2:
                         play.posX = 1;
                         play.posY = 13;
                         break;
                     case 3:
                         play.posX = 15;
                         play.posY = 13;
                         break;
                    }
                     play.lives = play.lives - 1;
                     play.lastBomb = null;
                 }
             }
        }
    }

    @Transactional
    public void poseBomb(PlayerModel pModel, Position pos) {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if (pModel.lastBomb == null || now.getTime() >= pModel.lastBomb.getTime() + tick * bombDelay) {
            pModel.lastBomb = now;
            pModel.bombX = pos.posX;
            pModel.bombY = pos.posY;
        }
        else {
            throw new WebApplicationException(429);
        }
    }
}
