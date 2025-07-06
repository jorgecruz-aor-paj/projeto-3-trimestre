package aor.proj3t.service.interfaces;

import aor.proj3t.dto.ai.*;

import java.util.List;

/**
 * Service interface for AI microservice integration
 */
public interface AIService {

    /**
     * Analyze the sentiment of a text
     * 
     * @param text Text to analyze
     * @return Sentiment analysis result
     */
    SentimentResponseDTO analyzeSentiment(String text);

    /**
     * Get training recommendations based on evaluations
     * 
     * @param evaluations      List of evaluation comments
     * @param skills           Optional list of skills
     * @param trainingsHistory Optional list of previous trainings
     * @return Training recommendation
     */
    TrainingRecommendationDTO recommendTraining(List<String> evaluations, List<String> skills,
            List<String> trainingsHistory);

    /**
     * Predict future performance based on historical scores
     * 
     * @param historicalScores List of previous evaluation scores
     * @return Performance prediction
     */
    PerformancePredictionDTO predictPerformance(List<Float> historicalScores);

    /**
     * Get a response from the chatbot
     * 
     * @param message User message
     * @return Chatbot response
     */
    ChatResponseDTO getChatbotResponse(String message);

    /**
     * Generate a report based on evaluation data
     * 
     * @param evaluations List of evaluation scores
     * @param comments    Optional list of comments
     * @return Generated report
     */
    ReportResponseDTO generateReport(List<Float> evaluations, List<String> comments);
}
