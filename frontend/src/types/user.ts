import type { Service } from './service';
import type { Booking } from './booking';
import type { User } from './auth.types';

export interface UserProfile extends User {
  bio?: string;
  dateOfBirth?: string;
  gender?: 'MALE' | 'FEMALE' | 'OTHER' | 'PREFER_NOT_TO_SAY';
  languages?: string[];
  address?: string;
  city?: string;
  state?: string;
  country?: string;
  postalCode?: string;
  coordinates?: {
    latitude: number;
    longitude: number;
  };
  isEmailVerified: boolean;
  isPhoneVerified: boolean;
  isProfileComplete: boolean;
  lastLoginAt?: string;
  lastActiveAt?: string;
  createdAt: string;
  updatedAt: string;
}

export interface ProfessionalProfile extends UserProfile {
  profession: string;
  specialties?: string[];
  experience: number; // in years
  hourlyRate: number;
  rating?: number;
  reviewCount?: number;
  isVerified: boolean;
  isAvailable: boolean;
  services?: Service[];
  education?: ProfessionalEducation[];
  certifications?: ProfessionalCertification[];
  workExperience?: ProfessionalExperience[];
  availability?: ProfessionalAvailability[];
  stats?: ProfessionalStats;
}

export interface ProfessionalEducation {
  id: string;
  degree: string;
  institution: string;
  fieldOfStudy: string;
  startYear: number;
  endYear?: number;
  isCurrent: boolean;
  description?: string;
}

export interface ProfessionalCertification {
  id: string;
  name: string;
  issuingOrganization: string;
  issueDate: string;
  expirationDate?: string;
  credentialId?: string;
  credentialUrl?: string;
}

export interface ProfessionalExperience {
  id: string;
  title: string;
  company: string;
  location?: string;
  startDate: string;
  endDate?: string;
  isCurrent: boolean;
  description?: string;
  skills?: string[];
}

export interface ProfessionalAvailability {
  id: string;
  dayOfWeek: number; // 0-6 (Sunday-Saturday)
  startTime: string;
  endTime: string;
  isRecurring: boolean;
  specificDate?: string;
  isAvailable: boolean;
}

export interface ProfessionalStats {
  totalBookings: number;
  completedBookings: number;
  cancelledBookings: number;
  averageRating: number;
  responseRate: number; // percentage
  responseTime: number; // in hours
  repeatClients: number;
  totalEarnings: number;
  lastMonthEarnings: number;
  upcomingBookings: number;
  availableSlots: number;
}

export interface UpdateProfileRequest {
  name?: string;
  phone?: string;
  bio?: string;
  dateOfBirth?: string;
  gender?: 'MALE' | 'FEMALE' | 'OTHER' | 'PREFER_NOT_TO_SAY';
  languages?: string[];
  address?: string;
  city?: string;
  state?: string;
  country?: string;
  postalCode?: string;
  profileImageUrl?: string;
}

export interface UpdateProfessionalProfileRequest extends UpdateProfileRequest {
  profession?: string;
  specialties?: string[];
  experience?: number;
  hourlyRate?: number;
  isAvailable?: boolean;
  education?: Omit<ProfessionalEducation, 'id'>[];
  certifications?: Omit<ProfessionalCertification, 'id'>[];
  workExperience?: Omit<ProfessionalExperience, 'id'>[];
  availability?: Omit<ProfessionalAvailability, 'id'>[];
}

export interface UserDashboard {
  user: UserProfile;
  upcomingBookings: Booking[];
  recentBookings: Booking[];
  recommendedServices: Service[];
  notifications: Notification[];
  stats: {
    totalBookings: number;
    upcomingBookings: number;
    completedBookings: number;
    cancelledBookings: number;
    totalSpent: number;
    favoriteService?: {
      id: string;
      name: string;
      count: number;
    };
  };
}

export interface Notification {
  id: string;
  type: 'BOOKING' | 'PAYMENT' | 'SYSTEM' | 'MESSAGE' | 'REVIEW';
  title: string;
  message: string;
  isRead: boolean;
  relatedId?: string;
  relatedType?: string;
  createdAt: string;
}
