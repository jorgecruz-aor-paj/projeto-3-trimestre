package aor.proj3t.service.impl;

import java.time.Instant;
import java.util.List;
import java.util.NoSuchElementException;

import aor.proj3t.exceptions.ConflictException;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.InvalidDataException;
import aor.proj3t.filters.DateRange;
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
    public void startCycle(Long requesterId, DateRange dateRange) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        checkIfRequesterIsAdmin(requester);
        if (daoFactory.forUser().countUsersWithoutManager() != 0) {
            throw new InvalidDataException(
                    "There are users without manager",
                    "Admin with id " + requesterId + " started evaluation cycle with unmanaged users"
            );
        }
        if (daoFactory.forEvaluationCycle().findActive().size() > 1) {
            throw new InvalidDataException(
                    "There is an active evaluation cycle running",
                    "Admin with id " + requesterId + " started evaluation cycle while another is active"
            );
        }
        EvaluationCycle evaluationCycle = new EvaluationCycle(dateRange.getStartDate(), dateRange.getEndDate(), requester);
        daoFactory.forEvaluationCycle().create(evaluationCycle);
    }

    @Override
    public EvaluationCycleDTO getActiveCycle(Long requesterId) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        checkIfRequesterIsAdmin(requester);
        List<EvaluationCycle> cycles = daoFactory.forEvaluationCycle().findActive();
        if (cycles.size() > 1) {
            throw new ConflictException(
                    "Two or more active evaluation cycles found",
                    "At least two or more active evaluation cycles found"
            );
        }
        return converterFactory.forEvaluationCycle().toDTO(cycles.getFirst());
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
            EvaluationCycle cycle = getEntity(daoFactory.forEvaluationCycle().findById(cycleId), cycleId, "User");
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
