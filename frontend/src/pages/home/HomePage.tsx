import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import {
  Box,
  Button,
  Container,
  Grid,
  Typography,
  Card,
  CardContent,
  CardMedia,
  CardActionArea,
  TextField,
  InputAdornment,
  Skeleton,
  Rating,
  Tabs,
  Tab,
} from '@mui/material';
import { Search, LocationOn, Star, Category, Handyman } from '@mui/icons-material';
import { useTheme } from '@mui/material/styles';
import { useAuth } from '../../context/AuthContext';

// Mock data - replace with actual API calls
const popularServices = [
  { id: 1, name: 'House Cleaning', image: '/images/cleaning.jpg', rating: 4.8, reviews: 124 },
  { id: 2, name: 'Plumbing', image: '/images/plumbing.jpg', rating: 4.7, reviews: 89 },
  { id: 3, name: 'Electrical', image: '/images/electrical.jpg', rating: 4.9, reviews: 156 },
  { id: 4, name: 'Beauty & Spa', image: '/images/beauty.jpg', rating: 4.6, reviews: 210 },
];

const categories = [
  { id: 1, name: 'Cleaning', icon: <Category /> },
  { id: 2, name: 'Repairs', icon: <Handyman /> },
  { id: 3, name: 'Beauty', icon: <Category /> },
  { id: 4, name: 'Moving', icon: <Category /> },
  { id: 5, name: 'Gardening', icon: <Category /> },
  { id: 6, name: 'Pest Control', icon: <Category /> },
];

const HomePage = () => {
  const theme = useTheme();
  const navigate = useNavigate();
  const { isAuthenticated } = useAuth();
  const [searchQuery, setSearchQuery] = useState('');
  const [location, setLocation] = useState('');
  const [activeTab, setActiveTab] = useState(0);

  // Fetch services data
  const { data: services = [], isLoading } = useQuery({
    queryKey: ['services'],
    queryFn: async () => {
      // Replace with actual API call
      await new Promise(resolve => setTimeout(resolve, 1000));
      return popularServices;
    },
  });

  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    if (searchQuery.trim()) {
      navigate(`/services?search=${encodeURIComponent(searchQuery)}`);
    }
  };

  const handleServiceClick = (id: number) => {
    navigate(`/services/${id}`);
  };

  const handleCategoryClick = (category: string) => {
    navigate(`/services?category=${encodeURIComponent(category)}`);
  };

  return (
    <Box>
      {/* Hero Section */}
      <Box
        sx={{
          background: `linear-gradient(rgba(0, 0, 0, 0.5), rgba(0, 0, 0, 0.5)), url('/images/hero-bg.jpg')`,
          backgroundSize: 'cover',
          backgroundPosition: 'center',
          color: 'white',
          py: 12,
          mb: 6,
        }}
      >
        <Container maxWidth="md">
          <Typography variant="h3" component="h1" align="center" gutterBottom sx={{ fontWeight: 700, mb: 3 }}>
            Find & Book Services for Your Home
          </Typography>
          <Typography variant="h6" align="center" sx={{ mb: 4, maxWidth: '700px', mx: 'auto' }}>
            Book trusted professionals for all your home service needs. Quality services at your convenience.
          </Typography>
          
          {/* Search Bar */}
          <Box
            component="form"
            onSubmit={handleSearch}
            sx={{
              display: 'flex',
              flexDirection: { xs: 'column', sm: 'row' },
              gap: 2,
              maxWidth: '800px',
              mx: 'auto',
              backgroundColor: 'white',
              borderRadius: 1,
              p: 2,
              boxShadow: 3,
            }}
          >
            <TextField
              fullWidth
              variant="outlined"
              placeholder="What service do you need?"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <Search color="action" />
                  </InputAdornment>
                ),
                sx: { backgroundColor: 'white' },
              }}
            />
            <TextField
              fullWidth
              variant="outlined"
              placeholder="Location"
              value={location}
              onChange={(e) => setLocation(e.target.value)}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <LocationOn color="action" />
                  </InputAdornment>
                ),
                sx: { backgroundColor: 'white' },
              }}
            />
            <Button
              type="submit"
              variant="contained"
              size="large"
              sx={{
                px: 4,
                py: 1.5,
                textTransform: 'none',
                fontWeight: 600,
                whiteSpace: 'nowrap',
              }}
            >
              Search
            </Button>
          </Box>
        </Container>
      </Box>

      <Container maxWidth="lg">
        {/* Categories Section */}
        <Box sx={{ mb: 8 }}>
          <Typography variant="h5" component="h2" sx={{ mb: 3, fontWeight: 600 }}>
            Popular Categories
          </Typography>
          <Grid container spacing={3}>
            {categories.map((category) => (
              <Grid item xs={6} sm={4} md={2} key={category.id}>
                <Card
                  variant="outlined"
                  sx={{
                    height: '100%',
                    display: 'flex',
                    flexDirection: 'column',
                    alignItems: 'center',
                    p: 2,
                    cursor: 'pointer',
                    transition: 'all 0.3s',
                    '&:hover': {
                      transform: 'translateY(-4px)',
                      boxShadow: 3,
                    },
                  }}
                  onClick={() => handleCategoryClick(category.name)}
                >
                  <Box
                    sx={{
                      width: 60,
                      height: 60,
                      borderRadius: '50%',
                      backgroundColor: 'primary.light',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      mb: 1.5,
                      color: 'primary.contrastText',
                    }}
                  >
                    {category.icon}
                  </Box>
                  <Typography variant="body2" align="center" fontWeight={500}>
                    {category.name}
                  </Typography>
                </Card>
              </Grid>
            ))}
          </Grid>
        </Box>

        {/* Popular Services Section */}
        <Box sx={{ mb: 8 }}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', mb: 3 }}>
            <Typography variant="h5" component="h2" sx={{ fontWeight: 600 }}>
              Popular Services
            </Typography>
            <Button
              variant="text"
              color="primary"
              onClick={() => navigate('/services')}
              sx={{ textTransform: 'none' }}
            >
              View All
            </Button>
          </Box>

          {isLoading ? (
            <Grid container spacing={3}>
              {[...Array(4)].map((_, index) => (
                <Grid item xs={12} sm={6} md={3} key={index}>
                  <Skeleton variant="rectangular" height={180} />
                  <Skeleton variant="text" />
                  <Skeleton variant="text" width="60%" />
                </Grid>
              ))}
            </Grid>
          ) : (
            <Grid container spacing={3}>
              {services.map((service: any) => (
                <Grid item xs={12} sm={6} md={3} key={service.id}>
                  <Card
                    sx={{
                      height: '100%',
                      display: 'flex',
                      flexDirection: 'column',
                      transition: 'transform 0.3s',
                      '&:hover': {
                        transform: 'translateY(-4px)',
                        boxShadow: 3,
                      },
                    }}
                  >
                    <CardActionArea onClick={() => handleServiceClick(service.id)}>
                      <CardMedia
                        component="img"
                        height="140"
                        image={service.image}
                        alt={service.name}
                        sx={{ objectFit: 'cover' }}
                      />
                      <CardContent>
                        <Typography gutterBottom variant="h6" component="div" noWrap>
                          {service.name}
                        </Typography>
                        <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                          <Rating
                            name="read-only"
                            value={service.rating}
                            precision={0.1}
                            readOnly
                            size="small"
                            sx={{ color: theme.palette.warning.main, mr: 1 }}
                          />
                          <Typography variant="body2" color="text.secondary">
                            {service.rating} ({service.reviews})
                          </Typography>
                        </Box>
                        <Button
                          size="small"
                          variant="outlined"
                          fullWidth
                          sx={{ mt: 1 }}
                        >
                          Book Now
                        </Button>
                      </CardContent>
                    </CardActionArea>
                  </Card>
                </Grid>
              ))}
            </Grid>
          )}
        </Box>

        {/* How It Works Section */}
        <Box sx={{ mb: 8, textAlign: 'center' }}>
          <Typography variant="h5" component="h2" sx={{ mb: 4, fontWeight: 600 }}>
            How It Works
          </Typography>
          <Grid container spacing={4}>
            {[
              {
                title: 'Search & Book',
                description: 'Find and book a service in just a few taps',
                icon: <Search fontSize="large" color="primary" />,
              },
              {
                title: 'Get Matched',
                description: 'We connect you with the best professionals',
                icon: <Handyman fontSize="large" color="primary" />,
              },
              {
                title: 'Relax & Enjoy',
                description: 'Sit back while we handle the rest',
                icon: <Star fontSize="large" color="primary" />,
              },
            ].map((step, index) => (
              <Grid item xs={12} md={4} key={index}>
                <Box
                  sx={{
                    p: 3,
                    borderRadius: 2,
                    backgroundColor: 'background.paper',
                    height: '100%',
                    boxShadow: 1,
                  }}
                >
                  <Box
                    sx={{
                      width: 60,
                      height: 60,
                      borderRadius: '50%',
                      backgroundColor: 'primary.light',
                      display: 'flex',
                      alignItems: 'center',
                      justifyContent: 'center',
                      mx: 'auto',
                      mb: 2,
                      color: 'primary.contrastText',
                    }}
                  >
                    {step.icon}
                  </Box>
                  <Typography variant="h6" gutterBottom sx={{ fontWeight: 600 }}>
                    {step.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {step.description}
                  </Typography>
                </Box>
              </Grid>
            ))}
          </Grid>
        </Box>

        {/* CTA Section */}
        {!isAuthenticated && (
          <Box
            sx={{
              backgroundColor: 'primary.light',
              color: 'primary.contrastText',
              p: 6,
              borderRadius: 2,
              textAlign: 'center',
              mb: 8,
            }}
          >
            <Typography variant="h4" component="h2" gutterBottom sx={{ fontWeight: 700 }}>
              Ready to get started?
            </Typography>
            <Typography variant="body1" sx={{ mb: 4, maxWidth: '700px', mx: 'auto' }}>
              Join thousands of satisfied customers who trust us for their home service needs.
            </Typography>
            <Button
              variant="contained"
              color="secondary"
              size="large"
              sx={{
                px: 4,
                py: 1.5,
                textTransform: 'none',
                fontWeight: 600,
                color: 'white',
              }}
              onClick={() => navigate('/register')}
            >
              Sign Up Now
            </Button>
          </Box>
        )}
      </Container>
    </Box>
  );
};

export default HomePage;
