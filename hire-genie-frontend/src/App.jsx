// import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
// import Landing from './pages/Landing';
// import Login from './pages/Login';
// import Register from './pages/Register';
// import Dashboard from './pages/Dashboard';
// import ResumeBuilder from './pages/ResumeBuilder';
// import JobsPage from './pages/JobsPage';
// import RecommendedJobsPage from './pages/RecommendedJobsPage';
// import RecruiterDashboard from './pages/RecruiterDashboard';
// import CompanyManagement from './pages/CompanyManagement';
// import JobManagement from './pages/JobManagement';
// import { useAuth } from './context/useAuth';
// import './App.css';

// // ─── Route Guards ─────────────────────────────────────────────────────────────

// const ProtectedRoute = ({ children }) => {
//   const { isAuthenticated } = useAuth();
//   return isAuthenticated ? children : <Navigate to="/login" replace />;
// };

// const GuestRoute = ({ children }) => {
//   const { isAuthenticated } = useAuth();
//   return !isAuthenticated ? children : <Navigate to="/dashboard" replace />;
// };

// /**
//  * RecruiterRoute — only allows users with the recruiter role.
//  * Assumes useAuth() exposes `isRecruiter` (boolean) derived from JWT roles.
//  * Non-recruiters are redirected to the employee dashboard.
//  */
// const RecruiterRoute = ({ children }) => {
//   const { isAuthenticated, isRecruiter } = useAuth();
//   if (!isAuthenticated) return <Navigate to="/login" replace />;
//   if (!isRecruiter) return <Navigate to="/dashboard" replace />;
//   return children;
// };

// // ─── App ──────────────────────────────────────────────────────────────────────

// function App() {
//   return (
//     <BrowserRouter>
//       <Routes>

//         {/* Public */}
//         <Route path="/" element={<Landing />} />
//         <Route path="/login" element={<GuestRoute><Login /></GuestRoute>} />
//         <Route path="/register" element={<GuestRoute><Register /></GuestRoute>} />

//         {/* Employee — Protected */}
//         <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
//         <Route path="/resume-builder" element={<ProtectedRoute><ResumeBuilder /></ProtectedRoute>} />
//         <Route path="/jobs" element={<ProtectedRoute><JobsPage /></ProtectedRoute>} />
//         <Route path="/recommended-jobs" element={<ProtectedRoute><RecommendedJobsPage /></ProtectedRoute>} />

//         {/* Recruiter — Recruiter Role Only */}
//         <Route path="/recruiter/dashboard" element={<RecruiterRoute><RecruiterDashboard /></RecruiterRoute>} />
//         <Route path="/recruiter/companies" element={<RecruiterRoute><CompanyManagement /></RecruiterRoute>} />
//         <Route path="/recruiter/jobs" element={<RecruiterRoute><JobManagement /></RecruiterRoute>} />

//         {/* Fallback */}
//         <Route path="*" element={<Navigate to="/" replace />} />

//       </Routes>
//     </BrowserRouter>
//   );
// }

// export default App;


import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import Landing from './pages/Landing';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import ResumeBuilder from './pages/ResumeBuilder';
import JobsPage from './pages/JobsPage';
import RecommendedJobsPage from './pages/RecommendedJobsPage';
import JobDetailPage from './pages/JobDetailPage';
import CompanyDetailPage from './pages/CompanyDetailPage';
import RecruiterDashboard from './pages/RecruiterDashboard';
import CompanyManagement from './pages/CompanyManagement';
import JobManagement from './pages/JobManagement';
import { useAuth } from './context/useAuth';
import './App.css';

// ─── Route Guards ─────────────────────────────────────────────────────────────

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? children : <Navigate to="/login" replace />;
};

const GuestRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return !isAuthenticated ? children : <Navigate to="/dashboard" replace />;
};

/**
 * RecruiterRoute — only allows users with the recruiter role.
 * Assumes useAuth() exposes `isRecruiter` (boolean) derived from JWT roles.
 * Non-recruiters are redirected to the employee dashboard.
 */
const RecruiterRoute = ({ children }) => {
  const { isAuthenticated, isRecruiter } = useAuth();
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  if (!isRecruiter) return <Navigate to="/dashboard" replace />;
  return children;
};

// ─── App ──────────────────────────────────────────────────────────────────────

function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* Public */}
        <Route path="/" element={<Landing />} />
        <Route path="/login" element={<GuestRoute><Login /></GuestRoute>} />
        <Route path="/register" element={<GuestRoute><Register /></GuestRoute>} />

        {/* Employee — Protected */}
        <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
        <Route path="/resume-builder" element={<ProtectedRoute><ResumeBuilder /></ProtectedRoute>} />
        <Route path="/jobs" element={<ProtectedRoute><JobsPage /></ProtectedRoute>} />
        <Route path="/jobs/:jobId" element={<ProtectedRoute><JobDetailPage /></ProtectedRoute>} />
        <Route path="/recommended-jobs" element={<ProtectedRoute><RecommendedJobsPage /></ProtectedRoute>} />

        {/* Recruiter — Recruiter Role Only */}
        <Route path="/recruiter/dashboard" element={<RecruiterRoute><RecruiterDashboard /></RecruiterRoute>} />
        <Route path="/recruiter/companies" element={<RecruiterRoute><CompanyManagement /></RecruiterRoute>} />
        <Route path="/recruiter/companies/:companyId" element={<RecruiterRoute><CompanyDetailPage /></RecruiterRoute>} />
        <Route path="/recruiter/jobs" element={<RecruiterRoute><JobManagement /></RecruiterRoute>} />

        {/* Fallback */}
        <Route path="*" element={<Navigate to="/" replace />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;