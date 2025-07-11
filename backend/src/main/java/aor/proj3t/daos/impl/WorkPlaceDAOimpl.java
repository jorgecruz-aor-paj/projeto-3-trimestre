package aor.proj3t.daos.impl;

import aor.proj3t.daos.interfaces.WorkplaceDAO;
import aor.proj3t.entities.Workplace;
import jakarta.ejb.Stateless;

/**
 * Stateless EJB implementation of the {@link WorkplaceDAO} interface.
 * <p>
 * This class provides concrete implementation for managing workplace/location
 * entities within the application. It extends {@link AbstractDAO} to leverage
 * all common CRUD operations for workplace management.
 * </p>
 * <p>
 * Current implementation features:
 * </p>
 * <ul>
 * <li>Inherits all basic CRUD operations from AbstractDAO</li>
 * <li>Type-safe operations for Workplace entities</li>
 * <li>Ready for extension with workplace-specific methods</li>
 * <li>Supports standard workplace creation, retrieval, update, and
 * deletion</li>
 * </ul>
 * <p>
 * Future enhancements could include:
 * </p>
 * <ul>
 * <li>Location-based lookup methods (findByCity, findByCountry)</li>
 * <li>Active workplace filtering (findActiveWorkplaces)</li>
 * <li>Workplace capacity and user count operations</li>
 * <li>Geographic proximity searches</li>
 * </ul>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 * @see WorkplaceDAO
 * @see AbstractDAO
 * @see Workplace
 */
@Stateless
public class WorkPlaceDAOimpl extends AbstractDAO<Workplace, Long> implements WorkplaceDAO {

    /**
     * {@inheritDoc}
     * 
     * @implNote Returns {@code Workplace.class} to enable type-safe operations
     *           throughout the DAO implementation.
     */
    @Override
    protected Class<Workplace> getEntityClass() {
        return Workplace.class;
    }
}
