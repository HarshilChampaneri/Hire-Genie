import { useState, useEffect } from 'react';
import { useLocation, useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import JobCard from '../components/jobs/JobCard';
import JobFormModal from '../components/jobs/JobFormModal';
import RoleplayModal from '../components/jobs/RoleplayModal';
import EmployeeRecommendationModal from '../components/recruiter/EmployeeRecommendationModal';
import Pagination from '../components/common/Pagination';
import { jobService } from '../services/jobService';
import { useAuth } from '../context/useAuth';

const Toast = ({ message, type, onClose }) => (
  <div className={`fixed bottom-6 right-6 z-[100] flex items-center gap-3 px-4 py-3 rounded-xl shadow-lg text-sm font-medium
    ${type === 'success' ? 'bg-emerald-600 text-white' : 'bg-red-600 text-white'}`}>
    {type === 'success' ? '✅' : '❌'} {message}
    <button onClick={onClose} className="ml-2 text-white/70 hover:text-white">✕</button>
  </div>
);

const ConfirmDialog = ({ isOpen, message, onConfirm, onCancel }) => {
  if (!isOpen) return null;
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm">
      <div className="bg-slate-800 border border-slate-700 rounded-2xl w-full max-w-sm p-6 shadow-2xl">
        <p className="text-white font-semibold text-center mb-6">{message}</p>
        <div className="flex gap-3">
          <button onClick={onCancel}
            className="flex-1 py-2.5 bg-slate-700 hover:bg-slate-600 text-slate-300 font-semibold rounded-lg text-sm transition-all">
            Cancel
          </button>
          <button onClick={onConfirm}
            className="flex-1 py-2.5 bg-red-600 hover:bg-red-500 text-white font-semibold rounded-lg text-sm transition-all">
            Delete
          </button>
        </div>
      </div>
    </div>
  );
};

const CompanyDetailPage = () => {
  const location = useLocation();
  const navigate = useNavigate();
  const { token } = useAuth();

  // Company passed from CompanyCard via navigate state
  const company = location.state?.company;

  const [jobs, setJobs] = useState([]);
  const [allCompanies, setAllCompanies] = useState([]);
  const [pageData, setPageData] = useState({ pageIndex: 0, totalPages: 0, isLastPage: true });
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [formLoading, setFormLoading] = useState(false);
  const [refreshTick, setRefreshTick] = useState(0);

  // Modals
  const [jobModalOpen, setJobModalOpen] = useState(false);
  const [editData, setEditData] = useState(null);
  const [confirmOpen, setConfirmOpen] = useState(false);
  const [deleteTargetId, setDeleteTargetId] = useState(null);
  const [roleplayOpen, setRoleplayOpen] = useState(false);
  const [roleplayData, setRoleplayData] = useState(null);
  const [roleplayLoading, setRoleplayLoading] = useState(false);
  const [recommendOpen, setRecommendOpen] = useState(false);
  const [candidates, setCandidates] = useState([]);
  const [recommendLoading, setRecommendLoading] = useState(false);

  const [toast, setToast] = useState(null);

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3500);
  };

  // Load companies list (needed for JobFormModal company select)
  useEffect(() => {
    const fetchCompanies = async () => {
      try {
        const res = await jobService.getAllCompanies(token, 0, 100);
        setAllCompanies(res.data.companyResponse || []);
      } catch { /* silent */ }
    };
    fetchCompanies();
  }, [token]);

  // Load jobs for this company
  useEffect(() => {
    if (!company) return;
    let cancelled = false;

    const fetchJobs = async () => {
      setLoading(true);
      try {
        const res = await jobService.getAllJobsByCompany(company.companyId, token, page, 9);
        if (!cancelled) {
          const data = res.data;
          setJobs(data.jobResponse || []);
          setPageData({ pageIndex: data.pageIndex, totalPages: data.totalPages, isLastPage: data.isLastPage });
        }
      } catch (err) {
        if (!cancelled) {
          if (err?.response?.status !== 404) showToast('Failed to load jobs.', 'error');
          setJobs([]);
        }
      } finally {
        if (!cancelled) setLoading(false);
      }
    };

    fetchJobs();
    return () => { cancelled = true; };
  }, [token, company, page, refreshTick]); // eslint-disable-line react-hooks/exhaustive-deps

  const triggerRefresh = () => setRefreshTick((t) => t + 1);

  // Guard — if navigated directly without state
  if (!company) {
    return (
      <div className="min-h-screen bg-slate-900 text-white flex flex-col">
        <Navbar />
        <div className="flex-1 flex flex-col items-center justify-center gap-4">
          <span className="text-5xl">🏢</span>
          <p className="text-slate-300 font-semibold text-lg">Company details not found</p>
          <p className="text-slate-500 text-sm">Please navigate here from the Company Management page.</p>
          <button
            onClick={() => navigate('/recruiter/companies')}
            className="mt-2 px-5 py-2.5 bg-blue-600 hover:bg-blue-500 text-white font-semibold rounded-xl text-sm transition-all"
          >
            ← Back to Companies
          </button>
        </div>
      </div>
    );
  }

  const handleAddJob = () => { setEditData(null); setJobModalOpen(true); };
  const handleEditJob = (job) => { setEditData(job); setJobModalOpen(true); };

  const handleJobSubmit = async (formData, companyId) => {
    setFormLoading(true);
    try {
      if (editData) {
        await jobService.updateJob(editData.jobId, formData, token);
        showToast('Job updated successfully!');
      } else {
        await jobService.addJob(companyId, formData, token);
        showToast('Job posted successfully!');
      }
      setJobModalOpen(false);
      triggerRefresh();
    } catch (err) {
      showToast(err?.response?.data?.message || 'Operation failed.', 'error');
    } finally {
      setFormLoading(false);
    }
  };

  const handleDeleteRequest = (jobId) => { setDeleteTargetId(jobId); setConfirmOpen(true); };

  const handleDeleteConfirm = async () => {
    setConfirmOpen(false);
    try {
      await jobService.deleteJob(deleteTargetId, token);
      showToast('Job deleted successfully.');
      triggerRefresh();
    } catch (err) {
      showToast(err?.response?.data?.message || 'Delete failed.', 'error');
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

  const handleRecommend = async (jobId) => {
    setRecommendOpen(true);
    setCandidates([]);
    setRecommendLoading(true);
    try {
      const res = await jobService.recommendEmployees(jobId, token);
      setCandidates(res.data || []);
    } catch {
      showToast('Failed to fetch recommendations.', 'error');
      setRecommendOpen(false);
    } finally {
      setRecommendLoading(false);
    }
  };

  return (
    <div className="min-h-screen bg-slate-900 text-white">
      <Navbar />

      <div className="max-w-6xl mx-auto p-6">

        {/* Back button */}
        <button
          onClick={() => navigate(-1)}
          className="flex items-center gap-1.5 text-slate-400 hover:text-white text-sm transition-colors mb-6"
        >
          <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 19l-7-7 7-7" />
          </svg>
          Back to Companies
        </button>

        {/* Company hero */}
        <div className="bg-slate-800 border border-slate-700 rounded-2xl p-6 mb-6">
          <div className="flex items-center gap-4 flex-wrap">
            {/* Avatar */}
            <div className="shrink-0 w-16 h-16 bg-blue-600/20 border border-blue-500/30 rounded-2xl
                            flex items-center justify-center text-blue-400 font-bold text-3xl">
              {company.companyName?.charAt(0).toUpperCase()}
            </div>

            <div className="flex-1 min-w-0">
              {/* Full company name — no truncation */}
              <h1 className="text-white font-bold text-2xl break-words">{company.companyName}</h1>
              <a
                href={company.companyUrl}
                target="_blank"
                rel="noopener noreferrer"
                className="text-blue-400 text-sm hover:text-blue-300 transition-colors mt-1 inline-flex items-center gap-1.5 break-all"
              >
                <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4 shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M13.828 10.172a4 4 0 00-5.656 0l-4 4a4 4 0 105.656 5.656l1.102-1.101m-.758-4.899a4 4 0 005.656 0l4-4a4 4 0 00-5.656-5.656l-1.1 1.1" />
                </svg>
                {/* Full URL — no truncation */}
                {company.companyUrl}
              </a>
            </div>

            <span className="shrink-0 bg-slate-700 text-slate-400 text-xs font-mono px-3 py-1.5 rounded-lg">
              Company ID: #{company.companyId}
            </span>
          </div>
        </div>

        {/* Jobs section header */}
        <div className="flex items-center justify-between mb-5 flex-wrap gap-3">
          <div>
            <h2 className="text-white font-bold text-lg">💼 Jobs at {company.companyName}</h2>
            <p className="text-slate-400 text-sm mt-0.5">All active job listings for this company</p>
          </div>
          <button
            onClick={handleAddJob}
            className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white font-semibold
                       px-4 py-2.5 rounded-xl transition-all text-sm"
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
            Post New Job
          </button>
        </div>

        {/* Jobs grid */}
        {loading ? (
          <div className="flex justify-center items-center py-24">
            <div className="w-10 h-10 border-4 border-blue-600 border-t-transparent rounded-full animate-spin" />
          </div>
        ) : jobs.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-20 gap-3 text-center">
            <span className="text-5xl">📭</span>
            <p className="text-slate-300 font-semibold text-lg">No jobs posted yet</p>
            <p className="text-slate-500 text-sm">Post the first job for {company.companyName}.</p>
            <button
              onClick={handleAddJob}
              className="mt-2 px-5 py-2.5 bg-blue-600 hover:bg-blue-500 text-white font-semibold rounded-xl text-sm transition-all"
            >
              + Post a Job
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
            {jobs.map((job) => (
              <JobCard
                key={job.jobId}
                job={job}
                isRecruiter={true}
                onEdit={handleEditJob}
                onDelete={handleDeleteRequest}
                onRecommend={handleRecommend}
                onRoleplay={handleRoleplay}
              />
            ))}
          </div>
        )}

        <Pagination
          pageIndex={pageData.pageIndex}
          totalPages={pageData.totalPages}
          isLastPage={pageData.isLastPage}
          onPageChange={setPage}
        />
      </div>

      {/* Modals */}
      <JobFormModal
        isOpen={jobModalOpen}
        onClose={() => setJobModalOpen(false)}
        onSubmit={handleJobSubmit}
        editData={editData}
        companies={allCompanies}
        loading={formLoading}
      />

      <RoleplayModal
        isOpen={roleplayOpen}
        onClose={() => setRoleplayOpen(false)}
        roleplayData={roleplayData}
        loading={roleplayLoading}
      />

      <EmployeeRecommendationModal
        isOpen={recommendOpen}
        onClose={() => setRecommendOpen(false)}
        candidates={candidates}
        loading={recommendLoading}
      />

      <ConfirmDialog
        isOpen={confirmOpen}
        message="⚠️ Are you sure you want to delete this job listing?"
        onConfirm={handleDeleteConfirm}
        onCancel={() => setConfirmOpen(false)}
      />

      {toast && <Toast message={toast.message} type={toast.type} onClose={() => setToast(null)} />}
    </div>
  );
};

export default CompanyDetailPage;