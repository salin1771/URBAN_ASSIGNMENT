export interface Coordinates {
  lat: number;
  lng: number;
}

export interface Address {
  id?: string;
  street: string;
  city: string;
  state: string;
  postalCode: string;
  country: string;
  isDefault?: boolean;
  coordinates?: Coordinates;
  formattedAddress?: string;
}

export interface User {
  id: string;
  email: string;
  name: string;
  phone?: string;
  role: UserRole;
  profileImageUrl?: string;
  avatar?: string;
  addresses?: Address[];
  isEmailVerified: boolean;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
  lastLoginAt?: string;
}

export interface AuthUser extends User {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
}

export enum UserRole {
  CUSTOMER = 'CUSTOMER',
  PROFESSIONAL = 'PROFESSIONAL',
  ADMIN = 'ADMIN',
}

export interface LoginCredentials {
  email: string;
  password: string;
  rememberMe?: boolean;
}

export interface RegisterCustomerData {
  name: string;
  email: string;
  password: string;
  phone: string;
}

export interface RegisterProfessionalData extends RegisterCustomerData {
  profession: string;
  bio?: string;
  experience?: number;
  skills?: string[];
  hourlyRate?: number;
  address: Omit<Address, 'id' | 'isDefault'>;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
  tokenType: string;
  expiresIn: number;
  role: UserRole;
  userId: string;
  name: string;
  email: string;
}

export interface PasswordResetRequest {
  email: string;
}

export interface PasswordResetConfirm {
  token: string;
  newPassword: string;
}

export interface ChangePasswordRequest {
  currentPassword: string;
  newPassword: string;
}
