FROM python:3.10-slim

WORKDIR /app

# Create model cache directory
RUN mkdir -p /app/models/chatbot_cache

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

COPY . .

EXPOSE 8000

CMD ["uvicorn", "ai_microservice:app", "--host", "0.0.0.0", "--port", "8000"]
