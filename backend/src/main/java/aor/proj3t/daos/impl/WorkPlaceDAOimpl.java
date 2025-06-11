package aor.proj3t.daos.impl;

import aor.proj3t.daos.interfaces.WorkplaceDAO;
import aor.proj3t.entities.Workplace;
import jakarta.ejb.Stateless;

@Stateless
public class WorkPlaceDAOimpl extends AbstractDAO<Workplace, Long> implements WorkplaceDAO {
    
    @Override
    protected Class<Workplace> getEntityClass() {
        return Workplace.class;
    }
}
