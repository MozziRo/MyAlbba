import axiosInstance from "../utils/axiosInstance";
import { API_URLS } from '../utils/apiConfig';

// 요청 응답 타입
// 간단히 이 안에 정의
interface MemoText {
  text: string;
}

interface AnalysisResult {
  text: string;
  sentiment_scores: {
    neg: number;
    neu: number;
    pos: number;
  };
}

// 면접 메모 AI 분석(감성 분석)
export const analyzeSentiment = async (memoText : MemoText) : Promise<AnalysisResult> => {
  const response = await axiosInstance.post(`${API_URLS.MEMO}/analyze_sentiment`, memoText);
  return response.data;
};

// 면접 메모 AI 분석(키워드 분석)
export const extractKeywords = async (memoText : MemoText) : Promise<AnalysisResult> => {
  const response = await axiosInstance.post(`${API_URLS.MEMO}/extract_keywords`, memoText);
  return response.data;
}

