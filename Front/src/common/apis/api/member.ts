import axiosInstance from '@/common/apis/utils/axiosInstance';
import { API_URLS } from '@/common/apis/utils/apiConfig';

import type {
  Owner,
  OwnerUpdateRequest,
  Worker,
  WorkerUpdateRequest
} from '@/common/types/member';

// 사장 본인 정보 조회
export const getOwnerMyInfo = async (): Promise<Owner> => {
  const response = await axiosInstance.get<Owner>(`${API_URLS.AUTH}/owner/my`);
  return response.data;
};

// 사장 본인 회원 정보 수정
export const updateOwnerInfo = async (data: OwnerUpdateRequest): Promise<Owner> => {
  const response = await axiosInstance.put<Owner>(`${API_URLS.AUTH}/owner`, data);
  return response.data;
};

// 알바생 본인 정보 조회
export const getWorkerMyInfo = async (): Promise<Worker> => {
  const response = await axiosInstance.get<Worker>(`${API_URLS.AUTH}/worker/my`);
  return response.data;
};

// 알바생 본인 회원 정보 수정
export const updateWorkerInfo = async (data: WorkerUpdateRequest): Promise<Worker> => {
  const response = await axiosInstance.put<Worker>(`${API_URLS.AUTH}/worker`, data);
  return response.data;
};

// 특정 알바생 정보 조회 (사장용)
export const getWorkerInfo = async (workerId: string): Promise<Worker> => {
  const response = await axiosInstance.get<Worker>(`${API_URLS.AUTH}/${workerId}`);
  return response.data;
};
