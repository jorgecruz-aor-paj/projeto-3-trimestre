# AI Microservice Integration with Backend

This document explains how to run the integrated system with the AI microservice.

## Setup Instructions

### 1. Start the AI Microservice

```bash
cd /home/jorgecruz/repositorios/projeto-3-trimestre/ai_microservice
# Install required dependencies if not already installed
pip install -r requirements.txt
# Start the microservice
bash start_microservice.sh
```

The AI microservice will run on port 8000.

### 2. Deploy the Backend

The backend has been updated to communicate with the AI microservice and includes:

- DTOs for communicating with the AI service
- AIService interface and implementation
- Integration with EvaluationService for sentiment analysis
- A new REST API endpoint for AI features

### 3. Test the Integration

#### Test Sentiment Analysis

```bash
curl -X POST -H "Content-Type: application/json" -d '{"text":"Este é um feedback muito positivo sobre o seu trabalho!"}' http://localhost:8080/jorge-osorio-proj3t/api/ai/analyze-sentiment
```

Expected response:

```json
{
  "polarity": 0.8,
  "subjectivity": 0.75,
  "sentiment": "positivo"
}
```

#### Test Training Recommendation

```bash
curl -X POST -H "Content-Type: application/json" -d '{"evaluations":["Precisa melhorar conhecimento em React", "Boa performance em backend"]}' http://localhost:8080/jorge-osorio-proj3t/api/ai/recommend-training
```

#### Test Performance Prediction

```bash
curl -X POST -H "Content-Type: application/json" -d '{"historicalScores":[3.5, 4.0, 3.8]}' http://localhost:8080/jorge-osorio-proj3t/api/ai/predict-performance
```

#### Test Chatbot

```bash
curl -X POST -H "Content-Type: application/json" -d '{"message":"Como preencher avaliação?"}' http://localhost:8080/jorge-osorio-proj3t/api/ai/chatbot
```

#### Test Report Generation

```bash
curl -X POST -H "Content-Type: application/json" -d '{"evaluations":[3.5, 4.0, 3.8], "comments":["Bom trabalho", "Precisa melhorar comunicação"]}' http://localhost:8080/jorge-osorio-proj3t/api/ai/generate-report
```

## System Architecture

The integration follows this architecture:

1. **Frontend**: Calls backend REST APIs
2. **Backend**: Java EE services and endpoints
   - Uses AIService to communicate with AI microservice
   - Integrates AI feedback into core business processes
3. **AI Microservice**: Python FastAPI service
   - Provides AI capabilities like sentiment analysis
   - Returns structured responses to the backend

## Future Enhancements

1. **LLM Integration**: Replace simple rules with a proper LLM
2. **Data Collection**: Store sentiment analysis results for ML training
3. **Load Balancing**: Add multiple AI service instances with load balancing
4. **Message Queue**: Use Apache Kafka for asynchronous processing
