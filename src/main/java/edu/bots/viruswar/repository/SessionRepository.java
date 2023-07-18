package edu.bots.viruswar.repository;

import edu.bots.viruswar.model.Session;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, String> {
    Optional<Session> findByHostIdOrClientId(Long hostId, Long clientId);

    @Transactional
    void deleteBySessionId(String sessionId);

    default Optional<Session> findByPlayerId(Long playerId) {
        return findByHostIdOrClientId(playerId, playerId);
    }
}
