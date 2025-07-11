package aor.proj3t.service.interfaces;

import aor.proj3t.dto.ai.*;

import java.util.List;

/**
 * Service interface for AI microservice integration and artificial intelligence
 * operations.
 * This interface defines the contract for AI-powered functionality including
 * sentiment analysis,
 * machine learning predictions, chatbot interactions, and intelligent insights
 * generation.
 * 
 * <p>
 * Key AI services include:
 * <ul>
 * <li><b>Sentiment Analysis:</b> Text emotion and sentiment classification with
 * polarity scoring</li>
 * <li><b>Training Recommendations:</b> AI-powered training suggestions based on
 * evaluation feedback</li>
 * <li><b>Performance Prediction:</b> Machine learning models for future
 * performance forecasting</li>
 * <li><b>Chatbot Integration:</b> Natural language processing for user
 * assistance and queries</li>
 * <li><b>Report Generation:</b> AI-assisted report creation with data analysis
 * and insights</li>
 * </ul>
 * 
 * <p>
 * This service acts as a bridge between the main application and the AI
 * microservice,
 * handling request formatting, HTTP communication, and response parsing to
 * provide
 * seamless AI functionality integration throughout the evaluation and training
 * systems.
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 2025
 */
public interface AIService {

    /**
     * Analyzes the sentiment of the provided text using AI microservice.
     * Returns comprehensive sentiment metrics including polarity, subjectivity, and
     * classification.
     * 
     * @param text the text content to analyze for emotional sentiment and tone
     * @return SentimentResponseDTO containing polarity score, subjectivity, and
     *         sentiment label
     * @throws RuntimeException if communication with the AI service fails
     */
    SentimentResponseDTO analyzeSentiment(String text);

    /**
     * Generates personalized training recommendations based on evaluation feedback
     * and history.
     * Uses machine learning algorithms to suggest relevant training programs.
     * 
     * @param evaluations      list of evaluation comments and feedback text for
     *                         analysis
     * @param skills           optional list of user skills for personalized
     *                         recommendations
     * @param trainingsHistory optional list of previously completed training
     *                         programs
     * @return TrainingRecommendationDTO containing suggested training programs and
     *         reasoning
     */
    TrainingRecommendationDTO recommendTraining(List<String> evaluations, List<String> skills,
            List<String> trainingsHistory);

    /**
     * Predicts future performance trends based on historical evaluation scores.
     * Employs time series analysis and machine learning for performance
     * forecasting.
     * 
     * @param historicalScores list of previous evaluation scores for trend analysis
     * @return PerformancePredictionDTO containing predicted scores and confidence
     *         intervals
     */
    PerformancePredictionDTO predictPerformance(List<Float> historicalScores);

    /**
     * Processes user messages through the AI chatbot for intelligent responses.
     * Provides natural language understanding and contextual assistance.
     * 
     * @param message the user's natural language message or question
     * @return ChatResponseDTO containing the chatbot's intelligent response
     */
    ChatResponseDTO getChatbotResponse(String message);

    /**
     * Generates comprehensive reports based on evaluation data using AI analysis.
     * Creates intelligent insights and summaries from evaluation scores and
     * comments.
     * 
     * @param evaluations list of numerical evaluation scores for statistical
     *                    analysis
     * @param comments    optional list of textual comments for qualitative analysis
     * @return ReportResponseDTO containing AI-generated report with insights and
     *         recommendations
     */
    ReportResponseDTO generateReport(List<Float> evaluations, List<String> comments);
}
