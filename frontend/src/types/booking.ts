import type { Service, ServiceAddon } from './service';
import type { User } from './auth.types';

export enum BookingStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  CANCELLED = 'CANCELLED',
  COMPLETED = 'COMPLETED',
  IN_PROGRESS = 'IN_PROGRESS',
  NO_SHOW = 'NO_SHOW',
  RESCHEDULED = 'RESCHEDULED',
}

export interface BookingAddon {
  id: string;
  addon: ServiceAddon;
  quantity: number;
  priceAtBooking: number;
  createdAt: string;
  updatedAt: string;
}

export interface Booking {
  id: string;
  bookingNumber: string;
  customer: Pick<User, 'id' | 'name' | 'email' | 'phone'>;
  professional: Pick<User, 'id' | 'name' | 'email' | 'phone'>;
  service: Pick<Service, 'id' | 'name' | 'basePrice' | 'duration'>;
  status: BookingStatus;
  startTime: string;
  endTime: string;
  totalAmount: number;
  address: string;
  notes?: string;
  cancellationReason?: string;
  cancellationDate?: string;
  addons?: BookingAddon[];
  review?: {
    id: string;
    rating: number;
    comment?: string;
    createdAt: string;
  };
  paymentStatus: 'PENDING' | 'PAID' | 'REFUNDED' | 'PARTIALLY_REFUNDED' | 'FAILED';
  paymentMethod?: string;
  paymentId?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateBookingRequest {
  serviceId: string;
  professionalId: string;
  startTime: string;
  endTime: string;
  address: string;
  notes?: string;
  addons?: Array<{
    addonId: string;
    quantity: number;
  }>;
  paymentMethod: string;
}

export interface RescheduleBookingRequest {
  bookingId: string;
  newStartTime: string;
  newEndTime: string;
  reason?: string;
}

export interface CancelBookingRequest {
  bookingId: string;
  reason: string;
}

export interface BookingFilterParams {
  status?: BookingStatus | BookingStatus[];
  startDate?: string;
  endDate?: string;
  customerId?: string;
  professionalId?: string;
  serviceId?: string;
  searchQuery?: string;
  sortBy?: 'date' | 'status' | 'totalAmount';
  sortOrder?: 'asc' | 'desc';
  page?: number;
  limit?: number;
}

export interface BookingStats {
  total: number;
  pending: number;
  confirmed: number;
  completed: number;
  cancelled: number;
  inProgress: number;
  revenue: number;
  averageRating: number;
  mostBookedService?: {
    id: string;
    name: string;
    count: number;
  };
  upcomingBookings: number;
  pastBookings: number;
}

export interface BookingTimeline {
  date: string;
  bookings: Booking[];
}
