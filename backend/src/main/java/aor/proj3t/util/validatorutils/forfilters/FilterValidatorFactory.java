package aor.proj3t.util.validatorutils.forfilters;

import aor.proj3t.filters.EvaluationFilter;
import aor.proj3t.filters.TrainingFilter;
import aor.proj3t.filters.UserFilter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Factory class for accessing validators of various filter types.
 * <p>
 * Centralizes instantiation and access to {@link TrainingFilterValidator},
 * {@link UserFilterValidator}, and {@link EvaluationFilterValidator} instances.
 * This class also provides convenient methods for validating filters directly,
 * avoiding the need to manually access each validator.
 * </p>
 *
 * @author Jorge Cruz & João Osório - Gr4
 * @version 1.0
 * @since 1.0
 */
@ApplicationScoped
public class FilterValidatorFactory {

    @Inject
    private TrainingFilterValidator trainingFilterValidator;

    @Inject
    private UserFilterValidator userFilterValidator;

    @Inject
    private EvaluationFilterValidator evaluationFilterValidator;

    /**
     * Retrieves the validator for {@link TrainingFilter}.
     *
     * @return instance of {@link TrainingFilterValidator}
     */
    public TrainingFilterValidator getTrainingValidator() {
        return trainingFilterValidator;
    }

    /**
     * Retrieves the validator for {@link UserFilter}.
     *
     * @return instance of {@link UserFilterValidator}
     */
    public UserFilterValidator getUserValidator() {
        return userFilterValidator;
    }

    /**
     * Retrieves the validator for {@link EvaluationFilter}.
     *
     * @return instance of {@link EvaluationFilterValidator}
     */
    public EvaluationFilterValidator getEvaluationValidator() {
        return evaluationFilterValidator;
    }

    /**
     * Validates a {@link TrainingFilter} using its respective validator.
     *
     * @param filter the training filter to validate
     */
    public void validate(TrainingFilter filter) {
        trainingFilterValidator.validate(filter);
    }

    /**
     * Validates a {@link UserFilter} using its respective validator.
     *
     * @param filter the user filter to validate
     */
    public void validate(UserFilter filter) {
        userFilterValidator.validate(filter);
    }

    /**
     * Validates an {@link EvaluationFilter} using its respective validator.
     *
     * @param filter the evaluation filter to validate
     */
    public void validate(EvaluationFilter filter) {
        evaluationFilterValidator.validate(filter);
    }
}

