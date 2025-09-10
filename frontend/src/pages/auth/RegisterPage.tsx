import { useState } from 'react';
import { Link as RouterLink } from 'react-router-dom';
import { useFormik } from 'formik';
import * as Yup from 'yup';
import { Box, Button, Container, Link, TextField, Typography, Tabs, Tab } from '@mui/material';
import { useAuth } from '../../context/AuthContext';

// Tab panel component
function TabPanel({ children, value, index }: any) {
  return <div hidden={value !== index}>{value === index && <Box sx={{ py: 2 }}>{children}</Box>}</div>;
}

const RegisterPage = () => {
  const { registerCustomer } = useAuth();
  const [tabValue, setTabValue] = useState(0);
  const [error, setError] = useState('');

  // Customer form
  const customerFormik = useFormik({
    initialValues: { name: '', email: '', password: '' },
    validationSchema: Yup.object({
      name: Yup.string().required('Required'),
      email: Yup.string().email('Invalid email').required('Required'),
      password: Yup.string().min(8, 'Minimum 8 characters').required('Required'),
    }),
    onSubmit: async (values) => {
      try {
        await registerCustomer(values);
      } catch (err: any) {
        setError(err.message || 'Registration failed');
      }
    },
  });

  return (
    <Container maxWidth="sm">
      <Box sx={{ mt: 4, textAlign: 'center' }}>
        <Typography variant="h4" sx={{ mb: 2 }}>Create an account</Typography>
        <Typography color="text.secondary" sx={{ mb: 4 }}>
          Already have an account?{' '}
          <Link component={RouterLink} to="/login">Sign in</Link>
        </Typography>

        <Tabs value={tabValue} onChange={(e, v) => setTabValue(v)} centered sx={{ mb: 3 }}>
          <Tab label="Customer" />
          <Tab label="Professional" />
        </Tabs>

        {error && <div style={{ color: 'red', marginBottom: '1rem' }}>{error}</div>}

        <TabPanel value={tabValue} index={0}>
          <form onSubmit={customerFormik.handleSubmit}>
            <TextField
              fullWidth
              margin="normal"
              id="name"
              name="name"
              label="Full Name"
              value={customerFormik.values.name}
              onChange={customerFormik.handleChange}
              error={customerFormik.touched.name && Boolean(customerFormik.errors.name)}
              helperText={customerFormik.touched.name && customerFormik.errors.name}
            />
            <TextField
              fullWidth
              margin="normal"
              id="email"
              name="email"
              label="Email"
              value={customerFormik.values.email}
              onChange={customerFormik.handleChange}
              error={customerFormik.touched.email && Boolean(customerFormik.errors.email)}
              helperText={customerFormik.touched.email && customerFormik.errors.email}
            />
            <TextField
              fullWidth
              margin="normal"
              id="password"
              name="password"
              label="Password"
              type="password"
              value={customerFormik.values.password}
              onChange={customerFormik.handleChange}
              error={customerFormik.touched.password && Boolean(customerFormik.errors.password)}
              helperText={customerFormik.touched.password && customerFormik.errors.password}
            />
            <Button
              fullWidth
              type="submit"
              variant="contained"
              sx={{ mt: 3, mb: 2, py: 1.5 }}
            >
              Sign Up as Customer
            </Button>
          </form>
        </TabPanel>

        <TabPanel value={tabValue} index={1}>
          <div>Professional registration coming soon</div>
        </TabPanel>
      </Box>
    </Container>
  );
};

export default RegisterPage;
