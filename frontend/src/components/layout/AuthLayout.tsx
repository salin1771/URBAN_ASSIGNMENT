import { Box, Container, CssBaseline, useTheme } from '@mui/material';
import { Outlet } from 'react-router-dom';

const AuthLayout = () => {
  const theme = useTheme();

  return (
    <Box
      sx={{
        display: 'flex',
        flexDirection: 'column',
        minHeight: '100vh',
        backgroundColor: theme.palette.grey[100],
        alignItems: 'center',
        justifyContent: 'center',
        p: 3,
      }}
    >
      <CssBaseline />
      <Container
        component="main"
        maxWidth="xs"
        sx={{
          display: 'flex',
          flexDirection: 'column',
          alignItems: 'center',
          backgroundColor: 'background.paper',
          p: 4,
          borderRadius: 2,
          boxShadow: 1,
        }}
      >
        <Outlet />
      </Container>
    </Box>
  );
};

export default AuthLayout;
