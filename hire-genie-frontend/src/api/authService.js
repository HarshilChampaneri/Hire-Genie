import axios from 'axios';

const API_BASE_URL = 'http://localhost:8080/api/security/auth';

export const authService = {
  register: (data) => axios.post(`${API_BASE_URL}/register`, data),
  login: (data) => axios.post(`${API_BASE_URL}/login`, data),
  verifyOtp: (data) => axios.post(`${API_BASE_URL}/verify-otp`, data),
  logout: (token) => axios.post(`${API_BASE_URL}/logout`, {}, {
    headers: { Authorization: `Bearer ${token}` }
  }),
};