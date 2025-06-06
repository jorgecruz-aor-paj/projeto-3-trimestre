package aor.proj3t.service.impl;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.service.interfaces.EvaluationCycleService;
import jakarta.inject.Inject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

public class EvaluationCycleServiceImpl extends BaseService implements EvaluationCycleService {
    private static final Logger logger = LogManager.getLogger(EvaluationCycleServiceImpl.class);

    @Inject
    DAOFactory daoFactory;

    @Override
    public Boolean startCycle(Long adminId, Instant startTime, Instant endTime) {
        try{
            User admin = getEntity(daoFactory.forUser().findById(adminId),adminId);
            EvaluationCycle evaluationCycle = new EvaluationCycle(startTime, endTime, admin);
            daoFactory.forEvaluationCycle().create(evaluationCycle);
            return true;
        } catch (NoSuchElementException e){
            logger.error("User with id {} not found", adminId);
            return false;
        } catch (Exception e){
            logger.error("Error while starting evaluation cycle", e);
            return false;
        }
    }

    @Override
    public EvaluationCycleDTO getActiveCycle() {
        return null;
    }

    @Override
    public List<EvaluationCycleDTO> getAllCycles() {
        return List.of();
    }

    @Override
    public Boolean updateCycleStatus(Long cycleId, EvaluationCycleStatus status) {
        return null;
    }
}
