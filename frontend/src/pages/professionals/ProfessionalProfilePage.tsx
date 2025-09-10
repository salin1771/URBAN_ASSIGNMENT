import { useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import { styled, useTheme } from '@mui/material/styles';
import {
  Box,
  Container,
  Typography,
  Button,
  Rating,
  Divider,
  Chip,
  Avatar,
  Tabs,
  Tab,
  Paper,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Skeleton,
  IconButton,
} from '@mui/material';
import {
  Work,
  LocationOn,
  CheckCircle,
  CalendarToday,
  ArrowBack,
  Share,
  Favorite,
  FavoriteBorder,
  Message,
} from '@mui/icons-material';

interface Review {
  id: number;
  user: string;
  rating: number;
  date: string;
  comment: string;
}

interface Professional {
  id: number;
  name: string;
  title: string;
  rating: number;
  reviewCount: number;
  location: string;
  experience: number;
  about: string;
  services: string[];
  languages: string[];
  availability: string;
  responseTime: string;
  reviews: Review[];
  certifications: string[];
  avatar?: string;
  isFavorite?: boolean;
}

// Mock data - replace with API call
const mockProfessional: Professional = {
  id: 1,
  name: 'Alex Johnson',
  title: 'Professional Plumber',
  rating: 4.8,
  reviewCount: 124,
  location: 'New York, NY',
  experience: 8,
  about: 'Licensed and insured plumber with over 8 years of experience in residential and commercial plumbing. Specializing in leak repairs, pipe installations, and bathroom remodeling.',
  services: [
    'Leak Detection & Repair',
    'Pipe Installation',
    'Water Heater Installation',
    'Drain Cleaning',
    'Bathroom Remodeling',
  ],
  certifications: [
    'Licensed Master Plumber',
    'Backflow Prevention Certification',
    'OSHA 30-Hour Construction Safety'
  ],
  availability: 'Monday - Friday: 8:00 AM - 6:00 PM',
  responseTime: 'Within 2 hours',
  languages: ['English', 'Spanish'],
  reviews: [
    {
      id: 1,
      user: 'Sarah M.',
      rating: 5,
      date: '2023-04-15',
      comment: 'Alex was prompt, professional, and fixed our leaky faucet in no time. Highly recommend!'
    },
    {
      id: 2,
      user: 'Michael T.',
      rating: 4,
      date: '2023-03-22',
      comment: 'Great service, but was a bit late to the appointment. Work quality was excellent though.'
    }
  ]
};

// Styled components
const StyledContainer = styled(Container)(({ theme }) => ({
  paddingTop: theme.spacing(4),
  paddingBottom: theme.spacing(4),
}));

const StyledPaper = styled(Paper)<{}>(({ theme }) => ({
  padding: theme.spacing(3),
  marginBottom: theme.spacing(4),
  borderRadius: Number(theme.shape.borderRadius) * 2,
}));

const ProfileHeader = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  [theme.breakpoints.up('md')]: {
    flexDirection: 'row',
    gap: theme.spacing(3),
  },
}));

const ProfileContent = styled(Box)(({ theme }) => ({
  display: 'flex',
  flexDirection: 'column',
  gap: theme.spacing(3),
  [theme.breakpoints.up('md')]: {
    flexDirection: 'row',
  },
}));

const MainContent = styled(Box)({
  flex: 2,
});

const Sidebar = styled(Box)({
  flex: 1,
});

const ServicesGrid = styled(Box)(({ theme }) => ({
  display: 'grid',
  gridTemplateColumns: 'repeat(auto-fill, minmax(250px, 1fr))',
  gap: theme.spacing(2),
  marginTop: theme.spacing(2),
}));

const ProfessionalProfilePage = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const theme = useTheme();
  const [tabValue, setTabValue] = useState<number>(0);
  const [isFavorite, setIsFavorite] = useState<boolean>(false);

  const { data: professional, isLoading } = useQuery<Professional>({
    queryKey: ['professional', id],
    queryFn: async () => mockProfessional,
    initialData: mockProfessional,
  });
  
  const handleTabChange = (_event: React.SyntheticEvent, newValue: number) => {
    setTabValue(newValue);
  };

  const toggleFavorite = () => {
    setIsFavorite(!isFavorite);
  };

  if (isLoading || !professional) {
    return (
      <StyledContainer maxWidth="lg">
        <Skeleton variant="rectangular" width="100%" height={200} sx={{ mb: 2, borderRadius: 2 }} />
        <Skeleton variant="text" width="60%" height={40} sx={{ mb: 2 }} />
        <Skeleton variant="text" width="40%" height={30} sx={{ mb: 2 }} />
      </StyledContainer>
    );
  }

  const renderTabContent = () => {
    switch (tabValue) {
      case 0: // About
        return (
          <Box>
            <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>About Me</Typography>
            <Typography paragraph>{professional.about}</Typography>
            
            <Typography variant="h6" sx={{ mt: 4, mb: 2, fontWeight: 600 }}>Languages</Typography>
            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 1, mb: 3 }}>
              {professional.languages.map((lang: string, index: number) => (
                <Chip key={index} label={lang} variant="outlined" size="small" />
              ))}
            </Box>
            
            <Typography variant="h6" sx={{ mt: 4, mb: 2, fontWeight: 600 }}>Availability</Typography>
            <List dense>
              <ListItem>
                <ListItemIcon>
                  <CalendarToday color="primary" />
                </ListItemIcon>
                <ListItemText 
                  primary="Availability" 
                  secondary={professional.availability} 
                />
              </ListItem>
              <ListItem>
                <ListItemIcon>
                  <CheckCircle color="primary" />
                </ListItemIcon>
                <ListItemText 
                  primary="Response Time" 
                  secondary={professional.responseTime} 
                />
              </ListItem>
            </List>
          </Box>
        );
        
      case 1: // Services
        return (
          <Box>
            <Typography variant="h6" sx={{ mb: 3, fontWeight: 600 }}>Services Offered</Typography>
            <ServicesGrid>
              {professional.services.map((service: string, index: number) => (
                <Box key={index} sx={{ display: 'flex', alignItems: 'center' }}>
                  <CheckCircle color="primary" sx={{ mr: 1, fontSize: 20 }} />
                  <Typography>{service}</Typography>
                </Box>
              ))}
            </ServicesGrid>
          </Box>
        );
        
      case 2: // Reviews
        return (
          <Box>
            <Typography variant="h6" sx={{ mb: 3, fontWeight: 600 }}>
              Customer Reviews ({professional.reviewCount})
            </Typography>
            <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
              {professional.reviews.map((review: Review) => (
                <Paper key={review.id} variant="outlined" sx={{ p: 2 }}>
                  <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                    <Typography fontWeight="bold">{review.user}</Typography>
                    <Rating value={review.rating} readOnly size="small" />
                  </Box>
                  <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                    {new Date(review.date).toLocaleDateString()}
                  </Typography>
                  <Typography>{review.comment}</Typography>
                </Paper>
              ))}
            </Box>
            <Button variant="outlined" fullWidth sx={{ mt: 2 }}>
              See All Reviews
            </Button>
          </Box>
        );
        
      case 3: // Certifications
        return (
          <Box>
            <Typography variant="h6" sx={{ mb: 3, fontWeight: 600 }}>
              Certifications & Qualifications
            </Typography>
            <List>
              {professional.certifications.map((cert: string, index: number) => (
                <ListItem key={index} sx={{ px: 0 }}>
                  <ListItemIcon>
                    <CheckCircle color="primary" />
                  </ListItemIcon>
                  <ListItemText primary={cert} />
                </ListItem>
              ))}
            </List>
          </Box>
        );
        
      default:
        return null;
    }
  };

  return (
    <StyledContainer maxWidth="lg">
      <Button
        startIcon={<ArrowBack />}
        onClick={() => navigate(-1)}
        sx={{ mb: 3 }}
      >
        Back to Results
      </Button>

      <StyledPaper elevation={3}>
        <ProfileHeader>
          <Avatar
            src={`https://i.pravatar.cc/150?u=${professional.id}`}
            alt={professional.name}
            sx={{ 
              width: 150, 
              height: 150, 
              alignSelf: 'center',
              [theme.breakpoints.down('md')]: {
                width: 120,
                height: 120,
                mb: 2
              }
            }}
          />
          <Box sx={{ flex: 1 }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
              <Box>
                <Typography variant="h4" component="h1" gutterBottom>
                  {professional.name}
                </Typography>
                <Typography variant="h6" sx={{ mb: 1, opacity: 0.9 }}>
                  {professional.title}
                </Typography>
              </Box>
              <Box sx={{ display: 'flex', gap: 1 }}>
                <IconButton 
                  onClick={toggleFavorite}
                  sx={{ 
                    backgroundColor: 'rgba(0, 0, 0, 0.04)',
                    '&:hover': { backgroundColor: 'rgba(0, 0, 0, 0.08)' },
                    color: isFavorite ? 'error.main' : 'inherit',
                  }}
                >
                  {isFavorite ? <Favorite /> : <FavoriteBorder />}
                </IconButton>
                <IconButton
                  sx={{ 
                    backgroundColor: 'rgba(0, 0, 0, 0.04)',
                    '&:hover': { backgroundColor: 'rgba(0, 0, 0, 0.08)' },
                  }}
                >
                  <Share />
                </IconButton>
              </Box>
            </Box>
            
            <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
              <Rating value={professional.rating} precision={0.1} readOnly size="small" />
              <Typography variant="body2" sx={{ ml: 1 }}>
                {professional.rating} ({professional.reviewCount} reviews)
              </Typography>
            </Box>
            
            <Box sx={{ display: 'flex', flexWrap: 'wrap', gap: 2, mt: 1.5 }}>
              <Chip 
                icon={<LocationOn />} 
                label={professional.location} 
                size="small"
                variant="outlined"
              />
              <Chip 
                icon={<Work />} 
                label={`${professional.experience} years experience`}
                size="small"
                variant="outlined"
              />
            </Box>
          </Box>
        </ProfileHeader>
      </StyledPaper>

      <ProfileContent>
        <MainContent>
          <StyledPaper elevation={0}>
            <Tabs 
              value={tabValue} 
              onChange={handleTabChange} 
              sx={{ mb: 3 }}
              variant="scrollable"
              scrollButtons="auto"
            >
              <Tab label="About" />
              <Tab label={`Services (${professional.services.length})`} />
              <Tab label={`Reviews (${professional.reviewCount})`} />
              <Tab label="Certifications" />
            </Tabs>
            {renderTabContent()}
          </StyledPaper>
        </MainContent>
        
        <Sidebar>
          <StyledPaper elevation={0} sx={{ border: '1px solid', borderColor: 'divider' }}>
            <Typography variant="h6" sx={{ mb: 2, fontWeight: 600 }}>
              Contact {professional.name.split(' ')[0]}
            </Typography>
            
            <Button 
              variant="contained" 
              fullWidth 
              size="large" 
              sx={{ mb: 2 }}
              onClick={() => navigate(`/book/professional/${professional.id}`)}
            >
              Book Appointment
            </Button>
            
            <Button 
              variant="outlined" 
              fullWidth 
              size="large"
              startIcon={<Message />}
              sx={{ mb: 3 }}
            >
              Send Message
            </Button>
            
            <Divider sx={{ my: 2 }} />
            
            <Box sx={{ mb: 2 }}>
              <Typography variant="subtitle2" color="text.secondary" sx={{ mb: 0.5 }}>
                Response Rate
              </Typography>
              <Typography>98%</Typography>
            </Box>
            
            <Box sx={{ mb: 2 }}>
              <Typography variant="subtitle2" color="text.secondary" sx={{ mb: 0.5 }}>
                Response Time
              </Typography>
              <Typography>{professional.responseTime}</Typography>
            </Box>
            
            <Box sx={{ mb: 2 }}>
              <Typography variant="subtitle2" color="text.secondary" sx={{ mb: 0.5 }}>
                Location
              </Typography>
              <Typography>{professional.location}</Typography>
              <Button size="small" sx={{ mt: 0.5 }}>View on Map</Button>
            </Box>
            
            <Divider sx={{ my: 2 }} />
            
            <Box>
              <Typography variant="subtitle2" color="text.secondary" sx={{ mb: 1 }}>
                Share this profile
              </Typography>
              <Box sx={{ display: 'flex', gap: 1, flexWrap: 'wrap' }}>
                {['Facebook', 'Twitter', 'LinkedIn', 'Copy Link'].map((social) => (
                  <Button 
                    key={social}
                    variant="outlined" 
                    size="small"
                    sx={{ flex: 1, minWidth: '100px', mb: 1 }}
                  >
                    {social}
                  </Button>
                ))}
              </Box>
            </Box>
          </StyledPaper>
        </Sidebar>
      </ProfileContent>
    </StyledContainer>
  );
};

export default ProfessionalProfilePage;
