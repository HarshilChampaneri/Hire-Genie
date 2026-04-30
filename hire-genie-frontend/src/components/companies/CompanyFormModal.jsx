import { useState } from 'react';

const INITIAL_FORM = { companyName: '', companyUrl: '' };

const buildForm = (editData) => {
  if (!editData) return INITIAL_FORM;
  return {
    companyName: editData.companyName || '',
    companyUrl: editData.companyUrl || '',
  };
};

const CompanyFormModal = ({ isOpen, onClose, onSubmit, editData = null, loading = false }) => {
  // Derived directly from props via lazy initialiser — no useEffect needed.
  // The key prop on the wrapper (below) remounts the component whenever
  // the modal opens for a different record, re-running this initialiser.
  const [form, setForm] = useState(() => buildForm(editData));

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(form);
  };

  const inputClass = `w-full bg-slate-700 border border-slate-600 text-white rounded-lg px-3 py-2.5 text-sm
    focus:outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-all
    placeholder:text-slate-500`;

  const labelClass = 'block text-slate-400 text-xs font-semibold mb-1.5 uppercase tracking-wide';

  return (
    // key remounts the component (resetting state) each time the modal opens
    // for a different company or switches between add and edit mode.
    <div
      key={editData?.companyId ?? 'new'}
      className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm"
    >
      <div className="bg-slate-800 border border-slate-700 rounded-2xl w-full max-w-md shadow-2xl shadow-black/50">

        {/* Header */}
        <div className="flex items-center justify-between px-6 py-4 border-b border-slate-700">
          <h2 className="text-white font-bold text-lg">
            {editData ? '✏️ Edit Company' : '🏢 Add New Company'}
          </h2>
          <button onClick={onClose} className="text-slate-400 hover:text-white transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 flex flex-col gap-4">
          <div>
            <label className={labelClass}>Company Name</label>
            <input
              name="companyName"
              value={form.companyName}
              onChange={handleChange}
              placeholder="e.g. Acme Corporation"
              className={inputClass}
              required
              maxLength={150}
            />
          </div>
          <div>
            <label className={labelClass}>Company Website URL</label>
            <input
              name="companyUrl"
              value={form.companyUrl}
              onChange={handleChange}
              placeholder="e.g. https://acme.com"
              className={inputClass}
              required
              maxLength={150}
            />
          </div>

          <div className="flex gap-3 pt-2">
            <button
              type="button"
              onClick={onClose}
              className="flex-1 py-2.5 bg-slate-700 hover:bg-slate-600 text-slate-300 font-semibold rounded-lg transition-all text-sm"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={loading}
              className="flex-1 py-2.5 bg-blue-600 hover:bg-blue-500 disabled:opacity-60 text-white font-semibold rounded-lg transition-all text-sm"
            >
              {loading ? 'Saving...' : editData ? 'Update Company' : 'Add Company'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default CompanyFormModal;