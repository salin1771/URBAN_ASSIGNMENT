import { useState } from 'react';
import { useNavigate, Link as RouterLink, useLocation } from 'react-router-dom';
import {
  AppBar,
  Toolbar,
  Button,
  IconButton,
  Box,
  Avatar,
  Menu,
  MenuItem,
  Divider,
  ListItemIcon,
  useTheme,
  useMediaQuery,
  Container,
  InputBase,
  Badge,
  alpha,
  styled,
  Stack,
  Typography,
} from '@mui/material';
import {
  Search as SearchIcon,
  Menu as MenuIcon,
  Person as PersonIcon,
  ExitToApp as LogoutIcon,
  Settings as SettingsIcon,
  Notifications as NotificationsIcon,
  ShoppingCart as CartIcon,
  Home as HomeIcon,
  Category as CategoryIcon,
  Info as InfoIcon,
  ContactMail as ContactIcon,
} from '@mui/icons-material';
import { useAuth } from "../../context/AuthContext";

// Styled components for better organization
const StyledToolbar = styled(Toolbar)(({ theme }) => ({
  padding: theme.spacing(1, 2),
  minHeight: '70px',
  backgroundColor: theme.palette.primary.main,
  color: theme.palette.primary.contrastText,
  boxShadow: theme.shadows[3],
}));

const LogoText = styled(Typography)(({ theme }) => ({
  fontWeight: 700,
  fontSize: '1.5rem',
  marginRight: theme.spacing(4),
  background: `linear-gradient(45deg, ${theme.palette.secondary.main} 30%, ${theme.palette.primary.light} 90%)`,
  WebkitBackgroundClip: 'text',
  WebkitTextFillColor: 'transparent',
  '&:hover': {
    transform: 'scale(1.03)',
    transition: 'transform 0.3s ease-in-out',
  },
}));

const Search = styled('div')(({ theme }) => ({
  position: 'relative',
  borderRadius: theme.shape.borderRadius,
  backgroundColor: alpha(theme.palette.common.white, 0.15),
  '&:hover': {
    backgroundColor: alpha(theme.palette.common.white, 0.25),
  },
  marginLeft: theme.spacing(2),
  marginRight: theme.spacing(2),
  width: '100%',
  maxWidth: '500px',
  [theme.breakpoints.down('sm')]: {
    marginLeft: theme.spacing(1),
    width: 'auto',
  },
  [theme.breakpoints.up('md')]: {
    width: '400px',
  },
}));

const SearchIconWrapper = styled('div')(({ theme }) => ({
  padding: theme.spacing(0, 2),
  height: '100%',
  position: 'absolute',
  pointerEvents: 'none',
  display: 'flex',
  alignItems: 'center',
  justifyContent: 'center',
  color: theme.palette.grey[500],
}));

const StyledInputBase = styled(InputBase)(({ theme }) => ({
  color: 'inherit',
  width: '100%',
  '& .MuiInputBase-input': {
    padding: theme.spacing(1, 1, 1, 0),
    paddingLeft: `calc(1em + ${theme.spacing(4)})`,
    transition: theme.transitions.create('width'),
    width: '100%',
    [theme.breakpoints.up('sm')]: {
      width: '20ch',
      '&:focus': {
        width: '30ch',
      },
    },
  },
}));

const Header = () => {
  const theme = useTheme();
  const location = useLocation();
  const isMobile = useMediaQuery(theme.breakpoints.down('md'));
  const navigate = useNavigate();
  const { user, logout } = useAuth();
  const [anchorEl, setAnchorEl] = useState<null | HTMLElement>(null);
  const [_, setMobileMoreAnchorEl] = useState<null | HTMLElement>(null);
  const [searchQuery, setSearchQuery] = useState('');

  const menuItems = [
    { text: 'Home', icon: <HomeIcon />, path: '/' },
    { text: 'Services', icon: <CategoryIcon />, path: '/services' },
    { text: 'About', icon: <InfoIcon />, path: '/about' },
    { text: 'Contact', icon: <ContactIcon />, path: '/contact' },
  ];

  const handleMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setAnchorEl(event.currentTarget);
  };

  const handleMenuClose = () => {
    setAnchorEl(null);
  };

  const handleLogout = () => {
    logout();
    handleMenuClose();
    navigate('/login');
  };

  const handleMobileMenuOpen = (event: React.MouseEvent<HTMLElement>) => {
    setMobileMoreAnchorEl(event.currentTarget);
  };

  // Mobile menu handler is managed by the menu component directly

  const handleProfileClick = () => {
    navigate('/profile');
  };

  return (
    <AppBar 
      position="sticky" 
      color="primary"
      elevation={0}
      sx={{
        boxShadow: theme.shadows[3],
      }}
    >
      <Container maxWidth={false} sx={{ px: { xs: 2, md: 4 } }}>
        <StyledToolbar disableGutters>
          {/* Logo and Company Name */}
          <Box
            component={RouterLink}
            to="/"
            sx={{
              display: 'flex',
              alignItems: 'center',
              textDecoration: 'none',
              color: 'inherit',
              '&:hover': {
                opacity: 0.9,
              },
            }}
          >
            <Box
              component="img"
              src="/logo.png"
              alt="UrbanServices"
              sx={{
                height: 40,
                width: 'auto',
                mr: 1,
                filter: 'brightness(0) invert(1)',
              }}
              onError={(e) => {
                const target = e.target as HTMLImageElement;
                target.style.display = 'none';
              }}
            />
            <LogoText variant="h6" noWrap>
              UrbanServices
            </LogoText>
          </Box>

          {/* Mobile menu button */}
          <Box sx={{ flexGrow: 1, display: { xs: 'flex', md: 'none' }, justifyContent: 'flex-end' }}>
            <IconButton
              size="large"
              aria-label="show more"
              aria-haspopup="true"
              onClick={handleMobileMenuOpen}
              color="inherit"
            >
              <MenuIcon />
            </IconButton>
          </Box>

          {/* Search Bar - Only show on desktop */}
          <Box sx={{ display: { xs: 'none', md: 'flex' }, flexGrow: 1, maxWidth: '500px', mx: 2 }}>
            <Search>
              <SearchIconWrapper>
                <SearchIcon />
              </SearchIconWrapper>
              <StyledInputBase
                placeholder="Search services..."
                inputProps={{ 'aria-label': 'search' }}
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                sx={{
                  color: 'common.white',
                  '& .MuiInputBase-input::placeholder': {
                    color: 'rgba(255, 255, 255, 0.7)',
                    opacity: 1,
                  },
                }}
              />
            </Search>
          </Box>

          {/* Desktop Navigation */}
          <Box sx={{ flexGrow: 1, display: { xs: 'none', md: 'flex' }, ml: 4 }}>
            {menuItems.map((item) => (
              <Button
                key={item.text}
                component={RouterLink}
                to={item.path}
                startIcon={item.icon}
                sx={{
                  my: 2,
                  color: 'common.white',
                  display: 'flex',
                  alignItems: 'center',
                  mx: 1,
                  px: 2,
                  borderRadius: 1,
                  '&:hover': {
                    backgroundColor: 'rgba(255, 255, 255, 0.1)',
                    transform: 'translateY(-2px)',
                    transition: 'all 0.2s ease-in-out',
                  },
                  ...(location.pathname === item.path && {
                    backgroundColor: 'rgba(255, 255, 255, 0.15)',
                    fontWeight: 'bold',
                  }),
                }}
              >
                {item.text}
              </Button>
            ))}
          </Box>

          {/* User Actions */}
          <Box sx={{ display: 'flex', alignItems: 'center', ml: 2 }}>
            {user ? (
              <Stack direction="row" spacing={1} alignItems="center">
                <IconButton
                  size="medium"
                  color="inherit"
                  sx={{
                    color: theme.palette.text.secondary,
                    '&:hover': {
                      backgroundColor: theme.palette.action.hover,
                    },
                  }}
                >
                  <Badge badgeContent={3} color="error">
                    <NotificationsIcon />
                  </Badge>
                </IconButton>

                <IconButton
                  size="medium"
                  color="inherit"
                  sx={{
                    color: theme.palette.text.secondary,
                    '&:hover': {
                      backgroundColor: theme.palette.action.hover,
                    },
                  }}
                >
                  <Badge badgeContent={2} color="primary">
                    <CartIcon />
                  </Badge>
                </IconButton>

                <IconButton
                  onClick={handleMenuOpen}
                  size="small"
                  sx={{
                    ml: 1,
                    border: `1px solid ${theme.palette.divider}`,
                    '&:hover': {
                      borderColor: theme.palette.primary.main,
                    },
                  }}
                  aria-controls={Boolean(anchorEl) ? 'account-menu' : undefined}
                  aria-haspopup="true"
                  aria-expanded={Boolean(anchorEl) ? 'true' : undefined}
                >
                  <Avatar
                    alt={user.name || 'User'}
                    src={user.avatar}
                    sx={{ 
                      width: 32, 
                      height: 32,
                      bgcolor: theme.palette.primary.main,
                      color: theme.palette.primary.contrastText,
                      fontSize: '0.875rem',
                    }}
                  >
                    {user.name ? user.name.charAt(0).toUpperCase() : <PersonIcon />}
                  </Avatar>
                </IconButton>
              </Stack>
            ) : (
              <Stack direction="row" spacing={2} sx={{ ml: 2 }}>
                <Button
                  variant="outlined"
                  color="inherit"
                  onClick={() => navigate('/login')}
                  sx={{
                    borderColor: 'rgba(255, 255, 255, 0.5)',
                    color: 'common.white',
                    '&:hover': {
                      borderColor: 'common.white',
                      backgroundColor: 'rgba(255, 255, 255, 0.1)',
                    },
                  }}
                >
                  Login
                </Button>
                <Button
                  variant="contained"
                  color="secondary"
                  onClick={() => navigate('/register')}
                  sx={{
                    boxShadow: 'none',
                    textTransform: 'none',
                    fontWeight: 600,
                    '&:hover': {
                      boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
                      transform: 'translateY(-2px)',
                      transition: 'all 0.2s ease-in-out',
                    },
                  }}
                >
                  Sign Up
                </Button>
              </Stack>
            )}
          </Box>
        </StyledToolbar>
      </Container>

      {/* Mobile Menu */}
      {isMobile && (
        <Box 
          sx={{
            backgroundColor: theme.palette.background.paper,
            borderTop: `1px solid ${theme.palette.divider}`,
            py: 1,
            px: 2,
          }}
        >
          <Stack spacing={1}>
            {menuItems.map((item) => (
              <Button
                key={item.text}
                component={RouterLink}
                to={item.path}
                fullWidth
                sx={{
                  justifyContent: 'flex-start',
                  color: location.pathname === item.path ? theme.palette.primary.main : theme.palette.text.primary,
                  fontWeight: location.pathname === item.path ? 600 : 400,
                  py: 1.5,
                  px: 2,
                  borderRadius: 1,
                  '&:hover': {
                    backgroundColor: theme.palette.action.hover,
                  },
                }}
                onClick={() => setMobileMoreAnchorEl(null)}
              >
                {item.text}
              </Button>
            ))}
          </Stack>
        </Box>
      )}

      {/* User menu dropdown */}
      <Menu
        anchorEl={anchorEl}
        id="account-menu"
        open={Boolean(anchorEl)}
        onClose={handleMenuClose}
        onClick={handleMenuClose}
        PaperProps={{
          elevation: 3,
          sx: {
            width: 240,
            borderRadius: 2,
            overflow: 'hidden',
            mt: 1.5,
            '& .MuiAvatar-root': {
              width: 32,
              height: 32,
              mr: 1.5,
            },
          },
        }}
        transformOrigin={{ horizontal: 'right', vertical: 'top' }}
        anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
      >
        {/* User Profile Header */}
        <Box sx={{ p: 2, bgcolor: 'primary.main', color: 'common.white' }}>
          <Box sx={{ display: 'flex', alignItems: 'center', mb: 1 }}>
            <Avatar 
              alt={user?.name || 'User'} 
              src={user?.avatar}
              sx={{ 
                width: 48, 
                height: 48,
                mr: 2,
                bgcolor: 'secondary.main',
                color: 'secondary.contrastText',
              }}
            >
              {user?.name ? user.name.charAt(0).toUpperCase() : <PersonIcon />}
            </Avatar>
            <Box>
              <Typography variant="subtitle2" fontWeight="bold">
                {user?.name || 'User'}
              </Typography>
              <Typography variant="body2" sx={{ opacity: 0.8 }}>
                {user?.email || ''}
              </Typography>
            </Box>
          </Box>
          <Button
            variant="outlined"
            size="small"
            fullWidth
            onClick={handleProfileClick}
            sx={{
              mt: 1,
              color: 'common.white',
              borderColor: 'rgba(255, 255, 255, 0.3)',
              '&:hover': {
                borderColor: 'common.white',
                backgroundColor: 'rgba(255, 255, 255, 0.1)',
              },
            }}
          >
            View Profile
          </Button>
        </Box>

        {/* Menu Items */}
        <MenuItem onClick={handleProfileClick}>
          <ListItemIcon>
            <PersonIcon fontSize="small" />
          </ListItemIcon>
          My Account
        </MenuItem>
        <MenuItem>
          <ListItemIcon>
            <SettingsIcon fontSize="small" />
          </ListItemIcon>
          Settings
        </MenuItem>
        <Divider />
        <MenuItem onClick={handleLogout}>
          <ListItemIcon>
            <LogoutIcon fontSize="small" />
          </ListItemIcon>
          Logout
        </MenuItem>
      </Menu>
    </AppBar>
  );
};

export default Header;
