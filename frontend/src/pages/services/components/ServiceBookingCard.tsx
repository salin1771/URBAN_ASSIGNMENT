import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  Box, 
  Typography, 
  Button, 
  Divider, 
  IconButton, 
  TextField, 
  InputAdornment,
  Paper,
  Alert,
  Snackbar,
  Dialog,
  DialogTitle,
  DialogContent,
  DialogContentText,
  DialogActions,
} from '@mui/material';
import { Add, Remove, CalendarToday, AccessTime } from '@mui/icons-material';

interface ServiceBookingCardProps {
  service: {
    price: number;
    rating: number;
    reviewCount: number;
  };
  isAuthenticated: boolean;
  onBookNow: () => void;
}

const ServiceBookingCard = ({ service, isAuthenticated, onBookNow }: ServiceBookingCardProps) => {
  const [quantity, setQuantity] = useState(1);
  const [bookingDate, setBookingDate] = useState('');
  const [bookingTime, setBookingTime] = useState('');
  const [openLoginDialog, setOpenLoginDialog] = useState(false);
  const [snackbar, setSnackbar] = useState({
    open: false,
    message: '',
    severity: 'success',
  });

  const navigate = useNavigate();

  const handleBookNow = () => {
    if (!isAuthenticated) {
      setOpenLoginDialog(true);
      return;
    }
    onBookNow();
  };

  const handleLogin = () => {
    setOpenLoginDialog(false);
    navigate('/login', { state: { from: window.location.pathname } });
  };

  const handleCloseSnackbar = () => {
    setSnackbar(prev => ({ ...prev, open: false }));
  };

  const totalPrice = (service.price * quantity).toFixed(2);
  const today = new Date().toISOString().split('T')[0];

  return (
    <Paper elevation={2} sx={{ p: 3, borderRadius: 2, position: 'sticky', top: 20 }}>
      <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
        Book This Service
      </Typography>
      
      {/* Price and Quantity */}
      <Box sx={{ mb: 3 }}>
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 1 }}>
          <Typography variant="body1" color="text.secondary">
            Price
          </Typography>
          <Typography variant="h5" fontWeight={700}>
            ${service.price.toFixed(2)}
          </Typography>
        </Box>
        
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 1 }}>
          <Typography variant="body2" color="text.secondary">
            Quantity (hours)
          </Typography>
          <Box sx={{ display: 'flex', alignItems: 'center' }}>
            <IconButton 
              size="small" 
              onClick={() => setQuantity(prev => Math.max(1, prev - 1))}
              disabled={quantity <= 1}
              sx={{ border: '1px solid', borderColor: 'divider' }}
            >
              <Remove fontSize="small" />
            </IconButton>
            <Typography sx={{ mx: 2, minWidth: 24, textAlign: 'center' }}>
              {quantity}
            </Typography>
            <IconButton 
              size="small" 
              onClick={() => setQuantity(prev => prev + 1)}
              sx={{ border: '1px solid', borderColor: 'divider' }}
            >
              <Add fontSize="small" />
            </IconButton>
          </Box>
        </Box>
        
        <Divider sx={{ my: 2 }} />
        
        <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 2 }}>
          <Typography variant="subtitle1" fontWeight={600}>
            Total
          </Typography>
          <Typography variant="h5" fontWeight={700} color="primary">
            ${totalPrice}
          </Typography>
        </Box>
      </Box>
      
      {/* Booking Form */}
      <Box sx={{ mb: 3 }}>
        <TextField
          fullWidth
          type="date"
          label="Select Date"
          value={bookingDate}
          onChange={(e) => setBookingDate(e.target.value)}
          InputLabelProps={{
            shrink: true,
          }}
          inputProps={{
            min: today,
          }}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <CalendarToday color="action" />
              </InputAdornment>
            ),
          }}
          sx={{ mb: 2 }}
        />
        
        <TextField
          fullWidth
          type="time"
          label="Select Time"
          value={bookingTime}
          onChange={(e) => setBookingTime(e.target.value)}
          InputLabelProps={{
            shrink: true,
          }}
          inputProps={{
            step: 300, // 5 min
          }}
          InputProps={{
            startAdornment: (
              <InputAdornment position="start">
                <AccessTime color="action" />
              </InputAdornment>
            ),
          }}
        />
      </Box>
      
      {/* Action Buttons */}
      <Button
        fullWidth
        variant="contained"
        size="large"
        onClick={handleBookNow}
        sx={{ py: 1.5, mb: 2, fontWeight: 600 }}
        disabled={!bookingDate || !bookingTime}
      >
        Book Now
      </Button>
      
      <Button
        fullWidth
        variant="outlined"
        size="large"
        sx={{ py: 1.5, fontWeight: 600 }}
      >
        Add to Cart
      </Button>
      
      <Box sx={{ mt: 2, textAlign: 'center' }}>
        <Typography variant="body2" color="text.secondary">
          <Box component="span" sx={{ color: 'success.main', fontWeight: 500 }}>
            Free cancellation
          </Box>{' '}
          up to 24 hours before
        </Typography>
      </Box>
      
      {/* Login Dialog */}
      <Dialog open={openLoginDialog} onClose={() => setOpenLoginDialog(false)}>
        <DialogTitle>Sign In Required</DialogTitle>
        <DialogContent>
          <DialogContentText>
            You need to be signed in to book this service. Would you like to sign in now?
          </DialogContentText>
        </DialogContent>
        <DialogActions>
          <Button onClick={() => setOpenLoginDialog(false)}>Cancel</Button>
          <Button onClick={handleLogin} variant="contained" color="primary">
            Sign In
          </Button>
        </DialogActions>
      </Dialog>
      
      {/* Snackbar for notifications */}
      <Snackbar
        open={snackbar.open}
        autoHideDuration={6000}
        onClose={handleCloseSnackbar}
        anchorOrigin={{ vertical: 'top', horizontal: 'center' }}
      >
        <Alert onClose={handleCloseSnackbar} severity={snackbar.severity as any} sx={{ width: '100%' }}>
          {snackbar.message}
        </Alert>
      </Snackbar>
    </Paper>
  );
};

export default ServiceBookingCard;
