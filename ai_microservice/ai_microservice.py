from fastapi import FastAPI
from pydantic import BaseModel
from textblob import TextBlob
from typing import List

app = FastAPI(title="Microserviço de IA para Gestão de Desempenho", version="1.0")


# =======================
# 1. Análise de Feedback e Sentimentos
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
    Análise simples de sentimento usando TextBlob.
    Retorna a polaridade, subjetividade e um rótulo (positivo, negativo, neutro).
    """
    blob = TextBlob(feedback.text)
    polarity = blob.sentiment.polarity
    subjectivity = blob.sentiment.subjectivity
    if polarity > 0.1:
        sentiment = "positivo"
    elif polarity < -0.1:
        sentiment = "negativo"
    else:
        sentiment = "neutro"
    return SentimentResponse(polarity=polarity, subjectivity=subjectivity, sentiment=sentiment)


# =======================
# 2. Recomendações Personalizadas de Treinamento
# =======================

class TrainingRequest(BaseModel):
    evaluations: List[str]  # Feedbacks ou comentários das avaliações
    skills: List[str] = []
    trainings_history: List[str] = []

class TrainingRecommendation(BaseModel):
    recommended_training: str
    reason: str

@app.post("/recommend-training", response_model=TrainingRecommendation)
async def recommend_training(req: TrainingRequest):
    """
    Simulação de recomendação de treinamento.
    Utiliza regras básicas para sugerir um curso com base nas avaliações.
    Em uma implementação real, esta função pode invocar um modelo de recomendação.
    """
    if any("React" in eval for eval in req.evaluations):
        return TrainingRecommendation(
            recommended_training="Advanced React Workshop",
            reason="Feedbacks indicam um interesse ou necessidade de aprofundar conhecimento em React."
        )
    elif any("backend" in eval.lower() for eval in req.evaluations):
        return TrainingRecommendation(
            recommended_training="Jakarta EE Deep Dive",
            reason="Os comentários sugerem que o foco de melhoria está na área de backend."
        )
    else:
        return TrainingRecommendation(
            recommended_training="Soft Skills Training",
            reason="Não foram detectados pontos técnicos específicos. Recomenda-se desenvolver soft skills."
        )


# =======================
# 3. Previsão de Tendências e Performance
# =======================

class PredictionRequest(BaseModel):
    historical_scores: List[float]  # Histórico de avaliações (por exemplo, notas de 1 a 4)
    extra_feature: float = None

class PerformancePrediction(BaseModel):
    predicted_score: float
    confidence: float

@app.post("/predict-performance", response_model=PerformancePrediction)
async def predict_performance(req: PredictionRequest):
    """
    Previsão simples da performance usando a média dos scores históricos.
    Em cenários reais, este endpoint pode invocar modelos de machine learning
    treinados para previsão de desempenho.
    """
    if not req.historical_scores:
        return PerformancePrediction(predicted_score=0.0, confidence=0.0)
    predicted_score = sum(req.historical_scores) / len(req.historical_scores)
    # Aqui, o 'confidence' é estático; em um modelo real, seria calculado.
    return PerformancePrediction(predicted_score=predicted_score, confidence=0.85)


# =======================
# 4. Assistência via Chatbot e Suporte Automatizado
# =======================

class ChatRequest(BaseModel):
    message: str

class ChatResponse(BaseModel):
    reply: str

@app.post("/chatbot", response_model=ChatResponse)
async def chatbot(req: ChatRequest):
    """
    Chatbot rudimentar que responde a dúvidas comuns. O ideal é integrá-lo com um modelo
    conversacional (por exemplo, usando uma API de modelo de linguagem) para respostas mais elaboradas.
    """
    lower_msg = req.message.lower()
    if "avaliação" in lower_msg:
        reply = "Para preencher a avaliação, acesse a seção 'Avaliações' no menu principal do sistema."
    elif "erro" in lower_msg:
        reply = ("Sinto muito pelo inconveniente. Por favor, tente reiniciar a aplicação ou contate o suporte técnico.")
    else:
        reply = "Desculpe, não entendi sua pergunta. Poderia reformular, por favor?"
    return ChatResponse(reply=reply)


# =======================
# 5. Automação de Processos Administrativos
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
    Gera um relatório simples com a média das avaliações e uma mensagem de resumo.
    Pode ser estendido para incluir dashboards e insights mais detalhados.
    """
    if req.evaluations:
        avg = sum(req.evaluations) / len(req.evaluations)
    else:
        avg = 0.0
    report = f"A média das avaliações é {avg:.2f}. Recomenda-se foco nas áreas com scores abaixo de 3."
    return ReportResponse(average_score=avg, report=report)
