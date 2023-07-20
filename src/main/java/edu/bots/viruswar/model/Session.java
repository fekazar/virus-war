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

import java.util.Arrays;
import java.util.stream.Collectors;

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
    private Integer move = 0;

    @Column(name = "field")
    private String field;

    public Long otherPlayer(Long playerId) {
        if (hostId.equals(playerId))
            return clientId;
        if (clientId.equals(playerId))
            return hostId;
        throw new IllegalStateException("There is no such player.");
    }

    public char[][] getMappedField() {
        var lines = field.split("\n");
        var res = new char[lines.length][lines[0].length()];

        for (int i = 0; i < lines.length; ++i) {
            for (int j = 0; j < lines[i].length(); ++j)
                res[i][j] = lines[i].charAt(j);
        }

        return res;
    }

    public void setMappedField(char[][] fieldArray) {
        field = Arrays.stream(fieldArray)
                .map(String::new)
                .collect(Collectors.joining("\n"));
    }
}
