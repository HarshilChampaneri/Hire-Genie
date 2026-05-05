import { useState, useEffect } from 'react';
import { resumeService } from '../../api/resumeService';
import { useAuth } from '../../context/useAuth';
import { YearMonthPicker, DynamicListInput, SectionCard, AIButton } from './ResumeUtils';

const emptyExp = () => ({
  companyName: '', position: '', startDate: '', isWorkingInCompany: false,
  endDate: '', description: ['']
});

const ExperienceForm = ({ exp, onChange, onAIRewrite, aiLoading, existingId }) => (
  <div className="space-y-4">
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div>
        <label className="block text-slate-400 text-xs font-semibold uppercase tracking-wider mb-2">Company Name *</label>
        <input value={exp.companyName} onChange={e => onChange('companyName', e.target.value)} placeholder="e.g. Google India"
          className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all" />
      </div>
      <div>
        <label className="block text-slate-400 text-xs font-semibold uppercase tracking-wider mb-2">Position *</label>
        <input value={exp.position} onChange={e => onChange('position', e.target.value)} placeholder="e.g. Senior Software Engineer"
          className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all" />
      </div>
    </div>

    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <YearMonthPicker label="Start Date" value={exp.startDate} onChange={v => onChange('startDate', v)} required />
      <div>
        <YearMonthPicker label="End Date" value={exp.endDate} onChange={v => onChange('endDate', v)} disabled={exp.isWorkingInCompany} />
        <label className="flex items-center gap-2 mt-2 cursor-pointer">
          <input type="checkbox" checked={exp.isWorkingInCompany} onChange={e => onChange('isWorkingInCompany', e.target.checked)}
            className="rounded border-slate-600 bg-slate-700 text-cyan-500" />
          <span className="text-slate-400 text-sm">Currently working here</span>
        </label>
      </div>
    </div>

    <div>
      <div className="flex items-center justify-between mb-1">
        <span className="text-slate-400 text-xs font-semibold uppercase tracking-wider">Description *</span>
        {existingId && <AIButton onClick={() => onAIRewrite(existingId)} loading={aiLoading} />}
      </div>
      <DynamicListInput
        items={exp.description} onChange={v => onChange('description', v)}
        placeholder="Describe your responsibilities and achievements…"
      />
    </div>
  </div>
);

const ExperienceSection = () => {
  const { token } = useAuth();
  const [experiences, setExperiences] = useState([]);
  const [newExps, setNewExps] = useState([emptyExp()]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [aiLoadingId, setAiLoadingId] = useState(null);
  const [editingId, setEditingId] = useState(null);
  const [editForm, setEditForm] = useState(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const fetchExperiences = async () => {
    try {
      const { data } = await resumeService.getAllExperiences(token);
      setExperiences(data.experiences || []);
    } catch { setExperiences([]); }
    finally { setLoading(false); }
  };

  useEffect(() => {
    fetchExperiences(); }, []);

  const handleNewChange = (i, field, value) => {
    setNewExps(prev => prev.map((e, idx) => idx === i ? { ...e, [field]: value } : e));
  };

  const handleAddRow = () => setNewExps(prev => [...prev, emptyExp()]);
  const handleRemoveNew = (i) => setNewExps(prev => prev.filter((_, idx) => idx !== i));

  const handleSaveNew = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');
    setSaving(true);
    try {
      const payload = newExps.map(exp => ({
        ...exp,
        endDate: exp.isWorkingInCompany ? undefined : exp.endDate || undefined,
      }));
      await resumeService.addExperiences(payload, token);
      setSuccess('Experiences saved!');
      setNewExps([emptyExp()]);
      fetchExperiences();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save experiences.');
    } finally { setSaving(false); }
  };

  const startEdit = (exp) => {
    setEditingId(exp.experienceId);
    setEditForm({ ...exp, description: exp.description || [''] });
  };

  const handleUpdate = async () => {
    setSaving(true); setError(''); setSuccess('');

    try {
      const { experienceId, ...payload } = editForm;

      await resumeService.updateExperience(editingId, {
        ...payload,
        endDate: payload.isWorkingInCompany ? undefined : payload.endDate || undefined,
      }, token);
      setSuccess('Experience updated!');
      setEditingId(null); setEditForm(null);
      fetchExperiences();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update.');
    } finally { setSaving(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this experience?')) return;
    try {
      await resumeService.deleteExperience(id, token);
      setExperiences(prev => prev.filter(e => e.experienceId !== id));
      setSuccess('Deleted.');
    } catch { setError('Failed to delete.'); }
  };

  const handleAIRewrite = async (id) => {
    setAiLoadingId(id);
    try {
      const { data } = await resumeService.rewriteExperienceDescAI(id, token);
      if (editingId === id) {
        setEditForm(prev => ({ ...prev, description: data.experienceDescription }));
      }
      setSuccess('✨ AI rewrote the description! Review and update.');
      fetchExperiences();
    } catch { setError('AI rewrite failed.'); }
    finally { setAiLoadingId(null); }
  };

  if (loading) return <div className="flex justify-center py-12"><div className="w-8 h-8 border-2 border-cyan-400 border-t-transparent rounded-full animate-spin" /></div>;

  return (
    <div>
      <div className="mb-6">
        <h3 className="text-xl font-bold text-white">Work Experience</h3>
        <p className="text-slate-400 text-sm mt-1">Add your professional experience history</p>
      </div>

      {error && <div className="bg-red-500/10 border border-red-500/50 text-red-400 text-sm rounded-xl p-3 mb-4">{error}</div>}
      {success && <div className="bg-emerald-500/10 border border-emerald-500/50 text-emerald-400 text-sm rounded-xl p-3 mb-4">{success}</div>}

      {/* Saved experiences */}
      {experiences.length > 0 && (
        <div className="space-y-4 mb-8">
          <h4 className="text-slate-300 font-semibold text-sm uppercase tracking-wider">Saved Experiences</h4>
          {experiences.map(exp => (
            <SectionCard
              key={exp.experienceId}
              title={exp.position}
              subtitle={`${exp.companyName} · ${exp.startDate} → ${exp.isWorkingInCompany ? 'Present' : exp.endDate}`}
              onDelete={() => handleDelete(exp.experienceId)}
            >
              {editingId === exp.experienceId ? (
                <div className="mt-4 space-y-4">
                  <ExperienceForm exp={editForm} onChange={(f, v) => setEditForm(p => ({ ...p, [f]: v }))}
                    onAIRewrite={handleAIRewrite} aiLoading={aiLoadingId === exp.experienceId} existingId={exp.experienceId} />
                  <div className="flex gap-3">
                    <button onClick={handleUpdate} disabled={saving}
                      className="flex-1 bg-gradient-to-r from-cyan-500 to-blue-600 hover:from-cyan-400 hover:to-blue-500 disabled:opacity-50 text-white font-semibold py-2.5 rounded-xl transition-all text-sm">
                      {saving ? 'Saving…' : 'Save Changes'}
                    </button>
                    <button onClick={() => setEditingId(null)} className="px-4 py-2.5 border border-slate-600 text-slate-400 hover:text-white rounded-xl text-sm transition-all">
                      Cancel
                    </button>
                  </div>
                </div>
              ) : (
                <div className="mt-2">
                  <ul className="space-y-1">
                    {exp.description?.map((d, i) => (
                      <li key={i} className="text-slate-400 text-sm flex gap-2"><span className="text-cyan-400 mt-0.5">▸</span>{d}</li>
                    ))}
                  </ul>
                  <button onClick={() => startEdit(exp)} className="mt-3 text-cyan-400 hover:text-cyan-300 text-sm font-medium transition-colors">
                    Edit →
                  </button>
                </div>
              )}
            </SectionCard>
          ))}
        </div>
      )}

      {/* Add new */}
      <div className="border border-dashed border-slate-600 rounded-2xl p-5">
        <h4 className="text-slate-300 font-semibold text-sm uppercase tracking-wider mb-4">
          {experiences.length === 0 ? 'Add Experience' : '+ Add More'}
        </h4>
        <form onSubmit={handleSaveNew} className="space-y-6">
          {newExps.map((exp, i) => (
            <div key={i}>
              {i > 0 && <div className="border-t border-slate-700 pt-6" />}
              {newExps.length > 1 && (
                <div className="flex justify-between items-center mb-4">
                  <span className="text-slate-400 text-sm font-medium">Experience #{i + 1}</span>
                  <button type="button" onClick={() => handleRemoveNew(i)} className="text-red-400 hover:text-red-300 text-xs">Remove</button>
                </div>
              )}
              <ExperienceForm exp={exp} onChange={(f, v) => handleNewChange(i, f, v)} />
            </div>
          ))}
          <div className="flex gap-3">
            <button type="button" onClick={handleAddRow}
              className="px-4 py-2.5 border border-dashed border-cyan-500/50 text-cyan-400 hover:border-cyan-400 rounded-xl text-sm font-medium transition-all">
              + Add Another
            </button>
            <button type="submit" disabled={saving}
              className="flex-1 bg-gradient-to-r from-cyan-500 to-blue-600 hover:from-cyan-400 hover:to-blue-500 disabled:opacity-50 text-white font-semibold py-2.5 rounded-xl transition-all text-sm">
              {saving ? 'Saving…' : 'Save All'}
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default ExperienceSection;