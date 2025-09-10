import { FC } from 'react';

declare const ServiceBookingCard: FC<{
  service: {
    price: number;
    rating: number;
    reviewCount: number;
  };
  isAuthenticated: boolean;
  onBookNow: () => void;
}>;

export default ServiceBookingCard;
