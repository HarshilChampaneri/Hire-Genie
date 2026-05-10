import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import Pagination from '../components/common/Pagination';
import AcceptApplicantModal from '../components/recruiter/AcceptApplicantModal';
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
          <button
            onClick={onCancel}
            className="flex-1 py-2.5 bg-slate-700 hover:bg-slate-600 text-slate-300 font-semibold rounded-lg text-sm transition-all"
          >
            Cancel
          </button>
          <button
            onClick={onConfirm}
            className="flex-1 py-2.5 bg-red-600 hover:bg-red-500 text-white font-semibold rounded-lg text-sm transition-all"
          >
            Reject
          </button>
        </div>
      </div>
    </div>
  );
};

const ApplicationCard = ({ application, onAccept, onReject }) => (
  <div className="bg-slate-800 border border-slate-700 rounded-2xl p-5 flex flex-col gap-4
                  hover:border-slate-500 transition-all duration-200 hover:shadow-lg hover:shadow-black/20">

    {/* Header */}
    <div className="flex items-start gap-3">
      {/* Avatar */}
      <div className="shrink-0 w-11 h-11 bg-blue-600/20 border border-blue-500/40 rounded-full
                      flex items-center justify-center text-blue-400 font-bold text-lg">
        {application.candidateEmail?.charAt(0).toUpperCase()}
      </div>

      <div className="flex-1 min-w-0">
        <p className="text-white font-semibold text-sm truncate">{application.candidateEmail}</p>
        <p className="text-slate-400 text-xs mt-0.5 truncate">
          Recruiter: {application.recruiterEmail}
        </p>
      </div>

      {/* Status badge */}
      <span className="shrink-0 bg-amber-500/15 border border-amber-500/30 text-amber-400
                       text-xs font-semibold px-2.5 py-1 rounded-full">
        Pending
      </span>
    </div>

    {/* Divider */}
    <div className="h-px bg-slate-700" />

    {/* Job details */}
    <div className="grid grid-cols-2 gap-3">
      <div>
        <p className="text-slate-500 text-xs font-semibold uppercase tracking-wide mb-0.5">Position</p>
        <p className="text-slate-200 text-sm font-medium truncate">{application.jobTitle}</p>
      </div>
      <div>
        <p className="text-slate-500 text-xs font-semibold uppercase tracking-wide mb-0.5">Company</p>
        <p className="text-slate-200 text-sm font-medium truncate">{application.companyName}</p>
      </div>
    </div>

    {/* Actions */}
    <div className="flex gap-2 pt-1 border-t border-slate-700">
      <button
        onClick={() => onAccept(application)}
        className="flex-1 py-2 bg-emerald-600/15 hover:bg-emerald-600/30 text-emerald-400
                   text-sm font-semibold rounded-lg transition-all border border-emerald-500/30"
      >
        ✅ Accept
      </button>
      <button
        onClick={() => onReject(application)}
        className="flex-1 py-2 bg-red-600/15 hover:bg-red-600/30 text-red-400
                   text-sm font-semibold rounded-lg transition-all border border-red-500/30"
      >
        ✕ Reject
      </button>
    </div>
  </div>
);

const SORT_OPTIONS = [
  { label: 'Job Title', value: 'jobTitle' },
  { label: 'Company', value: 'companyName' },
  { label: 'Candidate', value: 'candidateEmail' },
];

const PendingApplicationsPage = () => {
  const { token } = useAuth();
  const navigate = useNavigate();

  const [applications, setApplications] = useState([]);
  const [pageData, setPageData] = useState({ pageIndex: 0, totalPages: 0, isLastPage: true });
  const [page, setPage] = useState(0);
  const [sortBy, setSortBy] = useState('jobTitle');
  const [sortDir, setSortDir] = useState('asc');
  const [loading, setLoading] = useState(false);
  const [refreshTick, setRefreshTick] = useState(0);
  const triggerRefresh = () => setRefreshTick((t) => t + 1);

  // Accept modal
  const [acceptModalOpen, setAcceptModalOpen] = useState(false);
  const [selectedApplicant, setSelectedApplicant] = useState(null);
  const [acceptLoading, setAcceptLoading] = useState(false);

  // Reject confirm
  const [rejectConfirmOpen, setRejectConfirmOpen] = useState(false);
  const [rejectTarget, setRejectTarget] = useState(null);

  const [toast, setToast] = useState(null);

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3500);
  };

  // ── Fetch pending applications ──────────────────────────────────────────────
  useEffect(() => {
    let cancelled = false;

    const fetchApplications = async () => {
      setLoading(true);
      try {
        const res = await jobService.getPendingApplications(token, page, 9, sortBy, sortDir);
        if (!cancelled) {
          const data = res.data;
          setApplications(data.jobApplicationResponses || []);
          setPageData({
            pageIndex: data.pageIndex,
            totalPages: data.totalPages,
            isLastPage: data.isLastPage,
          });
        }
      } catch (err) {
        if (!cancelled) {
          if (err?.response?.status !== 404) showToast('Failed to load applications.', 'error');
          setApplications([]);
        }
      } finally {
        if (!cancelled) setLoading(false);
      }
    };

    fetchApplications();
    return () => { cancelled = true; };
  }, [token, page, sortBy, sortDir]); // eslint-disable-line react-hooks/exhaustive-deps

  // Refresh effect after accept / reject mutations
  useEffect(() => {
    if (refreshTick === 0) return;
    let cancelled = false;

    const fetchApplications = async () => {
      setLoading(true);
      try {
        const res = await jobService.getPendingApplications(token, page, 9, sortBy, sortDir);
        if (!cancelled) {
          const data = res.data;
          setApplications(data.jobApplicationResponses || []);
          setPageData({
            pageIndex: data.pageIndex,
            totalPages: data.totalPages,
            isLastPage: data.isLastPage,
          });
        }
      } catch (err) {
        if (!cancelled) {
          if (err?.response?.status !== 404) showToast('Failed to load applications.', 'error');
          setApplications([]);
        }
      } finally {
        if (!cancelled) setLoading(false);
      }
    };

    fetchApplications();
    return () => { cancelled = true; };
  }, [refreshTick]); // eslint-disable-line react-hooks/exhaustive-deps

  // ── Accept handlers ─────────────────────────────────────────────────────────
  const handleAcceptClick = (application) => {
    setSelectedApplicant(application);
    setAcceptModalOpen(true);
  };

  const handleAcceptSubmit = async (jobApplicationId, formData) => {
    setAcceptLoading(true);
    try {
      await jobService.acceptApplicant(jobApplicationId, formData, token);
      showToast('Application accepted! Interview email sent to candidate.', 'success');
      setAcceptModalOpen(false);
      setSelectedApplicant(null);
      triggerRefresh();
    } catch (err) {
      showToast(err?.response?.data?.message || 'Failed to accept application.', 'error');
    } finally {
      setAcceptLoading(false);
    }
  };

  // ── Reject handlers ─────────────────────────────────────────────────────────
  const handleRejectClick = (application) => {
    setRejectTarget(application);
    setRejectConfirmOpen(true);
  };

  const handleRejectConfirm = async () => {
    setRejectConfirmOpen(false);
    try {
      await jobService.rejectApplicant(rejectTarget.id, token);
      showToast('Application rejected. Email sent to candidate.', 'success');
      triggerRefresh();
    } catch (err) {
      showToast(err?.response?.data?.message || 'Failed to reject application.', 'error');
    } finally {
      setRejectTarget(null);
    }
  };

  return (
    <div className="min-h-screen bg-slate-900 text-white">
      <Navbar />

      <div className="max-w-6xl mx-auto p-6">

        {/* Page Header */}
        <div className="flex items-center justify-between mb-6 flex-wrap gap-4">
          <div>
            <button
              onClick={() => navigate('/recruiter/dashboard')}
              className="text-slate-400 hover:text-white text-sm transition-colors mb-1 block"
            >
              ← Dashboard
            </button>
            <h1 className="text-2xl font-bold text-white">📋 Pending Applications</h1>
            <p className="text-slate-400 text-sm mt-0.5">
              Review, accept or reject candidate applications
            </p>
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

        {/* Summary strip */}
        {!loading && applications.length > 0 && (
          <div className="mb-5 p-4 bg-amber-500/10 border border-amber-500/30 rounded-xl flex items-center gap-3">
            <span className="text-2xl">📬</span>
            <p className="text-amber-400 text-sm font-medium">
              You have <span className="font-bold">{pageData.totalElements ?? applications.length}</span> pending
              application{(pageData.totalElements ?? applications.length) !== 1 ? 's' : ''} waiting for review.
            </p>
          </div>
        )}

        {/* Content */}
        {loading ? (
          <div className="flex justify-center items-center py-24">
            <div className="w-10 h-10 border-4 border-blue-600 border-t-transparent rounded-full animate-spin" />
          </div>
        ) : applications.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-24 gap-3 text-center">
            <span className="text-5xl">🎉</span>
            <p className="text-slate-300 font-semibold text-lg">No pending applications</p>
            <p className="text-slate-500 text-sm">
              All caught up! New applications will appear here.
            </p>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
            {applications.map((app, index) => (
              <ApplicationCard
                key={app.id ?? index}
                application={app}
                onAccept={handleAcceptClick}
                onReject={handleRejectClick}
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

      {/* Accept Modal */}
      <AcceptApplicantModal
        isOpen={acceptModalOpen}
        onClose={() => { setAcceptModalOpen(false); setSelectedApplicant(null); }}
        onSubmit={handleAcceptSubmit}
        applicant={selectedApplicant}
        loading={acceptLoading}
      />

      {/* Reject Confirm Dialog */}
      <ConfirmDialog
        isOpen={rejectConfirmOpen}
        message={`⚠️ Reject application from ${rejectTarget?.candidateEmail}? An email will be sent to notify them.`}
        onConfirm={handleRejectConfirm}
        onCancel={() => { setRejectConfirmOpen(false); setRejectTarget(null); }}
      />

      {/* Toast */}
      {toast && <Toast message={toast.message} type={toast.type} onClose={() => setToast(null)} />}
    </div>
  );
};

export default PendingApplicationsPage;