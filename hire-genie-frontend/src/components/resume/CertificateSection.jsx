import { useState, useEffect } from 'react';
import { resumeService } from '../../api/resumeService';
import { useAuth } from '../../context/useAuth';
import { SectionCard } from './resumeUtils';

const emptyCert = () => ({ certificateTitle: '', certificateUrl: '' });

const CertificateSection = () => {
  const { token } = useAuth();
  const [certificates, setCertificates] = useState([]);
  const [newCerts, setNewCerts] = useState([emptyCert()]);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [editingId, setEditingId] = useState(null);
  const [editForm, setEditForm] = useState(null);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const fetchCerts = async () => {
    try {
      const { data } = await resumeService.getAllCertificates(token);
      setCertificates(data.certificates || []);
    } catch { setCertificates([]); }
    finally { setLoading(false); }
  };

  useEffect(() => {
    
      const fetchCerts = async () => {
        try {
          const { data } = await resumeService.getAllCertificates(token);
          setCertificates(data.certificates || []);
        } catch { setCertificates([]); }
        finally { setLoading(false); }
      };

    fetchCerts(); }, []);

  const handleNewChange = (i, field, value) => {
    setNewCerts(prev => prev.map((c, idx) => idx === i ? { ...c, [field]: value } : c));
  };

  const handleSaveNew = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');
    setSaving(true);
    try {
      await resumeService.addCertificates(newCerts, token);
      setSuccess('Certificates saved!');
      setNewCerts([emptyCert()]);
      fetchCerts();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save certificates.');
    } finally { setSaving(false); }
  };

  const handleUpdate = async () => {
    setSaving(true); setError(''); setSuccess('');
    try {
      await resumeService.updateCertificate(editingId, editForm, token);
      setSuccess('Certificate updated!');
      setEditingId(null); setEditForm(null);
      fetchCerts();
    } catch { setError('Failed to update.'); }
    finally { setSaving(false); }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this certificate?')) return;
    try {
      await resumeService.deleteCertificate(id, token);
      setCertificates(prev => prev.filter(c => c.certificateId !== id));
    } catch { setError('Failed to delete.'); }
  };

  if (loading) return <div className="flex justify-center py-12"><div className="w-8 h-8 border-2 border-cyan-400 border-t-transparent rounded-full animate-spin" /></div>;

  return (
    <div>
      <div className="mb-6">
        <h3 className="text-xl font-bold text-white">Certifications</h3>
        <p className="text-slate-400 text-sm mt-1">Professional certificates and online course completions</p>
      </div>

      {error && <div className="bg-red-500/10 border border-red-500/50 text-red-400 text-sm rounded-xl p-3 mb-4">{error}</div>}
      {success && <div className="bg-emerald-500/10 border border-emerald-500/50 text-emerald-400 text-sm rounded-xl p-3 mb-4">{success}</div>}

      {certificates.length > 0 && (
        <div className="space-y-3 mb-8">
          <h4 className="text-slate-300 font-semibold text-sm uppercase tracking-wider">Saved Certificates</h4>
          {certificates.map(cert => (
            <SectionCard key={cert.certificateId} title={cert.certificateTitle} onDelete={() => handleDelete(cert.certificateId)}>
              {editingId === cert.certificateId ? (
                <div className="space-y-3 mt-3">
                  <input value={editForm.certificateTitle} onChange={e => setEditForm(p => ({ ...p, certificateTitle: e.target.value }))}
                    placeholder="Certificate title"
                    className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-2.5 text-white placeholder-slate-500 outline-none transition-all text-sm" />
                  <input value={editForm.certificateUrl} onChange={e => setEditForm(p => ({ ...p, certificateUrl: e.target.value }))}
                    placeholder="Certificate URL"
                    className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-2.5 text-white placeholder-slate-500 outline-none transition-all text-sm" />
                  <div className="flex gap-2">
                    <button onClick={handleUpdate} disabled={saving}
                      className="flex-1 bg-gradient-to-r from-cyan-500 to-blue-600 text-white font-semibold py-2 rounded-xl text-sm disabled:opacity-50">
                      {saving ? 'Saving…' : 'Save'}
                    </button>
                    <button onClick={() => setEditingId(null)} className="px-4 py-2 border border-slate-600 text-slate-400 hover:text-white rounded-xl text-sm">Cancel</button>
                  </div>
                </div>
              ) : (
                <div className="mt-1">
                  <a href={cert.certificateUrl} target="_blank" rel="noopener noreferrer"
                    className="text-blue-400 hover:text-blue-300 text-sm transition-colors">🔗 {cert.certificateUrl}</a>
                  <br />
                  <button onClick={() => { setEditingId(cert.certificateId); setEditForm({ ...cert }); }}
                    className="mt-2 text-cyan-400 hover:text-cyan-300 text-sm font-medium transition-colors">Edit →</button>
                </div>
              )}
            </SectionCard>
          ))}
        </div>
      )}

      <div className="border border-dashed border-slate-600 rounded-2xl p-5">
        <h4 className="text-slate-300 font-semibold text-sm uppercase tracking-wider mb-4">
          {certificates.length === 0 ? 'Add Certificate' : '+ Add More'}
        </h4>
        <form onSubmit={handleSaveNew} className="space-y-4">
          {newCerts.map((cert, i) => (
            <div key={i} className="space-y-3">
              {i > 0 && <div className="border-t border-slate-700 pt-4" />}
              <div className="flex items-center justify-between">
                <span className="text-slate-500 text-xs">Certificate #{i + 1}</span>
                {newCerts.length > 1 && (
                  <button type="button" onClick={() => setNewCerts(p => p.filter((_, idx) => idx !== i))} className="text-red-400 text-xs">Remove</button>
                )}
              </div>
              <input value={cert.certificateTitle} onChange={e => handleNewChange(i, 'certificateTitle', e.target.value)}
                placeholder="e.g. AWS Certified Solutions Architect"
                className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all" />
              <input value={cert.certificateUrl} onChange={e => handleNewChange(i, 'certificateUrl', e.target.value)}
                placeholder="https://certificate-link.com"
                className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all" />
            </div>
          ))}
          <div className="flex gap-3">
            <button type="button" onClick={() => setNewCerts(p => [...p, emptyCert()])}
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

export default CertificateSection;