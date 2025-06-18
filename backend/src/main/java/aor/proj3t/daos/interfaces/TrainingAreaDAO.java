package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.TrainingArea;

import java.util.Optional;

/**
 * Repository interface for TrainingArea entity operations.
 */
public interface TrainingAreaDAO extends BasicDAO<TrainingArea, Long> {

    Optional<TrainingArea> findByName(String name);

}
