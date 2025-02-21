// 사장님 관련 타입
export interface Owner {
  email: string;
  name: string;
  phoneNumber: string;
}

export interface OwnerUpdateRequest {
  password: string;
  phoneNumber: string;
}

export interface StoreInfo {
  name: string;
  address: string;
  phoneNumber: string;
  payday: string;
}

// 알바생 관련 타입
export interface Worker {
  email: string;
  name: string;
  phoneNumber: string;
  bank: string;
  account: string;
  hourlySalary: number;
  tax: number;  // 0 또는 3.3 등
  insurance: number;  // 0 또는 9.4 등
  holidayPay: boolean;
}

export interface WorkerUpdateRequest {
  password: string;
  phoneNumber: string;
  bank: string;
  account: string;
}
