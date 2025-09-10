import { FC } from 'react';
import { Box, Typography, Grid, Divider, Avatar, Rating, TextField, Button } from '@mui/material';
import { CheckCircle } from '@mui/icons-material';

interface ServiceInfoProps {
  service: {
    description: string;
    features: string[];
    provider: {
      id: number;
      name: string;
      rating: number;
      completedJobs: number;
    };
    reviewCount: number;
  };
  tabValue: number;
}

const ServiceInfo: FC<ServiceInfoProps> = ({ service, tabValue }) => {
  const renderOverview = () => (
    <Box>
      <Typography variant="body1" paragraph>
        {service.description}
      </Typography>
      
      <Typography variant="h6" sx={{ mt: 4, mb: 2, fontWeight: 600 }}>
        What's included
      </Typography>
      <Grid container spacing={2}>
        {service.features.map((feature, index) => (
          <Grid item xs={12} sm={6} key={index}>
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
              <CheckCircle color="primary" sx={{ fontSize: 20, mr: 1 }} />
              <Typography>{feature}</Typography>
            </Box>
          </Grid>
        ))}
      </Grid>
      
      <Divider sx={{ my: 4 }} />
      
      <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
        About the Provider
      </Typography>
      <Box sx={{ display: 'flex', alignItems: 'center', mb: 3 }}>
        <Avatar sx={{ width: 60, height: 60, mr: 2 }} />
        <Box>
          <Typography variant="subtitle1" fontWeight={600}>
            {service.provider.name}
          </Typography>
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 0.5 }}>
            <Rating value={service.provider.rating} size="small" readOnly />
            <Typography variant="body2" color="text.secondary" sx={{ ml: 1 }}>
              {service.provider.rating} ({service.provider.completedJobs} jobs)
            </Typography>
          </Box>
        </Box>
      </Box>
    </Box>
  );

  const renderDetails = () => (
    <Box>
      <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
        Service Details
      </Typography>
      <Typography paragraph>
        Our professional cleaning service includes a thorough cleaning of your entire home. 
        We use eco-friendly products that are safe for your family and pets. Our trained 
        professionals pay attention to every detail to ensure your complete satisfaction.
      </Typography>
      
      <Typography variant="h6" sx={{ mt: 4, mb: 2, fontWeight: 600 }}>
        Service Area
      </Typography>
      <Typography paragraph>
        We currently service all areas within a 20-mile radius of downtown. Additional 
        travel fees may apply for locations outside this area.
      </Typography>
      
      <Typography variant="h6" sx={{ mt: 4, mb: 2, fontWeight: 600 }}>
        Cancellation Policy
      </Typography>
      <Typography paragraph>
        You can cancel or reschedule your appointment up to 24 hours before the scheduled 
        time without any charges. Cancellations made less than 24 hours in advance may be 
        subject to a cancellation fee.
      </Typography>
    </Box>
  );

  const renderReviews = () => (
    <Box>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
          Customer Reviews ({service.reviewCount})
        </Typography>
        
        {/* Review Form */}
        <Box sx={{ p: 3, bgcolor: 'background.paper', borderRadius: 2, border: '1px solid', borderColor: 'divider', mb: 3 }}>
          <Typography variant="subtitle1" fontWeight={600} mb={2}>
            Write a Review
          </Typography>
          <Box sx={{ mb: 2 }}>
            <Typography component="legend">Your Rating</Typography>
            <Rating
              value={5}
              size="large"
              sx={{ color: 'primary.main' }}
            />
          </Box>
          <TextField
            fullWidth
            multiline
            rows={4}
            placeholder="Share your experience..."
            variant="outlined"
            sx={{ mb: 2 }}
          />
          <Button variant="contained" color="primary">
            Submit Review
          </Button>
        </Box>
        
        {/* Reviews List */}
        <Box>
          {[1, 2].map((review) => (
            <Box key={review} sx={{ mb: 3, pb: 2, borderBottom: '1px solid', borderColor: 'divider' }}>
              <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                <Avatar sx={{ width: 40, height: 40, mr: 2 }} />
                <Box>
                  <Typography variant="subtitle2" fontWeight={600}>
                    Customer {review}
                  </Typography>
                  <Box sx={{ display: 'flex', alignItems: 'center' }}>
                    <Rating value={5} size="small" readOnly />
                    <Typography variant="body2" color="text.secondary" sx={{ ml: 1 }}>
                      {new Date().toLocaleDateString()}
                    </Typography>
                  </Box>
                </Box>
              </Box>
              <Typography variant="body1" sx={{ pl: 6 }}>
                {review === 1 
                  ? 'The service was excellent! The cleaner was punctual and did a fantastic job.'
                  : 'Very satisfied with the cleaning service. Will definitely book again!'
                }
              </Typography>
            </Box>
          ))}
        </Box>
      </Box>
    </Box>
  );

  return (
    <Box>
      {tabValue === 0 && renderOverview()}
      {tabValue === 1 && renderDetails()}
      {tabValue === 2 && renderReviews()}
    </Box>
  );
};

export default ServiceInfo;
