import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Container,
  Typography,
  Paper,
  Button,
  Avatar,
  Divider,
  TextField,
  Grid,
  IconButton,
  InputAdornment,
} from '@mui/material';
import { Edit, Save, ArrowBack } from '@mui/icons-material';

const ProfilePage = () => {
  const navigate = useNavigate();
  const [isEditing, setIsEditing] = useState(false);
  const [profile, setProfile] = useState({
    name: 'John Doe',
    email: 'john.doe@example.com',
    phone: '+1 (555) 123-4567',
    address: '123 Main St, Anytown, USA',
  });

  const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setProfile(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSave = () => {
    // Here you would typically make an API call to update the profile
    setIsEditing(false);
  };

  return (
    <Container maxWidth="lg" sx={{ py: 4 }}>
      <Box mb={4}>
        <Button
          startIcon={<ArrowBack />}
          onClick={() => navigate(-1)}
          sx={{ mb: 2 }}
        >
          Back
        </Button>

        <Box display="flex" justifyContent="space-between" alignItems="center" mb={3}>
          <Typography variant="h4" component="h1">
            My Profile
          </Typography>
          {!isEditing && (
            <Button
              variant="contained"
              startIcon={<Edit />}
              onClick={() => setIsEditing(true)}
            >
              Edit Profile
            </Button>
          )}
        </Box>

        <Paper elevation={0} sx={{ p: 3 }}>
          <Grid container spacing={3}>
            <Grid item xs={12} md={4} sx={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
              <Avatar
                sx={{
                  width: 150,
                  height: 150,
                  mb: 2,
                  fontSize: '3rem',
                }}
                src="/path/to/avatar.jpg"
              >
                {profile.name.split(' ').map(n => n[0]).join('')}
              </Avatar>
              {isEditing && (
                <Button variant="outlined" component="label" sx={{ mb: 2 }}>
                  Change Photo
                  <input type="file" hidden accept="image/*" />
                </Button>
              )}
            </Grid>

            <Grid item xs={12} md={8}>
              <Grid container spacing={2}>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Full Name"
                    name="name"
                    value={profile.name}
                    onChange={handleInputChange}
                    disabled={!isEditing}
                    variant={isEditing ? 'outlined' : 'standard'}
                    InputProps={!isEditing ? { disableUnderline: true } : {}}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Email"
                    name="email"
                    type="email"
                    value={profile.email}
                    onChange={handleInputChange}
                    disabled={!isEditing}
                    variant={isEditing ? 'outlined' : 'standard'}
                    InputProps={!isEditing ? { disableUnderline: true } : {}}
                  />
                </Grid>
                <Grid item xs={12} sm={6}>
                  <TextField
                    fullWidth
                    label="Phone"
                    name="phone"
                    value={profile.phone}
                    onChange={handleInputChange}
                    disabled={!isEditing}
                    variant={isEditing ? 'outlined' : 'standard'}
                    InputProps={!isEditing ? { disableUnderline: true } : {}}
                  />
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    fullWidth
                    label="Address"
                    name="address"
                    value={profile.address}
                    onChange={handleInputChange}
                    disabled={!isEditing}
                    multiline
                    rows={2}
                    variant={isEditing ? 'outlined' : 'standard'}
                    InputProps={!isEditing ? { disableUnderline: true } : {}}
                  />
                </Grid>
              </Grid>

              {isEditing && (
                <Box mt={3} display="flex" justifyContent="flex-end" gap={2}>
                  <Button
                    variant="outlined"
                    onClick={() => setIsEditing(false)}
                  >
                    Cancel
                  </Button>
                  <Button
                    variant="contained"
                    startIcon={<Save />}
                    onClick={handleSave}
                  >
                    Save Changes
                  </Button>
                </Box>
              )}
            </Grid>
          </Grid>
        </Paper>
      </Box>
    </Container>
  );
};

export default ProfilePage;
