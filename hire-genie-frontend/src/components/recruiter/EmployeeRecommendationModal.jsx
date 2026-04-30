const EmployeeRecommendationModal = ({ isOpen, onClose, candidates = [], loading = false }) => {
  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/70 backdrop-blur-sm">
      <div className="bg-slate-800 border border-slate-700 rounded-2xl w-full max-w-xl max-h-[85vh] flex flex-col
                      shadow-2xl shadow-black/60">

        {/* Header */}
        <div className="flex items-center justify-between px-6 py-4 border-b border-slate-700 shrink-0">
          <div>
            <h2 className="text-white font-bold text-lg">🤖 AI Employee Recommendations</h2>
            <p className="text-slate-400 text-xs mt-0.5">
              {loading ? 'Analyzing job description...' : `${candidates.length} candidates matched`}
            </p>
          </div>
          <button onClick={onClose} className="text-slate-400 hover:text-white transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        {/* Content */}
        <div className="flex-1 overflow-y-auto p-4">
          {loading ? (
            <div className="flex flex-col items-center justify-center py-16 gap-3">
              <div className="w-10 h-10 border-4 border-purple-600 border-t-transparent rounded-full animate-spin" />
              <p className="text-slate-400 text-sm">Finding best-fit candidates...</p>
            </div>
          ) : candidates.length === 0 ? (
            <div className="flex flex-col items-center justify-center py-16 gap-2">
              <span className="text-4xl">🔍</span>
              <p className="text-slate-400 text-sm">No matching candidates found.</p>
            </div>
          ) : (
            <div className="flex flex-col gap-3">
              {candidates.map((candidate, index) => (
                <div
                  key={candidate.profileId || index}
                  className="bg-slate-700/50 border border-slate-600 rounded-xl p-4 flex items-start gap-4"
                >
                  {/* Avatar */}
                  <div className="shrink-0 w-11 h-11 bg-purple-600/20 border border-purple-500/40 rounded-full
                                  flex items-center justify-center text-purple-400 font-bold text-lg">
                    {candidate.fullName?.charAt(0).toUpperCase()}
                  </div>

                  {/* Info */}
                  <div className="flex-1 min-w-0">
                    <h4 className="text-white font-semibold text-sm">{candidate.fullName}</h4>
                    {candidate.profession && (
                      <p className="text-blue-400 text-xs mt-0.5">{candidate.profession}</p>
                    )}
                    <div className="flex flex-wrap gap-x-4 gap-y-1 mt-2">
                      {candidate.email && (
                        <span className="text-slate-400 text-xs flex items-center gap-1">
                          <svg xmlns="http://www.w3.org/2000/svg" className="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 8l7.89 5.26a2 2 0 002.22 0L21 8M5 19h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                          </svg>
                          {candidate.email}
                        </span>
                      )}
                      {candidate.mobileNo && (
                        <span className="text-slate-400 text-xs flex items-center gap-1">
                          <svg xmlns="http://www.w3.org/2000/svg" className="w-3 h-3" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M3 5a2 2 0 012-2h3.28a1 1 0 01.948.684l1.498 4.493a1 1 0 01-.502 1.21l-2.257 1.13a11.042 11.042 0 005.516 5.516l1.13-2.257a1 1 0 011.21-.502l4.493 1.498a1 1 0 01.684.949V19a2 2 0 01-2 2h-1C9.716 21 3 14.284 3 6V5z" />
                          </svg>
                          {candidate.mobileNo}
                        </span>
                      )}
                    </div>

                    {/* URLs */}
                    {candidate.urls && candidate.urls.length > 0 && (
                      <div className="flex flex-wrap gap-2 mt-2">
                        {[...candidate.urls].map((url, i) => (
                          <a
                            key={i}
                            href={url}
                            target="_blank"
                            rel="noopener noreferrer"
                            className="text-blue-400 text-xs hover:text-blue-300 underline underline-offset-2 transition-colors"
                          >
                            🔗 Profile {i + 1}
                          </a>
                        ))}
                      </div>
                    )}
                  </div>

                  {/* Rank badge */}
                  <span className="shrink-0 bg-purple-600/20 border border-purple-500/40 text-purple-400
                                   text-xs font-bold px-2 py-1 rounded-lg">
                    #{index + 1}
                  </span>
                </div>
              ))}
            </div>
          )}
        </div>

        {/* Footer */}
        <div className="px-6 py-4 border-t border-slate-700 shrink-0">
          <button
            onClick={onClose}
            className="w-full py-2.5 bg-slate-700 hover:bg-slate-600 text-slate-200 font-semibold rounded-lg transition-all text-sm"
          >
            Close
          </button>
        </div>
      </div>
    </div>
  );
};

export default EmployeeRecommendationModal;