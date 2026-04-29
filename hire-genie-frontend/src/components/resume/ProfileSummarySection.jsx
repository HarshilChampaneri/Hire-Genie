import { useState, useEffect } from 'react';
import { resumeService } from '../../api/resumeService';
import { useAuth } from '../../context/useAuth';

const SparkleIcon = () => (
  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z" />
  </svg>
);

const ProfileSummarySection = () => {
  const { token } = useAuth();
  const [exists, setExists] = useState(false);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [aiLoading, setAiLoading] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [summary, setSummary] = useState('');

  useEffect(() => {
    
    const fetchSummary = async () => {
      try {
        const { data } = await resumeService.getProfileSummary(token);
        setSummary(data.profileSummary || '');
        setExists(true);
      } catch { setExists(false); }
      finally { setLoading(false); }
    };
    
    fetchSummary(); }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');
    setSaving(true);
    try {
      if (exists) {
        await resumeService.updateProfileSummary({ profileSummary: summary }, token);
        setSuccess('Summary updated!');
      } else {
        await resumeService.addProfileSummary({ profileSummary: summary }, token);
        setExists(true);
        setSuccess('Summary saved!');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save summary.');
    } finally { setSaving(false); }
  };

  const handleDelete = async () => {
    if (!window.confirm('Delete your profile summary?')) return;
    try {
      await resumeService.deleteProfileSummary(token);
      setSummary(''); setExists(false);
      setSuccess('Summary deleted.');
    } catch { setError('Failed to delete.'); }
  };

  const handleAIGenerate = async () => {
    setAiLoading(true); setError(''); setSuccess('');
    try {
      const { data } = exists
        ? await resumeService.rewriteProfileSummaryAI(token)
        : await resumeService.generateProfileSummaryAI(token);
      setSummary(data.profileSummary || '');
      setExists(true);
      setSuccess('✨ AI has crafted your summary! Review and save it.');
    } catch { setError('AI generation failed. Make sure your profile is filled in first.'); }
    finally { setAiLoading(false); }
  };

  if (loading) return <div className="flex justify-center py-12"><div className="w-8 h-8 border-2 border-cyan-400 border-t-transparent rounded-full animate-spin" /></div>;

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h3 className="text-xl font-bold text-white">Profile Summary</h3>
          <p className="text-slate-400 text-sm mt-1">A short professional bio (max 350 characters)</p>
        </div>
        {exists && (
          <button onClick={handleDelete} className="text-red-400 hover:text-red-300 text-sm border border-red-500/30 hover:border-red-400 px-3 py-1.5 rounded-lg transition-all">
            Delete
          </button>
        )}
      </div>

      {error && <div className="bg-red-500/10 border border-red-500/50 text-red-400 text-sm rounded-xl p-3 mb-4">{error}</div>}
      {success && <div className="bg-emerald-500/10 border border-emerald-500/50 text-emerald-400 text-sm rounded-xl p-3 mb-4">{success}</div>}

      <div className="mb-4">
        <button
          type="button" onClick={handleAIGenerate} disabled={aiLoading}
          className="flex items-center gap-2 bg-violet-600/20 hover:bg-violet-600/30 border border-violet-500/40 hover:border-violet-400 text-violet-300 px-4 py-2.5 rounded-xl text-sm font-medium transition-all disabled:opacity-50"
        >
          <SparkleIcon />
          {aiLoading ? 'AI is writing…' : exists ? '✨ Rewrite with AI' : '✨ Generate with AI'}
        </button>
        <p className="text-slate-500 text-xs mt-2">AI will use your saved profile & experience data to craft a compelling summary.</p>
      </div>

      <form onSubmit={handleSubmit}>
        <div className="relative">
          <textarea
            required value={summary}
            onChange={e => setSummary(e.target.value)}
            maxLength={350}
            rows={5}
            placeholder="Write a compelling professional summary that highlights your key skills, experience, and career goals…"
            className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all resize-none"
          />
          <span className={`absolute bottom-3 right-4 text-xs ${summary.length > 320 ? 'text-amber-400' : 'text-slate-500'}`}>
            {summary.length}/350
          </span>
        </div>

        <button
          type="submit" disabled={saving}
          className="mt-4 w-full bg-gradient-to-r from-cyan-500 to-blue-600 hover:from-cyan-400 hover:to-blue-500 disabled:opacity-50 text-white font-semibold py-3 rounded-xl transition-all shadow-lg shadow-cyan-500/20"
        >
          {saving ? 'Saving…' : exists ? 'Update Summary' : 'Save Summary'}
        </button>
      </form>
    </div>
  );
};

export default ProfileSummarySection;