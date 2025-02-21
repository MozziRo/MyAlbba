from pydantic import BaseModel

class TextRequest(BaseModel):
    """ 요청 데이터 모델 """
    text: str

class SentimentResponse(BaseModel):
    """ 감성 분석 응답 모델 """
    text: str
    sentiment_scores: dict

class KeywordsResponse(BaseModel):
    """ 키워드 추출 응답 모델 """
    text: str
    keywords: list

class CrawlRequest(BaseModel):
    """ 크롤링 요청 """
    store_name: str

class CrawlResponse(BaseModel):
    """ 크롤링 응답 """
    review_text: str
