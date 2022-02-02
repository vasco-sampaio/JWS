package fr.epita.assistant.jws.presentation.rest.response;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;

import java.util.HashSet;
import java.util.Set;

public class ListGamesResponse extends PanacheEntityBase {
    public long id;
    public int players;
    public String state;

    public ListGamesResponse(long id, int players, String state) {
        this.id = id;
        this.players = players;
        this.state = state;
    }
}
