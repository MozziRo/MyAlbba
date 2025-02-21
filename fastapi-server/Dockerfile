FROM python:3.9
WORKDIR /app

# 필수 패키지 및 Chrome / WebDriver 설치
RUN apt-get update && apt-get install -y \
    locales \
    wget \
    unzip \
    curl \
    chromium \
    chromium-driver \
    && locale-gen ko_KR.UTF-8

# Python 패키지 설치
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Selenium 의존성 패키지 추가
RUN pip install webdriver-manager selenium

# 소스 코드 복사
COPY . .

# FastAPI 실행 (자동 리로드 지원)
CMD ["uvicorn", "main:app", "--host", "0.0.0.0", "--port", "8000", "--reload"]
