import { useState } from 'react';
import { useNavigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/useAuth';
import ProfileModal from './ProfileModal';
import EmployeeSearchBar from './EmployeeSearchBar';

const Navbar = () => {
  const { logoutUser, token, isRecruiter } = useAuth();
  const navigate = useNavigate();
  const location = useLocation();
  const [profileOpen, setProfileOpen] = useState(false);

  const handleLogout = async () => {
    await logoutUser();
    navigate('/login');
  };

  const dashboardPath = isRecruiter ? '/recruiter/dashboard' : '/dashboard';
  const isDashboard =
    location.pathname === '/dashboard' || location.pathname === '/recruiter/dashboard';

  return (
    <>
      <nav className="w-full bg-slate-800 border-b border-slate-700 px-6 py-4 flex items-center gap-4 flex-wrap lg:flex-nowrap">
        {/* Brand */}
        <button
          onClick={() => navigate(dashboardPath)}
          className="text-blue-400 font-bold text-xl hover:text-blue-300 transition-colors shrink-0"
        >
          Hire-Genie
        </button>

        {/* Global candidate search */}
        <div className="order-3 w-full lg:order-0 lg:w-auto lg:flex-1 lg:flex lg:justify-center">
          <EmployeeSearchBar />
        </div>

        <div className="flex items-center gap-3 ml-auto shrink-0">
          <span className="text-slate-400 text-sm hidden sm:block">
            {token ? '🟢 Logged in' : ''}
          </span>

          {/* Dashboard back-link */}
          {!isDashboard && (
            <button
              onClick={() => navigate(dashboardPath)}
              className="flex items-center gap-1.5 text-slate-300 hover:text-white text-sm font-medium
                         px-3 py-2 bg-slate-700 hover:bg-slate-600 rounded-lg transition-all"
            >
              <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                  d="M3 12l2-2m0 0l7-7 7 7M5 10v10a1 1 0 001 1h3m10-11l2 2m-2-2v10a1 1 0 01-1 1h-3m-6 0a1 1 0 001-1v-4a1 1 0 011-1h2a1 1 0 011 1v4a1 1 0 001 1m-6 0h6" />
              </svg>
              Dashboard
            </button>
          )}

          {/* Profile button */}
          <button
            onClick={() => setProfileOpen(true)}
            title="View my profile"
            className="w-9 h-9 rounded-full bg-blue-600/20 border border-blue-500/40 text-blue-400
                       hover:bg-blue-600/30 hover:border-blue-400/70 transition-all
                       flex items-center justify-center shrink-0"
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
            </svg>
          </button>

          {/* Logout */}
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

      {profileOpen && <ProfileModal onClose={() => setProfileOpen(false)} />}
    </>
  );
};

export default Navbar;