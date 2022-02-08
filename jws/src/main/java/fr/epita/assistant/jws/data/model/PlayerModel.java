package fr.epita.assistant.jws.data.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "player")
@AllArgsConstructor
@NoArgsConstructor
@With
@ToString

public class PlayerModel extends PanacheEntityBase {
    public @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id;
    public Timestamp lastBomb;
    public int bombX;
    public int bombY;
    public Timestamp lastMovement;
    public int lives;
    public String name;
    public int posX;
    public int posY;
    public int position;
    public @ManyToOne GameModel game;
}
