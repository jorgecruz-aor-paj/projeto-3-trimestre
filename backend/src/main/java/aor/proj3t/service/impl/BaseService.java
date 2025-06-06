package aor.proj3t.service.impl;

import aor.proj3t.config.AppConstants;
import aor.proj3t.daos.DAOFactory;
import aor.proj3t.entities.*;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Supplier;

public abstract class BaseService {
    private static final Logger logger = LogManager.getLogger(BaseService.class);

    protected <T> T getEntity(Optional<T> entityOpt, Long id) {
        return entityOpt.orElseThrow(() ->
                new NoSuchElementException(entityOpt.getClass().getSimpleName() + " not found with ID: " + id));
    }

}
