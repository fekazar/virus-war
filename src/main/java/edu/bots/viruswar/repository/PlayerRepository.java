package edu.bots.viruswar.repository;

import edu.bots.viruswar.model.Player;
import org.springframework.data.repository.CrudRepository;

public interface PlayerRepository extends CrudRepository<Player, Long> {
}
