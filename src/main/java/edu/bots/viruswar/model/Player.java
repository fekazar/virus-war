package edu.bots.viruswar.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "players")
@NoArgsConstructor
@Getter
@Setter
public class Player {
    @Id
    private Long id;

    @Enumerated(EnumType.STRING)
    private State state;

    @Enumerated(EnumType.STRING)
    @Column(name = "plays_with")
    private Figure playsWith;

    public enum State {
        DEFAULT,
        AWAITS_CONNECT_ID,
        IN_GAME,
        AWAITS_OTHER_PLAYER,
        AWAITS_COORDINATES
    }

    public enum Figure {
        CROSS,
        CIRCLE
    }
}
