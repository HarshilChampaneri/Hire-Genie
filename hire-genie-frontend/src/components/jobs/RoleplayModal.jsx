import { useState } from 'react';

const QuestionCard = ({ item, index }) => {
  const [open, setOpen] = useState(false);

  return (
    <div className="bg-slate-700/50 border border-slate-600 rounded-xl overflow-hidden">
      {/* Question header */}
      <button
        onClick={() => setOpen(!open)}
        className="w-full flex items-start justify-between gap-3 px-4 py-3.5 text-left hover:bg-slate-700 transition-colors"
      >
        <div className="flex items-start gap-3 flex-1 min-w-0">
          <span className="shrink-0 w-6 h-6 bg-blue-600/20 border border-blue-500/40 text-blue-400 text-xs font-bold
                           rounded-full flex items-center justify-center mt-0.5">
            {index + 1}
          </span>
          <p className="text-white text-sm font-medium leading-relaxed">{item.question}</p>
        </div>
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className={`w-4 h-4 text-slate-400 shrink-0 mt-1 transition-transform ${open ? 'rotate-180' : ''}`}
          fill="none" viewBox="0 0 24 24" stroke="currentColor"
        >
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 9l-7 7-7-7" />
        </svg>
      </button>

      {/* Expanded content */}
      {open && (
        <div className="px-4 pb-4 border-t border-slate-600 pt-3 flex flex-col gap-3">
          <div>
            <p className="text-slate-400 text-xs font-semibold uppercase tracking-wide mb-1">🎯 Why they ask this</p>
            <p className="text-slate-300 text-sm leading-relaxed">{item.intentionToAsk}</p>
          </div>
          <div>
            <p className="text-slate-400 text-xs font-semibold uppercase tracking-wide mb-1">✅ Model Answer</p>
            <p className="text-slate-300 text-sm leading-relaxed">{item.solution}</p>
          </div>
        </div>
      )}
    </div>
  );
};

const RoleplayModal = ({ isOpen, onClose, roleplayData, loading = false }) => {
  const [activeTab, setActiveTab] = useState('technical');

  if (!isOpen) return null;

  const technicalQuestions = roleplayData?.technicalQuestions || [];
  const behavioralQuestions = roleplayData?.behavioralQuestions || [];

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center p-4 bg-black/70 backdrop-blur-sm">
      <div className="bg-slate-800 border border-slate-700 rounded-2xl w-full max-w-2xl max-h-[90vh] flex flex-col
                      shadow-2xl shadow-black/60">

        {/* Header */}
        <div className="flex items-center justify-between px-6 py-4 border-b border-slate-700 shrink-0">
          <div>
            <h2 className="text-white font-bold text-lg">🎭 AI Interview Roleplay</h2>
            <p className="text-slate-400 text-xs mt-0.5">Practice with AI-generated interview questions</p>
          </div>
          <button onClick={onClose} className="text-slate-400 hover:text-white transition-colors">
            <svg xmlns="http://www.w3.org/2000/svg" className="w-5 h-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
            </svg>
          </button>
        </div>

        {/* Tabs */}
        <div className="flex border-b border-slate-700 shrink-0">
          <button
            onClick={() => setActiveTab('technical')}
            className={`flex-1 py-3 text-sm font-semibold transition-all ${
              activeTab === 'technical'
                ? 'text-blue-400 border-b-2 border-blue-400 bg-blue-400/5'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            💻 Technical ({technicalQuestions.length})
          </button>
          <button
            onClick={() => setActiveTab('behavioral')}
            className={`flex-1 py-3 text-sm font-semibold transition-all ${
              activeTab === 'behavioral'
                ? 'text-purple-400 border-b-2 border-purple-400 bg-purple-400/5'
                : 'text-slate-400 hover:text-slate-200'
            }`}
          >
            🧠 Behavioral ({behavioralQuestions.length})
          </button>
        </div>

        {/* Content */}
        <div className="flex-1 overflow-y-auto p-4">
          {loading ? (
            <div className="flex flex-col items-center justify-center py-16 gap-3">
              <div className="w-10 h-10 border-4 border-blue-600 border-t-transparent rounded-full animate-spin" />
              <p className="text-slate-400 text-sm">Generating interview questions...</p>
            </div>
          ) : (
            <div className="flex flex-col gap-3">
              {activeTab === 'technical' && technicalQuestions.length === 0 && (
                <p className="text-slate-500 text-sm text-center py-8">No technical questions available.</p>
              )}
              {activeTab === 'behavioral' && behavioralQuestions.length === 0 && (
                <p className="text-slate-500 text-sm text-center py-8">No behavioral questions available.</p>
              )}
              {activeTab === 'technical' &&
                technicalQuestions.map((q, i) => (
                  <QuestionCard key={i} item={q} index={i} />
                ))}
              {activeTab === 'behavioral' &&
                behavioralQuestions.map((q, i) => (
                  <QuestionCard key={i} item={q} index={i} />
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

export default RoleplayModal;