package aor.proj3t.daos.interfaces;

import aor.proj3t.entities.Settings;

import java.util.Optional;

public interface SettingsDAO extends BasicDAO<Settings, Long>{

    Optional<Settings> getLatestSettings();
}
