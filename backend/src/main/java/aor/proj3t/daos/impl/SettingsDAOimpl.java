package aor.proj3t.daos.impl;

import java.util.Optional;

import aor.proj3t.daos.interfaces.SettingsDAO;
import aor.proj3t.entities.Settings;
import jakarta.ejb.Stateless;
import jakarta.persistence.TypedQuery;

@Stateless
public class SettingsDAOimpl extends AbstractDAO<Settings, Long> implements SettingsDAO {

    @Override
    public Optional<Settings> getLatestSettings() {
        TypedQuery<Settings> query = em.createNamedQuery("Settings.getLatestSettings", Settings.class);
        query.setMaxResults(1);
        return query.getResultList().stream().findFirst();
    }

}
