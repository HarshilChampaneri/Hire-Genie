import { useState, useEffect } from 'react';
import Navbar from '../components/Navbar';
import CompanyCard from '../components/companies/CompanyCard';
import CompanyFormModal from '../components/companies/CompanyFormModal';
import Pagination from '../components/common/Pagination';
import { jobService } from '../services/jobService';
import { useAuth } from '../context/useAuth';
import { useNavigate } from 'react-router-dom';

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

const CompanyManagement = () => {
  const { token } = useAuth();
  const navigate = useNavigate();

  const [companies, setCompanies] = useState([]);
  const [pageData, setPageData] = useState({ pageIndex: 0, totalPages: 0, isLastPage: true });
  const [page, setPage] = useState(0);
  const [loading, setLoading] = useState(false);
  const [formLoading, setFormLoading] = useState(false);

  const [modalOpen, setModalOpen] = useState(false);
  const [editData, setEditData] = useState(null);

  const [confirmOpen, setConfirmOpen] = useState(false);
  const [deleteTargetId, setDeleteTargetId] = useState(null);

  const [toast, setToast] = useState(null);

  // Increment to trigger a manual re-fetch after mutations (add / edit / delete)
  const [refreshTick, setRefreshTick] = useState(0);
  const triggerRefresh = () => setRefreshTick((t) => t + 1);

  const showToast = (message, type = 'success') => {
    setToast({ message, type });
    setTimeout(() => setToast(null), 3500);
  };

  // Load companies — inlined fetch with cancellation guard
  useEffect(() => {
    let cancelled = false;

    const fetchCompanies = async () => {
      setLoading(true);
      try {
        const res = await jobService.getAllCompanies(token, page, 9);
        if (!cancelled) {
          const data = res.data;
          setCompanies(data.companyResponse || []);
          setPageData({ pageIndex: data.pageIndex, totalPages: data.totalPages, isLastPage: data.isLastPage });
        }
      } catch (err) {
        if (!cancelled) {
          if (err?.response?.status !== 404) showToast('Failed to load companies.', 'error');
          setCompanies([]);
        }
      } finally {
        if (!cancelled) setLoading(false);
      }
    };

    fetchCompanies();

    return () => {
      cancelled = true;
    };
  }, [token, page, refreshTick]); // eslint-disable-line react-hooks/exhaustive-deps

  const handleAdd = () => { setEditData(null); setModalOpen(true); };
  const handleEdit = (company) => { setEditData(company); setModalOpen(true); };

  const handleSubmit = async (formData) => {
    setFormLoading(true);
    try {
      if (editData) {
        await jobService.updateCompany(editData.companyId, formData, token);
        showToast('Company updated successfully!');
      } else {
        await jobService.addCompany(formData, token);
        showToast('Company added successfully!');
      }
      setModalOpen(false);
      triggerRefresh();
    } catch (err) {
      const msg = err?.response?.data?.message || 'Operation failed.';
      showToast(msg, 'error');
    } finally {
      setFormLoading(false);
    }
  };

  const handleDeleteRequest = (companyId) => {
    setDeleteTargetId(companyId);
    setConfirmOpen(true);
  };

  const handleDeleteConfirm = async () => {
    setConfirmOpen(false);
    try {
      await jobService.deleteCompany(deleteTargetId, token);
      showToast('Company deleted successfully.');
      triggerRefresh();
    } catch (err) {
      const msg = err?.response?.data?.message || 'Delete failed.';
      showToast(msg, 'error');
    }
  };

  const handleViewJobs = (companyId, companyName) => {
    navigate(`/recruiter/jobs?companyId=${companyId}&companyName=${encodeURIComponent(companyName)}`);
  };

  return (
    <div className="min-h-screen bg-slate-900 text-white">
      <Navbar />

      <div className="max-w-6xl mx-auto p-6">
        {/* Header */}
        <div className="flex items-center justify-between mb-8 flex-wrap gap-4">
          <div>
            <div className="flex items-center gap-2 mb-1">
              <button
                onClick={() => navigate('/recruiter/dashboard')}
                className="text-slate-400 hover:text-white text-sm transition-colors"
              >
                ← Dashboard
              </button>
            </div>
            <h1 className="text-2xl font-bold text-white">🏢 Company Management</h1>
            <p className="text-slate-400 text-sm mt-0.5">Add, edit, and manage your companies</p>
          </div>
          <button
            onClick={handleAdd}
            className="flex items-center gap-2 bg-blue-600 hover:bg-blue-500 text-white font-semibold
                       px-5 py-2.5 rounded-xl transition-all text-sm"
          >
            <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
            </svg>
            Add Company
          </button>
        </div>

        {/* Grid */}
        {loading ? (
          <div className="flex justify-center items-center py-24">
            <div className="w-10 h-10 border-4 border-blue-600 border-t-transparent rounded-full animate-spin" />
          </div>
        ) : companies.length === 0 ? (
          <div className="flex flex-col items-center justify-center py-24 gap-3 text-center">
            <span className="text-5xl">🏢</span>
            <p className="text-slate-300 font-semibold text-lg">No companies yet</p>
            <p className="text-slate-500 text-sm">Add your first company to start posting jobs.</p>
            <button
              onClick={handleAdd}
              className="mt-2 px-5 py-2.5 bg-blue-600 hover:bg-blue-500 text-white font-semibold rounded-xl text-sm transition-all"
            >
              + Add Company
            </button>
          </div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 xl:grid-cols-3 gap-4">
            {companies.map((company) => (
              <CompanyCard
                key={company.companyId}
                company={company}
                onEdit={handleEdit}
                onDelete={handleDeleteRequest}
                onViewJobs={handleViewJobs}
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

      <CompanyFormModal
        isOpen={modalOpen}
        onClose={() => setModalOpen(false)}
        onSubmit={handleSubmit}
        editData={editData}
        loading={formLoading}
      />

      <ConfirmDialog
        isOpen={confirmOpen}
        message="⚠️ Delete this company? All associated jobs will also be deleted."
        onConfirm={handleDeleteConfirm}
        onCancel={() => setConfirmOpen(false)}
      />

      {toast && <Toast message={toast.message} type={toast.type} onClose={() => setToast(null)} />}
    </div>
  );
};

export default CompanyManagement;