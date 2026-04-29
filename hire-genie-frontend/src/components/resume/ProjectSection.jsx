import { useState, useEffect } from 'react';
import { resumeService } from '../../api/resumeService';
import { useAuth } from '../../context/useAuth';
import { YearMonthPicker, DynamicListInput, SectionCard, AIButton } from './resumeUtils';

const emptyProject = () => ({
  projectName: '', projectUrl: '', projectTechStacks: [''],
  projectStartDate: '', isProjectInProgress: false, projectEndDate: '',
  projectDescription: ['']
});

const ProjectForm = ({ proj, onChange, onAIRewrite, aiLoading, existingId }) => (
  <div className="space-y-4">
    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <div>
        <label className="block text-slate-400 text-xs font-semibold uppercase tracking-wider mb-2">Project Name *</label>
        <input value={proj.projectName} onChange={e => onChange('projectName', e.target.value)} placeholder="e.g. Hire-Genie Platform"
          className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all" />
      </div>
      <div>
        <label className="block text-slate-400 text-xs font-semibold uppercase tracking-wider mb-2">Project URL *</label>
        <input value={proj.projectUrl} onChange={e => onChange('projectUrl', e.target.value)} placeholder="https://github.com/you/project"
          className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all" />
      </div>
    </div>

    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
      <YearMonthPicker label="Start Date" value={proj.projectStartDate} onChange={v => onChange('projectStartDate', v)} required />
      <div>
        <YearMonthPicker label="End Date" value={proj.projectEndDate} onChange={v => onChange('projectEndDate', v)} disabled={proj.isProjectInProgress} />
        <label className="flex items-center gap-2 mt-2 cursor-pointer">
          <input type="checkbox" checked={proj.isProjectInProgress} onChange={e => onChange('isProjectInProgress', e.target.checked)}
            className="rounded border-slate-600 bg-slate-700 text-cyan-500" />
          <span className="text-slate-400 text-sm">Project in progress</span>
        </label>
      </div>
    </div>

    <DynamicListInput label="Tech Stack *" items={proj.projectTechStacks} onChange={v => onChange('projectTechStacks', v)} placeholder="e.g. React, Spring Boot, PostgreSQL" />

    <div>
      <div className="flex items-center justify-between mb-1">
        <span className="text-slate-400 text-xs font-semibold uppercase tracking-wider">Description *</span>
        {existingId && <AIButton onClick={() => onAIRewrite(existingId)} loading={aiLoading} />}
      </div>
      <DynamicListInput items={proj.projectDescription} onChange={v => onChange('projectDescription', v)} placeholder="Describe what you built and your impact…" />
    </div>
  </div>
);

const ProjectSection = () => {
  const { token } = useAuth();
  const [projects, setProjects] = useState([]);
  const [newProjects, setNewProjects] = useState([emptyProject()]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [aiLoadingId, setAiLoadingId] = useState(null);
  const [editingId, setEditingId] = useState(null);
  const [editForm, setEditForm] = useState(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

    const fetchProjects = async () => {
      try {
        const { data } = await resumeService.getAllProjects(token);
        setProjects(data.projects || []);
      } catch { setProjects([]); }
      finally { setLoading(false); }
    };

  useEffect(() => {
    fetchProjects(); }, []);

  const handleNewChange = (i, field, value) => {
    setNewProjects(prev => prev.map((p, idx) => idx === i ? { ...p, [field]: value } : p));
  };

  const handleSaveNew = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');
    setSaving(true);
    try {
      const payload = newProjects.map(p => ({
        ...p,
        projectEndDate: p.isProjectInProgress ? undefined : p.projectEndDate || undefined,
      }));
      await resumeService.addProjects(payload, token);
      setSuccess('Projects saved!');
      setNewProjects([emptyProject()]);
      fetchProjects();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save projects.');
    } finally { setSaving(false); }
  };

  const startEdit = (proj) => {
    setEditingId(proj.projectId);
    setEditForm({ ...proj, projectTechStacks: proj.projectTechStacks || [''], projectDescription: proj.projectDescription || [''] });
  };

  const handleUpdate = async () => {
    setSaving(true); setError(''); setSuccess('');
    try {
      const { projectId, ...payload } = editForm;

      await resumeService.updateProject(editingId, {
        ...payload,
        projectEndDate: payload.isProjectInProgress ? undefined : payload.projectEndDate || undefined,
      }, token);
      setSuccess('Project updated!');
      setEditingId(null); setEditForm(null);
      fetchProjects();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to update.');
    } finally { setSaving(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this project?')) return;
    try {
      await resumeService.deleteProject(id, token);
      setProjects(prev => prev.filter(p => p.projectId !== id));
    } catch { setError('Failed to delete.'); }
  };

  const handleAIRewrite = async (id) => {
    setAiLoadingId(id);
    try {
      const { data } = await resumeService.rewriteProjectDescAI(id, token);
      if (editingId === id) setEditForm(prev => ({ ...prev, projectDescription: data.projectDescription }));
      setSuccess('✨ AI rewrote the description!');
      fetchProjects();
    } catch { setError('AI rewrite failed.'); }
    finally { setAiLoadingId(null); }
  };

  if (loading) return <div className="flex justify-center py-12"><div className="w-8 h-8 border-2 border-cyan-400 border-t-transparent rounded-full animate-spin" /></div>;

  return (
    <div>
      <div className="mb-6">
        <h3 className="text-xl font-bold text-white">Projects</h3>
        <p className="text-slate-400 text-sm mt-1">Showcase your personal and professional projects</p>
      </div>

      {error && <div className="bg-red-500/10 border border-red-500/50 text-red-400 text-sm rounded-xl p-3 mb-4">{error}</div>}
      {success && <div className="bg-emerald-500/10 border border-emerald-500/50 text-emerald-400 text-sm rounded-xl p-3 mb-4">{success}</div>}

      {projects.length > 0 && (
        <div className="space-y-4 mb-8">
          <h4 className="text-slate-300 font-semibold text-sm uppercase tracking-wider">Saved Projects</h4>
          {projects.map(proj => (
            <SectionCard key={proj.projectId} title={proj.projectName}
              subtitle={`${proj.projectStartDate} → ${proj.isProjectInProgress ? 'In Progress' : proj.projectEndDate}`}
              onDelete={() => handleDelete(proj.projectId)}>
              {editingId === proj.projectId ? (
                <div className="mt-4 space-y-4">
                  <ProjectForm proj={editForm} onChange={(f, v) => setEditForm(p => ({ ...p, [f]: v }))}
                    onAIRewrite={handleAIRewrite} aiLoading={aiLoadingId === proj.projectId} existingId={proj.projectId} />
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
                  <div className="flex flex-wrap gap-2 mb-2">
                    {proj.projectTechStacks?.map((t, i) => (
                      <span key={i} className="bg-cyan-500/10 border border-cyan-500/30 text-cyan-400 text-xs px-2 py-0.5 rounded-full">{t}</span>
                    ))}
                  </div>
                  <ul className="space-y-1">
                    {proj.projectDescription?.map((d, i) => (
                      <li key={i} className="text-slate-400 text-sm flex gap-2"><span className="text-cyan-400 mt-0.5">▸</span>{d}</li>
                    ))}
                  </ul>
                  <a href={proj.projectUrl} target="_blank" rel="noopener noreferrer" className="text-blue-400 hover:text-blue-300 text-xs mt-2 inline-block transition-colors">
                    🔗 {proj.projectUrl}
                  </a>
                  <br />
                  <button onClick={() => startEdit(proj)} className="mt-2 text-cyan-400 hover:text-cyan-300 text-sm font-medium transition-colors">Edit →</button>
                </div>
              )}
            </SectionCard>
          ))}
        </div>
      )}

      <div className="border border-dashed border-slate-600 rounded-2xl p-5">
        <h4 className="text-slate-300 font-semibold text-sm uppercase tracking-wider mb-4">
          {projects.length === 0 ? 'Add Project' : '+ Add More'}
        </h4>
        <form onSubmit={handleSaveNew} className="space-y-6">
          {newProjects.map((proj, i) => (
            <div key={i}>
              {i > 0 && <div className="border-t border-slate-700 pt-6" />}
              {newProjects.length > 1 && (
                <div className="flex justify-between items-center mb-4">
                  <span className="text-slate-400 text-sm">Project #{i + 1}</span>
                  <button type="button" onClick={() => setNewProjects(p => p.filter((_, idx) => idx !== i))} className="text-red-400 text-xs">Remove</button>
                </div>
              )}
              <ProjectForm proj={proj} onChange={(f, v) => handleNewChange(i, f, v)} />
            </div>
          ))}
          <div className="flex gap-3">
            <button type="button" onClick={() => setNewProjects(p => [...p, emptyProject()])}
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

export default ProjectSection;