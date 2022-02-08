package fr.epita.assistant.jws.domain.entity;

import lombok.Value;
import lombok.With;

import java.sql.Timestamp;

@Value
@With
public class PlayerEntity {
    public long id;
    public Timestamp lastBomb;
    public Timestamp lastMovement;
    public int lives;
    public String name;
    public int posX;
    public int posY;
    public int position;
}
