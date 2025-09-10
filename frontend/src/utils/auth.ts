/**
 * Authentication utility functions for managing JWT tokens in localStorage
 */

const TOKEN_KEY = 'auth_token';

/**
 * Get the authentication token from localStorage
 * @returns {string | null} The authentication token or null if not found
 */
export const getToken = (): string | null => {
  if (typeof window !== 'undefined') {
    return localStorage.getItem(TOKEN_KEY);
  }
  return null;
};

/**
 * Set the authentication token in localStorage
 * @param {string} token - The JWT token to store
 */
export const setToken = (token: string): void => {
  if (typeof window !== 'undefined') {
    localStorage.setItem(TOKEN_KEY, token);
  }
};

/**
 * Remove the authentication token from localStorage
 */
export const clearToken = (): void => {
  if (typeof window !== 'undefined') {
    localStorage.removeItem(TOKEN_KEY);
  }
};

/**
 * Check if the user is authenticated
 * @returns {boolean} True if a valid token exists, false otherwise
 */
export const isAuthenticated = (): boolean => {
  const token = getToken();
  return !!token; // Simple check - in a real app, you might want to verify the token's validity
};

/**
 * Get the authorization header with the JWT token
 * @returns {Record<string, string>} The authorization header
 */
export const getAuthHeader = (): Record<string, string> => {
  const token = getToken();
  return token ? { Authorization: `Bearer ${token}` } : {};
};
