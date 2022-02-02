package fr.epita.assistant.jws.data.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Set;

@Entity
@Table(name = "game")
@AllArgsConstructor
@NoArgsConstructor
@With
@ToString
public class GameModel extends PanacheEntityBase {
    public @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id;
    public Timestamp startTime;
    public String state;
    public @OneToOne @MapsId GameMapModel gameMap;
    public @OneToMany Set<PlayerModel> players;
}
