package aor.proj3t.service.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import aor.proj3t.dto.ai.ChatRequestDTO;
import aor.proj3t.dto.ai.ChatResponseDTO;
import aor.proj3t.dto.ai.FeedbackDTO;
import aor.proj3t.dto.ai.PerformancePredictionDTO;
import aor.proj3t.dto.ai.PredictionRequestDTO;
import aor.proj3t.dto.ai.ReportRequestDTO;
import aor.proj3t.dto.ai.ReportResponseDTO;
import aor.proj3t.dto.ai.SentimentResponseDTO;
import aor.proj3t.dto.ai.TrainingRecommendationDTO;
import aor.proj3t.dto.ai.TrainingRequestDTO;
import aor.proj3t.service.interfaces.AIService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

/**
 * Implementation of AI service that communicates with the AI microservice
 */
@ApplicationScoped
public class AIServiceImpl implements AIService {

    private static final Logger logger = LogManager.getLogger(AIServiceImpl.class);
    // This should be configurable via environment variable or properties file
    private static final String AI_SERVICE_BASE_URL = "http://localhost:8000";

    private final HttpClient httpClient;
    private final Jsonb jsonb;

    public AIServiceImpl() {
        this.httpClient = HttpClient.newHttpClient();
        this.jsonb = JsonbBuilder.create();
    }

    @Override
    public SentimentResponseDTO analyzeSentiment(String text) {
        try {
            FeedbackDTO request = new FeedbackDTO(text);
            String requestBody = jsonb.toJson(request);

            HttpResponse<String> response = sendPostRequest("/analyze-sentiment", requestBody);
            if (response.statusCode() != 200) {
                logger.error("Error analyzing sentiment. Status code: {}", response.statusCode());
                return null;
            }

            return jsonb.fromJson(response.body(), SentimentResponseDTO.class);
        } catch (Exception e) {
            logger.error("Error analyzing sentiment", e);
            return null;
        }
    }

    @Override
    public TrainingRecommendationDTO recommendTraining(List<String> evaluations, List<String> skills,
            List<String> trainingsHistory) {
        try {
            TrainingRequestDTO request = new TrainingRequestDTO(evaluations, skills, trainingsHistory);
            String requestBody = jsonb.toJson(request);

            HttpResponse<String> response = sendPostRequest("/recommend-training", requestBody);
            if (response.statusCode() != 200) {
                logger.error("Error recommending training. Status code: {}", response.statusCode());
                return null;
            }

            return jsonb.fromJson(response.body(), TrainingRecommendationDTO.class);
        } catch (Exception e) {
            logger.error("Error recommending training", e);
            return null;
        }
    }

    @Override
    public PerformancePredictionDTO predictPerformance(List<Float> historicalScores) {
        try {
            PredictionRequestDTO request = new PredictionRequestDTO(historicalScores);
            String requestBody = jsonb.toJson(request);

            HttpResponse<String> response = sendPostRequest("/predict-performance", requestBody);
            if (response.statusCode() != 200) {
                logger.error("Error predicting performance. Status code: {}", response.statusCode());
                return null;
            }

            return jsonb.fromJson(response.body(), PerformancePredictionDTO.class);
        } catch (Exception e) {
            logger.error("Error predicting performance", e);
            return null;
        }
    }

    @Override
    public ChatResponseDTO getChatbotResponse(String message) {
        try {
            ChatRequestDTO request = new ChatRequestDTO(message);
            String requestBody = jsonb.toJson(request);

            HttpResponse<String> response = sendPostRequest("/chatbot", requestBody);
            if (response.statusCode() != 200) {
                logger.error("Error getting chatbot response. Status code: {}", response.statusCode());
                return null;
            }

            return jsonb.fromJson(response.body(), ChatResponseDTO.class);
        } catch (Exception e) {
            logger.error("Error getting chatbot response", e);
            return null;
        }
    }

    @Override
    public ReportResponseDTO generateReport(List<Float> evaluations, List<String> comments) {
        try {
            ReportRequestDTO request = new ReportRequestDTO(evaluations, comments);
            String requestBody = jsonb.toJson(request);

            HttpResponse<String> response = sendPostRequest("/generate-report", requestBody);
            if (response.statusCode() != 200) {
                logger.error("Error generating report. Status code: {}", response.statusCode());
                return null;
            }

            return jsonb.fromJson(response.body(), ReportResponseDTO.class);
        } catch (Exception e) {
            logger.error("Error generating report", e);
            return null;
        }
    }

    /**
     * Helper method to send HTTP POST requests to the AI microservice
     */
    private HttpResponse<String> sendPostRequest(String endpoint, String requestBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(AI_SERVICE_BASE_URL + endpoint))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }
}
