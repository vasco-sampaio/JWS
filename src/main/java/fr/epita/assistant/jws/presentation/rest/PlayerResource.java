package fr.epita.assistant.jws.presentation.rest;

import fr.epita.assistant.jws.domain.service.PlayerService;
import org.gradle.internal.impldep.javax.inject.Inject;

import java.sql.Timestamp;

public class PlayerResource {

    @Inject PlayerService service;

    public static class PlayerDTO {
        public long id;
        public Timestamp lastBomb;
        public Timestamp lastMovement;
        public int lives;
        public String name;
        public int posX;
        public int posY;
        public int position;
    }
}
