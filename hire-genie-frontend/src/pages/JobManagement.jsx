import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
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

const JobManagement = () => {
  const { token } = useAuth();
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  // If coming from Company Management with a companyId filter
  const companyIdFromQuery = searchParams.get('companyId');
  const companyNameFromQuery = searchParams.get('companyName');

  const [jobs, setJobs] = useState([]);
  const [companies, setCompanies] = useState([]);
  const [pageData, setPageData] = useState({ pageIndex: 0, totalPages: 0, isLastPage: true });
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [formLoading, setFormLoading] = useState(false);

  // Selected company filter
  const [selectedCompanyId, setSelectedCompanyId] = useState(companyIdFromQuery || null);
  const [selectedCompanyName, setSelectedCompanyName] = useState(companyNameFromQuery || 'All Jobs');

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

  // Load companies for the job form
  useEffect(() => {
    const fetchCompanies = async () => {
      try {
        const res = await jobService.getAllCompanies(token, 0, 100);
        setCompanies(res.data.companyResponse || []);
      } catch { /* silent */ }
    };
    fetchCompanies();
  }, [token]);

  // Load jobs — single effect, no useCallback, with cancellation guard
  useEffect(() => {
    let cancelled = false;

    const fetchJobs = async () => {
      setLoading(true);
      try {
        let res;
        if (selectedCompanyId) {
          res = await jobService.getAllJobsByCompany(selectedCompanyId, token, page, 9);
        } else {
          res = await jobService.getAllJobs(token, page, 9);
        }
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

    return () => {
      cancelled = true;
    };
  }, [token, selectedCompanyId, page]); // eslint-disable-line react-hooks/exhaustive-deps

  // Ref to expose a manual refresh (used after add/edit/delete)
  // We do this by toggling a refresh counter instead of calling fetchJobs directly
  const [refreshTick, setRefreshTick] = useState(0);
  const triggerRefresh = () => setRefreshTick((t) => t + 1);

  // Second effect that re-runs fetchJobs when refreshTick changes
  // (same body, separate deps — avoids stale closure issues after mutations)
  useEffect(() => {
    if (refreshTick === 0) return; // skip on mount; the first effect handles it

    let cancelled = false;

    const fetchJobs = async () => {
      setLoading(true);
      try {
        let res;
        if (selectedCompanyId) {
          res = await jobService.getAllJobsByCompany(selectedCompanyId, token, page, 9);
        } else {
          res = await jobService.getAllJobs(token, page, 9);
        }
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

    return () => {
      cancelled = true;
    };
  }, [refreshTick]); // eslint-disable-line react-hooks/exhaustive-deps

  const handleAdd = () => { setEditData(null); setJobModalOpen(true); };
  const handleEdit = (job) => { setEditData(job); setJobModalOpen(true); };

  const handleSubmit = async (formData, companyId) => {
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
      const msg = err?.response?.data?.message || 'Operation failed.';
      showToast(msg, 'error');
    } finally {
      setFormLoading(false);
    }
  };

  const handleDeleteRequest = (jobId) => {
    setDeleteTargetId(jobId);
    setConfirmOpen(true);
  };

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
        {/* Header */}
        <div className="flex items-center justify-between mb-6 flex-wrap gap-4">
          <div>
            <div className="flex items-center gap-2 mb-1">
              <button
                onClick={() => navigate('/recruiter/dashboard')}
                className="text-slate-400 hover:text-white text-sm transition-colors"
              >
                ← Dashboard
              </button>
            </div>
            <h1 className="text-2xl font-bold text-white">
              💼 {selectedCompanyId ? `Jobs — ${selectedCompanyName}` : 'Job Management'}
            </h1>
            <p className="text-slate-400 text-sm mt-0.5">Post and manage job listings</p>
          </div>
          <div className="flex items-center gap-3">
            {selectedCompanyId && (
              <button
                onClick={() => { setSelectedCompanyId(null); setSelectedCompanyName('All Jobs'); setPage(0); }}
                className="text-slate-400 hover:text-white text-sm transition-colors px-3 py-2 bg-slate-700 rounded-lg"
              >
                ✕ Clear Filter
              </button>
            )}
            <button
              onClick={handleAdd}
              disabled={companies.length === 0}
              className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 disabled:opacity-50
                         text-white font-semibold px-5 py-2.5 rounded-xl transition-all text-sm"
            >
              <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
              </svg>
              Post Job
            </button>
          </div>
        </div>

        {/* Company filter chips */}
        {companies.length > 0 && (
          <div className="flex gap-2 overflow-x-auto pb-2 mb-5">
            <button
              onClick={() => { setSelectedCompanyId(null); setSelectedCompanyName('All Jobs'); setPage(0); }}
              className={`shrink-0 px-3 py-1.5 rounded-full text-xs font-semibold transition-all
                ${!selectedCompanyId ? 'bg-blue-600 text-white' : 'bg-slate-700 text-slate-300 hover:bg-slate-600'}`}
            >
              All
            </button>
            {companies.map((c) => (
              <button
                key={c.companyId}
                onClick={() => { setSelectedCompanyId(c.companyId); setSelectedCompanyName(c.companyName); setPage(0); }}
                className={`shrink-0 px-3 py-1.5 rounded-full text-xs font-semibold transition-all
                  ${String(selectedCompanyId) === String(c.companyId)
                    ? 'bg-blue-600 text-white'
                    : 'bg-slate-700 text-slate-300 hover:bg-slate-600'}`}
              >
                {c.companyName}
              </button>
            ))}
          </div>
        )}

        {/* No companies warning */}
        {companies.length === 0 && !loading && (
          <div className="mb-5 p-4 bg-amber-500/10 border border-amber-500/30 rounded-xl text-amber-400 text-sm">
            ⚠️ You have no companies yet.{' '}
            <button onClick={() => navigate('/recruiter/companies')} className="underline font-semibold">
              Add a company first
            </button>{' '}
            before posting jobs.
          </div>
        )}

        {/* Jobs Grid */}
        {loading ? (
          <div className="flex justify-center items-center py-24">
            <div className="w-10 h-10 border-4 border-blue-600 border-t-transparent rounded-full animate-spin" />
          </div>
        ) : jobs.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-24 gap-3 text-center">
            <span className="text-5xl">📭</span>
            <p className="text-slate-300 font-semibold text-lg">No jobs posted yet</p>
            <p className="text-slate-500 text-sm">Start by posting a new job listing.</p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
            {jobs.map((job) => (
              <JobCard
                key={job.jobId}
                job={job}
                isRecruiter={true}
                onEdit={handleEdit}
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
        onSubmit={handleSubmit}
        editData={editData}
        companies={companies}
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

export default JobManagement;