// import { useNavigate } from 'react-router-dom';
// import Navbar from '../components/Navbar';

// const Dashboard = () => {
//   const navigate = useNavigate();

//   return (
//     <div className="min-h-screen bg-slate-900 text-white">
//       <Navbar />
//       <div className="p-8">
//         <h1 className="text-3xl font-bold text-blue-400">Dashboard</h1>
//         <p className="text-slate-400 mt-2">Welcome back! You are now logged in.</p>
//         <button
//           onClick={() => navigate('/resume-builder')}
//           className="mt-6 px-6 py-3 bg-blue-600 hover:bg-blue-500 text-white font-semibold rounded-lg transition-all"
//         >
//           Build My Resume →
//         </button>
//       </div>
//     </div>
//   );
// };

// export default Dashboard;

import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';

const ActionCard = ({ icon, label, description, onClick, accent = 'blue' }) => {
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

const Dashboard = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-slate-900 text-white">
      <Navbar />

      <div className="max-w-5xl mx-auto p-6 pt-10">

        {/* Header */}
        <div className="mb-10">
          <div className="inline-flex items-center gap-2 bg-emerald-600/10 border border-emerald-500/30
                          text-emerald-400 text-xs font-bold px-3 py-1.5 rounded-full mb-4 uppercase tracking-widest">
            ✅ Employee Portal
          </div>
          <h1 className="text-3xl font-bold text-white">Welcome back!</h1>
          <p className="text-slate-400 mt-2 text-base">
            Browse open positions, practice interviews, and build your resume — all in one place.
          </p>
        </div>

        {/* Action Cards */}
        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-5 mb-10">
          <ActionCard
            icon="💼"
            label="Browse Jobs"
            description="Explore open job listings, filter by company, and apply to roles that match your skills."
            onClick={() => navigate('/jobs')}
            accent="blue"
          />
          <ActionCard
            icon="📄"
            label="Resume Builder"
            description="Create or update your professional resume with our guided AI-powered resume builder."
            onClick={() => navigate('/resume-builder')}
            accent="purple"
          />
          <ActionCard
            icon="🎭"
            label="Interview Prep"
            description="Use AI roleplay on any job listing to practice technical and behavioral interview questions."
            onClick={() => navigate('/jobs')}
            accent="emerald"
          />
        </div>

        {/* Quick Guide */}
        <div className="bg-slate-800 border border-slate-700 rounded-2xl p-6">
          <h2 className="text-white font-bold text-base mb-4">📋 Quick Guide</h2>
          <div className="flex flex-col gap-3">
            {[
              { step: '1', text: 'Go to Browse Jobs to explore all available listings.' },
              { step: '2', text: 'Click "Apply Now" on any job to submit your application instantly.' },
              { step: '3', text: 'Use the 🎭 Roleplay button on a job to generate AI interview questions for that role.' },
              { step: '4', text: 'Head to Resume Builder to create a polished resume to share with recruiters.' },
            ].map(({ step, text }) => (
              <div key={step} className="flex items-start gap-3">
                <span className="shrink-0 w-6 h-6 bg-emerald-600/20 border border-emerald-500/40 text-emerald-400
                                 text-xs font-bold rounded-full flex items-center justify-center">
                  {step}
                </span>
                <p className="text-slate-300 text-sm leading-relaxed">{text}</p>
              </div>
            ))}
          </div>
        </div>

      </div>
    </div>
  );
};

export default Dashboard;