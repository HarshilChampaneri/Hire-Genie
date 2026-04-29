import { useNavigate } from 'react-router-dom';

const Landing = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-900 text-white">
      <div className="text-center space-y-6">
        <h1 className="text-4xl font-bold text-blue-400">Welcome to Hire-Genie</h1>
        <p className="text-slate-400">Your smart hiring platform</p>
        <div className="flex gap-4 justify-center">
          <button
            onClick={() => navigate('/login')}
            className="px-6 py-3 bg-blue-600 hover:bg-blue-500 rounded-lg font-semibold transition-all"
          >
            Login
          </button>
          <button
            onClick={() => navigate('/register')}
            className="px-6 py-3 bg-slate-700 hover:bg-slate-600 border border-slate-500 rounded-lg font-semibold transition-all"
          >
            Register
          </button>
        </div>
      </div>
    </div>
  );
};

export default Landing;