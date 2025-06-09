package aor.proj3t.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.EvaluationCycleDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.enums.Role;
import aor.proj3t.service.interfaces.EvaluationCycleService;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EvaluationCycleServiceImpl extends BaseService implements EvaluationCycleService {
    private static final Logger logger = LogManager.getLogger(EvaluationCycleServiceImpl.class);

    @Inject
    DAOFactory daoFactory;

    @Inject
    private ConverterFactory converterFactory;

    @Override
    public Boolean startCycle(Long adminId, Instant startTime, Instant endTime) {
        try {
            if (daoFactory.forUser().countUsersWithoutManager() != 0) {
                logger.warn("Unable to start cycle. Users without manager");
                return false;
            }
            User admin = getEntity(daoFactory.forUser().findById(adminId), adminId);
            EvaluationCycle evaluationCycle = new EvaluationCycle(startTime, endTime, admin);
            daoFactory.forEvaluationCycle().create(evaluationCycle);
            return createNewEvaluations(evaluationCycle);
        } catch (NoSuchElementException e) {
            logger.error("User with id {} not found", adminId);
            return false;
        } catch (Exception e) {
            logger.error("Error while starting evaluation cycle", e);
            return false;
        }
    }

    @Override
    public EvaluationCycleDTO getActiveCycle() {
        try {
            List<EvaluationCycle> cyles = daoFactory.forEvaluationCycle().findActive();
            if (cyles.size() > 1) {
                logger.warn("Two or more active evaluation cycles found");
                // todo figure out what to do in this case
                return null;
            } else if (cyles.size() == 1) {
                return converterFactory.forEvaluationCycle().toDTO(cyles.getFirst());
            } else {
                logger.info("No active evaluation cycles found");
                return null;
            }
        } catch (Exception e) {
            logger.error("Error while getting active cycle", e);
            return null;
        }
    }

    @Override
    public List<EvaluationCycleDTO> getAllCycles() {
        try {
            return converterFactory.forEvaluationCycle().toDTOList(daoFactory.forEvaluationCycle().findAll());
        } catch (Exception e) {
            logger.error("Error while getting all evaluation cycles", e);
            return null;
        }
    }

    @Override
    public Boolean updateCycleStatus(Long cycleId, EvaluationCycleStatus status) {
        try {
            EvaluationCycle cycle = getEntity(daoFactory.forEvaluationCycle().findById(cycleId), cycleId);
            cycle.setStatus(status);
            daoFactory.forEvaluationCycle().update(cycle);
            return null;
        } catch (NoSuchElementException e) {
            logger.error("No active evaluation cycle found with id {}", cycleId);
            return false;
        } catch (Exception e) {
            logger.error("Error while updating evaluation cycle status with id {}", cycleId, e);
            return false;
        }
    }

    private Boolean createNewEvaluations(EvaluationCycle evaluationCycle) {
        try {
            List<User> users = daoFactory.forUser().findAll();
            for (User user : users) {
                if (!user.getRole().equals(Role.ADMIN)) {
                    Evaluation evaluation = new Evaluation(evaluationCycle, user, user.getManager());
                    daoFactory.forEvaluation().create(evaluation);
                }
            }
            logger.info(" {} evaluations created in evaluation cycle with id {}", users.size(),
                    evaluationCycle.getId());
            return true;
        } catch (Exception e) {
            logger.error("Error while creating evaluations for cycle with id {}", evaluationCycle.getId(), e);
            return false;
        }
    }
}
