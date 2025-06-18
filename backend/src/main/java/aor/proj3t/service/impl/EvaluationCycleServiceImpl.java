package aor.proj3t.service.impl;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import aor.proj3t.config.AppConstants;
import aor.proj3t.exceptions.ConflictException;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.InvalidDataException;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.filters.DateRange;
import aor.proj3t.util.EmailUtil;
import jakarta.transaction.Transactional;
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
import org.hibernate.Hibernate;

@ApplicationScoped
public class EvaluationCycleServiceImpl extends BaseService implements EvaluationCycleService {
    private static final Logger logger = LogManager.getLogger(EvaluationCycleServiceImpl.class);

    @Inject
    DAOFactory daoFactory;

    @Inject
    private ConverterFactory converterFactory;
    @Inject
    private EmailUtil emailUtil;

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
        if (daoFactory.forEvaluationCycle().countActiveEvaluationCycles() > 0) {
            throw new InvalidDataException(
                    "There is an active evaluation cycle running",
                    "Admin with id " + requesterId + " started evaluation cycle while another is active"
            );
        }
        EvaluationCycle evaluationCycle = new EvaluationCycle(dateRange.getStartDate(), dateRange.getEndDate(), requester);
        daoFactory.forEvaluationCycle().create(evaluationCycle);
        logger.info("Created evaluation cycle with id {}", evaluationCycle.getId());
        createNewEvaluations(evaluationCycle);
        logger.info("Created evaluations for evaluation cycle with id {}", evaluationCycle.getId());
        sendCycleStartEmail(evaluationCycle);
        logger.info("Sent evaluation start email to managers for evaluation cycle with id {}", evaluationCycle.getId());
    }

    @Override
    @Transactional
    public EvaluationCycleDTO getActiveCycle(Long requesterId) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        checkIfRequesterIsAdmin(requester);
        List<EvaluationCycle> cycles = daoFactory.forEvaluationCycle().findActive(true);
        if (cycles.isEmpty()) {
            return new EvaluationCycleDTO();
        }
        if (cycles.size() > 1) {
            throw new ConflictException(
                    "Two or more active evaluation cycles found",
                    "Multiple active evaluation cycles exist — cannot determine the correct one");
        }
        logger.info("Found active evaluation cycle with id {}", cycles.getFirst().getId());
        return converterFactory.forEvaluationCycle().toDTO(cycles.getFirst());
    }

    @Override
    public List<EvaluationCycleDTO> getAllCycles(Long requesterId, boolean withEvaluations) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        checkIfRequesterIsAdmin(requester);
        List<EvaluationCycle> cycles = daoFactory.forEvaluationCycle().findAll(withEvaluations);
        logger.info("Found {} active evaluation cycles for user with id {}", cycles.size(), requesterId);
        return converterFactory.forEvaluationCycle().toDTOList(cycles);
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

    private void createNewEvaluations(EvaluationCycle evaluationCycle) {
        List<User> users = daoFactory.forUser().findAll();
        for (User user : users) {
            if (!user.getRole().equals(Role.ADMIN)) {
                Evaluation evaluation = new Evaluation(evaluationCycle, user, user.getManager());
                daoFactory.forEvaluation().create(evaluation);
            }
        }
        logger.info(" {} evaluations created in evaluation cycle with id {}", users.size(),
                evaluationCycle.getId());

    }

    private void sendCycleStartEmail(EvaluationCycle cycle) {
        List<User> userList = daoFactory.forUser().getManagers();
        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd MMMM yyyy")
                .withZone(ZoneId.systemDefault());
        String formattedStart = formatter.format(cycle.getStartDate());
        String formattedEnd = formatter.format(cycle.getEndDate());
        for (User user : userList) {
            String subject = "Evaluation Cycle Started";
            String body =
                    "<h1>Evaluation Cycle Now Active</h1>" +
                            "<p>Dear " + user.getFirstName() + ",</p>" +
                            "<p>The new evaluation cycle has officially started.</p>" +
                            "<p><strong>Start Date:</strong> " + formattedStart + "</p>" +
                            "<p><strong>End Date:</strong> " + formattedEnd + "</p>" +
                            "<p>Managers should complete performance evaluations for their managed users within this time frame.</p>" +
                            "<br>" +
                            "<p>Thank you for your collaboration.</p>";
            emailUtil.sendEmail(user.getEmail(), subject, body);
        }
    }

}
