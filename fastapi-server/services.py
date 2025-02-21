import nltk
from nltk.sentiment import SentimentIntensityAnalyzer
import yake
from googletrans import Translator
import numpy as np
from selenium import webdriver

from webdriver_manager.chrome import ChromeDriverManager
from selenium.webdriver.common.keys import Keys
from selenium.webdriver.common.by import By
from urllib.parse import quote
import time



nltk.download("vader_lexicon")


sia = SentimentIntensityAnalyzer()


translator = Translator()

def translate_to_english(text: str) -> str:
    try:
        translated_text = translator.translate(text, src="ko", dest="en").text
        return translated_text
    except Exception as e:
        print(f"your error is : {e}")
        return text  

def analyze_sentiment(text: str) -> dict:
    """ sentiment analysis by using VADER """

 
    if any(ord(char) > 128 for char in text):
        text = translate_to_english(text)

    
    vader_scores = sia.polarity_scores(text)

    return {
        "neg": float(vader_scores["neg"]) if not np.isnan(vader_scores["neg"]) else 0,
        "neu": float(vader_scores["neu"]) if not np.isnan(vader_scores["neu"]) else 0,
        "pos": float(vader_scores["pos"]) if not np.isnan(vader_scores["pos"]) else 0,
    }


def extract_keywords(text: str) -> list:
    """ YAKE를 이용한 키워드 추출 """
    kw_extractor = yake.KeywordExtractor(lan="ko", n=2, top=5)  # 한국어 지원
    keywords = kw_extractor.extract_keywords(text)
    return [kw[0] for kw in keywords]


def perform_crawling(store_name: str) -> list:
    """Selenium을 사용해 네이버 지도에서 리뷰 크롤링"""
    base_url = 'https://map.naver.com/p/search/'
    encoded_store_name = quote(store_name)
    search_url = f"{base_url}{encoded_store_name}"

    # Chrome WebDriver 설정
    options = webdriver.ChromeOptions()
    options.add_argument('user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36')
    options.add_argument('window-size=1380,900')
    options.add_argument('--disable-blink-features=AutomationControlled')  # Selenium 감지 방지
    options.add_argument('--headless')  # 필요 시 headless 모드 실행
    options.add_argument('--disable-gpu')
    options.add_argument('--no-sandbox')

    # WebDriver 실행
    driver = webdriver.Chrome(options=options)
    
    try:
        driver.get(search_url)
        time.sleep(8)  # 페이지 로드 대기

        # searchIframe으로 전환
        driver.switch_to.frame(driver.find_element(By.ID, "searchIframe"))
        time.sleep(3)

        try:
            # entryIframe이 직접 있는지 확인
            driver.switch_to.default_content()
            driver.switch_to.frame(driver.find_element(By.XPATH, "//iframe[@title='Naver Place Entry']"))
            print("entryIframe 바로 접근 가능")
        except:
            print("entryIframe 직접 접근 불가, 첫 번째 가게 클릭 시도")

            # searchIframe에서 첫 번째 가게 클릭
            driver.switch_to.frame(driver.find_element(By.ID, "searchIframe"))
            time.sleep(3)

            first_store = driver.find_element(By.CSS_SELECTOR, "ul > li.UEzoS.rTjJo .place_bluelink")
            print("클릭 대상 텍스트:", first_store.text)
            first_store.click()
            time.sleep(5)  # entryIframe 로딩 대기

            # entryIframe으로 전환
            driver.switch_to.default_content()
            time.sleep(3)
            driver.switch_to.frame(driver.find_element(By.XPATH, "//iframe[@title='Naver Place Entry']"))
            print("entryIframe 로드 완료")

        # 리뷰 탭 클릭 (WebDriverWait 사용 안 함)
        try:
            time.sleep(5)  # 요소 로딩을 기다리기 위한 딜레이 (필요에 따라 조정)
            review_tab = driver.find_element(By.XPATH, "//a[contains(@class, '_tab-menu') and contains(., '리뷰')]")
            review_tab.click()
            print("리뷰 탭 클릭 완료")
            time.sleep(5)
        except Exception as e:
            print(f"리뷰 탭 클릭 실패: {e}")
            return []



        all_reviews = []
        while len(all_reviews) < 20:

            try:
                reviews = driver.find_elements(By.CSS_SELECTOR, ".pui__vn15t2 > a")
                for review in reviews:
                    text = review.text.strip()
                    if text and text not in all_reviews:
                        all_reviews.append(text)

                print(f"현재 리뷰 개수: {len(all_reviews)}")

                # 더보기 버튼 클릭
                try:
                    more_button = driver.find_element(By.XPATH, "//a[contains(@class, 'fvwqf')]/span[contains(., '더보기')]")
                    if more_button.is_displayed():
                        more_button.click()
                        print("더보기 버튼 클릭 완료")
                        time.sleep(3)
                    else:
                        print("더보기 버튼을 찾을 수 없습니다. 더 이상 리뷰가 없습니다.")
                        break
                except:
                    print("더보기 버튼 없음, 크롤링 종료")
                    break

            except Exception as e:
                print(f"리뷰 데이터 크롤링 실패: {e}")
                break

        # print("최종 리뷰 데이터:")
        # for review in all_reviews:
        #     print(review)

        return all_reviews

    except Exception as e:
        print(f"오류 발생: {e}")
        return []
    finally:
        driver.quit()

