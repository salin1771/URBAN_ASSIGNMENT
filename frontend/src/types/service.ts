import type { User } from './auth.types';

export interface ServiceCategory {
  id: string;
  name: string;
  description?: string;
  imageUrl?: string;
  icon?: string;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface ServiceAddon {
  id: string;
  name: string;
  description?: string;
  price: number;
  duration: number; // in minutes
  isActive: boolean;
  serviceId: string;
  createdAt: string;
  updatedAt: string;
}

export interface Service {
  id: string;
  name: string;
  description: string;
  basePrice: number;
  duration: number; // in minutes
  isActive: boolean;
  category: ServiceCategory;
  professional: User;
  addons?: ServiceAddon[];
  images?: string[];
  rating?: number;
  reviewCount?: number;
  createdAt: string;
  updatedAt: string;
}

export interface ServiceReview {
  id: string;
  rating: number;
  comment?: string;
  isAnonymous: boolean;
  service: Pick<Service, 'id' | 'name'>;
  customer: Pick<User, 'id' | 'name' | 'profileImageUrl'>;
  bookingId: string;
  createdAt: string;
  updatedAt: string;
}

export interface ServiceFilterParams {
  categoryId?: string;
  minPrice?: number;
  maxPrice?: number;
  rating?: number;
  searchQuery?: string;
  sortBy?: 'price' | 'rating' | 'name';
  sortOrder?: 'asc' | 'desc';
  page?: number;
  limit?: number;
}

export interface ServiceAvailability {
  date: string;
  availableSlots: {
    startTime: string;
    endTime: string;
    isAvailable: boolean;
  }[];
}
