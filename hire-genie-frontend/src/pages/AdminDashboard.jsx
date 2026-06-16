import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';

const ActionCard = ({ icon, label, description, onClick, accent = 'blue', badge = null }) => {
  const accents = {
    blue:    'border-blue-500/30 hover:border-blue-400/60 bg-blue-500/5',
    purple:  'border-purple-500/30 hover:border-purple-400/60 bg-purple-500/5',
    emerald: 'border-emerald-500/30 hover:border-emerald-400/60 bg-emerald-500/5',
    amber:   'border-amber-500/30 hover:border-amber-400/60 bg-amber-500/5',
    rose:    'border-rose-500/30 hover:border-rose-400/60 bg-rose-500/5',
  };
  const iconAccents = {
    blue:    'bg-blue-600/20 text-blue-400',
    purple:  'bg-purple-600/20 text-purple-400',
    emerald: 'bg-emerald-600/20 text-emerald-400',
    amber:   'bg-amber-600/20 text-amber-400',
    rose:    'bg-rose-600/20 text-rose-400',
  };

  return (
    <button
      onClick={onClick}
      className={`w-full text-left p-6 bg-slate-800 border rounded-2xl transition-all duration-200
                  hover:shadow-lg hover:shadow-black/20 hover:-translate-y-0.5 ${accents[accent]}`}
    >
      <div className="flex items-start justify-between mb-4">
        <div className={`w-12 h-12 rounded-xl flex items-center justify-center text-2xl ${iconAccents[accent]}`}>
          {icon}
        </div>
        {badge && (
          <span className="text-xs font-bold px-2 py-1 rounded-full bg-blue-600/20 border border-blue-500/30 text-blue-400">
            {badge}
          </span>
        )}
      </div>
      <h3 className="text-white font-bold text-lg mb-1">{label}</h3>
      <p className="text-slate-400 text-sm leading-relaxed">{description}</p>
      <div className="mt-4 flex items-center gap-1 text-sm font-semibold text-slate-300">
        Go to {label} <span>→</span>
      </div>
    </button>
  );
};

const SectionHeading = ({ children, accent = 'blue' }) => {
  const colors = {
    blue: 'text-blue-400 border-blue-500/30',
    emerald: 'text-emerald-400 border-emerald-500/30',
  };
  return (
    <h2 className={`text-sm font-bold uppercase tracking-widest mb-4 pb-2 border-b ${colors[accent]}`}>
      {children}
    </h2>
  );
};

const AdminDashboard = () => {
  const navigate = useNavigate();

  return (
    <div className="min-h-screen bg-slate-900 text-white">
      <Navbar />

      <div className="max-w-5xl mx-auto p-6 pt-10">

        {/* Header */}
        <div className="mb-10">
          <div className="inline-flex items-center gap-2 bg-rose-600/10 border border-rose-500/30
                          text-rose-400 text-xs font-bold px-3 py-1.5 rounded-full mb-4 uppercase tracking-widest">
            👑 Admin Portal
          </div>
          <h1 className="text-3xl font-bold text-white">Admin Dashboard</h1>
          <p className="text-slate-400 mt-2 text-base">
            Full access to every employee and recruiter feature, all in one place.
          </p>
        </div>

        {/* Recruiter-side features */}
        <div className="mb-10">
          <SectionHeading accent="blue">🔷 Recruiter Tools</SectionHeading>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
            <ActionCard
              icon="🏢"
              label="Companies"
              description="Add and manage companies, and organize jobs under each one."
              onClick={() => navigate('/recruiter/companies')}
              accent="blue"
            />
            <ActionCard
              icon="💼"
              label="Manage Jobs"
              description="Post new job listings, update existing ones, and remove closed positions."
              onClick={() => navigate('/recruiter/jobs')}
              accent="purple"
            />
            <ActionCard
              icon="🤖"
              label="AI Recommend"
              description="Use AI to find the best-fit candidates from the talent pool for any job."
              onClick={() => navigate('/recruiter/jobs')}
              accent="emerald"
              badge="AI"
            />
            <ActionCard
              icon="📋"
              label="Applications"
              description="Review pending candidate applications. Accept and schedule interviews or reject."
              onClick={() => navigate('/recruiter/pending-applications')}
              accent="amber"
            />
          </div>
        </div>

        {/* Employee-side features */}
        <div className="mb-10">
          <SectionHeading accent="emerald">✅ Employee Tools</SectionHeading>
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-5">
            <ActionCard
              icon="💼"
              label="Browse Jobs"
              description="Explore all open job listings and filter by company."
              onClick={() => navigate('/jobs')}
              accent="blue"
            />
            <ActionCard
              icon="🤖"
              label="Recommended Jobs"
              description="See AI-recommended job openings tailored to a profile's resume."
              onClick={() => navigate('/recommended-jobs')}
              accent="amber"
              badge="AI"
            />
            <ActionCard
              icon="📄"
              label="Resume Builder"
              description="Create or update a professional resume with the guided AI builder."
              onClick={() => navigate('/resume-builder')}
              accent="purple"
            />
            <ActionCard
              icon="🎭"
              label="Interview Prep"
              description="Use AI roleplay on any job listing to practice interview questions."
              onClick={() => navigate('/jobs')}
              accent="rose"
            />
          </div>
        </div>

        {/* Quick Guide */}
        <div className="bg-slate-800 border border-slate-700 rounded-2xl p-6">
          <h2 className="text-white font-bold text-base mb-4">📋 Quick Guide</h2>
          <div className="flex flex-col gap-3">
            {[
              { step: '1', text: 'Use Companies and Manage Jobs to administer recruiter-side data for any company.' },
              { step: '2', text: 'Use AI Recommend or Applications to review and process candidates for any job.' },
              { step: '3', text: 'Use Browse Jobs and Resume Builder to view or test the employee experience directly.' },
              { step: '4', text: 'Use the 🎭 Roleplay button on a job to generate AI interview questions for that role.' },
            ].map(({ step, text }) => (
              <div key={step} className="flex items-start gap-3">
                <span className="shrink-0 w-6 h-6 bg-rose-600/20 border border-rose-500/40 text-rose-400
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

export default AdminDashboard;