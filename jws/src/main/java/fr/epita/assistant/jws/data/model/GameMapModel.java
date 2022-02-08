package fr.epita.assistant.jws.data.model;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.With;

import javax.persistence.*;

@Entity
@Table(name = "game_map")
@AllArgsConstructor
@NoArgsConstructor
@With
@ToString
public class GameMapModel extends PanacheEntityBase {
    public @Id @GeneratedValue(strategy = GenerationType.IDENTITY) long id;
    public @Column(columnDefinition = "TEXT") String map;
    public @OneToOne @PrimaryKeyJoinColumn GameModel gameModel;
}
