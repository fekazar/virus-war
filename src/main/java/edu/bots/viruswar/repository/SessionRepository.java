package edu.bots.viruswar.repository;

import edu.bots.viruswar.model.Session;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SessionRepository extends CrudRepository<Session, String> {
    @QueryHints(@QueryHint(value = "true", name = HibernateHints.HINT_CACHEABLE))
    Optional<Session> findByHostIdOrClientId(Long hostId, Long clientId);

    void deleteBySessionId(String sessionId);

    default Optional<Session> findByPlayerId(Long playerId) {
        return findByHostIdOrClientId(playerId, playerId);
    }
}
