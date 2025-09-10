import { createTheme } from '@mui/material/styles';

declare module '@mui/material/styles' {
  interface Theme {
    custom: {
      maxContentWidth: number;
    };
  }
  // Allow configuration using `createTheme`
  interface ThemeOptions {
    custom?: {
      maxContentWidth?: number;
    };
  }
}

// Create a theme instance
const theme = createTheme({
  palette: {
    primary: {
      main: '#1a73e8', // Urban Company blue
      light: '#4285f4',
      dark: '#1557b0',
      contrastText: '#ffffff',
    },
    secondary: {
      main: '#0f9d58', // Urban Company green
      light: '#34a853',
      dark: '#0b8043',
      contrastText: '#ffffff',
    },
    error: {
      main: '#d93025',
      light: '#f4b400',
      dark: '#b31412',
    },
    warning: {
      main: '#f9ab00',
      light: '#fbbc04',
      dark: '#e37400',
    },
    info: {
      main: '#1a73e8',
      light: '#8ab4f8',
      dark: '#174ea6',
    },
    success: {
      main: '#0f9d58',
      light: '#81c995',
      dark: '#0b8043',
    },
    text: {
      primary: '#3c4043',
      secondary: '#5f6368',
      disabled: '#9aa0a6',
    },
    background: {
      default: '#f8f9fa',
      paper: '#ffffff',
    },
    divider: 'rgba(0, 0, 0, 0.12)',
  },
  typography: {
    fontFamily: [
      'Inter',
      '-apple-system',
      'BlinkMacSystemFont',
      '"Segoe UI"',
      'Roboto',
      'sans-serif',
    ].join(','),
    h1: {
      fontWeight: 600,
      fontSize: '2.5rem',
      lineHeight: 1.2,
      letterSpacing: '-0.02em',
      color: '#202124',
    },
    h2: {
      fontWeight: 600,
      fontSize: '2rem',
      lineHeight: 1.25,
      letterSpacing: '-0.015em',
      color: '#202124',
    },
    h3: {
      fontWeight: 600,
      fontSize: '1.75rem',
      lineHeight: 1.3,
      letterSpacing: '0em',
      color: '#202124',
    },
    h4: {
      fontWeight: 500,
      fontSize: '1.5rem',
      lineHeight: 1.33,
      letterSpacing: '0.00735em',
      color: '#202124',
    },
    h5: {
      fontWeight: 500,
      fontSize: '1.25rem',
      lineHeight: 1.4,
      letterSpacing: '0em',
      color: '#202124',
    },
    h6: {
      fontWeight: 500,
      fontSize: '1.125rem',
      lineHeight: 1.5,
      letterSpacing: '0.0075em',
      color: '#202124',
    },
    subtitle1: {
      fontWeight: 500,
      fontSize: '1rem',
      lineHeight: 1.5,
      letterSpacing: '0.00938em',
      color: '#3c4043',
    },
    subtitle2: {
      fontWeight: 500,
      fontSize: '0.875rem',
      lineHeight: 1.5,
      letterSpacing: '0.00714em',
      color: '#5f6368',
    },
    body1: {
      fontWeight: 400,
      fontSize: '1rem',
      lineHeight: 1.5,
      letterSpacing: '0.00938em',
      color: '#3c4043',
    },
    body2: {
      fontWeight: 400,
      fontSize: '0.875rem',
      lineHeight: 1.5,
      letterSpacing: '0.01071em',
      color: '#5f6368',
    },
    button: {
      fontWeight: 500,
      fontSize: '0.875rem',
      lineHeight: 1.75,
      letterSpacing: '0.02em',
      textTransform: 'none',
    },
    caption: {
      fontWeight: 400,
      fontSize: '0.75rem',
      lineHeight: 1.5,
      letterSpacing: '0.03333em',
      color: '#5f6368',
    },
    overline: {
      fontWeight: 500,
      fontSize: '0.75rem',
      lineHeight: 1.5,
      letterSpacing: '0.05em',
      textTransform: 'uppercase',
      color: '#5f6368',
    },
  },
  shape: {
    borderRadius: 8,
  },
  components: {
    MuiButton: {
      styleOverrides: {
        root: {
          textTransform: 'none',
          fontWeight: 500,
          padding: '8px 24px',
          boxShadow: 'none',
          '&:hover': {
            boxShadow: '0 1px 2px 0 rgba(60, 64, 67, 0.3), 0 1px 3px 1px rgba(60, 64, 67, 0.15)',
          },
        },
        contained: {
          '&.MuiButton-containedPrimary': {
            backgroundColor: '#1a73e8',
            '&:hover': {
              backgroundColor: '#1557b0',
            },
          },
          '&.MuiButton-containedSecondary': {
            backgroundColor: '#0f9d58',
            '&:hover': {
              backgroundColor: '#0b8043',
            },
          },
        },
        outlined: {
          borderWidth: '1.5px',
          '&:hover': {
            borderWidth: '1.5px',
          },
        },
      },
    },
    MuiCard: {
      styleOverrides: {
        root: {
          borderRadius: 12,
          boxShadow: '0 1px 2px 0 rgba(60, 64, 67, 0.1), 0 1px 3px 1px rgba(60, 64, 67, 0.1)',
          transition: 'all 0.2s ease-in-out',
          overflow: 'visible',
          '&:hover': {
            transform: 'translateY(-2px)',
            boxShadow: '0 4px 8px 0 rgba(60, 64, 67, 0.1), 0 2px 10px 0 rgba(60, 64, 67, 0.1)',
          },
        },
      },
    },
    MuiAppBar: {
      styleOverrides: {
        root: {
          backgroundColor: '#ffffff',
          color: '#3c4043',
          boxShadow: '0 1px 2px 0 rgba(60, 64, 67, 0.1), 0 1px 3px 1px rgba(60, 64, 67, 0.1)',
        },
      },
    },
    MuiTextField: {
      styleOverrides: {
        root: {
          '& .MuiOutlinedInput-root': {
            borderRadius: 8,
            '& fieldset': {
              borderColor: '#dadce0',
            },
            '&:hover fieldset': {
              borderColor: '#1a73e8',
            },
            '&.Mui-focused fieldset': {
              borderColor: '#1a73e8',
              borderWidth: '1px',
            },
          },
        },
      },
    },
    MuiChip: {
      styleOverrides: {
        root: {
          borderRadius: 4,
          fontWeight: 500,
        },
      },
    },
  },
  custom: {
    maxContentWidth: 1200,
  },
});

export default theme;
