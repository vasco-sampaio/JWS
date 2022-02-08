package fr.epita.assistant.jws.presentation.rest.response;

import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

public class GetGameResponse {
    public Timestamp startTime;
    public String state;
    public Set<GetGameResponse.Player> players;
    public List<String> map;
    public long id;

    public GetGameResponse(Timestamp startTime, String state, Set<GetGameResponse.Player> players, List<String> map, long id) {
        this.startTime = startTime;
        this.state = state;
        this.players = players;
        this.map = map;
        this.id = id;
    }

    public static class Player{
        public long id;
        public String name;
        public int lives;
        public int posX;
        public int posY;

        public Player(long id, String name, int lives, int posX, int posY) {
            this.id = id;
            this.name = name;
            this.lives = lives;
            this.posX = posX;
            this.posY = posY;
        }
    }
}
