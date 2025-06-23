package aor.proj3t.resource;

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
import aor.proj3t.service.ServiceFactory;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/ai")
@RequestScoped
public class AIResource {

    @Inject
    private ServiceFactory serviceFactory;

    /**
     * Analyze the sentiment of a text
     *
     * @param feedback Text to analyze
     * @return Sentiment analysis result
     */
    @POST
    @Path("/analyze-sentiment")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response analyzeSentiment(FeedbackDTO feedback) {
        try {
            SentimentResponseDTO result = serviceFactory.forAI().analyzeSentiment(feedback.getText());
            if (result == null) {
                return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Unable to analyze sentiment").build();
            }
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error analyzing sentiment: " + e.getMessage()).build();
        }
    }

    /**
     * Get training recommendations based on evaluations
     *
     * @param request Training request with evaluations, skills, and training
     *                history
     * @return Training recommendation
     */
    @POST
    @Path("/recommend-training")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response recommendTraining(TrainingRequestDTO request) {
        try {
            TrainingRecommendationDTO result = serviceFactory.forAI().recommendTraining(
                    request.getEvaluations(), request.getSkills(), request.getTrainingsHistory());
            if (result == null) {
                return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Unable to get training recommendations").build();
            }
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error getting training recommendations: " + e.getMessage()).build();
        }
    }

    /**
     * Predict future performance based on historical scores
     *
     * @param request Prediction request with historical scores
     * @return Performance prediction
     */
    @POST
    @Path("/predict-performance")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response predictPerformance(PredictionRequestDTO request) {
        try {
            PerformancePredictionDTO result = serviceFactory.forAI().predictPerformance(
                    request.getHistoricalScores());
            if (result == null) {
                return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Unable to predict performance").build();
            }
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error predicting performance: " + e.getMessage()).build();
        }
    }

    /**
     * Get a response from the chatbot
     *
     * @param request Chat request with user message
     * @return Chatbot response
     */
    @POST
    @Path("/chatbot")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response getChatbotResponse(ChatRequestDTO request) {
        try {
            ChatResponseDTO result = serviceFactory.forAI().getChatbotResponse(request.getMessage());
            if (result == null) {
                return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Unable to get chatbot response").build();
            }
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error getting chatbot response: " + e.getMessage()).build();
        }
    }

    /**
     * Generate a report based on evaluation data
     *
     * @param request Report request with evaluations and comments
     * @return Generated report
     */
    @POST
    @Path("/generate-report")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response generateReport(ReportRequestDTO request) {
        try {
            ReportResponseDTO result = serviceFactory.forAI().generateReport(
                    request.getEvaluations(), request.getComments());
            if (result == null) {
                return Response.status(Response.Status.SERVICE_UNAVAILABLE)
                        .entity("Unable to generate report").build();
            }
            return Response.ok(result).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error generating report: " + e.getMessage()).build();
        }
    }
}
