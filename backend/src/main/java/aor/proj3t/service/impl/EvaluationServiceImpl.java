package aor.proj3t.service.impl;

import java.time.Instant;
import java.util.*;

import aor.proj3t.dto.EvaluationCloseDTO;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.BadRequestException;
import aor.proj3t.exceptions.ConflictException;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.filters.EvaluationFilter;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.service.interfaces.EvaluationService;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class EvaluationServiceImpl extends BaseService implements EvaluationService {
    private static final Logger logger = LogManager.getLogger(EvaluationServiceImpl.class);

    @Inject
    DAOFactory daoFactory;

    @Inject
    ConverterFactory converterFactory;


    @Override
    public Boolean createEvaluation(Long evaluatedUserId, Long evaluatorUserId, Long evaluationCycleId) {
        try {
            User evaluated = getEntity(daoFactory.forUser().findById(evaluatedUserId), evaluatedUserId, "User");
            User evaluator = getEntity(daoFactory.forUser().findById(evaluatorUserId), evaluatorUserId, "User");
            EvaluationCycle cycle = getEntity(daoFactory.forEvaluationCycle().findById(evaluationCycleId),
                    evaluationCycleId, "Evaluation cycle");
            Evaluation evaluation = new Evaluation(cycle, evaluated, evaluator);
            daoFactory.forEvaluation().create(evaluation);
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Users with id {} or {} not found", evaluatedUserId, evaluatorUserId, e);
            return false;
        } catch (Exception e) {
            logger.error("Error while creating evaluation", e);
            return false;
        }
    }

    @Override
    public EvaluationDTO getEvaluationById(Long evaluationId) {
        try {
            return converterFactory.forEvaluation()
                    .toDTO(getEntity(daoFactory.forEvaluation().findById(evaluationId), evaluationId, "Evaluation"));
        } catch (NoSuchElementException e) {
            logger.error("Evaluation with id {} not found", evaluationId);
            return null;
        } catch (Exception e) {
            logger.error("Error while getting evaluation with id {}", evaluationId, e);
            return null;
        }
    }

    @Override
    public void updateEvaluation(Long requesterId, EvaluationDTO evaluationDTO) {
        Evaluation evaluation = getEntity(daoFactory.forEvaluation().findById(evaluationDTO.getId()), evaluationDTO.getId(), "Evaluation");
        evaluation.setScale(evaluationDTO.getScale());
        evaluation.setFeedback(evaluationDTO.getFeedback());
        evaluation.setStatus(evaluationDTO.getStatus());
        daoFactory.forEvaluation().update(evaluation);
        checkAndCompleteEvaluationCycle(evaluation.getEvaluationCycle().getId());
    }

    @Override
    public List<EvaluationDTO> findEvaluationsByEvaluator(Long evaluatorId) {
        try {
            return converterFactory.forEvaluation().toDTOList(daoFactory.forEvaluation().findByEvaluator(evaluatorId));
        } catch (Exception e) {
            logger.error("Error while finding evaluations by evaluator {}", evaluatorId, e);
            return null;
        }
    }

    @Override
    public List<EvaluationDTO> findEvaluationsByEvaluated(Long evaluatedId) {
        try {
            return converterFactory.forEvaluation().toDTOList(daoFactory.forEvaluation().findByEvaluator(evaluatedId));
        } catch (Exception e) {
            logger.error("Error while finding evaluations by evaluated {}", evaluatedId, e);
            return null;
        }
    }

    @Override
    public List<EvaluationDTO> findEvaluationsByCycle(Long cycleId) {
        try {
            return converterFactory.forEvaluation().toDTOList(daoFactory.forEvaluation().findByCycleId(cycleId));
        } catch (Exception e) {
            logger.error("Error while finding evaluations by evaluation cycle {}", cycleId, e);
            return null;
        }
    }

    @Override
    public List<EvaluationDTO> findEvaluationsByDateRange(Instant startDate, Instant endDate) {
        try {
            return converterFactory.forEvaluation()
                    .toDTOList(daoFactory.forEvaluation().findByDateRange(startDate, endDate));
        } catch (Exception e) {
            logger.error("Error while finding evaluations between dates {} and {}", startDate, endDate, e);
            return null;
        }
    }

    @Override
    public Boolean completeEvaluation(Long evaluationId) {
        try {
            Evaluation evaluation = getEntity(daoFactory.forEvaluation().findById(evaluationId), evaluationId, "Evaluation");
            evaluation.setStatus(EvaluationStatus.COMPLETED);
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Evaluation with id {} not found", evaluationId);
            return false;
        } catch (Exception e) {
            logger.error("Error while marking evaluations with id {} as completed", evaluationId, e);
            return false;
        }
    }

    @Override
    public Boolean closeEvaluation(Long evaluationId) {
        try {
            Evaluation evaluation = getEntity(daoFactory.forEvaluation().findById(evaluationId), evaluationId, "Evaluation");
            evaluation.setStatus(EvaluationStatus.CLOSED);
            checkAndCloseEvaluationCycle(evaluation.getEvaluationCycle().getId());
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Evaluation with id {} not found", evaluationId);
            return false;
        } catch (Exception e) {
            logger.error("Error while marking evaluations with id {} as closed", evaluationId, e);
            return false;
        }
    }

    @Override
    @Transactional // Rollback if any RuntimeException occurs
    public void closeEvaluationsBulk(Long requesterId, EvaluationCloseDTO dto) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        checkIfRequesterIsAdmin(requester);
        EvaluationCycle cycle = getEntity(
                daoFactory.forEvaluationCycle().findById(dto.getCycleId()),
                dto.getCycleId(),
                "Evaluation cycle");
        if (cycle.getStatus().equals(EvaluationCycleStatus.CLOSED)) {
            throw new BadRequestException(
                    "Evaluation cycle has already been closed",
                    "User with id " + requesterId + " tried closing evaluations from closed cycle with id " + dto.getCycleId());
        }
        List<Evaluation> evaluations = getEvaluationListFromIds(dto.getEvaluationIds());
        if (evaluations.isEmpty()) {
            throw new NotFoundException(
                    "No valid evaluations",
                    "User with id " + requesterId + " attempted to close evaluations with no valid IDs");
        }
        for (Evaluation eval : evaluations) {
            if (!eval.getEvaluationCycle().getId().equals(cycle.getId())) {
                throw new BadRequestException(
                        "Evaluation belongs to a different cycle",
                        "Evaluation ID " + eval.getId() + " does not belong to cycle ID " + cycle.getId());
            }
            if (eval.getEvaluationCycle().getStatus().equals(EvaluationCycleStatus.CLOSED)) {
                throw new BadRequestException(
                        "Evaluation already closed",
                        "Evaluation ID " + eval.getId() + " is already part of a closed cycle");
            }
            if (eval.getStatus().equals(EvaluationStatus.ONGOING)) {
                throw new ForbiddenException(
                        "Evaluation is still ongoing",
                        "User with id " + requesterId + "closing ongoing evaluation with id " + eval.getId());
            }
            eval.setStatus(EvaluationStatus.CLOSED);
            daoFactory.forEvaluation().update(eval);
        }
        checkAndCloseEvaluationCycle(dto.getCycleId());
    }

    @Override
    public Integer closeAllCompletedEvaluations() {
        try {
            Integer closedEvaluations = daoFactory.forEvaluation().closeAllCompletedEvaluations();
            return closedEvaluations == null ? 0 : closedEvaluations;
        } catch (Exception e) {
            logger.error("Error while closing all complete evaluations", e);
            return -1;
        }
    }

    @Override
    public List<EvaluationDTO> getEvaluationsByFilter(Long requesterId, EvaluationFilter filter) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        if (filter.getEvaluatedId() != null) {
            User evaluated = getEntity(daoFactory.forUser().findById(filter.getEvaluatedId()), filter.getEvaluatedId(), "User");
            canViewEvaluationsByEvaluated(requester, evaluated);
        } else if (requester.getRole().equals(Role.USER)) {
            filter.setEvaluatedId(requesterId);
        }
        if (filter.getEvaluatorId() != null) {
            User evaluator = getEntity(daoFactory.forUser().findById(filter.getEvaluatorId()), filter.getEvaluatorId(), "User");
            canViewEvaluationsByEvaluator(requester, evaluator);
        } else if (requester.getRole().equals(Role.MANAGER)) {
            filter.setEvaluatorId(requesterId);
        }
        List<Evaluation> evaluation = daoFactory.forEvaluation().getEvaluationsByFilters(filter);
        return converterFactory.forEvaluation().toDTOList(evaluation);
    }

    private void canViewEvaluationsByEvaluated(User requester, User evaluated) {
        boolean isRequesterAdmin = requester.getRole() == Role.ADMIN;
        boolean isRequesterSelf = requester.getId().equals(evaluated.getId());
        boolean isRequesterManagerOfEvaluated = evaluated.getManager() != null &&
                evaluated.getManager().getId().equals(requester.getId());
        if (!(isRequesterAdmin || isRequesterSelf || isRequesterManagerOfEvaluated)) {
            throw new ForbiddenException("Permission denied",
                    "User with id " + requester.getId() + " cannot view evaluations of user " + evaluated.getId());
        }
    }

    private void canViewEvaluationsByEvaluator(User requester, User evaluator) {
        boolean isRequesterAdmin = requester.getRole() == Role.ADMIN;
        boolean isRequesterSelf = requester.getId().equals(evaluator.getId());
        if (!(isRequesterAdmin || isRequesterSelf)) {
            throw new ForbiddenException("Permission denied",
                    "User with id " + requester.getId() + " cannot view evaluations made by user " + evaluator.getId());
        }
    }


    private List<Evaluation> getEvaluationListFromIds(List<Long> evaluationIds) {

        List<Evaluation> evaluations = new ArrayList<>(evaluationIds.size());
        for (Long evaluationId : evaluationIds) {
            Evaluation evaluation = getEntity(daoFactory.forEvaluation().findById(evaluationId), evaluationId, "Evaluation");
            if (evaluation != null) {
                evaluations.add(evaluation);
            } else {
                logger.error("Evaluation not found with ID: " + evaluationId);
            }
        }
        return evaluations;
    }

    private void checkAndCloseEvaluationCycle(Long evaluationCycleId) {
        if (daoFactory.forEvaluation().countUnclosedEvaluationsByCycleId(evaluationCycleId) == 0) {
            EvaluationCycle cycle = getEntity(daoFactory.forEvaluationCycle().findById(evaluationCycleId), evaluationCycleId, "EvaluationCycle");
            cycle.setStatus(EvaluationCycleStatus.CLOSED);
            cycle.setClosureDate(Instant.now());
            logger.info("Evaluation cycle with id {} has been closed", evaluationCycleId);
        }
    }

    private void checkAndCompleteEvaluationCycle(Long evaluationCycleId) {
        if (daoFactory.forEvaluation().countOngoingEvaluationsByCycle(evaluationCycleId) == 0) {
            daoFactory.forEvaluationCycle().updateStatus(evaluationCycleId, EvaluationCycleStatus.COMPLETED);
            logger.info("Evaluation cycle with id {} has been completed", evaluationCycleId);
            //TODO maybe sent a notification to admin responsible for cycle
        }
    }

    public Map<String, Long> getEvaluationsCompletedStats(Long requesterId) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        checkIfRequesterIsAdmin(requester);
        List<EvaluationCycle> cycles = daoFactory.forEvaluationCycle().findActive(false);

        if (cycles.isEmpty()) {
            return Collections.emptyMap();
        } else if (cycles.size() > 1) {
            throw new ConflictException("Two or more active evaluation cycles found",
                    "Multiple active evaluation cycles exist — cannot determine the correct one");
        }
        Long cycleId = cycles.getFirst().getId();
        Long total = daoFactory.forEvaluation().countByCycleId(cycleId);
        Long ongoing = daoFactory.forEvaluation().countOngoingEvaluationsByCycle(cycleId);
        Map<String, Long> stats = new HashMap<>();
        stats.put("total", total);
        stats.put("ongoing", ongoing);
        return stats;
    }


}
