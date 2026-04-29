import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authService } from '../api/authService';

const EyeIcon = ({ open }) => open ? (
  <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.477 0 8.268 2.943 9.542 7-1.274 4.057-5.065 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
  </svg>
) : (
  <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13.875 18.825A10.05 10.05 0 0112 19c-4.477 0-8.268-2.943-9.542-7a9.97 9.97 0 012.223-3.592M6.53 6.533A9.956 9.956 0 0112 5c4.477 0 8.268 2.943 9.542 7a10.025 10.025 0 01-4.132 5.411M3 3l18 18" />
  </svg>
);

const Register = () => {
  const navigate = useNavigate();
  const [step, setStep] = useState(1); // 1: Fill form, 2: Verify OTP
  const [otp, setOtp] = useState('');
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [showAdminSecret, setShowAdminSecret] = useState(false);

  const [formData, setFormData] = useState({
    email: '',
    password: '',
    roles: ['ROLE_EMPLOYEE'],
    adminSecret: ''
  });

  const handleRoleChange = (role) => {
    setFormData(prev => ({
      ...prev,
      roles: [role],
      adminSecret: role === 'ROLE_ADMIN' ? prev.adminSecret : ''
    }));
  };

  // Step 1: Send OTP to email to verify it's real
  const handleSendOtp = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await authService.sendRegistrationOtp(formData.email);
      setStep(2);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to send OTP. Check your email address.');
    } finally {
      setLoading(false);
    }
  };

  // Step 2: Verify OTP then register
  const handleVerifyAndRegister = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await authService.register({ ...formData, otp });
      alert('Registration Successful! Please login.');
      navigate('/login');
    } catch (err) {
      setError(err.response?.data?.message || 'Invalid OTP or registration failed.');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-900 text-white p-6">
      <div className="w-full max-w-md bg-slate-800 rounded-2xl shadow-xl p-8 border border-slate-700">

        {/* Step indicator */}
        <div className="flex items-center justify-center gap-3 mb-6">
          <div className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-bold
            ${step === 1 ? 'bg-blue-600' : 'bg-green-600'}`}>
            {step === 1 ? '1' : '✓'}
          </div>
          <div className={`h-1 w-12 rounded ${step === 2 ? 'bg-blue-600' : 'bg-slate-600'}`} />
          <div className={`w-8 h-8 rounded-full flex items-center justify-center text-sm font-bold
            ${step === 2 ? 'bg-blue-600' : 'bg-slate-600'}`}>
            2
          </div>
        </div>

        <h2 className="text-3xl font-bold text-center mb-2 text-blue-400">Join Hire-Genie</h2>
        <p className="text-center text-slate-400 text-sm mb-6">
          {step === 1 ? 'Fill in your details' : `Enter the OTP sent to ${formData.email}`}
        </p>

        {error && (
          <div className="bg-red-500/10 border border-red-500 text-red-400 text-sm rounded-lg p-3 mb-4">
            {error}
          </div>
        )}

        {step === 1 ? (
          <form onSubmit={handleSendOtp} className="space-y-4">
            <input
              type="email" placeholder="Email"
              className="w-full p-3 rounded bg-slate-700 border border-slate-600 focus:outline-none focus:border-blue-500"
              value={formData.email}
              onChange={(e) => setFormData({ ...formData, email: e.target.value })}
              required
            />

            <div className="relative">
              <input
                type={showPassword ? 'text' : 'password'}
                placeholder="Password"
                className="w-full p-3 rounded bg-slate-700 border border-slate-600 focus:outline-none focus:border-blue-500 pr-10"
                onChange={(e) => setFormData({ ...formData, password: e.target.value })}
                required
              />
              <button type="button" onClick={() => setShowPassword(p => !p)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-white">
                <EyeIcon open={showPassword} />
              </button>
            </div>

            <div className="flex gap-4 py-2">
              {['ROLE_EMPLOYEE', 'ROLE_RECRUITER', 'ROLE_ADMIN'].map(role => (
                <label key={role} className="flex items-center text-sm cursor-pointer">
                  <input type="radio" name="role" className="mr-2"
                    checked={formData.roles.includes(role)}
                    onChange={() => handleRoleChange(role)}
                  />
                  {role.split('_')[1]}
                </label>
              ))}
            </div>

            {formData.roles.includes('ROLE_ADMIN') && (
              <div className="relative">
                <input
                  type={showAdminSecret ? 'text' : 'password'}
                  placeholder="Admin Secret Key"
                  className="w-full p-3 rounded bg-slate-700 border border-red-500 focus:outline-none pr-10"
                  onChange={(e) => setFormData({ ...formData, adminSecret: e.target.value })}
                  required
                />
                <button type="button" onClick={() => setShowAdminSecret(p => !p)}
                  className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-white">
                  <EyeIcon open={showAdminSecret} />
                </button>
              </div>
            )}

            <button
              disabled={loading}
              className="w-full bg-blue-600 hover:bg-blue-500 disabled:opacity-50 disabled:cursor-not-allowed py-3 rounded-lg font-semibold transition-all"
            >
              {loading ? 'Sending OTP...' : 'Send Verification OTP'}
            </button>
          </form>

        ) : (
          <form onSubmit={handleVerifyAndRegister} className="space-y-4">
            <input
              type="text" maxLength="6" placeholder="Enter 6-digit OTP"
              className="w-full p-3 rounded bg-slate-700 border border-slate-600 text-center tracking-widest text-xl focus:outline-none focus:border-blue-500"
              onChange={(e) => setOtp(e.target.value)}
              required
            />

            <button
              disabled={loading}
              className="w-full bg-green-600 hover:bg-green-500 disabled:opacity-50 disabled:cursor-not-allowed py-3 rounded-lg font-semibold transition-all"
            >
              {loading ? 'Creating Account...' : 'Verify & Create Account'}
            </button>

            {/* Allow going back to fix email */}
            <button
              type="button"
              onClick={() => { setStep(1); setError(''); setOtp(''); }}
              className="w-full text-slate-400 hover:text-white text-sm py-2 transition-all"
            >
              ← Back / Wrong email?
            </button>
          </form>
        )}

        <p className="text-center text-sm text-slate-400 mt-4">
          Already have an account?{' '}
          <Link to="/login" className="text-blue-400 hover:underline">Login here</Link>
        </p>
      </div>
    </div>
  );
};

export default Register;