// @/common/apis/utils/date.js

/**
 * 날짜를 YYYY-MM-DD 형식의 문자열로 변환
 * @param {Date|string} date - 변환할 날짜
 * @returns {string|null} 포맷된 날짜 문자열 또는 null
 */
export const formatDate = (date) => {
  // 문자열인 경우 그대로 반환
  if (typeof date === 'string') return date;
  
  // 유효하지 않은 날짜인 경우 null 반환
  if (!(date instanceof Date) || isNaN(date)) return null;
  
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
};

/**
 * 시간을 HH:MM 형식으로 변환
 * @param {string} time - 변환할 시간 문자열
 * @returns {string} 포맷된 시간 문자열
 */
export const formatTime = (time) => {
  if (!time) return '';
  return time.substring(0, 5); // HH:MM 형식으로 자르기
};

/**
 * 날짜를 한국어 상세 형식으로 변환 (예: 2025년 2월 7일 금요일)
 * @param {Date} date - 변환할 날짜
 * @returns {string} 포맷된 날짜 문자열
 */
export const formatDetailDate = (date) => {
  if (!date) return '';
  return new Intl.DateTimeFormat('ko-KR', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  }).format(date);
};

/**
 * 문자열을 Date 객체로 변환
 * @param {string} dateString - 변환할 날짜 문자열
 * @returns {Date|null} 변환된 Date 객체 또는 null
 */
export const parseDateString = (dateString) => {
  if (!dateString) return null;
  const date = new Date(dateString);
  return isNaN(date) ? null : date;
};

/**
 * 두 날짜가 같은 날인지 비교
 * @param {Date} date1 - 첫 번째 날짜
 * @param {Date} date2 - 두 번째 날짜
 * @returns {boolean} 같은 날인지 여부
 */
export const isSameDay = (date1, date2) => {
  if (!date1 || !date2) return false;
  return (
    date1.getDate() === date2.getDate() &&
    date1.getMonth() === date2.getMonth() &&
    date1.getFullYear() === date2.getFullYear()
  );
};

/**
 * 주어진 날짜가 오늘인지 확인
 * @param {Date} date - 확인할 날짜
 * @returns {boolean} 오늘인지 여부
 */
export const isToday = (date) => {
  return isSameDay(date, new Date());
};

/**
 * 주어진 날짜가 주말인지 확인
 * @param {Date} date - 확인할 날짜
 * @returns {boolean} 주말인지 여부
 */
export const isWeekend = (date) => {
  if (!date) return false;
  const day = date.getDay();
  return day === 0 || day === 6;
};