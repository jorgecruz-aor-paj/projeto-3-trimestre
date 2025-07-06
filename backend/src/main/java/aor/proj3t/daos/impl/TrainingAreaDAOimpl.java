package aor.proj3t.daos.impl;

import aor.proj3t.daos.interfaces.TrainingAreaDAO;
import aor.proj3t.entities.TrainingArea;
import jakarta.ejb.Stateless;

import java.util.Optional;

@Stateless
public class TrainingAreaDAOimpl extends AbstractDAO<TrainingArea, Long> implements TrainingAreaDAO {

    @Override
    protected Class<TrainingArea> getEntityClass() {
        return TrainingArea.class;
    }


    public Optional<TrainingArea> findByName(String name) {
        return findSingleByProperty("name", name);
    }


}
