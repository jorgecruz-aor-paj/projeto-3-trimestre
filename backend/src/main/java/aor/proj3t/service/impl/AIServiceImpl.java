package aor.proj3t.service.impl;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.ai.ChatResponseDTO;
import aor.proj3t.dto.ai.PerformancePredictionDTO;
import aor.proj3t.dto.ai.ReportResponseDTO;
import aor.proj3t.dto.ai.SentimentResponseDTO;
import aor.proj3t.dto.ai.TrainingRecommendationDTO;
import aor.proj3t.service.interfaces.AIService;
import aor.proj3t.util.HttpClientUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;

/**
 * Implementation of the AIService interface.
 * This service provides integration with the AI microservice for various
 * artificial intelligence
 * operations including sentiment analysis, chatbot interactions, and AI-powered
 * insights.
 * 
 * <p>
 * Key functionalities include:
 * <ul>
 * <li>Text sentiment analysis with polarity and subjectivity scores</li>
 * <li>Chatbot conversation management and response generation</li>
 * <li>AI-powered evaluation insights and recommendations</li>
 * <li>HTTP client integration with the AI microservice</li>
 * <li>JSON payload construction and response parsing</li>
 * </ul>
 * 
 * <p>
 * This service acts as a bridge between the main application and the AI
 * microservice,
 * handling request formatting, HTTP communication, and response parsing to
 * provide
 * seamless AI functionality integration.
 * 
 * @author Jorge Cruz
 * @version 1.0
 * @since 2025
 */
@ApplicationScoped
public class AIServiceImpl implements AIService {

    private static final Logger LOGGER = LogManager.getLogger(AIServiceImpl.class);
    private static final String AI_SERVICE_BASE_URL = "http://ai-service:8000";

    /**
     * {@inheritDoc}
     */
    @Override
    public SentimentResponseDTO analyzeSentiment(String text) {
        try {
            JsonObject payload = Json.createObjectBuilder()
                    .add("text", text)
                    .build();

            String responseBody = HttpClientUtil.post(AI_SERVICE_BASE_URL + "/analyze-sentiment",
                    payload.toString());

            // Parse the JSON response
            try (JsonReader jsonReader = Json.createReader(new StringReader(responseBody))) {
                JsonObject jsonObject = jsonReader.readObject();

                // Create and populate DTO from JSON
                SentimentResponseDTO dto = new SentimentResponseDTO();
                dto.setPolarity((float) jsonObject.getJsonNumber("polarity").doubleValue());
                dto.setSubjectivity((float) jsonObject.getJsonNumber("subjectivity").doubleValue());
                dto.setSentiment(jsonObject.getString("sentiment"));

                return dto;
            }
        } catch (IOException e) {
            LOGGER.error("Error analyzing sentiment", e);
            return createDefaultSentimentResponse();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public TrainingRecommendationDTO recommendTraining(List<String> evaluations, List<String> skills,
            List<String> trainingsHistory) {
        try {
            JsonObjectBuilder payloadBuilder = Json.createObjectBuilder();

            // Add evaluations
            JsonArrayBuilder evaluationsArray = Json.createArrayBuilder();
            for (String evaluation : evaluations) {
                evaluationsArray.add(evaluation);
            }
            payloadBuilder.add("evaluations", evaluationsArray);

            // Add skills if provided
            if (skills != null && !skills.isEmpty()) {
                JsonArrayBuilder skillsArray = Json.createArrayBuilder();
                for (String skill : skills) {
                    skillsArray.add(skill);
                }
                payloadBuilder.add("skills", skillsArray);
            }

            // Add training history if provided
            if (trainingsHistory != null && !trainingsHistory.isEmpty()) {
                JsonArrayBuilder historyArray = Json.createArrayBuilder();
                for (String training : trainingsHistory) {
                    historyArray.add(training);
                }
                payloadBuilder.add("trainings_history", historyArray);
            }

            String responseBody = HttpClientUtil.post(AI_SERVICE_BASE_URL + "/recommend-training",
                    payloadBuilder.build().toString());

            // Parse the JSON response
            try (JsonReader jsonReader = Json.createReader(new StringReader(responseBody))) {
                JsonObject jsonObject = jsonReader.readObject();

                // Create and populate DTO from JSON
                TrainingRecommendationDTO dto = new TrainingRecommendationDTO();
                dto.setRecommendedTraining(jsonObject.getString("recommended_training"));
                dto.setReason(jsonObject.getString("reason"));

                return dto;
            }
        } catch (IOException e) {
            LOGGER.error("Error recommending training", e);
            return createDefaultTrainingRecommendation();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public PerformancePredictionDTO predictPerformance(List<Float> historicalScores) {
        try {
            JsonObjectBuilder payloadBuilder = Json.createObjectBuilder();

            // Add historical scores
            JsonArrayBuilder scoresArray = Json.createArrayBuilder();
            for (Float score : historicalScores) {
                scoresArray.add(score);
            }
            payloadBuilder.add("historical_scores", scoresArray);

            String responseBody = HttpClientUtil.post(AI_SERVICE_BASE_URL + "/predict-performance",
                    payloadBuilder.build().toString());

            // Parse the JSON response
            try (JsonReader jsonReader = Json.createReader(new StringReader(responseBody))) {
                JsonObject jsonObject = jsonReader.readObject();

                // Create and populate DTO from JSON
                PerformancePredictionDTO dto = new PerformancePredictionDTO();
                dto.setPredictedScore((float) jsonObject.getJsonNumber("predicted_score").doubleValue());
                dto.setConfidence((float) jsonObject.getJsonNumber("confidence").doubleValue());

                return dto;
            }
        } catch (IOException e) {
            LOGGER.error("Error predicting performance", e);
            return createDefaultPerformancePrediction();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ChatResponseDTO getChatbotResponse(String message) {
        try {
            JsonObject payload = Json.createObjectBuilder()
                    .add("message", message)
                    .build();

            String responseBody = HttpClientUtil.post(AI_SERVICE_BASE_URL + "/chatbot",
                    payload.toString());

            // Parse the JSON response
            try (JsonReader jsonReader = Json.createReader(new StringReader(responseBody))) {
                JsonObject jsonObject = jsonReader.readObject();

                // Create and populate DTO from JSON
                ChatResponseDTO dto = new ChatResponseDTO();
                dto.setReply(jsonObject.getString("reply"));

                return dto;
            }
        } catch (IOException e) {
            LOGGER.error("Error getting chatbot response", e);
            return createDefaultChatResponse();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ReportResponseDTO generateReport(List<Float> evaluations, List<String> comments) {
        try {
            JsonObjectBuilder payloadBuilder = Json.createObjectBuilder();

            // Add evaluations
            JsonArrayBuilder evaluationsArray = Json.createArrayBuilder();
            for (Float evaluation : evaluations) {
                evaluationsArray.add(evaluation);
            }
            payloadBuilder.add("evaluations", evaluationsArray);

            // Add comments if provided
            if (comments != null && !comments.isEmpty()) {
                JsonArrayBuilder commentsArray = Json.createArrayBuilder();
                for (String comment : comments) {
                    commentsArray.add(comment);
                }
                payloadBuilder.add("comments", commentsArray);
            }

            String responseBody = HttpClientUtil.post(AI_SERVICE_BASE_URL + "/generate-report",
                    payloadBuilder.build().toString());

            // Parse the JSON response
            try (JsonReader jsonReader = Json.createReader(new StringReader(responseBody))) {
                JsonObject jsonObject = jsonReader.readObject();

                // Create and populate DTO from JSON
                ReportResponseDTO dto = new ReportResponseDTO();
                dto.setAverageScore((float) jsonObject.getJsonNumber("average_score").doubleValue());
                dto.setReport(jsonObject.getString("report"));

                return dto;
            }
        } catch (IOException e) {
            LOGGER.error("Error generating report", e);
            return createDefaultReportResponse();
        }
    }

    /**
     * Create a default sentiment response in case of error
     */
    private SentimentResponseDTO createDefaultSentimentResponse() {
        SentimentResponseDTO response = new SentimentResponseDTO();
        response.setPolarity(0.0f);
        response.setSubjectivity(0.0f);
        response.setSentiment("neutral");
        return response;
    }

    /**
     * Create a default training recommendation in case of error
     */
    private TrainingRecommendationDTO createDefaultTrainingRecommendation() {
        TrainingRecommendationDTO response = new TrainingRecommendationDTO();
        response.setRecommendedTraining("General Skills Training");
        response.setReason("Unable to process the recommendation request. This is a default recommendation.");
        return response;
    }

    /**
     * Create a default performance prediction in case of error
     */
    private PerformancePredictionDTO createDefaultPerformancePrediction() {
        PerformancePredictionDTO response = new PerformancePredictionDTO();
        response.setPredictedScore(3.0f); // Assuming a scale of 1-5
        response.setConfidence(0.5f); // Low confidence as this is a default
        return response;
    }

    /**
     * Create a default chat response in case of error
     */
    private ChatResponseDTO createDefaultChatResponse() {
        ChatResponseDTO response = new ChatResponseDTO();
        response.setReply(
                "Desculpe, estou a ter problemas a processar o seu pedido neste momento. Por favor volte a tentar mais tarde.");
        return response;
    }

    /**
     * Create a default report response in case of error
     */
    private ReportResponseDTO createDefaultReportResponse() {
        ReportResponseDTO response = new ReportResponseDTO();
        response.setAverageScore(0.0f);
        response.setReport("Unable to generate report due to a technical issue. Please try again later.");
        return response;
    }
}
