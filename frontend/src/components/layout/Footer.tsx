import { Box, Container, Link, Typography, useTheme, Button } from '@mui/material';
import { Link as RouterLink } from 'react-router-dom';

const Footer = () => {
  const theme = useTheme();
  const currentYear = new Date().getFullYear();

  return (
    <Box
      component="footer"
      sx={{
        width: '100%',
        maxWidth: '100vw',
        backgroundColor: theme.palette.background.paper,
        borderTop: `1px solid ${theme.palette.divider}`,
        py: 6,
        mt: 'auto',
      }}
    >
      <Container maxWidth={false} sx={{ px: { xs: 2, sm: 3, md: 4 } }}>
        <Box sx={{ 
          display: 'flex', 
          flexWrap: 'wrap', 
          gap: 4, 
          mb: 4,
          maxWidth: '100%',
          width: '100%',
          margin: '0 auto',
          px: { xs: 2, sm: 3, md: 4 }
        }}>
          {/* Company Info */}
          <Box sx={{ flex: '1 1 240px', minWidth: '200px' }}>
            <Box 
              component="img"
              src="/images/logo.png" 
              alt="UrbanServices Logo"
              sx={{ 
                height: 40,
                width: 'auto',
                mb: 2,
                display: 'block'
              }}
              onError={(e) => {
                // Fallback to text if image fails to load
                const target = e.target as HTMLImageElement;
                target.style.display = 'none';
                const textFallback = document.createElement('div');
                textFallback.textContent = 'UrbanServices';
                textFallback.style.fontWeight = 'bold';
                textFallback.style.color = theme.palette.primary.main;
                target.parentNode?.insertBefore(textFallback, target.nextSibling);
              }}
            />
            <Typography variant="body2" color="text.secondary">
              Your trusted partner for all home and business service needs. Quality professionals at your fingertips.
            </Typography>
          </Box>

          {/* Services */}
          <Box sx={{ flex: '1 1 160px', minWidth: '160px' }}>
            <Typography variant="subtitle1" color="text.primary" gutterBottom>
              Services
            </Typography>
            <Box component="ul" sx={{ listStyle: 'none', p: 0, m: 0 }}>
              {['Cleaning', 'Repairs', 'Beauty & Wellness'].map((service) => (
                <li key={service}>
                  <Link
                    component={RouterLink}
                    to={`/services/${service.toLowerCase().replace(/\s+/g, '-')}`}
                    variant="body2"
                    color="text.secondary"
                    sx={{
                      display: 'block',
                      mb: 1,
                      textDecoration: 'none',
                      '&:hover': {
                        color: 'primary.main',
                        textDecoration: 'underline',
                      },
                    }}
                  >
                    {service}
                  </Link>
                </li>
              ))}
            </Box>
          </Box>

          {/* Company */}
          <Box sx={{ flex: '1 1 160px', minWidth: '160px' }}>
            <Typography variant="subtitle1" color="text.primary" gutterBottom>
              Company
            </Typography>
            <Box component="ul" sx={{ listStyle: 'none', p: 0, m: 0 }}>
              {['About Us', 'Careers', 'Blog', 'Press'].map((item) => (
                <li key={item}>
                  <Link
                    component={RouterLink}
                    to={`/${item.toLowerCase().replace(/\s+/g, '-')}`}
                    variant="body2"
                    color="text.secondary"
                    sx={{
                      display: 'block',
                      mb: 1,
                      textDecoration: 'none',
                      '&:hover': {
                        color: 'primary.main',
                        textDecoration: 'underline',
                      },
                    }}
                  >
                    {item}
                  </Link>
                </li>
              ))}
            </Box>
          </Box>

          {/* Support */}
          <Box sx={{ flex: '1 1 160px', minWidth: '160px' }}>
            <Typography variant="subtitle1" color="text.primary" gutterBottom>
              Support
            </Typography>
            <Box component="ul" sx={{ listStyle: 'none', p: 0, m: 0 }}>
              {['Help Center', 'Contact Us', 'Privacy Policy', 'Terms of Service'].map((item) => (
                <li key={item}>
                  <Link
                    component={RouterLink}
                    to={`/${item.toLowerCase().replace(/\s+/g, '-')}`}
                    variant="body2"
                    color="text.secondary"
                    sx={{
                      display: 'block',
                      mb: 1,
                      textDecoration: 'none',
                      '&:hover': {
                        color: 'primary.main',
                        textDecoration: 'underline',
                      },
                    }}
                  >
                    {item}
                  </Link>
                </li>
              ))}
            </Box>
          </Box>

          {/* Connect */}
          <Box sx={{ flex: '1 1 240px', minWidth: '200px' }}>
            <Typography variant="subtitle1" color="text.primary" gutterBottom>
              Connect With Us
            </Typography>
            <Box sx={{ display: 'flex', gap: 2, mb: 3, flexWrap: 'wrap' }}>
              {[
                { 
                  icon: <Box component="img" src="/images/facebook.svg" alt="Facebook" sx={{ width: 24, height: 24 }} />, 
                  url: 'https://facebook.com' 
                },
                { 
                  icon: <Box component="img" src="/images/twitter.svg" alt="Twitter" sx={{ width: 24, height: 24 }} />, 
                  url: 'https://twitter.com' 
                },
                { 
                  icon: <Box component="img" src="/images/instagram.svg" alt="Instagram" sx={{ width: 24, height: 24 }} />, 
                  url: 'https://instagram.com' 
                },
                { 
                  icon: <Box component="img" src="/images/linkedin.svg" alt="LinkedIn" sx={{ width: 24, height: 24 }} />, 
                  url: 'https://linkedin.com' 
                },
              ].map((social, index) => (
                <Box
                  key={index}
                  component="a"
                  href={social.url}
                  target="_blank"
                  rel="noopener noreferrer"
                  sx={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    width: 36,
                    height: 36,
                    borderRadius: '50%',
                    backgroundColor: theme.palette.action.hover,
                    '&:hover': {
                      backgroundColor: theme.palette.action.selected,
                      transform: 'translateY(-2px)',
                      transition: 'all 0.3s ease',
                    },
                  }}
                >
                  {social.icon}
                </Box>
              ))}
            </Box>
            <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
              Subscribe to our newsletter for the latest updates
            </Typography>
            <Box sx={{ display: 'flex' }}>
              <input
                type="email"
                placeholder="Your email"
                style={{
                  flex: 1,
                  padding: '8px 12px',
                  border: `1px solid ${theme.palette.divider}`,
                  borderRadius: '4px 0 0 4px',
                  outline: 'none',
                }}
              />
              <Button
                variant="contained"
                color="primary"
                sx={{
                  borderRadius: '0 4px 4px 0',
                  height: '40px',
                  '&:hover': {
                    transform: 'translateY(-2px)',
                    boxShadow: theme.shadows[4],
                  },
                }}
              >
                Subscribe
              </Button>
            </Box>
          </Box>
        </Box>

        {/* Copyright */}
        <Box
          sx={{
            borderTop: `1px solid ${theme.palette.divider}`,
            pt: 3,
            display: 'flex',
            flexDirection: { xs: 'column', sm: 'row' },
            justifyContent: 'space-between',
            alignItems: 'center',
            gap: 2,
          }}
        >
          <Typography variant="body2" color="text.secondary">
            © {currentYear} UrbanServices. All rights reserved.
          </Typography>
          <Box sx={{ display: 'flex', gap: 3 }}>
            {['Terms', 'Privacy', 'Accessibility', 'Cookies'].map((item) => (
              <Link
                key={item}
                component={RouterLink}
                to={`/${item.toLowerCase()}`}
                variant="body2"
                color="text.secondary"
                sx={{ textDecoration: 'none', '&:hover': { textDecoration: 'underline' } }}
              >
                {item}
              </Link>
            ))}
          </Box>
        </Box>
      </Container>
    </Box>
  );
};

export default Footer;
