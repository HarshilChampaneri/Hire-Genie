import { useState } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import RoleplayModal from '../components/jobs/RoleplayModal';
import { jobService } from '../services/jobService';
import { useAuth } from '../context/useAuth';

const JOB_TYPE_STYLES = {
  FULL_TIME: 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/30',
  PART_TIME: 'bg-amber-500/15 text-amber-400 border border-amber-500/30',
};

const WORK_MODE_STYLES = {
  REMOTE: 'bg-blue-500/15 text-blue-400 border border-blue-500/30',
  HYBRID: 'bg-purple-500/15 text-purple-400 border border-purple-500/30',
  ON_SITE: 'bg-rose-500/15 text-rose-400 border border-rose-500/30',
};

const formatSalary = (min, max, currency) => {
  if (!min && !max) return null;
  const fmt = (v) => Number(v).toLocaleString();
  const cur = currency || 'USD';
  if (min && max) return `${cur} ${fmt(min)} – ${fmt(max)}`;
  if (min) return `${cur} ${fmt(min)}+`;
  return `Up to ${cur} ${fmt(max)}`;
};

const Toast = ({ message, type, onClose }) => (
  <div className={`fixed bottom-6 right-6 z-[100] flex items-center gap-3 px-4 py-3 rounded-xl shadow-lg text-sm font-medium
    ${type === 'success' ? 'bg-emerald-600 text-white' : 'bg-red-600 text-white'}`}>
    {type === 'success' ? '✅' : '❌'} {message}
    <button onClick={onClose} className="ml-2 text-white/70 hover:text-white">✕</button>
  </div>
);

const DetailRow = ({ icon, label, value }) => (
  <div className="flex items-start gap-3 py-3 border-b border-slate-700/60 last:border-0">
    <span className="text-lg shrink-0 mt-0.5">{icon}</span>
    <div className="flex-1 min-w-0">
      <p className="text-slate-500 text-xs font-semibold uppercase tracking-wide mb-0.5">{label}</p>
      <p className="text-slate-200 text-sm font-medium">{value}</p>
    </div>
  </div>
);

const JobDetailPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { token, isRecruiter } = useAuth();

  // Data passed from JobCard via navigate state
  const job = location.state?.job;

  const [roleplayOpen, setRoleplayOpen] = useState(false);
  const [roleplayData, setRoleplayData] = useState(null);
  const [roleplayLoading, setRoleplayLoading] = useState(false);
  const [applyLoading, setApplyLoading] = useState(false);
  const [toast, setToast] = useState(null);

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3500);
  };

  // Guard — if navigated directly without state
  if (!job) {
    return (
      <div className="min-h-screen bg-slate-900 text-white flex flex-col">
        <Navbar />
        <div className="flex-1 flex flex-col items-center justify-center gap-4">
          <span className="text-5xl">🔍</span>
          <p className="text-slate-300 font-semibold text-lg">Job details not found</p>
          <p className="text-slate-500 text-sm">Please navigate to this page from the jobs list.</p>
          <button
            onClick={() => navigate('/jobs')}
            className="mt-2 px-5 py-2.5 bg-blue-600 hover:bg-blue-500 text-white font-semibold rounded-xl text-sm transition-all"
          >
            ← Back to Jobs
          </button>
        </div>
      </div>
    );
  }

  const salary = formatSalary(job.minSalary, job.maxSalary, job.currency);

  const handleApply = async () => {
    setApplyLoading(true);
    try {
      await jobService.applyForJob(job.jobId, token);
      showToast('Application submitted! Check your email shortly.', 'success');
    } catch (err) {
      showToast(err?.response?.data?.message || 'Could not apply for this job.', 'error');
    } finally {
      setApplyLoading(false);
    }
  };

  const handleRoleplay = async () => {
    setRoleplayOpen(true);
    setRoleplayData(null);
    setRoleplayLoading(true);
    try {
      const res = await jobService.startRoleplay(job.jobId, token);
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

      <div className="max-w-3xl mx-auto p-6">

        {/* Back button */}
        <button
          onClick={() => navigate(-1)}
          className="flex items-center gap-1.5 text-slate-400 hover:text-white text-sm transition-colors mb-6"
        >
          <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
          </svg>
          Back
        </button>

        {/* Hero section */}
        <div className="bg-slate-800 border border-slate-700 rounded-2xl p-6 mb-5">
          <div className="flex items-start justify-between gap-4 flex-wrap mb-4">
            <div className="flex-1 min-w-0">
              {/* Full title — no truncation */}
              <h1 className="text-white font-bold text-2xl leading-snug break-words">
                {job.jobTitle}
              </h1>
              <div className="flex items-center gap-1.5 text-slate-400 text-sm mt-2">
                <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4 shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
                </svg>
                {/* Full location — no truncation */}
                <span>{job.location}</span>
              </div>
            </div>

            <span className="shrink-0 bg-slate-700 text-slate-300 text-sm font-semibold px-3 py-1.5 rounded-full">
              {job.vacancies} {job.vacancies === 1 ? 'vacancy' : 'vacancies'}
            </span>
          </div>

          {/* Badges */}
          <div className="flex flex-wrap gap-2">
            <span className={`text-xs font-semibold px-3 py-1.5 rounded-full ${JOB_TYPE_STYLES[job.jobType] || ''}`}>
              {job.jobType?.replace('_', ' ')}
            </span>
            <span className={`text-xs font-semibold px-3 py-1.5 rounded-full ${WORK_MODE_STYLES[job.workMode] || ''}`}>
              {job.workMode?.replace('_', ' ')}
            </span>
          </div>
        </div>

        {/* Job Description */}
        <div className="bg-slate-800 border border-slate-700 rounded-2xl p-6 mb-5">
          <h2 className="text-white font-bold text-base mb-3 flex items-center gap-2">
            📋 Job Description
          </h2>
          {/* Full description — no line-clamp */}
          <p className="text-slate-300 text-sm leading-relaxed whitespace-pre-wrap break-words">
            {job.jobDescription}
          </p>
        </div>

        {/* Details grid */}
        <div className="bg-slate-800 border border-slate-700 rounded-2xl p-6 mb-5">
          <h2 className="text-white font-bold text-base mb-2 flex items-center gap-2">
            📊 Job Details
          </h2>
          <div>
            <DetailRow icon="💼" label="Job Type" value={job.jobType?.replace('_', ' ')} />
            <DetailRow icon="🏠" label="Work Mode" value={job.workMode?.replace('_', ' ')} />
            <DetailRow icon="📍" label="Location" value={job.location} />
            <DetailRow icon="👥" label="Vacancies" value={`${job.vacancies} open position${job.vacancies !== 1 ? 's' : ''}`} />
            {salary && <DetailRow icon="💰" label="Salary Range" value={salary} />}
            <DetailRow icon="🆔" label="Job ID" value={`#${job.jobId}`} />
          </div>
        </div>

        {/* Actions */}
        <div className="bg-slate-800 border border-slate-700 rounded-2xl p-6">
          <h2 className="text-white font-bold text-base mb-4">
            {isRecruiter ? '⚙️ Recruiter Actions' : '🚀 Apply for this Job'}
          </h2>

          {!isRecruiter ? (
            <div className="flex flex-col sm:flex-row gap-3">
              <button
                onClick={handleApply}
                disabled={applyLoading}
                className="flex-1 py-3 bg-blue-600 hover:bg-blue-500 disabled:opacity-60 text-white font-semibold
                           rounded-xl transition-all text-sm shadow-lg shadow-blue-600/20"
              >
                {applyLoading ? 'Submitting...' : '✉️ Apply Now'}
              </button>
              <button
                onClick={handleRoleplay}
                className="flex-1 py-3 bg-slate-700 hover:bg-slate-600 text-slate-200 font-semibold
                           rounded-xl transition-all text-sm"
              >
                🎭 Practice Roleplay
              </button>
            </div>
          ) : (
            <div className="flex flex-col sm:flex-row gap-3">
              <button
                onClick={handleRoleplay}
                className="flex-1 py-3 bg-slate-700 hover:bg-slate-600 text-slate-200 font-semibold
                           rounded-xl transition-all text-sm"
              >
                🎭 Practice Roleplay
              </button>
              <button
                onClick={() => navigate('/recruiter/jobs')}
                className="flex-1 py-3 bg-blue-600/20 hover:bg-blue-600/40 text-blue-400 font-semibold
                           rounded-xl transition-all text-sm border border-blue-500/30"
              >
                ✏️ Manage This Job
              </button>
            </div>
          )}

          <p className="text-slate-600 text-xs mt-3 text-center">
            {isRecruiter
              ? 'To edit or delete this job, go to Job Management.'
              : 'You will receive an email confirmation after applying.'}
          </p>
        </div>
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

export default JobDetailPage;