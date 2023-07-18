package edu.bots.viruswar.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Session {
    @Id
    @Column(name = "session_id")
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
