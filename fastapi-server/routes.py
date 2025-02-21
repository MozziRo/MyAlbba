from fastapi import APIRouter
from models import TextRequest, SentimentResponse, KeywordsResponse, CrawlRequest, CrawlResponse
from services import analyze_sentiment, extract_keywords, perform_crawling
from typing import List

router = APIRouter()

@router.post("/analyze_sentiment/", response_model=SentimentResponse)
async def analyze_sentiment_endpoint(request: TextRequest):
    """ 감성 분석 API """
    if not request.text or request.text.strip() == "":
        raise HTTPException(status_code=400, detail="텍스트 입력이 비어 있습니다.")
    
    sentiment_scores = analyze_sentiment(request.text)

    if not sentiment_scores:
        raise HTTPException(status_code=500, detail="감성 분석 결과를 생성할 수 없습니다.")

    return SentimentResponse(text=request.text, sentiment_scores=sentiment_scores)
@router.post("/extract_keywords/", response_model=KeywordsResponse)
async def extract_keywords_endpoint(request: TextRequest):
    """ 키워드 추출 API """
    keywords = extract_keywords(request.text)
    return KeywordsResponse(text=request.text, keywords=keywords)

@router.post("/crawl_and_save/", response_model=List[CrawlResponse])
async def crawl_and_save(request: CrawlRequest):
    """ 크롤링 및 저장 API """
    reviews = perform_crawling(request.store_name)

    if not reviews:
        return []

    response_data = [
        CrawlResponse(review_text=review)
        for review in reviews
    ]

    return response_data