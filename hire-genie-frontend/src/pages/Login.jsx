import { useState } from 'react';
import { authService } from '../api/authService';
import { useAuth } from '../context/useAuth';

const Login = () => {
  const [step, setStep] = useState(1); // 1: Credentials, 2: OTP
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [otp, setOtp] = useState('');
  const { loginUser } = useAuth();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      await authService.login({ email, password });
      setStep(2); // Move to OTP step
    } catch {
      alert('Invalid Credentials');
    }
  };

  const handleVerifyOtp = async (e) => {
    e.preventDefault();
    try {
      const { data } = await authService.verifyOtp({ email, otp });
      loginUser(data.token);
      localStorage.setItem('token', data.token); // Store JWT
      alert('Logged in successfully!');
    } catch { alert('Invalid OTP'); }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-900 text-white">
      <div className="w-full max-w-sm bg-slate-800 p-8 rounded-2xl border border-slate-700">
        <h2 className="text-2xl font-bold mb-6 text-center">
          {step === 1 ? 'Welcome Back' : 'Verify Identity'}
        </h2>

        {step === 1 ? (
          <form onSubmit={handleLogin} className="space-y-4">
            <input type="email" placeholder="Email" className="w-full p-3 rounded bg-slate-700" onChange={e => setEmail(e.target.value)} />
            <input type="password" placeholder="Password" className="w-full p-3 rounded bg-slate-700" onChange={e => setPassword(e.target.value)} />
            <button className="w-full bg-blue-600 py-3 rounded-lg hover:bg-blue-500">Get OTP</button>
          </form>
        ) : (
          <form onSubmit={handleVerifyOtp} className="space-y-4">
            <p className="text-sm text-slate-400">An OTP has been sent to {email}</p>
            <input type="text" maxLength="6" placeholder="Enter 6-digit OTP" className="w-full p-3 rounded bg-slate-700 text-center tracking-widest" onChange={e => setOtp(e.target.value)} />
            <button className="w-full bg-green-600 py-3 rounded-lg hover:bg-green-500">Verify & Login</button>
          </form>
        )}
      </div>
    </div>
  );
};

export default Login;
