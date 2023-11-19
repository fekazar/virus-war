package edu.bots.viruswar.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

@Entity(name = "players")
@NoArgsConstructor
@Data
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
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
