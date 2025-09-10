import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import {
  Box,
  Container,
  Typography,
  Button,
  Skeleton,
  useTheme,
  IconButton,
  Divider,
  Chip,
  Tabs,
  Tab,
  Rating,
} from '@mui/material';
import { ArrowBack, Favorite, FavoriteBorder, Share } from '@mui/icons-material';
import { useAuth } from '../../context/AuthContext';
import ServiceGallery from './components/ServiceGallery';
import ServiceInfo from './components/ServiceInfo';
import ServiceBookingCard from './components/ServiceBookingCard';

// Mock data
const mockService = {
  id: 1,
  name: 'Professional House Cleaning',
  description: 'Thorough cleaning of your entire home by professional cleaners.',
  price: 79.99,
  rating: 4.8,
  reviewCount: 124,
  category: 'Cleaning',
  images: ['/images/service-1.jpg', '/images/service-2.jpg'],
  provider: {
    id: 1,
    name: 'CleanPro Services',
    rating: 4.9,
    completedJobs: 245,
  },
  features: [
    'Eco-friendly cleaning products',
    'Bathroom deep cleaning',
    'Kitchen deep cleaning',
  ],
};

const ServiceDetailPage = () => {
  const { id } = useParams();
  const navigate = useNavigate();
  const theme = useTheme();
  const { isAuthenticated } = useAuth();
  const [tabValue, setTabValue] = useState(0);
  const [isFavorite, setIsFavorite] = useState(false);

  const { data: service, isLoading } = useQuery({
    queryKey: ['service', id],
    queryFn: async () => {
      await new Promise(resolve => setTimeout(resolve, 800));
      return mockService;
    },
  });

  const handleTabChange = (event: React.SyntheticEvent, newValue: number) => {
    setTabValue(newValue);
  };

  const toggleFavorite = () => {
    setIsFavorite(!isFavorite);
  };

  if (isLoading || !service) {
    return <ServiceDetailSkeleton />;
  }

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Button 
        startIcon={<ArrowBack />} 
        onClick={() => navigate(-1)} 
        sx={{ mb: 3 }}
      >
        Back
      </Button>

      <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
        {/* Left Column */}
        <Box sx={{ flex: '1 1 0%', minWidth: { xs: '100%', md: 'calc(66.666% - 16px)' } }}>
          <ServiceGallery images={service.images} />
          
          <Box sx={{ display: 'flex', justifyContent: 'space-between', mt: 2, mb: 3 }}>
            <Box>
              <Typography variant="h4" component="h1" sx={{ fontWeight: 700, mb: 1 }}>
                {service.name}
              </Typography>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                <Rating value={service.rating} precision={0.1} readOnly size="small" />
                <Typography variant="body2" sx={{ ml: 1, color: 'text.secondary' }}>
                  {service.rating} ({service.reviewCount} reviews)
                </Typography>
              </Box>
              <Chip 
                label={service.category} 
                color="primary" 
                variant="outlined" 
                size="small" 
              />
            </Box>
            <Box>
              <IconButton onClick={toggleFavorite} color={isFavorite ? 'error' : 'default'}>
                {isFavorite ? <Favorite /> : <FavoriteBorder />}
              </IconButton>
              <IconButton>
                <Share />
              </IconButton>
            </Box>
          </Box>

          <Divider sx={{ my: 3 }} />

          <Tabs value={tabValue} onChange={handleTabChange} sx={{ mb: 3 }}>
            <Tab label="Overview" />
            <Tab label="Details" />
            <Tab label={`Reviews (${service.reviewCount})`} />
          </Tabs>
          <ServiceInfo service={service} tabValue={tabValue} />
        </Box>

        {/* Right Column */}
        <Box sx={{ flex: '0 0 auto', width: { xs: '100%', md: 'calc(33.333% - 16px)' } }}>
          <ServiceBookingCard 
            service={service} 
            isAuthenticated={isAuthenticated} 
            onBookNow={() => navigate('/book/checkout')}
          />
        </Box>
      </Box>
    </Container>
  );
};

// Skeleton Loader
const ServiceDetailSkeleton = () => (
  <Container maxWidth="lg" sx={{ py: 4 }}>
    <Skeleton variant="rectangular" width={100} height={36} sx={{ mb: 3 }} />
    <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 4 }}>
      <Box sx={{ flex: '1 1 0%', minWidth: { xs: '100%', md: 'calc(66.666% - 16px)' } }}>
        <Skeleton variant="rectangular" height={400} sx={{ borderRadius: 2 }} />
        <Box sx={{ mt: 2, display: 'flex', gap: 2 }}>
          {[1, 2].map((i) => (
            <Skeleton key={i} variant="rectangular" width={80} height={60} />
          ))}
        </Box>
        <Box sx={{ mt: 3 }}>
          <Skeleton width="70%" height={40} />
          <Skeleton width="40%" height={24} sx={{ mt: 1 }} />
          <Skeleton width="60%" height={24} sx={{ mt: 2 }} />
        </Box>
      </Box>
      <Box sx={{ flex: '0 0 auto', width: { xs: '100%', md: 'calc(33.333% - 16px)' } }}>
        <Skeleton variant="rectangular" height={300} sx={{ borderRadius: 2 }} />
      </Box>
    </Box>
  </Container>
);

export default ServiceDetailPage;
