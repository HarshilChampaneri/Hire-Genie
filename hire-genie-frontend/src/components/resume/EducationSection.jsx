import { useState, useEffect } from 'react';
import { resumeService } from '../../api/resumeService';
import { useAuth } from '../../context/useAuth';
import { YearMonthPicker, SectionCard } from './resumeUtils';

const emptyEdu = () => ({
  educationTitle: '', location: '', fieldOfStudy: '',
  startDate: '', isEducationInProgress: false, endDate: '',
  gradeTitle: '', grades: ''
});

const EducationForm = ({ edu, onChange }) => (
  <div className="space-y-4">
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div>
        <label className="block text-slate-400 text-xs font-semibold uppercase tracking-wider mb-2">Institution Name *</label>
        <input value={edu.educationTitle} onChange={e => onChange('educationTitle', e.target.value)} placeholder="e.g. IIT Bombay"
          className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all" />
      </div>
      <div>
        <label className="block text-slate-400 text-xs font-semibold uppercase tracking-wider mb-2">Location *</label>
        <input value={edu.location} onChange={e => onChange('location', e.target.value)} placeholder="e.g. Mumbai, India"
          className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all" />
      </div>
      <div>
        <label className="block text-slate-400 text-xs font-semibold uppercase tracking-wider mb-2">Field of Study *</label>
        <input value={edu.fieldOfStudy} onChange={e => onChange('fieldOfStudy', e.target.value)} placeholder="e.g. Computer Science & Engineering"
          className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all" />
      </div>
      <div className="grid grid-cols-2 gap-2">
        <div>
          <label className="block text-slate-400 text-xs font-semibold uppercase tracking-wider mb-2">Grade Title</label>
          <input value={edu.gradeTitle} onChange={e => onChange('gradeTitle', e.target.value)} placeholder="CGPA / %"
            className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all" />
        </div>
        <div>
          <label className="block text-slate-400 text-xs font-semibold uppercase tracking-wider mb-2">Grade Value</label>
          <input type="number" step="0.01" value={edu.grades} onChange={e => onChange('grades', parseFloat(e.target.value) || '')} placeholder="e.g. 8.5"
            className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all" />
        </div>
      </div>
    </div>

    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <YearMonthPicker label="Start Date" value={edu.startDate} onChange={v => onChange('startDate', v)} required />
      <div>
        <YearMonthPicker label="End Date" value={edu.endDate} onChange={v => onChange('endDate', v)} disabled={edu.isEducationInProgress} />
        <label className="flex items-center gap-2 mt-2 cursor-pointer">
          <input type="checkbox" checked={edu.isEducationInProgress} onChange={e => onChange('isEducationInProgress', e.target.checked)}
            className="rounded border-slate-600 bg-slate-700 text-cyan-500" />
          <span className="text-slate-400 text-sm">Currently studying</span>
        </label>
      </div>
    </div>
  </div>
);

const EducationSection = () => {
  const { token } = useAuth();
  const [educations, setEducations] = useState([]);
  const [newEdus, setNewEdus] = useState([emptyEdu()]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [editForm, setEditForm] = useState(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const fetchEducations = async () => {
    try {
      const { data } = await resumeService.getAllEducations(token);
      setEducations(data.educations || []);
    } catch { setEducations([]); }
    finally { setLoading(false); }
  };

  useEffect(() => {
    fetchEducations(); }, []);

  const handleNewChange = (i, field, value) => {
    setNewEdus(prev => prev.map((e, idx) => idx === i ? { ...e, [field]: value } : e));
  };

  const handleSaveNew = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');
    setSaving(true);
    try {
      const payload = newEdus.map(edu => ({
        ...edu,
        grades: edu.grades !== '' ? Number(edu.grades) : undefined,
        gradeTitle: edu.gradeTitle || undefined,
        endDate: edu.isEducationInProgress ? undefined : edu.endDate || undefined,
      }));
      await resumeService.addEducations(payload, token);
      setSuccess('Education saved!');
      setNewEdus([emptyEdu()]);
      fetchEducations();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save education.');
    } finally { setSaving(false); }
  };

  const startEdit = (edu) => {
    setEditingId(edu.educationId);
    setEditForm({ ...edu });
  };

  const handleUpdate = async () => {
    setSaving(true); setError(''); setSuccess('');
    try {
      const { educationId, ...rest } = editForm;
      await resumeService.updateEducation(editingId, {
        ...rest,
        grades: rest.grades !== '' ? Number(rest.grades) : undefined,
        endDate: rest.isEducationInProgress ? undefined : rest.endDate || undefined,
      }, token);
      setSuccess('Education updated!');
      setEditingId(null); setEditForm(null);
      fetchEducations();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update.');
    } finally { setSaving(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this education entry?')) return;
    try {
      await resumeService.deleteEducation(id, token);
      setEducations(prev => prev.filter(e => e.educationId !== id));
    } catch { setError('Failed to delete.'); }
  };

  if (loading) return <div className="flex justify-center py-12"><div className="w-8 h-8 border-2 border-cyan-400 border-t-transparent rounded-full animate-spin" /></div>;

  return (
    <div>
      <div className="mb-6">
        <h3 className="text-xl font-bold text-white">Education</h3>
        <p className="text-slate-400 text-sm mt-1">Your academic qualifications and achievements</p>
      </div>

      {error && <div className="bg-red-500/10 border border-red-500/50 text-red-400 text-sm rounded-xl p-3 mb-4">{error}</div>}
      {success && <div className="bg-emerald-500/10 border border-emerald-500/50 text-emerald-400 text-sm rounded-xl p-3 mb-4">{success}</div>}

      {educations.length > 0 && (
        <div className="space-y-4 mb-8">
          <h4 className="text-slate-300 font-semibold text-sm uppercase tracking-wider">Saved Education</h4>
          {educations.map(edu => (
            <SectionCard key={edu.educationId} title={edu.educationTitle}
              subtitle={`${edu.fieldOfStudy} · ${edu.location} · ${edu.startDate} → ${edu.isEducationInProgress ? 'Present' : edu.endDate}`}
              onDelete={() => handleDelete(edu.educationId)}>
              {editingId === edu.educationId ? (
                <div className="mt-4 space-y-4">
                  <EducationForm edu={editForm} onChange={(f, v) => setEditForm(p => ({ ...p, [f]: v }))} />
                  <div className="flex gap-3">
                    <button onClick={handleUpdate} disabled={saving}
                      className="flex-1 bg-gradient-to-r from-cyan-500 to-blue-600 hover:from-cyan-400 hover:to-blue-500 disabled:opacity-50 text-white font-semibold py-2.5 rounded-xl transition-all text-sm">
                      {saving ? 'Saving…' : 'Save Changes'}
                    </button>
                    <button onClick={() => setEditingId(null)} className="px-4 py-2.5 border border-slate-600 text-slate-400 hover:text-white rounded-xl text-sm transition-all">Cancel</button>
                  </div>
                </div>
              ) : (
                <div className="mt-2">
                  {edu.grades && <span className="text-slate-400 text-sm">{edu.gradeTitle}: <span className="text-cyan-400 font-medium">{edu.grades}</span></span>}
                  <br />
                  <button onClick={() => startEdit(edu)} className="mt-2 text-cyan-400 hover:text-cyan-300 text-sm font-medium transition-colors">Edit →</button>
                </div>
              )}
            </SectionCard>
          ))}
        </div>
      )}

      <div className="border border-dashed border-slate-600 rounded-2xl p-5">
        <h4 className="text-slate-300 font-semibold text-sm uppercase tracking-wider mb-4">
          {educations.length === 0 ? 'Add Education' : '+ Add More'}
        </h4>
        <form onSubmit={handleSaveNew} className="space-y-6">
          {newEdus.map((edu, i) => (
            <div key={i}>
              {i > 0 && <div className="border-t border-slate-700 pt-6" />}
              {newEdus.length > 1 && (
                <div className="flex justify-between items-center mb-4">
                  <span className="text-slate-400 text-sm">Education #{i + 1}</span>
                  <button type="button" onClick={() => setNewEdus(p => p.filter((_, idx) => idx !== i))} className="text-red-400 text-xs">Remove</button>
                </div>
              )}
              <EducationForm edu={edu} onChange={(f, v) => handleNewChange(i, f, v)} />
            </div>
          ))}
          <div className="flex gap-3">
            <button type="button" onClick={() => setNewEdus(p => [...p, emptyEdu()])}
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

export default EducationSection;