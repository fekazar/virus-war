package edu.bots.viruswar.repository;

import edu.bots.viruswar.model.Player;
import jakarta.persistence.QueryHint;
import org.hibernate.jpa.HibernateHints;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlayerRepository extends CrudRepository<Player, Long> {
    @QueryHints(@QueryHint(value = "true", name = HibernateHints.HINT_CACHEABLE))
    @Query("select player.state from players player where player.id = :playerId")
    Player.State getState(@Param("playerId") Long playerId);
}
