import { useState, useEffect } from 'react';
import { AuthContext } from './AuthContext';
import { authService } from '../api/authService';

/**
 * Decodes a JWT token payload without verifying its signature.
 * Used purely on the client side for UI role-checks.
 * The backend still enforces role-based access on every API request.
 *
 * JWT structure confirmed from JwtService.java:
 * {
 *   "sub":   "user@email.com",
 *   "roles": ["ROLE_EMPLOYEE", "ROLE_RECRUITER"],
 *   "iat":   ...,
 *   "exp":   ...
 * }
 *
 * @param {string} token - JWT string
 * @returns {object|null} decoded payload, or null if decoding fails
 */
const decodeJwt = (token) => {
    try {
        const payloadBase64 = token.split('.')[1];
        const base64 = payloadBase64.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(
            atob(base64)
                .split('')
                .map((c) => '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2))
                .join('')
        );
        return JSON.parse(jsonPayload);
    } catch {
        return null;
    }
};

export const AuthProvider = ({ children }) => {
    const [token, setToken] = useState(localStorage.getItem('token'));

    // Decode JWT and extract roles on every token change
    const payload         = decodeJwt(token);
    const roles           = Array.isArray(payload?.roles) ? payload.roles : [];

    // Derived booleans — role strings match Spring Security GrantedAuthority format
    const isAuthenticated = !!token;
    const isRecruiter     = roles.includes('ROLE_RECRUITER');

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
            console.error('Logout failed at backend, clearing local state anyway.', error);
        } finally {
            setToken(null);
        }
    };

    return (
        <AuthContext.Provider value={{ token, isAuthenticated, isRecruiter, loginUser, logoutUser }}>
            {children}
        </AuthContext.Provider>
    );
};