package fr.epita.assistant.jws.domain.entity;

import lombok.Value;
import lombok.With;

import java.sql.Timestamp;

@Value
@With
public class GameEntity {
    public long id;
    public Timestamp startTime;
    public String state;
}
