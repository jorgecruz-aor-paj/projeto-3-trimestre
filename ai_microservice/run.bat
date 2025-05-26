@echo off
uvicorn ai_microservice:app --host 0.0.0.0 --port 8000
pause
REM This script starts the AI microservice using Uvicorn.