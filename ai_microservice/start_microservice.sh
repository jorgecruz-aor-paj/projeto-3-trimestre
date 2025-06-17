#!/bin/bash
# This script starts the microservice and waits for it to be ready
uvicorn ai_microservice:app --host 0.0.0.0 --port 8000
read -p "Press any key to continue..."