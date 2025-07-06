from fastapi import FastAPI
from pydantic import BaseModel
from textblob import TextBlob
from typing import List

# Import the LLM chatbot
from chatbot import llm_chatbot

app = FastAPI(title="AI Microservice for Performance Management", version="1.0")


# =======================
# 1. Feedback and Sentiment Analysis
# =======================

class Feedback(BaseModel):
    text: str

class SentimentResponse(BaseModel):
    polarity: float
    subjectivity: float
    sentiment: str

@app.post("/analyze-sentiment", response_model=SentimentResponse)
async def analyze_sentiment(feedback: Feedback):
    """
    Simple sentiment analysis using TextBlob.
    Returns polarity, subjectivity, and a label (positive, negative, neutral).
    """
    blob = TextBlob(feedback.text)
    polarity = blob.sentiment.polarity
    subjectivity = blob.sentiment.subjectivity
    if polarity > 0.1:
        sentiment = "positive"
    elif polarity < -0.1:
        sentiment = "negative"
    else:
        sentiment = "neutral"
    return SentimentResponse(polarity=polarity, subjectivity=subjectivity, sentiment=sentiment)


# =======================
# 2. Personalized Training Recommendations
# =======================

class TrainingRequest(BaseModel):
    evaluations: List[str]  # Feedback or comments from evaluations
    skills: List[str] = []
    trainings_history: List[str] = []

class TrainingRecommendation(BaseModel):
    recommended_training: str
    reason: str

@app.post("/recommend-training", response_model=TrainingRecommendation)
async def recommend_training(req: TrainingRequest):
    """
    Training recommendation simulation.
    Uses basic rules to suggest a course based on evaluations.
    In a real implementation, this function could invoke a recommendation model.
    """
    if any("React" in eval for eval in req.evaluations):
        return TrainingRecommendation(
            recommended_training="Advanced React Workshop",
            reason="Feedback indicates an interest or need to deepen knowledge in React."
        )
    elif any("backend" in eval.lower() for eval in req.evaluations):
        return TrainingRecommendation(
            recommended_training="Jakarta EE Deep Dive",
            reason="The comments suggest that the focus for improvement is in the backend area."
        )
    else:
        return TrainingRecommendation(
            recommended_training="Soft Skills Training",
            reason="No specific technical points were detected. It is recommended to develop soft skills."
        )


# =======================
# 3. Trends and Performance Prediction
# =======================

class PredictionRequest(BaseModel):
    historical_scores: List[float]  # History of evaluations (for example, scores from 1 to 4)
    extra_feature: float = None

class PerformancePrediction(BaseModel):
    predicted_score: float
    confidence: float

@app.post("/predict-performance", response_model=PerformancePrediction)
async def predict_performance(req: PredictionRequest):
    """
    Simple performance prediction using the average of historical scores.
    In real scenarios, this endpoint could invoke machine learning models
    trained for performance prediction.
    """
    if not req.historical_scores:
        return PerformancePrediction(predicted_score=0.0, confidence=0.0)
    predicted_score = sum(req.historical_scores) / len(req.historical_scores)
    # Here, the 'confidence' is static; in a real model, it would be calculated.
    return PerformancePrediction(predicted_score=predicted_score, confidence=0.85)


# =======================
# 4. Chatbot Assistance and Automated Support
# =======================

class ChatRequest(BaseModel):
    message: str

class ChatResponse(BaseModel):
    reply: str

@app.post("/chatbot", response_model=ChatResponse)
async def chatbot(req: ChatRequest):
    """
    Enhanced chatbot using fine-tuned LLM
    """
    try:
        print(f"🎯 Chatbot request: '{req.message}'")
        
        # Create context about the training system
        context = """
        This is a training and performance evaluation management system.
        Users can view trainings, participate, view evaluations, and get recommendations.
        The system has features for messaging, profile management, and AI assistance.
        """
        
        reply = llm_chatbot.generate_response(req.message, context)
        
        print(f"🎯 Chatbot response: '{reply[:100]}...'")
        
        return ChatResponse(reply=reply)
        
    except Exception as e:
        print(f"❌ Chatbot error: {e}")
        return simple_chatbot_fallback(req.message)

def simple_chatbot_fallback(message: str) -> ChatResponse:
    """Improved fallback logic"""
    lower_msg = message.lower()
    
    # More specific keyword matching
    if any(word in lower_msg for word in ["evaluation", "evaluate", "performance"]):
        reply = "To access evaluations, go to the 'Evaluations' section in the main menu. You can view your evaluation history and current evaluation status there."
    elif any(word in lower_msg for word in ["training", "course", "learn"]):
        reply = "You can find available trainings in the 'Trainings' section. Your manager can register training participations for you, and you can view your training history in your profile."
    elif any(word in lower_msg for word in ["message", "chat", "communication"]):
        reply = "Use the Messages section to communicate with colleagues. You can start new conversations and receive real-time messages."
    elif any(word in lower_msg for word in ["login", "password", "account"]):
        reply = "For login issues, check your credentials or use 'Forgot Password'. For account registration, contact your administrator."
    elif any(word in lower_msg for word in ["error", "problem", "issue"]):
        reply = "Try refreshing the page or clearing your browser cache. If problems persist, contact your system administrator for technical support."
    else:
        reply = "I'm here to help with the Training and Performance Management System. You can ask about evaluations, trainings, messages, user accounts, or system navigation."
    
    return ChatResponse(reply=reply)


# =======================
# 5. Administrative Process Automation
# =======================

class ReportRequest(BaseModel):
    evaluations: List[float]
    comments: List[str] = []

class ReportResponse(BaseModel):
    average_score: float
    report: str

@app.post("/generate-report", response_model=ReportResponse)
async def generate_report(req: ReportRequest):
    """
    Generates a simple report with the average of evaluations and a summary message.
    Can be extended to include dashboards and more detailed insights.
    """
    if req.evaluations:
        avg = sum(req.evaluations) / len(req.evaluations)
    else:
        avg = 0.0
    report = f"The average of the evaluations is {avg:.2f}. Focus is recommended on areas with scores below 3."
    return ReportResponse(average_score=avg, report=report)

@app.get("/health")
async def health_check():
    """Health check endpoint for Docker."""
    return {"status": "healthy", "service": "ai-microservice"}