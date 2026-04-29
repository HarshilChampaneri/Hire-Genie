import axios from 'axios';

const API_BASE_URL = '/api/security/auth';

export const authService = {
  sendRegistrationOtp: (email) => axios.post(`${API_BASE_URL}/send-registration-otp`, { email }),
  register: (data) => axios.post(`${API_BASE_URL}/register`, data),
  login: (data) => axios.post(`${API_BASE_URL}/login`, data),
  verifyOtp: (data) => axios.post(`${API_BASE_URL}/verify-otp`, data),
  logout: (token) => axios.post(`${API_BASE_URL}/logout`, {}, {
    headers: { Authorization: `Bearer ${token}` }
  }),
};