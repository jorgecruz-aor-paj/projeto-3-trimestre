package aor.proj3t.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.daos.DAOFactory;
import aor.proj3t.dto.EvaluationCloseDTO;
import aor.proj3t.dto.EvaluationDTO;
import aor.proj3t.dto.ai.SentimentResponseDTO;
import aor.proj3t.entities.Evaluation;
import aor.proj3t.entities.EvaluationCycle;
import aor.proj3t.entities.Notification;
import aor.proj3t.entities.User;
import aor.proj3t.enums.EvaluationCycleStatus;
import aor.proj3t.enums.EvaluationStatus;
import aor.proj3t.enums.NotificationType;
import aor.proj3t.enums.Role;
import aor.proj3t.exceptions.BadRequestException;
import aor.proj3t.exceptions.ConflictException;
import aor.proj3t.exceptions.ForbiddenException;
import aor.proj3t.exceptions.NotFoundException;
import aor.proj3t.filters.EvaluationFilter;
import aor.proj3t.service.ServiceFactory;
import aor.proj3t.service.interfaces.AuthorizationService;
import aor.proj3t.service.interfaces.EvaluationService;
import aor.proj3t.util.converterutils.ConverterFactory;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Implementation of the EvaluationService interface.
 * This comprehensive service manages the complete evaluation lifecycle
 * including creation,
 * submission, feedback collection, approval workflows, and AI-powered sentiment
 * analysis.
 * 
 * <p>
 * Key functionalities include:
 * <ul>
 * <li>Evaluation creation and management within evaluation cycles</li>
 * <li>Self-evaluation and manager feedback collection</li>
 * <li>Multi-stage approval workflows (manager, admin)</li>
 * <li>AI sentiment analysis integration for feedback text</li>
 * <li>Evaluation statistics and reporting</li>
 * <li>Notification system integration for workflow events</li>
 * <li>Authorization and permission validation</li>
 * </ul>
 * 
 * <p>
 * This service ensures proper evaluation workflow progression, maintains data
 * integrity,
 * and provides comprehensive evaluation tracking and reporting capabilities.
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 2025
 */
@ApplicationScoped
public class EvaluationServiceImpl extends BaseService implements EvaluationService {
    private static final Logger logger = LogManager.getLogger(EvaluationServiceImpl.class);

    @Inject
    DAOFactory daoFactory;

    @Inject
    ConverterFactory converterFactory;

    @Inject
    ServiceFactory serviceFactory;

    @Override
    public EvaluationDTO updateEvaluation(Long requesterId, EvaluationDTO evaluationDTO) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");
        Evaluation evaluation = getEntity(daoFactory.forEvaluation().findById(evaluationDTO.getId()),
                evaluationDTO.getId(),
                "Evaluation");

        // Validate status change permissions based on user role
        EvaluationStatus currentStatus = evaluation.getStatus();
        EvaluationStatus newStatus = evaluationDTO.getStatus();

        if (currentStatus != newStatus) {
            if (requester.getRole() == Role.MANAGER) {
                // Managers can only change status from ONGOING to COMPLETED
                if (currentStatus != EvaluationStatus.ONGOING || newStatus != EvaluationStatus.COMPLETED) {
                    throw new ForbiddenException(
                            "Status change not allowed",
                            "Manager with id " + requesterId + " attempted to change evaluation status from "
                                    + currentStatus + " to " + newStatus
                                    + ". Managers can only change from ONGOING to COMPLETED.");
                }
            } else if (requester.getRole() == Role.USER) {
                // Regular users cannot change status at all
                throw new ForbiddenException(
                        "Status change not allowed",
                        "User with id " + requesterId
                                + " attempted to change evaluation status. Only admins and managers can change evaluation status.");
            }
            // Admins (Role.ADMIN) can change status to any value - no restrictions
        }

        evaluation.setScale(evaluationDTO.getScale());
        evaluation.setFeedback(evaluationDTO.getFeedback());
        evaluation.setStatus(evaluationDTO.getStatus());

        // Perform sentiment analysis on feedback if provided
        String feedback = evaluationDTO.getFeedback();
        if (feedback != null && !feedback.trim().isEmpty()) {
            try {
                SentimentResponseDTO sentimentResult = serviceFactory.forAI().analyzeSentiment(feedback);
                if (sentimentResult != null) {
                    logger.info("Feedback sentiment analysis: sentiment={}, polarity={}, subjectivity={}",
                            sentimentResult.getSentiment(),
                            sentimentResult.getPolarity(),
                            sentimentResult.getSubjectivity());
                    // TODO
                    // Store sentiment information in the evaluation (optional)
                    // Maybe add a field to Evaluation entity to store this info
                }
            } catch (Exception e) {
                // Don't fail the evaluation update if sentiment analysis fails
                logger.warn("Error during sentiment analysis, continuing with evaluation update", e);
            }
        }

        daoFactory.forEvaluation().update(evaluation);

        // Send notification to the evaluated user about the evaluation update
        try {
            User evaluator = evaluation.getEvaluator();
            User evaluated = evaluation.getEvaluated();
            if (evaluation.getStatus() == EvaluationStatus.COMPLETED) {
                Notification notification = new Notification();
                notification.setSender(evaluator);
                notification.setRecipient(evaluated);
                notification.setType(NotificationType.EVALUATION);
                notification.setContent("Your evaluation has been completed by " + evaluator.getFirstName() + " "
                        + evaluator.getLastName());
                serviceFactory.forNotification().createNotification(notification);
            }
        } catch (Exception e) {
            logger.error("Failed to send notification for updated evaluation {}", evaluation.getId(), e);
        }

        checkAndCompleteEvaluationCycle(evaluation.getEvaluationCycle().getId());
        checkAndCloseEvaluationCycle(evaluation.getEvaluationCycle().getId());
        return converterFactory.forEvaluation().toDTO(evaluation);
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
                    "User with id " + requesterId + " tried closing evaluations from closed cycle with id "
                            + dto.getCycleId());
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
    public List<EvaluationDTO> getEvaluationsByFilter(Long requesterId, EvaluationFilter filter) {
        User requester = getEntity(daoFactory.forUser().findById(requesterId), requesterId, "User");

        // Authorize access based on filter parameters
        AuthorizationService authService = serviceFactory.forAuthorization();
        authService.authorizeEvaluationAccess(requester, filter.getEvaluatedId(), filter.getEvaluatorId());

        // Apply default filters based on role
        if (requester.getRole().equals(Role.USER)) {
            // Users can only see their own evaluations
            filter.setEvaluatedId(requesterId);
            filter.setCycleClosed(true);
        } else if (requester.getRole().equals(Role.MANAGER) && filter.getEvaluatedId() == null
                && filter.getEvaluatorId() == null) {
            // If no specific filter is provided, limit managers to evaluations they
            // performed
            filter.setEvaluatorId(requesterId);
        }
        // Admin has no restrictions
        filter.hydrate();
        List<Evaluation> evaluation = daoFactory.forEvaluation().getEvaluationsByFilters(filter);
        return converterFactory.forEvaluation().toDTOList(evaluation);
    }

    private List<Evaluation> getEvaluationListFromIds(List<Long> evaluationIds) {

        List<Evaluation> evaluations = new ArrayList<>(evaluationIds.size());
        for (Long evaluationId : evaluationIds) {
            Evaluation evaluation = getEntity(daoFactory.forEvaluation().findById(evaluationId), evaluationId,
                    "Evaluation");
            if (evaluation != null) {
                evaluations.add(evaluation);
            } else {
                logger.error("Evaluation not found with ID: " + evaluationId);
            }
        }
        return evaluations;
    }

    private void checkAndCloseEvaluationCycle(Long evaluationCycleId) {
        if (daoFactory.forEvaluation().countUnclosedEvaluationsByCycleId(evaluationCycleId) > 0) {
            return;
        }
        EvaluationCycle cycle = getEntity(daoFactory.forEvaluationCycle().findById(evaluationCycleId),
                evaluationCycleId, "EvaluationCycle");
        cycle.setStatus(EvaluationCycleStatus.CLOSED);
        cycle.setClosureDate(Instant.now());
        daoFactory.forEvaluationCycle().update(cycle);
        logger.info("Evaluation cycle with id {} has been closed", evaluationCycleId);

        // Send emails asynchronously to avoid transaction timeout
        serviceFactory.forAsyncEmail().sendCycleClosureEmailAsync(evaluationCycleId);
    }

    private void checkAndCompleteEvaluationCycle(Long evaluationCycleId) {
        if (daoFactory.forEvaluation().countOngoingEvaluationsByCycle(evaluationCycleId) > 0) {
            return;
        }
        daoFactory.forEvaluationCycle().updateStatus(evaluationCycleId, EvaluationCycleStatus.COMPLETED);
        logger.info("Evaluation cycle with id {} has been completed", evaluationCycleId);

        // Send notification to admin responsible for cycle
        try {
            EvaluationCycle cycle = getEntity(daoFactory.forEvaluationCycle().findById(evaluationCycleId),
                    evaluationCycleId, "EvaluationCycle");
            User admin = cycle.getCreatedBy();
            if (admin != null) {
                Notification notification = new Notification();
                notification.setSender(admin);
                notification.setRecipient(admin);
                notification.setType(NotificationType.CYCLE);
                notification.setContent("Evaluation cycle completed: All evaluations in cycle " + evaluationCycleId
                        + " have been completed.");
                serviceFactory.forNotification().createNotification(notification);
            }
        } catch (Exception e) {
            logger.error("Failed to send notification for completed evaluation cycle {}", evaluationCycleId, e);
        }

    }

    @Override
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
