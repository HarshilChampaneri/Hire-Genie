import { useState } from 'react';

const INITIAL_FORM = {
  interviewDate: '',
  interviewTime: '',
  venue: '',
  additionalDetails: '',
};

const AcceptApplicantModal = ({ isOpen, onClose, onSubmit, applicant, loading = false }) => {
  const [form, setForm] = useState(INITIAL_FORM);

  if (!isOpen) return null;

  const inputClass = `w-full bg-slate-700 border border-slate-600 text-white rounded-lg px-3 py-2.5 text-sm
    focus:outline-none focus:border-emerald-500 focus:ring-1 focus:ring-emerald-500 transition-all
    placeholder:text-slate-500`;

  const labelClass = 'block text-slate-400 text-xs font-semibold mb-1.5 uppercase tracking-wide';

  const handleChange = (e) => {
    const { name, value } = e.target;
    setForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleSubmit = (e) => {
  e.preventDefault();
  onSubmit(applicant.id, {
    interviewDate: form.interviewDate,    // "YYYY-MM-DD"
    interviewTime: form.interviewTime,    // "HH:mm" — backend handles it
    venue: form.venue,
    additionalDetails: form.additionalDetails || null,
  });
};

  const handleClose = () => {
    setForm(INITIAL_FORM);
    onClose();
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/60 backdrop-blur-sm">
      <div className="bg-slate-800 border border-slate-700 rounded-2xl w-full max-w-lg max-h-[90vh] overflow-y-auto
                      shadow-2xl shadow-black/50">

        {/* Header */}
        <div className="flex items-center justify-between px-6 py-4 border-b border-slate-700">
          <div>
            <h2 className="text-white font-bold text-lg">✅ Accept Application</h2>
            <p className="text-slate-400 text-xs mt-0.5">
              Schedule an interview for{' '}
              <span className="text-emerald-400 font-semibold">{applicant?.candidateEmail}</span>
            </p>
          </div>
          <button onClick={handleClose} className="text-slate-400 hover:text-white transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        {/* Applicant Info Strip */}
        <div className="mx-6 mt-5 p-4 bg-slate-700/40 border border-slate-600/60 rounded-xl flex items-center gap-3">
          <div className="shrink-0 w-10 h-10 bg-emerald-600/20 border border-emerald-500/40 rounded-full
                          flex items-center justify-center text-emerald-400 font-bold text-base">
            {applicant?.candidateEmail?.charAt(0).toUpperCase()}
          </div>
          <div className="flex-1 min-w-0">
            <p className="text-white text-sm font-semibold truncate">{applicant?.candidateEmail}</p>
            <p className="text-slate-400 text-xs mt-0.5 truncate">
              {applicant?.jobTitle} — {applicant?.companyName}
            </p>
          </div>
          <span className="shrink-0 bg-emerald-600/15 border border-emerald-500/30 text-emerald-400
                           text-xs font-semibold px-2.5 py-1 rounded-full">
            Pending
          </span>
        </div>

        {/* Form */}
        <form onSubmit={handleSubmit} className="p-6 flex flex-col gap-4">

          {/* Date + Time */}
          <div className="grid grid-cols-2 gap-4">
            <div>
              <label className={labelClass}>Interview Date</label>
              <input
                name="interviewDate"
                type="date"
                value={form.interviewDate}
                onChange={handleChange}
                className={inputClass}
                required
                min={new Date().toISOString().split('T')[0]}
              />
            </div>
            <div>
              <label className={labelClass}>Interview Time</label>
              <input
                name="interviewTime"
                type="time"
                value={form.interviewTime}
                onChange={handleChange}
                className={inputClass}
                required
              />
            </div>
          </div>

          {/* Venue */}
          <div>
            <label className={labelClass}>Venue</label>
            <input
              name="venue"
              type="text"
              value={form.venue}
              onChange={handleChange}
              placeholder="e.g. Office Floor 3, Room 12 / Google Meet link"
              className={inputClass}
              required
              maxLength={200}
            />
          </div>

          {/* Additional Details */}
          <div>
            <label className={labelClass}>
              Additional Details{' '}
              <span className="text-slate-600 normal-case font-normal">(optional)</span>
            </label>
            <textarea
              name="additionalDetails"
              value={form.additionalDetails}
              onChange={handleChange}
              placeholder="Any special instructions, documents to bring, dress code, etc."
              className={`${inputClass} resize-none h-24`}
              maxLength={750}
            />
            <p className="text-slate-500 text-xs mt-1 text-right">
              {form.additionalDetails.length}/750
            </p>
          </div>

          {/* Actions */}
          <div className="flex gap-3 pt-1">
            <button
              type="button"
              onClick={handleClose}
              className="flex-1 py-2.5 bg-slate-700 hover:bg-slate-600 text-slate-300 font-semibold
                         rounded-lg transition-all text-sm"
            >
              Cancel
            </button>
            <button
              type="submit"
              disabled={loading}
              className="flex-1 py-2.5 bg-emerald-600 hover:bg-emerald-500 disabled:opacity-60
                         text-white font-semibold rounded-lg transition-all text-sm
                         shadow-lg shadow-emerald-600/20"
            >
              {loading ? 'Sending...' : '✅ Accept & Send Email'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AcceptApplicantModal;