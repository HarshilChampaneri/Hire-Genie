import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';

const StatCard = ({ icon, label, description, onClick, accent = 'blue' }) => {
  const accents = {
    blue: 'border-blue-500/30 hover:border-blue-400/60 bg-blue-500/5',
    purple: 'border-purple-500/30 hover:border-purple-400/60 bg-purple-500/5',
    emerald: 'border-emerald-500/30 hover:border-emerald-400/60 bg-emerald-500/5',
  };
  const iconAccents = {
    blue: 'bg-blue-600/20 text-blue-400',
    purple: 'bg-purple-600/20 text-purple-400',
    emerald: 'bg-emerald-600/20 text-emerald-400',
  };

  return (
    <button
      onClick={onClick}
      className={`w-full text-left p-6 bg-slate-800 border rounded-2xl transition-all duration-200
                  hover:shadow-lg hover:shadow-black/20 hover:-translate-y-0.5 ${accents[accent]}`}
    >
      <div className={`w-12 h-12 rounded-xl flex items-center justify-center text-2xl mb-4 ${iconAccents[accent]}`}>
        {icon}
      </div>
      <h3 className="text-white font-bold text-lg mb-1">{label}</h3>
      <p className="text-slate-400 text-sm leading-relaxed">{description}</p>
      <div className="mt-4 flex items-center gap-1 text-sm font-semibold text-slate-300">
        Go to {label} <span>→</span>
      </div>
    </button>
  );
};

const RecruiterDashboard = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-slate-900 text-white">
      <Navbar />

      <div className="max-w-5xl mx-auto p-6 pt-10">

        {/* Header */}
        <div className="mb-10">
          <div className="inline-flex items-center gap-2 bg-blue-600/10 border border-blue-500/30
                          text-blue-400 text-xs font-bold px-3 py-1.5 rounded-full mb-4 uppercase tracking-widest">
            🔷 Recruiter Portal
          </div>
          <h1 className="text-3xl font-bold text-white">Recruiter Dashboard</h1>
          <p className="text-slate-400 mt-2 text-base">
            Manage your companies, post jobs, and find the best candidates with AI.
          </p>
        </div>

        {/* Action Cards */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5 mb-10">
          <StatCard
            icon="🏢"
            label="Companies"
            description="Add and manage the companies you recruit for. Organize jobs under each company."
            onClick={() => navigate('/recruiter/companies')}
            accent="blue"
          />
          <StatCard
            icon="💼"
            label="Jobs"
            description="Post new job listings, update existing ones, and remove closed positions."
            onClick={() => navigate('/recruiter/jobs')}
            accent="purple"
          />
          <StatCard
            icon="🤖"
            label="AI Recommend"
            description="Use AI to find the best-fit candidates from our talent pool for any job."
            onClick={() => navigate('/recruiter/jobs')}
            accent="emerald"
          />
        </div>

        {/* Quick Info */}
        <div className="bg-slate-800 border border-slate-700 rounded-2xl p-6">
          <h2 className="text-white font-bold text-base mb-4">📋 Quick Guide</h2>
          <div className="flex flex-col gap-3">
            {[
              { step: '1', text: 'Go to Companies and add the companies you are hiring for.' },
              { step: '2', text: 'Navigate to Jobs and post new listings under your companies.' },
              { step: '3', text: 'Use AI Recommend on any job to get best-fit candidates instantly.' },
              { step: '4', text: 'Use the 🎭 Roleplay button to generate AI interview questions for any role.' },
            ].map(({ step, text }) => (
              <div key={step} className="flex items-start gap-3">
                <span className="shrink-0 w-6 h-6 bg-blue-600/20 border border-blue-500/40 text-blue-400
                                 text-xs font-bold rounded-full flex items-center justify-center">
                  {step}
                </span>
                <p className="text-slate-300 text-sm leading-relaxed">{text}</p>
              </div>
            ))}
          </div>
        </div>

        {/* Employee view link */}
        <div className="mt-6 flex justify-center">
          <button
            onClick={() => navigate('/jobs')}
            className="text-slate-400 hover:text-slate-200 text-sm transition-colors underline underline-offset-4"
          >
            Switch to Employee View →
          </button>
        </div>
      </div>
    </div>
  );
};

export default RecruiterDashboard;