import { useState, useEffect } from 'react';
import { AuthContext } from './AuthContext';
import { authService } from '../api/authService';

export const AuthProvider = ({ children }) => {
    const [token, setToken] = useState(localStorage.getItem('token'));
    const isAuthenticated = !!token;

    useEffect(() => {
        if (token) {
            localStorage.setItem('token', token);
        } else {
            localStorage.removeItem('token');
        }
    }, [token]);

    const loginUser = (receivedToken) => setToken(receivedToken);

    const logoutUser = async () => {
        try {
            if (token) await authService.logout(token);
        } catch (error) {
            console.error("Logout failed at backend, clearing local state anyway.", error);
        } finally {
            setToken(null);
        }
    };

    return (
        <AuthContext.Provider value={{ token, isAuthenticated, loginUser, logoutUser }}>
            {children}
        </AuthContext.Provider>
    );
};