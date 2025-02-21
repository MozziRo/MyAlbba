import os

class Config:
    APP_NAME = "FastAPI Sentiment & Keyword Analysis"
    VERSION = "1.0.0"
    DEBUG = os.getenv("DEBUG", False)
    HOST = os.getenv("HOST", "0.0.0.0")
    PORT = int(os.getenv("PORT", 8000))

config = Config()
