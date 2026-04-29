// SkillsSection.jsx
import { useState, useEffect } from 'react';
import { resumeService } from '../../api/resumeService';
import { useAuth } from '../../context/useAuth';

export const SkillsSection = () => {
  const { token } = useAuth();
  const [skills, setSkills] = useState(null); // Map<String, List<String>>
  const [loading, setLoading] = useState(true);
  const [aiLoading, setAiLoading] = useState(false);
  const [customPrompt, setCustomPrompt] = useState('');
  const [showPrompt, setShowPrompt] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    
    const fetchSkills = async () => {
      try {
        const { data } = await resumeService.getSkills(token);
        setSkills(data.technicalSkills || null);
      } catch { setSkills(null); }
      finally { setLoading(false); }
    };

    fetchSkills(); }, []);

  const handleGenerate = async () => {
    setAiLoading(true); setError(''); setSuccess('');
    try {
      const { data } = await resumeService.generateSkillSummary(customPrompt || '', token);
      setSkills(data.technicalSkills || null);
      setSuccess('✨ Skills generated from your profile! They\'ve been saved automatically.');
    } catch { setError('AI skill generation failed. Make sure your profile and experience are filled in first.'); }
    finally { setAiLoading(false); }
  };

  if (loading) return <div className="flex justify-center py-12"><div className="w-8 h-8 border-2 border-cyan-400 border-t-transparent rounded-full animate-spin" /></div>;

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h3 className="text-xl font-bold text-white">Skills</h3>
          <p className="text-slate-400 text-sm mt-1">AI-powered skill extraction from your profile & experience</p>
        </div>
      </div>

      {error && <div className="bg-red-500/10 border border-red-500/50 text-red-400 text-sm rounded-xl p-3 mb-4">{error}</div>}
      {success && <div className="bg-emerald-500/10 border border-emerald-500/50 text-emerald-400 text-sm rounded-xl p-3 mb-4">{success}</div>}

      <div className="bg-violet-500/5 border border-violet-500/20 rounded-2xl p-5 mb-6">
        <div className="flex items-start gap-3">
          <span className="text-2xl">🤖</span>
          <div className="flex-1">
            <h4 className="text-white font-semibold mb-1">AI Skill Analyzer</h4>
            <p className="text-slate-400 text-sm mb-4">Our AI analyzes your experience, projects, and profile to automatically categorize your technical skills. You can also provide a custom prompt to refine the output.</p>
            <button type="button" onClick={() => setShowPrompt(p => !p)}
              className="text-violet-400 hover:text-violet-300 text-sm underline mb-3 transition-colors">
              {showPrompt ? 'Hide custom prompt ↑' : 'Add custom prompt (optional) ↓'}
            </button>
            {showPrompt && (
              <textarea value={customPrompt} onChange={e => setCustomPrompt(e.target.value)} rows={3}
                placeholder="e.g. Focus on backend and cloud technologies. Include Docker, Kubernetes, and AWS prominently."
                className="w-full bg-slate-700/50 border border-slate-600 focus:border-violet-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all resize-none text-sm mb-3" />
            )}
            <button onClick={handleGenerate} disabled={aiLoading}
              className="flex items-center gap-2 bg-violet-600 hover:bg-violet-500 disabled:opacity-50 text-white px-5 py-2.5 rounded-xl text-sm font-semibold transition-all">
              <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z" />
              </svg>
              {aiLoading ? 'Analyzing…' : skills ? 'Regenerate Skills' : 'Generate Skills with AI'}
            </button>
          </div>
        </div>
      </div>

      {skills && Object.keys(skills).length > 0 ? (
        <div className="space-y-4">
          {Object.entries(skills).map(([category, skillList]) => (
            <div key={category} className="bg-slate-700/30 border border-slate-600/50 rounded-2xl p-5">
              <h4 className="text-cyan-400 font-semibold text-sm uppercase tracking-wider mb-3">{category}</h4>
              <div className="flex flex-wrap gap-2">
                {skillList.map((skill, i) => (
                  <span key={i} className="bg-slate-700 border border-slate-600 text-white text-sm px-3 py-1.5 rounded-full hover:border-cyan-500/50 transition-colors">
                    {skill}
                  </span>
                ))}
              </div>
            </div>
          ))}
        </div>
      ) : (
        <div className="text-center py-12 text-slate-500">
          <div className="text-4xl mb-3">🎯</div>
          <p>No skills generated yet. Fill in your experiences and projects, then click Generate.</p>
        </div>
      )}
    </div>
  );
};

// OtherSection.jsx — for "Other" freeform section
export const OtherSection = () => {
  const { token } = useAuth();
  const [exists, setExists] = useState(false);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [aiLoading, setAiLoading] = useState(false);
  const [otherId, setOtherId] = useState(null);
  const [description, setDescription] = useState(['']);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  useEffect(() => {
    
    const fetchOther = async () => {
      try {
        const { data } = await resumeService.getOther(token);
        setDescription(data.description || ['']);
        setOtherId(data.otherId);
        setExists(true);
      } catch { setExists(false); }
      finally { setLoading(false); }
    };
    
    fetchOther(); }, []);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');
    setSaving(true);
    const payload = { description: description.filter(d => d.trim() !== '') };
    try {
      if (exists) {
        await resumeService.updateOther(payload, token);
        setSuccess('Updated!');
      } else {
        const { data } = await resumeService.addOther(payload, token);
        setOtherId(data.otherId);
        setExists(true);
        setSuccess('Saved!');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save.');
    } finally { setSaving(false); }
  };

  const handleDelete = async () => {
    if (!window.confirm('Delete this section?')) return;
    try {
      await resumeService.deleteOther(token);
      setExists(false); setDescription(['']); setOtherId(null);
      setSuccess('Deleted.');
    } catch { setError('Failed to delete.'); }
  };

  const handleAIRewrite = async () => {
    if (!otherId) return;
    setAiLoading(true); setError('');
    try {
      const { data } = await resumeService.rewriteOtherDescAI(otherId, token);
      setDescription(data.otherDescription || ['']);
      setSuccess('✨ AI rewrote your "Other" section!');
    } catch { setError('AI rewrite failed.'); }
    finally { setAiLoading(false); }
  };

  const updateItem = (i, val) => { const d = [...description]; d[i] = val; setDescription(d); };
  const addItem = () => setDescription(p => [...p, '']);
  const removeItem = (i) => setDescription(p => p.filter((_, idx) => idx !== i));

  if (loading) return <div className="flex justify-center py-12"><div className="w-8 h-8 border-2 border-cyan-400 border-t-transparent rounded-full animate-spin" /></div>;

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h3 className="text-xl font-bold text-white">Other / Achievements</h3>
          <p className="text-slate-400 text-sm mt-1">Awards, languages, volunteering, hobbies, publications…</p>
        </div>
        {exists && (
          <button onClick={handleDelete} className="text-red-400 hover:text-red-300 text-sm border border-red-500/30 hover:border-red-400 px-3 py-1.5 rounded-lg transition-all">Delete</button>
        )}
      </div>

      {error && <div className="bg-red-500/10 border border-red-500/50 text-red-400 text-sm rounded-xl p-3 mb-4">{error}</div>}
      {success && <div className="bg-emerald-500/10 border border-emerald-500/50 text-emerald-400 text-sm rounded-xl p-3 mb-4">{success}</div>}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <div className="flex items-center justify-between mb-2">
            <label className="text-slate-400 text-xs font-semibold uppercase tracking-wider">Descriptions *</label>
            <div className="flex gap-2">
              {exists && (
                <button type="button" onClick={handleAIRewrite} disabled={aiLoading}
                  className="flex items-center gap-1.5 bg-violet-600/20 hover:bg-violet-600/30 border border-violet-500/40 text-violet-300 px-3 py-1.5 rounded-lg text-xs font-medium transition-all disabled:opacity-50">
                  <svg className="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z" />
                  </svg>
                  {aiLoading ? 'Rewriting…' : 'Rewrite with AI'}
                </button>
              )}
              <button type="button" onClick={addItem} className="text-cyan-400 hover:text-cyan-300 text-xs font-medium flex items-center gap-1 transition-colors">
                <svg className="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
                </svg>
                Add
              </button>
            </div>
          </div>
          <div className="space-y-2">
            {description.map((item, i) => (
              <div key={i} className="flex gap-2 items-start">
                <span className="text-cyan-400 mt-3.5 text-xs">▸</span>
                <input value={item} onChange={e => updateItem(i, e.target.value)}
                  placeholder="e.g. Winner – Smart India Hackathon 2023"
                  className="flex-1 bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-2.5 text-white placeholder-slate-500 outline-none transition-all text-sm" />
                {description.length > 1 && (
                  <button type="button" onClick={() => removeItem(i)} className="text-slate-500 hover:text-red-400 transition-colors mt-2">
                    <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                    </svg>
                  </button>
                )}
              </div>
            ))}
          </div>
        </div>
        <button type="submit" disabled={saving}
          className="w-full bg-gradient-to-r from-cyan-500 to-blue-600 hover:from-cyan-400 hover:to-blue-500 disabled:opacity-50 text-white font-semibold py-3 rounded-xl transition-all shadow-lg shadow-cyan-500/20">
          {saving ? 'Saving…' : exists ? 'Update' : 'Save'}
        </button>
      </form>
    </div>
  );
};