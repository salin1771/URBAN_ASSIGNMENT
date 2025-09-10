import { createContext, useContext, useState, useCallback, useMemo, useEffect } from 'react';
import type { ReactNode } from 'react';
import { useQuery, useQueryClient } from '@tanstack/react-query';
import { getUserProfile } from '../services/authService';
import type { User } from '../types/auth.types';

type AuthContextType = {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (token: string, user: User) => void;
  logout: () => void;
  updateUser: (user: User) => void;
};

const AuthContext = createContext<AuthContextType | undefined>(undefined);

type AuthProviderProps = {
  children: ReactNode;
};

export const AuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const queryClient = useQueryClient();

  // Handle successful authentication
  const handleSuccess = useCallback((data: User) => {
    setUser(data);
    setIsLoading(false);
  }, []);

  // Handle authentication error
  const handleError = useCallback(() => {
    setUser(null);
    setIsLoading(false);
  }, []);

  // Check if user is authenticated on initial load
  const { isFetching } = useQuery({
    queryKey: ['user'],
    queryFn: getUserProfile,
    enabled: !user,
    refetchOnWindowFocus: false,
    retry: 1,
  });

  // Handle query success and error using useEffect
  useEffect(() => {
    if (isFetching) return;
    
    const currentUser = queryClient.getQueryData<User>(['user']);
    if (currentUser) {
      handleSuccess(currentUser);
    } else {
      handleError();
    }
  }, [isFetching, queryClient, handleSuccess, handleError]);

  const login = useCallback((token: string, userData: User) => {
    localStorage.setItem('token', token);
    setUser(userData);
  }, []);

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    setUser(null);
    queryClient.clear();
  }, [queryClient]);

  const updateUser = useCallback((userData: User) => {
    setUser(userData);
  }, []);

  const value = useMemo(() => ({
    user,
    isAuthenticated: !!user,
    isLoading: isLoading || isFetching,
    login,
    logout,
    updateUser,
  }), [user, isLoading, isFetching, login, logout, updateUser]);

  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
