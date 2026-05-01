import { useState } from 'react';
import Navbar from '../components/Navbar';
import JobCard from '../components/jobs/JobCard';
import RoleplayModal from '../components/jobs/RoleplayModal';
import { resumeService } from '../api/resumeService';
import { jobService } from '../services/jobService';
import { useAuth } from '../context/useAuth';

const Toast = ({ message, type, onClose }) => (
  <div className={`fixed bottom-6 right-6 z-[100] flex items-center gap-3 px-4 py-3 rounded-xl shadow-lg text-sm font-medium
    ${type === 'success' ? 'bg-emerald-600 text-white' : 'bg-red-600 text-white'}`}>
    {type === 'success' ? '✅' : '❌'} {message}
    <button onClick={onClose} className="ml-2 text-white/70 hover:text-white">✕</button>
  </div>
);

const EmptyState = ({ onFetch, loading }) => (
  <div className="flex flex-col items-center justify-center py-24 gap-5 text-center">
    {/* Animated illustration */}
    <div className="relative">
      <div className="w-24 h-24 bg-blue-600/10 border border-blue-500/20 rounded-full flex items-center
                      justify-center text-5xl">
        🤖
      </div>
      <span className="absolute -top-1 -right-1 w-6 h-6 bg-emerald-500 rounded-full flex items-center
                       justify-center text-xs animate-bounce">✨</span>
    </div>

    <div>
      <h2 className="text-white font-bold text-xl mb-2">AI Job Recommendations</h2>
      <p className="text-slate-400 text-sm max-w-sm leading-relaxed">
        Our AI will analyse your resume profile and surface the most relevant job openings tailored just for you.
      </p>
    </div>

    <button
      onClick={onFetch}
      disabled={loading}
      className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 disabled:opacity-60
                 text-white font-semibold px-6 py-3 rounded-xl transition-all text-sm shadow-lg shadow-blue-600/20"
    >
      {loading ? (
        <>
          <div className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
          Analysing your profile...
        </>
      ) : (
        <>
          ✨ Get My Job Recommendations
        </>
      )}
    </button>
  </div>
);

const RecommendedJobsPage = () => {
  const { token } = useAuth();

  const [jobs, setJobs] = useState([]);
  const [fetched, setFetched] = useState(false);
  const [loading, setLoading] = useState(false);

  // Roleplay modal
  const [roleplayOpen, setRoleplayOpen] = useState(false);
  const [roleplayData, setRoleplayData] = useState(null);
  const [roleplayLoading, setRoleplayLoading] = useState(false);

  // Toast
  const [toast, setToast] = useState(null);

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3500);
  };

  const handleFetchRecommendations = async () => {
    setLoading(true);
    try {
      const res = await resumeService.getRecommendedJobs(token);
      setJobs(res.data || []);
      setFetched(true);
      if ((res.data || []).length === 0) {
        showToast('No recommendations found. Complete your resume profile for better results.', 'error');
      }
    } catch (err) {
      const msg = err?.response?.data?.message || 'Failed to fetch recommendations. Please complete your resume profile first.';
      showToast(msg, 'error');
    } finally {
      setLoading(false);
    }
  };

  const handleApply = async (jobId) => {
    try {
      await jobService.applyForJob(jobId, token);
      showToast('Application submitted! Check your email shortly.', 'success');
    } catch (err) {
      const msg = err?.response?.data?.message || 'Could not apply for this job.';
      showToast(msg, 'error');
    }
  };

  const handleRoleplay = async (jobId) => {
    setRoleplayOpen(true);
    setRoleplayData(null);
    setRoleplayLoading(true);
    try {
      const res = await jobService.startRoleplay(jobId, token);
      setRoleplayData(res.data);
    } catch {
      showToast('Failed to generate roleplay questions.', 'error');
      setRoleplayOpen(false);
    } finally {
      setRoleplayLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-900 text-white">
      <Navbar />

      <div className="max-w-6xl mx-auto p-6">

        {/* Header */}
        <div className="flex items-start justify-between flex-wrap gap-4 mb-8">
          <div>
            <div className="inline-flex items-center gap-2 bg-blue-600/10 border border-blue-500/30
                            text-blue-400 text-xs font-bold px-3 py-1.5 rounded-full mb-3 uppercase tracking-widest">
              🤖 AI Powered
            </div>
            <h1 className="text-2xl font-bold text-white">Recommended Jobs</h1>
            <p className="text-slate-400 text-sm mt-1">
              Jobs curated by AI based on your resume profile
            </p>
          </div>

          {/* Refresh button — only shown after first fetch */}
          {fetched && (
            <button
              onClick={handleFetchRecommendations}
              disabled={loading}
              className="flex items-center gap-2 bg-slate-700 hover:bg-slate-600 disabled:opacity-60
                         text-slate-200 font-semibold px-4 py-2.5 rounded-xl transition-all text-sm"
            >
              {loading ? (
                <div className="w-4 h-4 border-2 border-slate-400 border-t-transparent rounded-full animate-spin" />
              ) : (
                <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
                    d="M4 4v5h.582m15.356 2A8.001 8.001 0 004.582 9m0 0H9m11 11v-5h-.581m0 0a8.003 8.003 0 01-15.357-2m15.357 2H15" />
                </svg>
              )}
              Refresh
            </button>
          )}
        </div>

        {/* Info banner */}
        {!fetched && (
          <div className="mb-6 p-4 bg-blue-500/10 border border-blue-500/20 rounded-xl flex items-start gap-3">
            <span className="text-blue-400 text-lg shrink-0">💡</span>
            <p className="text-slate-300 text-sm leading-relaxed">
              For the best recommendations, make sure your{' '}
              <span className="text-blue-400 font-semibold">resume profile</span> is complete — including your
              experience, skills, education, and projects.
            </p>
          </div>
        )}

        {/* Empty / Fetch CTA */}
        {!fetched ? (
          <EmptyState onFetch={handleFetchRecommendations} loading={loading} />
        ) : loading ? (
          <div className="flex justify-center items-center py-24">
            <div className="flex flex-col items-center gap-4">
              <div className="w-12 h-12 border-4 border-blue-600 border-t-transparent rounded-full animate-spin" />
              <p className="text-slate-400 text-sm">Analysing your resume profile...</p>
            </div>
          </div>
        ) : jobs.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-20 gap-3 text-center">
            <span className="text-5xl">🔍</span>
            <p className="text-slate-300 font-semibold text-lg">No recommendations found</p>
            <p className="text-slate-500 text-sm max-w-sm">
              Please complete your resume profile with more details and try again.
            </p>
            <button
              onClick={handleFetchRecommendations}
              className="mt-2 px-5 py-2.5 bg-blue-600 hover:bg-blue-500 text-white font-semibold rounded-xl text-sm transition-all"
            >
              Try Again
            </button>
          </div>
        ) : (
          <>
            {/* Result count */}
            <div className="flex items-center gap-2 mb-5">
              <span className="w-2 h-2 bg-emerald-400 rounded-full animate-pulse" />
              <p className="text-slate-400 text-sm">
                <span className="text-white font-semibold">{jobs.length}</span> job{jobs.length !== 1 ? 's' : ''} matched your profile
              </p>
            </div>

            {/* Jobs Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
              {jobs.map((job) => (
                <JobCard
                  key={job.jobId}
                  job={job}
                  isRecruiter={false}
                  onApply={handleApply}
                  onRoleplay={handleRoleplay}
                />
              ))}
            </div>
          </>
        )}
      </div>

      {/* Roleplay Modal */}
      <RoleplayModal
        isOpen={roleplayOpen}
        onClose={() => setRoleplayOpen(false)}
        roleplayData={roleplayData}
        loading={roleplayLoading}
      />

      {/* Toast */}
      {toast && <Toast message={toast.message} type={toast.type} onClose={() => setToast(null)} />}
    </div>
  );
};

export default RecommendedJobsPage;
