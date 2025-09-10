import { useParams, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import {
  Box,
  Container,
  Typography,
  Paper,
  Button,
  Avatar,
  Divider,
  Chip,
  Rating,
  Skeleton,
  Grid as MuiGrid,
} from '@mui/material';
import {
  ArrowBack,
  EventAvailable,
  AccessTime,
  LocationOn,
  Phone,
  Receipt,
  Message,
  Close,
  Edit,
} from '@mui/icons-material';

// Create a custom Grid component that properly handles the item prop
const Grid = (props: any) => {
  const { item, ...other } = props;
  return <MuiGrid {...(item ? { item: true } : {})} {...other} />;
};

// Mock data - replace with actual API call
const fetchBooking = async (id: string) => {
  // Simulate API call
  return new Promise<any>((resolve) => {
    setTimeout(() => {
      resolve({
        id,
        service: 'Plumbing Service',
        professional: 'John Doe',
        professionalAvatar: '/path/to/avatar.jpg',
        date: '2023-06-15',
        time: '10:00 AM',
        duration: '2 hours',
        price: 120,
        status: 'confirmed',
        address: '123 Main St, Anytown, USA',
        description: 'Fixing leaky faucet and checking all plumbing fixtures.',
        rating: 4.5,
        review: 'Great service, very professional!',
        professionalPhone: '+1 (555) 123-4567',
        professionalEmail: 'john.doe@example.com',
      });
    }, 500);
  });
};

const BookingDetailPage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const { data: booking, isLoading } = useQuery({
    queryKey: ['booking', id],
    queryFn: () => fetchBooking(id || ''),
    enabled: !!id,
  });

  if (isLoading) {
    return <BookingDetailSkeleton />;
  }

  if (!booking) {
    return (
      <Container maxWidth="lg" sx={{ py: 4 }}>
        <Typography variant="h5">Booking not found</Typography>
      </Container>
    );
  }

  const getStatusColor = (status: string) => {
    switch (status) {
      case 'confirmed':
        return 'success';
      case 'pending':
        return 'warning';
      case 'completed':
        return 'info';
      case 'cancelled':
        return 'error';
      default:
        return 'default';
    }
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box mb={4}>
        <Button
          startIcon={<ArrowBack />}
          onClick={() => navigate(-1)}
          sx={{ mb: 2 }}
        >
          Back to Bookings
        </Button>

        <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
          <Typography variant="h4" component="h1">
            Booking Details
          </Typography>
          <Chip
            label={booking.status.charAt(0).toUpperCase() + booking.status.slice(1)}
            color={getStatusColor(booking.status) as any}
            size="small"
          />
        </Box>

        <Grid container spacing={3}>
          <Grid item xs={12} md={8}>
            <Paper elevation={0} sx={{ p: 3, mb: 3 }}>
              <Typography variant="h6" gutterBottom>
                Service Details
              </Typography>
              <Typography variant="h5" gutterBottom>
                {booking.service}
              </Typography>
              <Typography color="text.secondary" paragraph>
                {booking.description}
              </Typography>
              
              <Divider sx={{ my: 3 }} />
              
              <Box display="flex" alignItems="center" mb={2}>
                <EventAvailable color="action" sx={{ mr: 1 }} />
                <Typography variant="body1">
                  <strong>Date:</strong> {new Date(booking.date).toLocaleDateString()}
                </Typography>
              </Box>
              
              <Box display="flex" alignItems="center" mb={2}>
                <AccessTime color="action" sx={{ mr: 1 }} />
                <Typography variant="body1">
                  <strong>Time:</strong> {booking.time}
                </Typography>
              </Box>
              
              <Box display="flex" alignItems="center" mb={2}>
                <AccessTime color="action" sx={{ mr: 1 }} />
                <Typography variant="body1">
                  <strong>Duration:</strong> {booking.duration}
                </Typography>
              </Box>
              
              <Box display="flex" alignItems="center" mb={2}>
                <LocationOn color="action" sx={{ mr: 1 }} />
                <Typography variant="body1">
                  <strong>Address:</strong> {booking.address}
                </Typography>
              </Box>
            </Paper>

            <Paper elevation={0} sx={{ p: 3, mb: 3 }}>
              <Typography variant="h6" gutterBottom>
                Professional
              </Typography>
              
              <Box display="flex" alignItems="center" mb={2}>
                <Avatar src={booking.professionalAvatar} sx={{ width: 56, height: 56, mr: 2 }} />
                <Box>
                  <Typography variant="subtitle1">{booking.professional}</Typography>
                  <Box display="flex" alignItems="center">
                    <Rating value={booking.rating} readOnly precision={0.5} size="small" />
                    <Typography variant="body2" color="text.secondary" sx={{ ml: 1 }}>
                      {booking.rating}
                    </Typography>
                  </Box>
                </Box>
              </Box>
              
              <Button
                variant="outlined"
                startIcon={<Message />}
                sx={{ mr: 2, mt: 1 }}
              >
                Message
              </Button>
              
              <Button
                variant="outlined"
                startIcon={<Phone />}
                sx={{ mr: 2, mt: 1 }}
              >
                Call
              </Button>
            </Paper>
          </Grid>
          
          <Grid item={true} xs={12} md={4}>
            <Paper elevation={0} sx={{ p: 3, position: 'sticky', top: 20 }}>
              <Typography variant="h6" gutterBottom>
                Booking Summary
              </Typography>
              
              <Box display="flex" justifyContent="space-between" mb={1}>
                <Typography color="text.secondary">Service Fee</Typography>
                <Typography>${booking.price.toFixed(2)}</Typography>
              </Box>
              
              <Box display="flex" justifyContent="space-between" mb={3}>
                <Typography color="text.secondary">Tax</Typography>
                <Typography>$0.00</Typography>
              </Box>
              
              <Divider sx={{ my: 2 }} />
              
              <Box display="flex" justifyContent="space-between" mb={3}>
                <Typography variant="subtitle1">Total</Typography>
                <Typography variant="subtitle1">${booking.price.toFixed(2)}</Typography>
              </Box>
              
              <Button
                variant="contained"
                fullWidth
                size="large"
                sx={{ mb: 2 }}
                startIcon={<Receipt />}
              >
                Download Receipt
              </Button>
              
              <Button
                variant="outlined"
                fullWidth
                size="large"
                color="error"
                startIcon={<Close />}
              >
                Cancel Booking
              </Button>
            </Paper>
            
            {booking.review && (
              <Paper elevation={0} sx={{ p: 3, mt: 3 }}>
                <Typography variant="h6" gutterBottom>
                  Your Review
                </Typography>
                <Box display="flex" alignItems="center" mb={1}>
                  <Rating value={booking.rating} readOnly />
                  <Typography variant="body2" color="text.secondary" sx={{ ml: 1 }}>
                    {booking.rating}
                  </Typography>
                </Box>
                <Typography>{booking.review}</Typography>
                <Button
                  startIcon={<Edit />}
                  size="small"
                  sx={{ mt: 1 }}
                >
                  Edit Review
                </Button>
              </Paper>
            )}
          </Grid>
        </Grid>
      </Box>
    </Container>
  );
};

// Skeleton Loader for the booking detail page
const BookingDetailSkeleton = () => (
  <Container maxWidth="lg" sx={{ py: 4 }}>
    <Box mb={4}>
      <Skeleton variant="rectangular" width={120} height={36} sx={{ mb: 2 }} />
      
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
        <Skeleton variant="text" width={200} height={40} />
        <Skeleton variant="rectangular" width={100} height={24} />
      </Box>
      
      <Grid container spacing={3}>
        <Grid item={true} xs={12} md={8}>
          <Paper elevation={0} sx={{ p: 3, mb: 3 }}>
            <Skeleton variant="text" width={150} height={30} sx={{ mb: 2 }} />
            <Skeleton variant="text" width={250} height={40} sx={{ mb: 2 }} />
            <Skeleton variant="text" width="100%" height={60} sx={{ mb: 2 }} />
            
            <Divider sx={{ my: 3 }} />
            
            {[1, 2, 3, 4].map((i) => (
              <Box key={i} display="flex" alignItems="center" mb={2}>
                <Skeleton variant="circular" width={24} height={24} sx={{ mr: 1 }} />
                <Skeleton variant="text" width={200} height={24} />
              </Box>
            ))}
          </Paper>
          
          <Paper elevation={0} sx={{ p: 3, mb: 3 }}>
            <Skeleton variant="text" width={150} height={30} sx={{ mb: 2 }} />
            <Box display="flex" alignItems="center" mb={2}>
              <Skeleton variant="circular" width={56} height={56} sx={{ mr: 2 }} />
              <Box>
                <Skeleton variant="text" width={150} height={24} sx={{ mb: 1 }} />
                <Skeleton variant="text" width={100} height={20} />
              </Box>
            </Box>
            <Skeleton variant="rectangular" width={120} height={36} sx={{ mr: 2, display: 'inline-block' }} />
            <Skeleton variant="rectangular" width={100} height={36} sx={{ display: 'inline-block' }} />
          </Paper>
        </Grid>
        
        <Grid item={true} xs={12} md={4}>
          <Paper elevation={0} sx={{ p: 3, position: 'sticky', top: 20 }}>
            <Skeleton variant="text" width={150} height={30} sx={{ mb: 2 }} />
            
            {[1, 2].map((i) => (
              <Box key={i} display="flex" justifyContent="space-between" mb={1}>
                <Skeleton variant="text" width={80} height={24} />
                <Skeleton variant="text" width={60} height={24} />
              </Box>
            ))}
            
            <Divider sx={{ my: 2 }} />
            
            <Box display="flex" justifyContent="space-between" mb={3}>
              <Skeleton variant="text" width={60} height={28} />
              <Skeleton variant="text" width={80} height={28} />
            </Box>
            
            <Skeleton variant="rectangular" width="100%" height={40} sx={{ mb: 2 }} />
            <Skeleton variant="rectangular" width="100%" height={40} />
          </Paper>
        </Grid>
      </Grid>
    </Box>
  </Container>
);

export default BookingDetailPage;
