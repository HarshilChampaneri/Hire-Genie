// import { useState } from 'react';
// import { Link, useNavigate } from 'react-router-dom';
// import { authService } from '../api/authService';
// import { useAuth } from '../context/useAuth';

// const EyeIcon = ({ open }) => open ? (
//   <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
//     <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 12a3 3 0 11-6 0 3 3 0 016 0z" />
//     <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M2.458 12C3.732 7.943 7.523 5 12 5c4.477 0 8.268 2.943 9.542 7-1.274 4.057-5.065 7-9.542 7-4.477 0-8.268-2.943-9.542-7z" />
//   </svg>
// ) : (
//   <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
//     <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13.875 18.825A10.05 10.05 0 0112 19c-4.477 0-8.268-2.943-9.542-7a9.97 9.97 0 012.223-3.592M6.53 6.533A9.956 9.956 0 0112 5c4.477 0 8.268 2.943 9.542 7a10.025 10.025 0 01-4.132 5.411M3 3l18 18" />
//   </svg>
// );

// const Login = () => {
//   const navigate = useNavigate();
//   const [step, setStep] = useState(1);
//   const [email, setEmail] = useState('');
//   const [password, setPassword] = useState('');
//   const [otp, setOtp] = useState('');
//   const [showPassword, setShowPassword] = useState(false);
//   const { loginUser } = useAuth();

//   const handleLogin = async (e) => {
//     e.preventDefault();
//     try {
//       await authService.login({ email, password });
//       setStep(2);
//     } catch {
//       alert('Invalid Credentials');
//     }
//   };

//   const handleVerifyOtp = async (e) => {
//     e.preventDefault();
//     try {
//       const { data } = await authService.verifyOtp({ email, otp });
//       loginUser(data.token);
//       navigate('/dashboard');
//     } catch {
//       alert('Invalid OTP');
//     }
//   };

//   return (
//     <div className="min-h-screen flex items-center justify-center bg-slate-900 text-white">
//       <div className="w-full max-w-sm bg-slate-800 p-8 rounded-2xl border border-slate-700">
//         <h2 className="text-2xl font-bold mb-6 text-center">
//           {step === 1 ? 'Welcome Back' : 'Verify Identity'}
//         </h2>

//         {step === 1 ? (
//           <form onSubmit={handleLogin} className="space-y-4">
//             <input
//               type="email" placeholder="Email"
//               className="w-full p-3 rounded bg-slate-700"
//               onChange={e => setEmail(e.target.value)}
//             />
//             <div className="relative">
//               <input
//                 type={showPassword ? 'text' : 'password'}
//                 placeholder="Password"
//                 className="w-full p-3 rounded bg-slate-700 pr-10"
//                 onChange={e => setPassword(e.target.value)}
//               />
//               <button
//                 type="button"
//                 onClick={() => setShowPassword(prev => !prev)}
//                 className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-white"
//               >
//                 <EyeIcon open={showPassword} />
//               </button>
//             </div>
//             <button className="w-full bg-blue-600 py-3 rounded-lg hover:bg-blue-500">Get OTP</button>
//           </form>
//         ) : (
//           <form onSubmit={handleVerifyOtp} className="space-y-4">
//             <p className="text-sm text-slate-400">An OTP has been sent to {email}</p>
//             <input
//               type="text" maxLength="6" placeholder="Enter 6-digit OTP"
//               className="w-full p-3 rounded bg-slate-700 text-center tracking-widest"
//               onChange={e => setOtp(e.target.value)}
//             />
//             <button className="w-full bg-green-600 py-3 rounded-lg hover:bg-green-500">Verify & Login</button>
//           </form>
//         )}

//         <p className="text-center text-sm text-slate-400 mt-4">
//           Don't have an account?{' '}
//           <Link to="/register" className="text-blue-400 hover:underline">Register here</Link>
//         </p>
//       </div>
//     </div>
//   );
// };

// export default Login;


import { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { authService } from '../api/authService';
import { useAuth } from '../context/useAuth';

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

const Login = () => {
  const navigate = useNavigate();
  const [step, setStep] = useState(1);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [otp, setOtp] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const { loginUser } = useAuth();

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      await authService.login({ email, password });
      setStep(2);
    } catch {
      alert('Invalid Credentials');
    }
  };

  const handleVerifyOtp = async (e) => {
    e.preventDefault();
    try {
      const { data } = await authService.verifyOtp({ email, otp });
      loginUser(data.token);
      // Decode the token right away to determine role for redirect.
      // We can't rely on isRecruiter from context here because the state
      // update from loginUser is async — so we decode the token directly.
      const payload = JSON.parse(atob(data.token.split('.')[1].replace(/-/g, '+').replace(/_/g, '/')));
      const roles = Array.isArray(payload?.roles) ? payload.roles : [];
      if (roles.includes('ROLE_RECRUITER')) {
        navigate('/recruiter/dashboard');
      } else {
        navigate('/dashboard');
      }
    } catch {
      alert('Invalid OTP');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-900 text-white">
      <div className="w-full max-w-sm bg-slate-800 p-8 rounded-2xl border border-slate-700">
        <h2 className="text-2xl font-bold mb-6 text-center">
          {step === 1 ? 'Welcome Back' : 'Verify Identity'}
        </h2>

        {step === 1 ? (
          <form onSubmit={handleLogin} className="space-y-4">
            <input
              type="email" placeholder="Email"
              className="w-full p-3 rounded bg-slate-700"
              onChange={e => setEmail(e.target.value)}
            />
            <div className="relative">
              <input
                type={showPassword ? 'text' : 'password'}
                placeholder="Password"
                className="w-full p-3 rounded bg-slate-700 pr-10"
                onChange={e => setPassword(e.target.value)}
              />
              <button
                type="button"
                onClick={() => setShowPassword(prev => !prev)}
                className="absolute right-3 top-1/2 -translate-y-1/2 text-slate-400 hover:text-white"
              >
                <EyeIcon open={showPassword} />
              </button>
            </div>
            <button className="w-full bg-blue-600 py-3 rounded-lg hover:bg-blue-500">Get OTP</button>
          </form>
        ) : (
          <form onSubmit={handleVerifyOtp} className="space-y-4">
            <p className="text-sm text-slate-400">An OTP has been sent to {email}</p>
            <input
              type="text" maxLength="6" placeholder="Enter 6-digit OTP"
              className="w-full p-3 rounded bg-slate-700 text-center tracking-widest"
              onChange={e => setOtp(e.target.value)}
            />
            <button className="w-full bg-green-600 py-3 rounded-lg hover:bg-green-500">Verify & Login</button>
          </form>
        )}

        <p className="text-center text-sm text-slate-400 mt-4">
          Don't have an account?{' '}
          <Link to="/register" className="text-blue-400 hover:underline">Register here</Link>
        </p>
      </div>
    </div>
  );
};

export default Login;