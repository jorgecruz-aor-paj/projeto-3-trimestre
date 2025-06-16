package aor.proj3t.util.validatorutils.forfilters;

import aor.proj3t.filters.EvaluationFilter;
import aor.proj3t.filters.TrainingFilter;
import aor.proj3t.filters.UserFilter;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

/**
 * Factory class to provide the appropriate validator for each type of filter.
 * This centralizes access to all validators and makes it easy to get the right
 * one.
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
     * Get the validator for TrainingFilter
     * 
     * @return TrainingFilterValidator
     */
    public TrainingFilterValidator getTrainingValidator() {
        return trainingFilterValidator;
    }

    /**
     * Get the validator for UserFilter
     * 
     * @return UserFilterValidator
     */
    public UserFilterValidator getUserValidator() {
        return userFilterValidator;
    }

    /**
     * Get the validator for EvaluationFilter
     * 
     * @return EvaluationFilterValidator
     */
    public EvaluationFilterValidator getEvaluationValidator() {
        return evaluationFilterValidator;
    }

    /**
     * Validate a TrainingFilter
     * 
     * @param filter The filter to validate
     */
    public void validate(TrainingFilter filter) {
        trainingFilterValidator.validate(filter);
    }

    /**
     * Validate a UserFilter
     * 
     * @param filter The filter to validate
     */
    public void validate(UserFilter filter) {
        userFilterValidator.validate(filter);
    }

    /**
     * Validate an EvaluationFilter
     * 
     * @param filter The filter to validate
     */
    public void validate(EvaluationFilter filter) {
        evaluationFilterValidator.validate(filter);
    }
}
