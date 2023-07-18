package edu.bots.viruswar.model;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
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

    public enum State {
        DEFAULT
    }
}
