// api/review.js
import axiosInstance from "../utils/axiosInstance";
import { API_URLS } from "../utils/apiConfig";

export const fetchAndSaveReviews = async () => {
    console.log("widget.js : fetchAndSaveReviews");
    const response = await axiosInstance.post(`${API_URLS.REVIEW}/crawl_and_save`);
    return response;
}

export const getReviews = async () => {
    const response = await axiosInstance.get(`${API_URLS.REVIEW}`);
    console.log("getReviews Response", response);
    return response;
}