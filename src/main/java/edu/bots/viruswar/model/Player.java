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
    private State state = State.DEFAULT;

    @Column(name = "plays_with")
    private Character playsWith = null;

    public enum State {
        DEFAULT,
        AWAITS_CONNECT_ID,
        AWAITS_OTHER_PLAYER,
        AWAITS_COORDINATES
    }
}
