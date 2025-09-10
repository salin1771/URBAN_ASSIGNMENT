import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import {
  Box,
  Container,
  Typography,
  Tabs,
  Tab,
  Paper,
  Button,
  Avatar,
  Divider,
  Chip,
  useTheme,
  Skeleton,
  IconButton,
  Menu,
  MenuItem,
  ListItemIcon,
  ListItemText,
  Rating,
} from '@mui/material';
import {
  EventAvailable,
  History,
  MoreVert,
  Edit,
  StarBorder,
  Message,
  Receipt,
  Close,
  AccessTime,
  CalendarToday,
} from '@mui/icons-material';

// Mock data - replace with API calls
interface Booking {
  id: string;
  service: string;
  professional: string;
  date: string;
  time: string;
  duration: string;
  price: number;
  status: 'confirmed' | 'pending' | 'completed' | 'cancelled';
  professionalAvatar: string;
  rating?: number;
  review?: string;
}

const mockBookings = {
  upcoming: [
    {
      id: 'B001',
      service: 'Home Cleaning',
      professional: 'CleanPro Services',
      date: '2023-06-15',
      time: '10:00 AM',
      duration: '2 hours',
      price: 120,
      status: 'confirmed',
      professionalAvatar: '/images/avatar-1.jpg',
    },
    {
      id: 'B002',
      service: 'Plumbing Service',
      professional: 'PlumbMaster',
      date: '2023-06-18',
      time: '2:30 PM',
      duration: '1 hour',
      price: 89,
      status: 'pending',
      professionalAvatar: '/images/avatar-2.jpg',
    },
  ],
  past: [
    {
      id: 'B003',
      service: 'Appliance Repair',
      professional: 'FixIt All',
      date: '2023-05-20',
      time: '11:00 AM',
      duration: '1.5 hours',
      price: 95,
      status: 'completed',
      rating: 5,
      review: 'Great service, fixed my washing machine in no time!',
      professionalAvatar: '/images/avatar-3.jpg',
    },
    {
      id: 'B004',
      service: 'Electrical Work',
      professional: 'Bright Sparks',
      date: '2023-04-10',
      time: '3:00 PM',
      duration: '2 hours',
      price: 150,
      status: 'cancelled',
      professionalAvatar: '/images/avatar-4.jpg',
    },
  ],
};

const BookingsPage = () => {
  const theme = useTheme();
  const navigate = useNavigate();
  const [tabValue, setTabValue] = useState(0);
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [selectedBooking, setSelectedBooking] = useState<string | null>(null);

  const { data: bookings = { upcoming: [], past: [] }, isLoading } = useQuery({
    queryKey: ['bookings'],
    queryFn: async () => {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 800));
      return mockBookings as { upcoming: Booking[]; past: Booking[] };
    },
  });

  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setTabValue(newValue);
  };

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>, bookingId: string) => {
    setAnchorEl(event.currentTarget);
    setSelectedBooking(bookingId);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
    setSelectedBooking(null);
  };

  const handleViewDetails = (bookingId: string) => {
    navigate(`/bookings/${bookingId}`);
    handleMenuClose();
  };

  const handleReschedule = (bookingId: string) => {
    console.log('Reschedule booking:', bookingId);
    handleMenuClose();
  };

  const handleCancel = (bookingId: string) => {
    console.log('Cancel booking:', bookingId);
    handleMenuClose();
  };

  const handleRate = (bookingId: string) => {
    console.log('Rate booking:', bookingId);
    handleMenuClose();
  };

  if (isLoading || !bookings) {
    return <BookingsSkeleton />;
  }

  const currentBookings = tabValue === 0 ? (bookings?.upcoming || []) : (bookings?.past || []);

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1" sx={{ fontWeight: 700, mb: 1 }}>
          My Bookings
        </Typography>
        <Typography variant="body1" color="text.secondary">
          {tabValue === 0 ? 'Upcoming and active bookings' : 'Your booking history'}
        </Typography>
      </Box>

      <Paper elevation={0} sx={{ mb: 4, borderRadius: 2 }}>
        <Tabs
          value={tabValue}
          onChange={handleTabChange}
          variant="fullWidth"
          sx={{
            '& .MuiTabs-indicator': {
              height: 4,
              borderRadius: '4px 4px 0 0',
            },
          }}
        >
          <Tab 
            label={
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <EventAvailable sx={{ mr: 1 }} />
                <span>Upcoming</span>
                {bookings.upcoming.length > 0 && (
                  <Chip 
                    label={bookings.upcoming.length} 
                    size="small" 
                    sx={{ ml: 1, height: 20, minWidth: 20 }}
                  />
                )}
              </Box>
            }
          />
          <Tab 
            label={
              <Box sx={{ display: 'flex', alignItems: 'center' }}>
                <History sx={{ mr: 1 }} />
                <span>Past</span>
                {bookings.past.length > 0 && (
                  <Chip 
                    label={bookings.past.length} 
                    size="small" 
                    sx={{ ml: 1, height: 20, minWidth: 20 }}
                  />
                )}
              </Box>
            }
          />
        </Tabs>

        <Box sx={{ p: { xs: 1, sm: 3 } }}>
          {currentBookings.length === 0 ? (
            <Box sx={{ textAlign: 'center', py: 6 }}>
              <EventAvailable sx={{ fontSize: 64, color: 'text.disabled', mb: 2 }} />
              <Typography variant="h6" sx={{ mb: 1 }}>
                No {tabValue === 0 ? 'upcoming' : 'past'} bookings
              </Typography>
              <Typography variant="body1" color="text.secondary" sx={{ mb: 3, maxWidth: 500, mx: 'auto' }}>
                {tabValue === 0 
                  ? 'You don\'t have any upcoming bookings. Browse our services to book a professional.'
                  : 'Your past bookings will appear here.'
                }
              </Typography>
              {tabValue === 0 && (
                <Button 
                  variant="contained" 
                  color="primary"
                  onClick={() => navigate('/services')}
                >
                  Browse Services
                </Button>
              )}
            </Box>
          ) : (
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              {currentBookings.map((booking) => (
                <Paper 
                  key={booking.id}
                  elevation={0}
                  sx={{
                    p: 2,
                    borderRadius: 2,
                    border: '1px solid',
                    borderColor: 'divider',
                    '&:hover': {
                      boxShadow: 1,
                    },
                  }}
                >
                  <Box sx={{ display: 'flex', alignItems: 'flex-start' }}>
                    <Avatar 
                      src={booking.professionalAvatar} 
                      alt={booking.professional}
                      sx={{ width: 56, height: 56, mr: 2 }}
                    />
                    <Box sx={{ flex: 1, minWidth: 0 }}>
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
                        <Box>
                          <Typography variant="subtitle1" fontWeight={600} noWrap>
                            {booking.service}
                          </Typography>
                          <Typography variant="body2" color="text.secondary">
                            {booking.professional}
                          </Typography>
                        </Box>
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                          {booking.status === 'pending' && (
                            <Chip 
                              label="Pending" 
                              size="small" 
                              color="warning"
                              sx={{ mr: 1 }}
                            />
                          )}
                          {booking.status === 'cancelled' && (
                            <Chip 
                              label="Cancelled" 
                              size="small" 
                              color="error"
                              variant="outlined"
                              sx={{ mr: 1 }}
                            />
                          )}
                          {booking.status === 'completed' && !booking.rating && (
                            <Chip 
                              label="Rate Now" 
                              size="small" 
                              color="primary"
                              variant="outlined"
                              onClick={() => handleRate(booking.id)}
                              sx={{ mr: 1, cursor: 'pointer' }}
                            />
                          )}
                          <IconButton 
                            size="small" 
                            onClick={(e) => handleMenuOpen(e, booking.id)}
                            aria-label="booking actions"
                          >
                            <MoreVert />
                          </IconButton>
                        </Box>
                      </Box>
                      
                      <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2, mt: 1.5 }}>
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                          <CalendarToday fontSize="small" color="action" sx={{ mr: 0.5 }} />
                          <Typography variant="body2">
                            {new Date(booking.date).toLocaleDateString('en-US', {
                              weekday: 'short',
                              month: 'short',
                              day: 'numeric',
                            })}
                            {' • '}
                            {booking.time}
                          </Typography>
                        </Box>
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                          <AccessTime fontSize="small" color="action" sx={{ mr: 0.5 }} />
                          <Typography variant="body2">{booking.duration}</Typography>
                        </Box>
                        <Box sx={{ display: 'flex', alignItems: 'center' }}>
                          <Receipt fontSize="small" color="action" sx={{ mr: 0.5 }} />
                          <Typography variant="body2" fontWeight={600}>
                            ${booking.price.toFixed(2)}
                          </Typography>
                        </Box>
                      </Box>
                      
                      {'rating' in booking && booking.rating && (
                        <Box sx={{ display: 'flex', alignItems: 'center', mt: 1 }}>
                          <Rating value={booking.rating} size="small" readOnly />
                          {'review' in booking && booking.review && (
                            <Typography variant="body2" color="text.secondary" sx={{ ml: 1 }} noWrap>
                              "{booking.review}"
                            </Typography>
                          )}
                        </Box>
                      )}
                    </Box>
                  </Box>
                </Paper>
              ))}
            </Box>
          )}
        </Box>
      </Paper>

      {/* Booking Actions Menu */}
      <Menu
        anchorEl={anchorEl}
        open={Boolean(anchorEl)}
        onClose={handleMenuClose}
        anchorOrigin={{
          vertical: 'bottom',
          horizontal: 'right',
        }}
        transformOrigin={{
          vertical: 'top',
          horizontal: 'right',
        }}
      >
        <MenuItem onClick={() => selectedBooking && handleViewDetails(selectedBooking)}>
          <ListItemIcon>
            <Receipt fontSize="small" />
          </ListItemIcon>
          <ListItemText>View Details</ListItemText>
        </MenuItem>
        {tabValue === 0 && (
          <>
            <MenuItem onClick={() => selectedBooking && handleReschedule(selectedBooking)}>
              <ListItemIcon>
                <Edit fontSize="small" />
              </ListItemIcon>
              <ListItemText>Reschedule</ListItemText>
            </MenuItem>
            <MenuItem onClick={() => selectedBooking && handleCancel(selectedBooking)}>
              <ListItemIcon>
                <Close fontSize="small" />
              </ListItemIcon>
              <ListItemText>Cancel Booking</ListItemText>
            </MenuItem>
          </>
        )}
        {tabValue === 1 && !bookings.past.find((b: Booking) => b.id === selectedBooking)?.rating && (
          <MenuItem onClick={() => selectedBooking && handleRate(selectedBooking)}>
            <ListItemIcon>
              <StarBorder fontSize="small" />
            </ListItemIcon>
            <ListItemText>Rate & Review</ListItemText>
          </MenuItem>
        )}
        <MenuItem onClick={() => selectedBooking && console.log('Message about booking:', selectedBooking)}>
          <ListItemIcon>
            <Message fontSize="small" />
          </ListItemIcon>
          <ListItemText>Message Professional</ListItemText>
        </MenuItem>
      </Menu>
    </Container>
  );
};

// Skeleton Loader
const BookingsSkeleton = () => (
  <Container maxWidth="lg" sx={{ py: 4 }}>
    <Skeleton variant="rectangular" width={200} height={40} sx={{ mb: 3 }} />
    <Skeleton variant="rectangular" width={300} height={24} sx={{ mb: 4 }} />
    
    <Paper elevation={0} sx={{ mb: 4, borderRadius: 2 }}>
      <Box sx={{ display: 'flex', borderBottom: '1px solid', borderColor: 'divider' }}>
        <Skeleton variant="rectangular" width="50%" height={48} />
        <Skeleton variant="rectangular" width="50%" height={48} />
      </Box>
      
      <Box sx={{ p: 3 }}>
        {[1, 2, 3].map((item) => (
          <Paper 
            key={item} 
            elevation={0}
            sx={{ 
              p: 2, 
              mb: 2, 
              borderRadius: 2,
              border: '1px solid',
              borderColor: 'divider',
            }}
          >
            <Box sx={{ display: 'flex' }}>
              <Skeleton variant="circular" width={56} height={56} sx={{ mr: 2 }} />
              <Box sx={{ flex: 1 }}>
                <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                  <Skeleton variant="text" width={150} height={24} />
                  <Skeleton variant="text" width={80} height={24} />
                </Box>
                <Skeleton variant="text" width={120} height={20} sx={{ mb: 1.5 }} />
                <Box sx={{ display: 'flex', gap: 2 }}>
                  <Skeleton variant="text" width={100} height={20} />
                  <Skeleton variant="text" width={80} height={20} />
                  <Skeleton variant="text" width={60} height={20} />
                </Box>
              </Box>
            </Box>
          </Paper>
        ))}
      </Box>
    </Paper>
  </Container>
);

export default BookingsPage;
