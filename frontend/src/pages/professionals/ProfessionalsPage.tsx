import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
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
  Avatar,
} from '@mui/material';
import { Search, FilterList, Star, Work, LocationOn } from '@mui/icons-material';

// Define Professional interface
interface Professional {
  id: number;
  name: string;
  title: string;
  rating: string;
  reviewCount: number;
  location: string;
  experience: number;
  avatar: string;
  isAvailable: boolean;
}

// Mock data - replace with actual API calls
const mockProfessionals: Professional[] = Array(8).fill(0).map((_, i) => ({
  id: i + 1,
  name: `Professional ${i + 1}`,
  title: ['Plumber', 'Electrician', 'Cleaner', 'Gardener'][i % 4],
  rating: (Math.random() * 1 + 4).toFixed(1),
  reviewCount: Math.floor(Math.random() * 100),
  location: ['New York', 'Los Angeles', 'Chicago', 'Houston'][i % 4],
  experience: Math.floor(Math.random() * 10) + 1,
  avatar: `/images/avatar-${(i % 5) + 1}.jpg`,
  isAvailable: Math.random() > 0.3,
}));

const categories = [
  'All Categories',
  'Plumbing',
  'Electrical',
  'Cleaning',
  'Gardening',
  'Painting',
  'Carpentry',
  'Moving',
];

const sortOptions = [
  { value: 'rating', label: 'Highest Rated' },
  { value: 'experience', label: 'Most Experienced' },
  { value: 'reviews', label: 'Most Reviews' },
];

const ProfessionalsPage = () => {
  const theme = useTheme();
  const navigate = useNavigate();
  const [searchQuery, setSearchQuery] = useState('');
  const [category, setCategory] = useState('All Categories');
  const [sortBy, setSortBy] = useState('rating');
  const [page, setPage] = useState(1);
  const [showFilters, setShowFilters] = useState(false);

  const itemsPerPage = 8;
  const professionals = mockProfessionals;
  const totalPages = Math.ceil(professionals.length / itemsPerPage);
  const paginatedPros: Professional[] = professionals.slice(
    (page - 1) * itemsPerPage,
    page * itemsPerPage
  );

  const handlePageChange = (event: React.ChangeEvent<unknown>, value: number) => {
    setPage(value);
    window.scrollTo({ top: 0, behavior: 'smooth' });
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box sx={{ mb: 4 }}>
        <Typography variant="h4" component="h1" sx={{ fontWeight: 700, mb: 2 }}>
          Find Professionals
        </Typography>
        <Typography variant="body1" color="text.secondary">
          Connect with trusted professionals for all your service needs
        </Typography>
      </Box>

      {/* Search and Filter Bar */}
      <Box sx={{ mb: 4 }}>
        <Box
          component="form"
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
            placeholder="Search professionals..."
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
              startIcon={<FilterList />}
              onClick={() => setShowFilters(!showFilters)}
              sx={{ whiteSpace: 'nowrap' }}
            >
              Filters
            </Button>
          </Box>
        </Box>
      </Box>

      {/* Professionals Grid */}
      <Grid container spacing={3}>
        {paginatedPros.map((pro) => (
          <Grid item xs={12} sm={6} md={4} lg={3} key={pro.id} component="div" data-testid={`professional-${pro.id}`} {...{ item: true } as any}>
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
                onClick={() => navigate(`/professionals/${pro.id}`)}
                sx={{ flexGrow: 1, display: 'flex', flexDirection: 'column', alignItems: 'stretch' }}
              >
                <Box sx={{ position: 'relative', pt: 2, display: 'flex', justifyContent: 'center' }}>
                  <Avatar
                    src={pro.avatar}
                    alt={pro.name}
                    imgProps={{
                      onError: (e) => {
                        const target = e.target as HTMLImageElement;
                        target.onerror = null;
                        target.src = `https://ui-avatars.com/api/?name=${encodeURIComponent(pro.name)}&background=random`;
                      }
                    }}
                    sx={{
                      width: 100,
                      height: 100,
                      border: '3px solid #fff',
                      boxShadow: 1,
                      mt: 2,
                      mb: 1,
                      fontSize: '2.5rem',
                      backgroundColor: (theme) => 
                        theme.palette.mode === 'light' 
                          ? theme.palette.grey[300] 
                          : theme.palette.grey[700],
                    }}
                  />
                  {pro.isAvailable && (
                    <Chip
                      label="Available Now"
                      color="success"
                      size="small"
                      sx={{
                        position: 'absolute',
                        top: 16,
                        right: 16,
                        fontWeight: 600,
                      }}
                    />
                  )}
                </Box>
                <CardContent sx={{ textAlign: 'center', flexGrow: 1 }}>
                  <Typography variant="h6" component="h3" noWrap sx={{ mb: 0.5 }}>
                    {pro.name}
                  </Typography>
                  <Typography variant="body2" color="primary" sx={{ fontWeight: 600, mb: 1 }}>
                    {pro.title}
                  </Typography>
                  <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', mb: 1 }}>
                    <Rating value={parseFloat(pro.rating)} precision={0.5} size="small" readOnly />
                    <Typography variant="body2" color="text.secondary" sx={{ ml: 0.5 }}>
                      ({pro.reviewCount})
                    </Typography>
                  </Box>
                  <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 1, mb: 1 }}>
                    <LocationOn fontSize="small" color="action" />
                    <Typography variant="body2" color="text.secondary">
                      {pro.location}
                    </Typography>
                  </Box>
                  <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 1 }}>
                    <Work fontSize="small" color="action" />
                    <Typography variant="body2" color="text.secondary">
                      {pro.experience} years experience
                    </Typography>
                  </Box>
                </CardContent>
              </CardActionArea>
              <Button
                variant="contained"
                color="primary"
                fullWidth
                size="large"
                sx={{ mt: 'auto', borderTopLeftRadius: 0, borderTopRightRadius: 0 }}
                onClick={(e) => {
                  e.stopPropagation();
                  navigate(`/professionals/${pro.id}/book`);
                }}
              >
                Book Now
              </Button>
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
    </Container>
  );
};

export default ProfessionalsPage;
