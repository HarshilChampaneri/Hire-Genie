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
import PendingApplicationsPage from './pages/PendingApplicationsPage';
import AdminDashboard from './pages/AdminDashboard';
import { useAuth } from './context/useAuth';
import './App.css';

const ProtectedRoute = ({ children }) => {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? children : <Navigate to="/login" replace />;
};

const GuestRoute = ({ children }) => {
  const { isAuthenticated, isRecruiter, isAdmin } = useAuth();
  if (!isAuthenticated) return children;
  if (isAdmin) return <Navigate to="/admin/dashboard" replace />;
  return <Navigate to={isRecruiter ? '/recruiter/dashboard' : '/dashboard'} replace />;
};

const RecruiterRoute = ({ children }) => {
  const { isAuthenticated, isRecruiter, isAdmin } = useAuth();
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  if (!isRecruiter && !isAdmin) return <Navigate to="/dashboard" replace />;
  return children;
};

const AdminRoute = ({ children }) => {
  const { isAuthenticated, isAdmin } = useAuth();
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  if (!isAdmin) return <Navigate to="/dashboard" replace />;
  return children;
};

const SmartDashboardRoute = ({ children }) => {
  const { isAuthenticated, isRecruiter, isAdmin } = useAuth();
  if (!isAuthenticated) return <Navigate to="/login" replace />;
  if (isAdmin) return <Navigate to="/admin/dashboard" replace />;
  if (isRecruiter) return <Navigate to="/recruiter/dashboard" replace />;
  return children;
};

function App() {
  return (
    <BrowserRouter>
      <Routes>

        {/* Public */}
        <Route path="/" element={<Landing />} />
        <Route path="/login" element={<GuestRoute><Login /></GuestRoute>} />
        <Route path="/register" element={<GuestRoute><Register /></GuestRoute>} />

        {/* Employee — Protected */}
        <Route path="/dashboard" element={<SmartDashboardRoute><Dashboard /></SmartDashboardRoute>} />
        <Route path="/resume-builder" element={<ProtectedRoute><ResumeBuilder /></ProtectedRoute>} />
        <Route path="/jobs" element={<ProtectedRoute><JobsPage /></ProtectedRoute>} />
        <Route path="/jobs/:jobId" element={<ProtectedRoute><JobDetailPage /></ProtectedRoute>} />
        <Route path="/recommended-jobs" element={<ProtectedRoute><RecommendedJobsPage /></ProtectedRoute>} />

        {/* Recruiter — Recruiter or Admin */}
        <Route path="/recruiter/dashboard" element={<RecruiterRoute><RecruiterDashboard /></RecruiterRoute>} />
        <Route path="/recruiter/companies" element={<RecruiterRoute><CompanyManagement /></RecruiterRoute>} />
        <Route path="/recruiter/companies/:companyId" element={<RecruiterRoute><CompanyDetailPage /></RecruiterRoute>} />
        <Route path="/recruiter/jobs" element={<RecruiterRoute><JobManagement /></RecruiterRoute>} />
        <Route path="/recruiter/pending-applications" element={<RecruiterRoute><PendingApplicationsPage /></RecruiterRoute>} />

        {/* Admin — Admin Role Only */}
        <Route path="/admin/dashboard" element={<AdminRoute><AdminDashboard /></AdminRoute>} />

        {/* Fallback */}
        <Route path="*" element={<Navigate to="/" replace />} />

      </Routes>
    </BrowserRouter>
  );
}

export default App;