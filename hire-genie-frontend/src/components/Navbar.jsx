// import { useNavigate } from 'react-router-dom';
// import { useAuth } from '../context/useAuth';

// const Navbar = () => {
//   const { logoutUser, token } = useAuth();
//   const navigate = useNavigate();

//   const handleLogout = async () => {
//     await logoutUser();
//     navigate('/login');
//   };

//   return (
//     <nav className="w-full bg-slate-800 border-b border-slate-700 px-6 py-4 flex items-center justify-between">
//       <span className="text-blue-400 font-bold text-xl">Hire-Genie</span>

//       <div className="flex items-center gap-4">
//         <span className="text-slate-400 text-sm hidden sm:block">
//           {token ? '🟢 Logged in' : ''}
//         </span>
//         <button
//           onClick={handleLogout}
//           className="flex items-center gap-2 bg-red-600 hover:bg-red-500 text-white text-sm font-semibold px-4 py-2 rounded-lg transition-all"
//         >
//           {/* Logout icon */}
//           <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
//             <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
//               d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a2 2 0 01-2 2H5a2 2 0 01-2-2V7a2 2 0 012-2h6a2 2 0 012 2v1" />
//           </svg>
//           Logout
//         </button>
//       </div>
//     </nav>
//   );
// };

// export default Navbar;


import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/useAuth';

const Navbar = () => {
  const { logoutUser, token, isRecruiter } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();

  const handleLogout = async () => {
    await logoutUser();
    navigate('/login');
  };

  // Determine the correct dashboard path based on role
  const dashboardPath = isRecruiter ? '/recruiter/dashboard' : '/dashboard';

  // Show "Dashboard" link on all pages except the dashboard itself
  const isDashboard =
    location.pathname === '/dashboard' || location.pathname === '/recruiter/dashboard';

  return (
    <nav className="w-full bg-slate-800 border-b border-slate-700 px-6 py-4 flex items-center justify-between">
      {/* Brand — always navigates to the correct dashboard */}
      <button
        onClick={() => navigate(dashboardPath)}
        className="text-blue-400 font-bold text-xl hover:text-blue-300 transition-colors"
      >
        Hire-Genie
      </button>

      <div className="flex items-center gap-4">
        <span className="text-slate-400 text-sm hidden sm:block">
          {token ? '🟢 Logged in' : ''}
        </span>

        {/* Dashboard back-link — hidden when already on a dashboard */}
        {!isDashboard && (
          <button
            onClick={() => navigate(dashboardPath)}
            className="flex items-center gap-1.5 text-slate-300 hover:text-white text-sm font-medium
                       px-3 py-2 bg-slate-700 hover:bg-slate-600 rounded-lg transition-all"
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
            </svg>
            Dashboard
          </button>
        )}

        <button
          onClick={handleLogout}
          className="flex items-center gap-2 bg-red-600 hover:bg-red-500 text-white text-sm font-semibold px-4 py-2 rounded-lg transition-all"
        >
          <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
              d="M17 16l4-4m0 0l-4-4m4 4H7m6 4v1a2 2 0 01-2 2H5a2 2 0 01-2-2V7a2 2 0 012-2h6a2 2 0 012 2v1" />
          </svg>
          Logout
        </button>
      </div>
    </nav>
  );
};

export default Navbar;