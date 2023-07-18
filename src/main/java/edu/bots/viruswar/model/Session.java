package edu.bots.viruswar.model;

import edu.bots.viruswar.repository.SessionIdGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

@Entity(name = "sessions")
@NoArgsConstructor
@Getter
@Setter
public class Session {
    @Id
    @Column(name = "session_id")
    @GeneratedValue(generator = "random_strings")
    @GenericGenerator(name = "random_strings", type = SessionIdGenerator.class)
    private String sessionId;

    @Column(name = "host_id")
    private Long hostId;

    @Column(name = "client_id")
    private Long clientId;

    @Column(name = "move")
    private Integer move;

    @Column(name = "field")
    private String field;
}
