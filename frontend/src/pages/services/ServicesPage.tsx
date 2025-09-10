import { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import { useQuery } from '@tanstack/react-query';
import {
  Box,
  Container,
  Grid,
  Typography,
  Card,
  CardContent,
  CardMedia,
  CardActionArea,
  TextField,
  InputAdornment,
  Button,
  Chip,
  Rating,
  Pagination,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Skeleton,
  useTheme,
} from '@mui/material';
import { Search, FilterList, Star, Tune } from '@mui/icons-material';

// Mock data - replace with actual API calls
const mockServices = Array(8).fill(0).map((_, i) => ({
  id: i + 1,
  name: `Service ${i + 1}`,
  description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit.',
  price: Math.floor(Math.random() * 100) + 10,
  rating: (Math.random() * 1 + 4).toFixed(1),
  reviewCount: Math.floor(Math.random() * 100),
  category: ['Cleaning', 'Repair', 'Beauty', 'Moving'][Math.floor(Math.random() * 4)],
  image: `/images/service-${(i % 4) + 1}.jpg`,
}));

const categories = [
  'All Categories',
  'Cleaning',
  'Repair',
  'Beauty',
  'Moving',
  'Gardening',
  'Pest Control',
  'Painting',
];

const sortOptions = [
  { value: 'popular', label: 'Most Popular' },
  { value: 'rating', label: 'Highest Rated' },
  { value: 'price-low', label: 'Price: Low to High' },
  { value: 'price-high', label: 'Price: High to Low' },
];

const ServicesPage = () => {
  const theme = useTheme();
  const navigate = useNavigate();
  const location = useLocation();
  const [searchQuery, setSearchQuery] = useState('');
  const [category, setCategory] = useState('All Categories');
  const [sortBy, setSortBy] = useState('popular');
  const [page, setPage] = useState(1);
  const [filters, setFilters] = useState({
    priceRange: [0, 1000],
    rating: 0,
  });
  const [showFilters, setShowFilters] = useState(false);

  const itemsPerPage = 8;

  // Parse URL query parameters
  useEffect(() => {
    const params = new URLSearchParams(location.search);
    const search = params.get('search');
    const cat = params.get('category');
    
    if (search) setSearchQuery(search);
    if (cat) setCategory(cat);
  }, [location.search]);

  // Fetch services data
  const { data: services = [], isLoading } = useQuery({
    queryKey: ['services', { searchQuery, category, sortBy }],
    queryFn: async () => {
      // Simulate API call
      await new Promise(resolve => setTimeout(resolve, 500));
      
      // Filter and sort mock data
      let filtered = [...mockServices];
      
      if (searchQuery) {
        filtered = filtered.filter(service => 
          service.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
          service.description.toLowerCase().includes(searchQuery.toLowerCase())
        );
      }
      
      if (category !== 'All Categories') {
        filtered = filtered.filter(service => service.category === category);
      }
      
      // Apply sorting
      switch (sortBy) {
        case 'rating':
          filtered.sort((a, b) => parseFloat(b.rating) - parseFloat(a.rating));
          break;
        case 'price-low':
          filtered.sort((a, b) => a.price - b.price);
          break;
        case 'price-high':
          filtered.sort((a, b) => b.price - a.price);
          break;
        default: // 'popular'
          filtered.sort((a, b) => b.reviewCount - a.reviewCount);
      }
      
      return filtered;
    },
  });

  // Handle search
  const handleSearch = (e: React.FormEvent) => {
    e.preventDefault();
    const params = new URLSearchParams();
    if (searchQuery) params.set('search', searchQuery);
    if (category !== 'All Categories') params.set('category', category);
    navigate({ search: params.toString() });
  };

  // Handle filter changes
  const handleFilterChange = (key: string, value: any) => {
    setFilters(prev => ({
      ...prev,
      [key]: value,
    }));
  };

  // Handle pagination
  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  // Calculate pagination
  const totalPages = Math.ceil(services.length / itemsPerPage);
  const paginatedServices = services.slice(
    (page - 1) * itemsPerPage,
    page * itemsPerPage
  );

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      {/* Page Header */}
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1" sx={{ fontWeight: 700, mb: 2 }}>
          {category === 'All Categories' ? 'All Services' : category}
        </Typography>
        <Typography variant="body1" color="text.secondary">
          {services.length} services available
          {searchQuery && ` for "${searchQuery}"`}
        </Typography>
      </Box>

      {/* Search and Filter Bar */}
      <Box sx={{ mb: 4 }}>
        <Box
          component="form"
          onSubmit={handleSearch}
          sx={{
            display: 'flex',
            flexDirection: { xs: 'column', sm: 'row' },
            gap: 2,
            mb: 3,
          }}
        >
          <TextField
            fullWidth
            variant="outlined"
            placeholder="Search services..."
            value={searchQuery}
            onChange={(e) => setSearchQuery(e.target.value)}
            InputProps={{
              startAdornment: (
                <InputAdornment position="start">
                  <Search />
                </InputAdornment>
              ),
            }}
          />
          <Box sx={{ display: 'flex', gap: 2, width: { xs: '100%', sm: 'auto' } }}>
            <FormControl sx={{ minWidth: 200 }} size="small">
              <InputLabel id="category-label">Category</InputLabel>
              <Select
                labelId="category-label"
                value={category}
                label="Category"
                onChange={(e) => setCategory(e.target.value)}
              >
                {categories.map((cat) => (
                  <MenuItem key={cat} value={cat}>
                    {cat}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <Button
              variant="outlined"
              startIcon={<Tune />}
              onClick={() => setShowFilters(!showFilters)}
              sx={{ whiteSpace: 'nowrap' }}
            >
              Filters
            </Button>
            <Button
              type="submit"
              variant="contained"
              sx={{ whiteSpace: 'nowrap' }}
            >
              Search
            </Button>
          </Box>
        </Box>

        {/* Filters Panel */}
        {showFilters && (
          <Box
            sx={{
              p: 3,
              mb: 3,
              border: `1px solid ${theme.palette.divider}`,
              borderRadius: 1,
              backgroundColor: 'background.paper',
            }}
          >
            <Typography variant="subtitle1" sx={{ mb: 2, fontWeight: 600 }}>
              Filters
            </Typography>
            <Grid container spacing={3}>
              <Grid item xs={12} sm={6} md={3}>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                  Price Range
                </Typography>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2 }}>
                  <TextField
                    size="small"
                    type="number"
                    value={filters.priceRange[0]}
                    onChange={(e) =>
                      handleFilterChange('priceRange', [
                        Number(e.target.value),
                        filters.priceRange[1],
                      ])
                    }
                    sx={{ width: 100 }}
                  />
                  <Typography>—</Typography>
                  <TextField
                    size="small"
                    type="number"
                    value={filters.priceRange[1]}
                    onChange={(e) =>
                      handleFilterChange('priceRange', [
                        filters.priceRange[0],
                        Number(e.target.value),
                      ])
                    }
                    sx={{ width: 100 }}
                  />
                </Box>
              </Grid>
              <Grid item xs={12} sm={6} md={3}>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                  Minimum Rating
                </Typography>
                <Box sx={{ display: 'flex', alignItems: 'center' }}>
                  <Rating
                    value={filters.rating}
                    onChange={(event, newValue) => {
                      handleFilterChange('rating', newValue);
                    }}
                    precision={0.5}
                  />
                  <Typography variant="body2" sx={{ ml: 1 }}>
                    {filters.rating > 0 ? `${filters.rating}+` : 'Any'}
                  </Typography>
                </Box>
              </Grid>
              <Grid item xs={12} sm={6} md={3}>
                <Typography variant="body2" color="text.secondary" sx={{ mb: 1 }}>
                  Sort By
                </Typography>
                <FormControl fullWidth size="small">
                  <Select
                    value={sortBy}
                    onChange={(e) => setSortBy(e.target.value)}
                  >
                    {sortOptions.map((option) => (
                      <MenuItem key={option.value} value={option.value}>
                        {option.label}
                      </MenuItem>
                    ))}
                  </Select>
                </FormControl>
              </Grid>
            </Grid>
          </Box>
        )}
      </Box>

      {/* Services Grid */}
      {isLoading ? (
        <Grid container spacing={3}>
          {[...Array(8)].map((_, index) => (
            <Grid item xs={12} sm={6} md={4} lg={3} key={index}>
              <Card>
                <Skeleton variant="rectangular" height={140} />
                <CardContent>
                  <Skeleton variant="text" width="80%" height={24} />
                  <Skeleton variant="text" width="60%" height={20} />
                  <Skeleton variant="text" width="40%" height={20} />
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      ) : paginatedServices.length > 0 ? (
        <>
          <Grid container spacing={3}>
            {paginatedServices.map((service) => (
              <Grid item xs={12} sm={6} md={4} lg={3} key={service.id}>
                <Card
                  sx={{
                    height: '100%',
                    display: 'flex',
                    flexDirection: 'column',
                    transition: 'transform 0.3s, box-shadow 0.3s',
                    '&:hover': {
                      transform: 'translateY(-4px)',
                      boxShadow: 3,
                    },
                  }}
                >
                  <CardActionArea
                    onClick={() => navigate(`/services/${service.id}`)}
                    sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column', alignItems: 'stretch' }}
                  >
                    <CardMedia
                      component="img"
                      height="140"
                      image={service.image}
                      alt={service.name}
                      sx={{ objectFit: 'cover' }}
                    />
                    <CardContent sx={{ flexGrow: 1 }}>
                      <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                        <Typography variant="h6" component="h3" noWrap>
                          {service.name}
                        </Typography>
                        <Chip
                          label={service.category}
                          size="small"
                          color="primary"
                          variant="outlined"
                        />
                      </Box>
                      <Typography
                        variant="body2"
                        color="text.secondary"
                        sx={{
                          display: '-webkit-box',
                          WebkitLineClamp: 2,
                          WebkitBoxOrient: 'vertical',
                          overflow: 'hidden',
                          mb: 1,
                          minHeight: '2.8em',
                        }}
                      >
                        {service.description}
                      </Typography>
                      <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
                        <Rating
                          value={parseFloat(service.rating)}
                          precision={0.5}
                          readOnly
                          size="small"
                          sx={{ color: theme.palette.warning.main, mr: 0.5 }}
                        />
                        <Typography variant="body2" color="text.secondary">
                          {service.rating} ({service.reviewCount})
                        </Typography>
                      </Box>
                      <Typography variant="h6" color="primary" sx={{ fontWeight: 600 }}>
                        ${service.price}/hr
                      </Typography>
                    </CardContent>
                  </CardActionArea>
                </Card>
              </Grid>
            ))}
          </Grid>

          {/* Pagination */}
          {totalPages > 1 && (
            <Box sx={{ display: 'flex', justifyContent: 'center', mt: 4 }}>
              <Pagination
                count={totalPages}
                page={page}
                onChange={handlePageChange}
                color="primary"
                showFirstButton
                showLastButton
              />
            </Box>
          )}
        </>
      ) : (
        <Box
          sx={{
            display: 'flex',
            flexDirection: 'column',
            alignItems: 'center',
            justifyContent: 'center',
            py: 8,
            textAlign: 'center',
          }}
        >
          <Search sx={{ fontSize: 60, color: 'text.disabled', mb: 2 }} />
          <Typography variant="h6" gutterBottom>
            No services found
          </Typography>
          <Typography variant="body1" color="text.secondary" sx={{ maxWidth: 500, mb: 3 }}>
            We couldn't find any services matching your search criteria. Try adjusting your filters or search query.
          </Typography>
          <Button
            variant="outlined"
            onClick={() => {
              setSearchQuery('');
              setCategory('All Categories');
              setFilters({
                priceRange: [0, 1000],
                rating: 0,
              });
            }}
          >
            Clear all filters
          </Button>
        </Box>
      )}
    </Container>
  );
};

export default ServicesPage;
