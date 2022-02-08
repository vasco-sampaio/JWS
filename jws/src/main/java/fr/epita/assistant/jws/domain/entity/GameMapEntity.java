package fr.epita.assistant.jws.domain.entity;

import lombok.Value;
import lombok.With;

@Value
@With
public class GameMapEntity {
    public long id;
    public String map;
}
