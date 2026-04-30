import { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import JobCard from '../components/jobs/JobCard';
import RoleplayModal from '../components/jobs/RoleplayModal';
import Pagination from '../components/common/Pagination';
import { jobService } from '../services/jobService';
import { useAuth } from '../context/useAuth';

const SORT_OPTIONS = [
  { label: 'Job Type', value: 'jobType' },
  { label: 'Title', value: 'jobTitle' },
  { label: 'Location', value: 'location' },
];

const Toast = ({ message, type, onClose }) => (
  <div className={`fixed bottom-6 right-6 z-[100] flex items-center gap-3 px-4 py-3 rounded-xl shadow-lg text-sm font-medium
    ${type === 'success' ? 'bg-emerald-600 text-white' : 'bg-red-600 text-white'}`}>
    {type === 'success' ? '✅' : '❌'} {message}
    <button onClick={onClose} className="ml-2 text-white/70 hover:text-white">✕</button>
  </div>
);

const JobsPage = () => {
  const { token } = useAuth();

  // Jobs state
  const [jobs, setJobs] = useState([]);
  const [pageData, setPageData] = useState({ pageIndex: 0, totalPages: 0, isLastPage: true });
  const [loading, setLoading] = useState(false);

  // Companies for filter
  const [companies, setCompanies] = useState([]);
  const [selectedCompany, setSelectedCompany] = useState(null); // null = all jobs

  // Pagination & sort
  const [page, setPage] = useState(0);
  const [sortBy, setSortBy] = useState('jobType');
  const [sortDir, setSortDir] = useState('asc');

  // Roleplay
  const [roleplayOpen, setRoleplayOpen] = useState(false);
  const [roleplayData, setRoleplayData] = useState(null);
  const [roleplayLoading, setRoleplayLoading] = useState(false);

  // Toast
  const [toast, setToast] = useState(null);

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3500);
  };

  // Load companies for sidebar filter
  useEffect(() => {
    const fetchCompanies = async () => {
      try {
        const res = await jobService.getAllCompanies(token, 0, 100);
        setCompanies(res.data.companyResponse || []);
      } catch {
        // silent — companies filter is optional
      }
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
        if (selectedCompany) {
          res = await jobService.getAllJobsByCompany(selectedCompany.companyId, token, page, 9, sortBy, sortDir);
        } else {
          res = await jobService.getAllJobs(token, page, 9, sortBy, sortDir);
        }
        if (!cancelled) {
          const data = res.data;
          setJobs(data.jobResponse || []);
          setPageData({ pageIndex: data.pageIndex, totalPages: data.totalPages, isLastPage: data.isLastPage });
        }
      } catch (err) {
        if (!cancelled) {
          const msg = err?.response?.data?.message || 'Failed to load jobs.';
          if (err?.response?.status !== 404) showToast(msg, 'error');
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
  }, [token, selectedCompany, page, sortBy, sortDir]); // eslint-disable-line react-hooks/exhaustive-deps

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

      <div className="flex">
        {/* Sidebar — Company Filter */}
        <aside className="hidden lg:flex flex-col w-64 shrink-0 border-r border-slate-700/60 min-h-[calc(100vh-64px)]
                          p-4 gap-2 sticky top-0 h-screen overflow-y-auto">
          <p className="text-slate-400 text-xs font-bold uppercase tracking-widest px-2 mb-1">Filter by Company</p>

          <button
            onClick={() => { setSelectedCompany(null); setPage(0); }}
            className={`w-full text-left px-3 py-2.5 rounded-xl text-sm font-medium transition-all
              ${!selectedCompany ? 'bg-blue-600 text-white' : 'text-slate-300 hover:bg-slate-700'}`}
          >
            🌐 All Jobs
          </button>

          {companies.map((c) => (
            <button
              key={c.companyId}
              onClick={() => { setSelectedCompany(c); setPage(0); }}
              className={`w-full text-left px-3 py-2.5 rounded-xl text-sm font-medium transition-all truncate
                ${selectedCompany?.companyId === c.companyId
                  ? 'bg-blue-600 text-white'
                  : 'text-slate-300 hover:bg-slate-700'}`}
            >
              🏢 {c.companyName}
            </button>
          ))}
        </aside>

        {/* Main Content */}
        <main className="flex-1 p-6 max-w-6xl">
          {/* Page Header */}
          <div className="flex items-center justify-between mb-6 flex-wrap gap-4">
            <div>
              <h1 className="text-2xl font-bold text-white">
                {selectedCompany ? `Jobs at ${selectedCompany.companyName}` : 'Browse All Jobs'}
              </h1>
              <p className="text-slate-400 text-sm mt-0.5">Find your next opportunity</p>
            </div>

            {/* Sort controls */}
            <div className="flex items-center gap-2">
              <select
                value={sortBy}
                onChange={(e) => { setSortBy(e.target.value); setPage(0); }}
                className="bg-slate-700 border border-slate-600 text-slate-200 text-sm rounded-lg px-3 py-2
                           focus:outline-none focus:border-blue-500 transition-all"
              >
                {SORT_OPTIONS.map((o) => (
                  <option key={o.value} value={o.value}>{o.label}</option>
                ))}
              </select>
              <button
                onClick={() => { setSortDir((d) => (d === 'asc' ? 'desc' : 'asc')); setPage(0); }}
                className="bg-slate-700 border border-slate-600 text-slate-200 text-sm rounded-lg px-3 py-2
                           hover:bg-slate-600 transition-all"
              >
                {sortDir === 'asc' ? '↑ Asc' : '↓ Desc'}
              </button>
            </div>
          </div>

          {/* Mobile Company Filter */}
          <div className="lg:hidden mb-4 flex gap-2 overflow-x-auto pb-2">
            <button
              onClick={() => { setSelectedCompany(null); setPage(0); }}
              className={`shrink-0 px-3 py-1.5 rounded-full text-xs font-semibold transition-all
                ${!selectedCompany ? 'bg-blue-600 text-white' : 'bg-slate-700 text-slate-300'}`}
            >
              All
            </button>
            {companies.map((c) => (
              <button
                key={c.companyId}
                onClick={() => { setSelectedCompany(c); setPage(0); }}
                className={`shrink-0 px-3 py-1.5 rounded-full text-xs font-semibold transition-all
                  ${selectedCompany?.companyId === c.companyId ? 'bg-blue-600 text-white' : 'bg-slate-700 text-slate-300'}`}
              >
                {c.companyName}
              </button>
            ))}
          </div>

          {/* Jobs Grid */}
          {loading ? (
            <div className="flex justify-center items-center py-24">
              <div className="w-10 h-10 border-4 border-blue-600 border-t-transparent rounded-full animate-spin" />
            </div>
          ) : jobs.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-24 gap-3 text-center">
              <span className="text-5xl">💼</span>
              <p className="text-slate-300 font-semibold text-lg">No jobs found</p>
              <p className="text-slate-500 text-sm">Try a different company filter or check back later.</p>
            </div>
          ) : (
            <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
              {jobs.map((job) => (
                <JobCard
                  key={job.jobId}
                  job={job}
                  onApply={handleApply}
                  onRoleplay={handleRoleplay}
                  isRecruiter={false}
                />
              ))}
            </div>
          )}

          {/* Pagination */}
          <Pagination
            pageIndex={pageData.pageIndex}
            totalPages={pageData.totalPages}
            isLastPage={pageData.isLastPage}
            onPageChange={setPage}
          />
        </main>
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

export default JobsPage;