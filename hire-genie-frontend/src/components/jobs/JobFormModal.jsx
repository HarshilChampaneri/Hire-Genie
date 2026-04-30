import { useState } from 'react';

const INITIAL_FORM = {
  jobTitle: '',
  jobDescription: '',
  jobType: 'FULL_TIME',
  workMode: 'ON_SITE',
  location: '',
  minSalary: '',
  maxSalary: '',
  currency: 'USD',
  vacancies: 1,
};

const buildForm = (editData) => {
  if (!editData) return INITIAL_FORM;
  return {
    jobTitle: editData.jobTitle || '',
    jobDescription: editData.jobDescription || '',
    jobType: editData.jobType || 'FULL_TIME',
    workMode: editData.workMode || 'ON_SITE',
    location: editData.location || '',
    minSalary: editData.minSalary || '',
    maxSalary: editData.maxSalary || '',
    currency: editData.currency || 'USD',
    vacancies: editData.vacancies || 1,
  };
};

const JobFormModal = ({ isOpen, onClose, onSubmit, editData = null, companies = [], loading = false }) => {
  // Derive initial state directly from props — no useEffect needed.
  // Using editData + isOpen as the key on the wrapper div (see below) resets
  // this state automatically whenever the modal is opened for a different record.
  const [form, setForm] = useState(() => buildForm(editData));
  const [selectedCompanyId, setSelectedCompanyId] = useState(
    () => companies[0]?.companyId?.toString() || ''
  );

  if (!isOpen) return null;

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const payload = {
      ...form,
      minSalary: form.minSalary ? parseFloat(form.minSalary) : null,
      maxSalary: form.maxSalary ? parseFloat(form.maxSalary) : null,
      vacancies: parseInt(form.vacancies, 10),
    };
    onSubmit(payload, selectedCompanyId);
  };

  const inputClass = `w-full bg-slate-700 border border-slate-600 text-white rounded-lg px-3 py-2.5 text-sm
    focus:outline-none focus:border-blue-500 focus:ring-1 focus:ring-blue-500 transition-all
    placeholder:text-slate-500`;

  const labelClass = 'block text-slate-400 text-xs font-semibold mb-1.5 uppercase tracking-wide';

  return (
    // key forces React to remount (and therefore re-initialise state) whenever
    // the modal opens for a different editData record or switches add ↔ edit.
    <div
      key={editData?.jobId ?? 'new'}
      className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm"
    >
      <div className="bg-slate-800 border border-slate-700 rounded-2xl w-full max-w-2xl max-h-[90vh] overflow-y-auto
                      shadow-2xl shadow-black/50">

        {/* Header */}
        <div className="flex items-center justify-between px-6 py-4 border-b border-slate-700">
          <h2 className="text-white font-bold text-lg">
            {editData ? '✏️ Edit Job' : '➕ Add New Job'}
          </h2>
          <button onClick={onClose} className="text-slate-400 hover:text-white transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 flex flex-col gap-4">

          {/* Company select — only for add */}
          {!editData && (
            <div>
              <label className={labelClass}>Company</label>
              <select
                value={selectedCompanyId}
                onChange={(e) => setSelectedCompanyId(e.target.value)}
                className={inputClass}
                required
              >
                {companies.map((c) => (
                  <option key={c.companyId} value={c.companyId}>{c.companyName}</option>
                ))}
              </select>
            </div>
          )}

          {/* Job Title */}
          <div>
            <label className={labelClass}>Job Title</label>
            <input
              name="jobTitle"
              value={form.jobTitle}
              onChange={handleChange}
              placeholder="e.g. Senior Software Engineer"
              className={inputClass}
              required
              maxLength={150}
            />
          </div>

          {/* Description */}
          <div>
            <label className={labelClass}>Job Description</label>
            <textarea
              name="jobDescription"
              value={form.jobDescription}
              onChange={handleChange}
              placeholder="Describe the role, responsibilities, requirements..."
              className={`${inputClass} resize-none h-28`}
              required
              maxLength={1000}
            />
            <p className="text-slate-500 text-xs mt-1">{form.jobDescription.length}/1000</p>
          </div>

          {/* Type + Mode */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className={labelClass}>Job Type</label>
              <select name="jobType" value={form.jobType} onChange={handleChange} className={inputClass}>
                <option value="FULL_TIME">Full Time</option>
                <option value="PART_TIME">Part Time</option>
              </select>
            </div>
            <div>
              <label className={labelClass}>Work Mode</label>
              <select name="workMode" value={form.workMode} onChange={handleChange} className={inputClass}>
                <option value="REMOTE">Remote</option>
                <option value="HYBRID">Hybrid</option>
                <option value="ON_SITE">On Site</option>
              </select>
            </div>
          </div>

          {/* Location + Vacancies */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className={labelClass}>Location</label>
              <input
                name="location"
                value={form.location}
                onChange={handleChange}
                placeholder="e.g. New York, NY"
                className={inputClass}
                required
                maxLength={100}
              />
            </div>
            <div>
              <label className={labelClass}>Vacancies</label>
              <input
                name="vacancies"
                type="number"
                min={1}
                value={form.vacancies}
                onChange={handleChange}
                className={inputClass}
                required
              />
            </div>
          </div>

          {/* Salary */}
          <div className="grid grid-cols-3 gap-4">
            <div>
              <label className={labelClass}>Currency</label>
              <select name="currency" value={form.currency} onChange={handleChange} className={inputClass}>
                <option value="USD">USD</option>
                <option value="EUR">EUR</option>
                <option value="GBP">GBP</option>
                <option value="INR">INR</option>
                <option value="AED">AED</option>
              </select>
            </div>
            <div>
              <label className={labelClass}>Min Salary</label>
              <input
                name="minSalary"
                type="number"
                min={0}
                value={form.minSalary}
                onChange={handleChange}
                placeholder="Optional"
                className={inputClass}
              />
            </div>
            <div>
              <label className={labelClass}>Max Salary</label>
              <input
                name="maxSalary"
                type="number"
                min={0}
                value={form.maxSalary}
                onChange={handleChange}
                placeholder="Optional"
                className={inputClass}
              />
            </div>
          </div>

          {/* Actions */}
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
              {loading ? 'Saving...' : editData ? 'Update Job' : 'Add Job'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default JobFormModal;