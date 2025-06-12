package aor.proj3t.service.impl;

import java.util.NoSuchElementException;
import java.util.Optional;

import aor.proj3t.exceptions.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class BaseService {
    private static final Logger logger = LogManager.getLogger(BaseService.class);

    protected <T> T getEntity(Optional<T> entityOpt, Long id, String fieldName) {
        return entityOpt.orElseThrow(
                () -> new NotFoundException(fieldName + " not found with ID: " + id));
    }



}
