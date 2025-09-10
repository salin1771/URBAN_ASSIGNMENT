import axiosInstance from '../api/axios';
import type { LoginCredentials, RegisterCustomerData, RegisterProfessionalData, AuthResponse, User } from '../types/auth.types';

export const login = async (credentials: LoginCredentials): Promise<AuthResponse> => {
  const response = await axiosInstance.post<AuthResponse>('/auth/login', credentials);
  return response.data;
};

export const registerCustomer = async (userData: RegisterCustomerData): Promise<AuthResponse> => {
  const response = await axiosInstance.post<AuthResponse>('/auth/register/customer', userData);
  return response.data;
};

export const registerProfessional = async (userData: RegisterProfessionalData): Promise<AuthResponse> => {
  const response = await axiosInstance.post<AuthResponse>('/auth/register/professional', userData);
  return response.data;
};

export const refreshToken = async (): Promise<AuthResponse> => {
  const response = await axiosInstance.post<AuthResponse>('/auth/refresh-token');
  return response.data;
};

export const logout = async (): Promise<void> => {
  try {
    await axiosInstance.post('/auth/logout');
  } catch (error) {
    console.error('Logout error:', error);
  } finally {
    // Clear local storage and redirect
    localStorage.removeItem('token');
    window.location.href = '/login';
  }
};

export const getUserProfile = async (): Promise<User> => {
  const response = await axiosInstance.get<User>('/users/me');
  return response.data;
};

export const updateProfile = async (userData: Partial<User>): Promise<User> => {
  const response = await axiosInstance.put<User>('/users/me', userData);
  return response.data;
};

export const changePassword = async (currentPassword: string, newPassword: string): Promise<void> => {
  await axiosInstance.put('/users/me/password', { currentPassword, newPassword });
};

export const requestPasswordReset = async (email: string): Promise<void> => {
  await axiosInstance.post('/auth/forgot-password', { email });
};

export const resetPassword = async (token: string, newPassword: string): Promise<void> => {
  await axiosInstance.post('/auth/reset-password', { token, newPassword });
};
