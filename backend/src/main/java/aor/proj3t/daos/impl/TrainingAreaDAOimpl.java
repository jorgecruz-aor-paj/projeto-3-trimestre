package aor.proj3t.daos.impl;

import java.util.Optional;

import aor.proj3t.daos.interfaces.TrainingAreaDAO;
import aor.proj3t.entities.TrainingArea;
import jakarta.ejb.Stateless;

/**
 * Stateless EJB implementation of the {@link TrainingAreaDAO} interface.
 * <p>
 * This class provides concrete implementation for managing training areas
 * within the application. It extends {@link AbstractDAO} to leverage common
 * CRUD operations and implements specific business logic for training area
 * categorization and lookup operations.
 * </p>
 * <p>
 * Key implementation features:
 * </p>
 * <ul>
 * <li>Efficient name-based lookup using inherited helper methods</li>
 * <li>Leverages AbstractDAO's findSingleByProperty for optimal performance</li>
 * <li>Type-safe operations through proper entity class specification</li>
 * <li>Support for training categorization and organization</li>
 * </ul>
 *
 * @author Jorge Cruz
 * @version 1.0
 * @since 1.0
 * @see TrainingAreaDAO
 * @see AbstractDAO
 * @see TrainingArea
 */
@Stateless
public class TrainingAreaDAOimpl extends AbstractDAO<TrainingArea, Long> implements TrainingAreaDAO {

    /**
     * {@inheritDoc}
     * 
     * @implNote Returns {@code TrainingArea.class} to enable type-safe operations
     *           throughout the DAO implementation.
     */
    @Override
    protected Class<TrainingArea> getEntityClass() {
        return TrainingArea.class;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Implementation leverages the inherited
     * {@link AbstractDAO#findSingleByProperty(String, Object)}
     * method for efficient and type-safe property-based lookup. This demonstrates
     * the power of the AbstractDAO helper methods for common query patterns.
     * </p>
     *
     * @implNote Uses the "name" property for filtering, which should be unique
     *           or indexed for optimal performance. The helper method handles
     *           all the Criteria API complexity and exception handling.
     */
    public Optional<TrainingArea> findByName(String name) {
        return findSingleByProperty("name", name);
    }

}
