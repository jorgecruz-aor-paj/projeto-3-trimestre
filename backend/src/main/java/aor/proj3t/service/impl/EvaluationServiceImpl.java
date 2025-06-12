package aor.proj3t.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationScale;
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
    public Boolean updateEvaluation(Long evaluationId, Integer score, String feedback) {
        try {
            Evaluation evaluation = getEntity(daoFactory.forEvaluation().findById(evaluationId), evaluationId, "Evaluation");
            evaluation.setScale(EvaluationScale.getByScore(score));
            evaluation.setFeedback(feedback);
            daoFactory.forEvaluation().update(evaluation);
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Evaluation with id {} not found", evaluationId);
            return false;
        } catch (IllegalArgumentException e) {
            logger.error("Invalid evaluation score value of {}", score, e);
            return false;
        } catch (Exception e) {
            logger.error("Error while updating evaluation with id {}", evaluationId, e);
            return false;
        }
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
    public Boolean closeEvaluationsBulk(Long[] evaluationIds) {
        try {
            if (evaluationIds.length == 0) {
                logger.warn("No evaluation IDs provided for closing evaluations in bulk");
                return true;
            }
            List<Evaluation> evaluations = getEvaluationListFromIds(evaluationIds);
            if (evaluations == null || evaluations.isEmpty()) {
                logger.error("No valid users Id's assigning manager in bulk. ");
                return false;
            }
            for (Evaluation evaluation : evaluations) {
                evaluation.setStatus(EvaluationStatus.CLOSED);
                daoFactory.forEvaluation().update(evaluation);
            }
            checkAndCloseEvaluationCycle(evaluations.get(0).getEvaluationCycle().getId());
            return true;
        } catch (NoSuchElementException e) {
            logger.error("Evaluation not found while closing in bulk", e);
            return false;
        } catch (Exception e) {
            logger.error("Error while marking evaluations as closed in bulk", e);
            return false;
        }
    }

    @Override
    public int closeAllCompletedEvaluations() {
        try {
            Integer closedEvaluations = daoFactory.forEvaluation().closeAllCompletedEvaluations();
            return closedEvaluations == null ? 0 : closedEvaluations;
        } catch (Exception e) {
            logger.error("Error while closing all complete evaluations", e);
            return -1;
        }
    }

    private List<Evaluation> getEvaluationListFromIds(Long[] evaluationIds) {
        try {
            List<Evaluation> evaluations = new ArrayList<>(evaluationIds.length);
            for (Long evaluationId : evaluationIds) {
                Evaluation evaluation = getEntity(daoFactory.forEvaluation().findById(evaluationId), evaluationId, "Evaluation");
                if (evaluation != null) {
                    evaluations.add(evaluation);
                } else {
                    logger.error("Evaluation not found with ID: " + evaluationId);
                }
            }
            return evaluations;
        } catch (NoSuchElementException e) {
            logger.error("Evaluation not found with ID: " + evaluationIds[0]);
            return null;
        } catch (Exception e) {
            logger.error("Error getting user list. ", e);
            return null;
        }
    }

    private void checkAndCloseEvaluationCycle(Long evaluationCycleId) {
        if (daoFactory.forEvaluation().countUnclosedEvaluationsByCycleId(evaluationCycleId) == 0) {
            daoFactory.forEvaluationCycle().updateStatus(evaluationCycleId, EvaluationStatus.CLOSED);
            logger.info("Evaluation cycle with id {} has been closed", evaluationCycleId);
        }
    }
}
