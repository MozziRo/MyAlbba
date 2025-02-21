from fastapi import FastAPI
from routes import router
import uvicorn

app = FastAPI(
    title="Sentiment & Keyword Analysis API",
    description="This API analyzes text using VADER for sentiment analysis and YAKE for keyword extraction.",
    version="1.0.0",
)


app.include_router(router)

@app.get("/")
async def root():
    return {"message": "FastAPI Sentiment & Keyword Analysis Service is Running"}

if __name__ == "__main__":
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
