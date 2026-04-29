import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';

const Dashboard = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-slate-900 text-white">
      <Navbar />
      <div className="p-8">
        <h1 className="text-3xl font-bold text-blue-400">Dashboard</h1>
        <p className="text-slate-400 mt-2">Welcome back! You are now logged in.</p>
        <button
          onClick={() => navigate('/resume-builder')}
          className="mt-6 px-6 py-3 bg-blue-600 hover:bg-blue-500 text-white font-semibold rounded-lg transition-all"
        >
          Build My Resume →
        </button>
      </div>
    </div>
  );
};

export default Dashboard;