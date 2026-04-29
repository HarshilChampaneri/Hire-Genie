import { useState, useEffect } from 'react';
import { resumeService } from '../../api/resumeService';
import { useAuth } from '../../context/useAuth';

const PlusIcon = () => (
  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
  </svg>
);
const TrashIcon = () => (
  <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
  </svg>
);

const ProfileSection = ({ onSaved }) => {
  const { token } = useAuth();
  const [exists, setExists] = useState(false);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [form, setForm] = useState({
    fullName: '', profession: '', email: '', mobileNo: '', urls: ['']
  });

  useEffect(() => {
    const fetchProfile = async () => {
        try {
          const { data } = await resumeService.getProfile(token);
          setForm({
            fullName: data.fullName || '',
            profession: data.profession || '',
            email: data.email || '',
            mobileNo: data.mobileNo || '',
            urls: data.urls?.length ? [...data.urls] : [''],
          });
          setExists(true);
        } catch {
          setExists(false);
        } finally {
          setLoading(false);
        }
    };

    fetchProfile();
  }, []);

  const handleChange = (field, value) => setForm(p => ({ ...p, [field]: value }));

  const handleUrlChange = (i, value) => {
    const urls = [...form.urls];
    urls[i] = value;
    setForm(p => ({ ...p, urls }));
  };

  const addUrl = () => setForm(p => ({ ...p, urls: [...p.urls, ''] }));
  const removeUrl = (i) => setForm(p => ({ ...p, urls: p.urls.filter((_, idx) => idx !== i) }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError(''); setSuccess('');
    setSaving(true);
    const payload = { ...form, urls: form.urls.filter(u => u.trim() !== '') };
    try {
      if (exists) {
        await resumeService.updateProfile(payload, token);
        setSuccess('Profile updated successfully!');
      } else {
        await resumeService.addProfile(payload, token);
        setExists(true);
        setSuccess('Profile created successfully!');
      }
      onSaved?.();
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to save profile.');
    } finally {
      setSaving(false);
    }
  };

  const handleDelete = async () => {
    if (!window.confirm('Delete your profile?')) return;
    try {
      await resumeService.deleteProfile(token);
      setExists(false);
      setForm({ fullName: '', profession: '', email: '', mobileNo: '', urls: [''] });
      setSuccess('Profile deleted.');
    } catch {
      setError('Failed to delete profile.');
    }
  };

  if (loading) return <div className="flex justify-center py-12"><div className="w-8 h-8 border-2 border-cyan-400 border-t-transparent rounded-full animate-spin" /></div>;

  return (
    <div>
      <div className="flex items-center justify-between mb-6">
        <div>
          <h3 className="text-xl font-bold text-white">Personal Profile</h3>
          <p className="text-slate-400 text-sm mt-1">Your basic contact info shown at the top of your resume</p>
        </div>
        {exists && (
          <button onClick={handleDelete} className="flex items-center gap-2 text-red-400 hover:text-red-300 text-sm border border-red-500/30 hover:border-red-400 px-3 py-1.5 rounded-lg transition-all">
            <TrashIcon /> Delete Profile
          </button>
        )}
      </div>

      {error && <div className="bg-red-500/10 border border-red-500/50 text-red-400 text-sm rounded-xl p-3 mb-4">{error}</div>}
      {success && <div className="bg-emerald-500/10 border border-emerald-500/50 text-emerald-400 text-sm rounded-xl p-3 mb-4">{success}</div>}

      <form onSubmit={handleSubmit} className="space-y-5">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          <div>
            <label className="block text-slate-400 text-xs font-semibold uppercase tracking-wider mb-2">Full Name *</label>
            <input
              required value={form.fullName}
              onChange={e => handleChange('fullName', e.target.value)}
              placeholder="e.g. Arjun Sharma"
              className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all"
            />
          </div>
          <div>
            <label className="block text-slate-400 text-xs font-semibold uppercase tracking-wider mb-2">Profession *</label>
            <input
              required value={form.profession}
              onChange={e => handleChange('profession', e.target.value)}
              placeholder="e.g. Full Stack Developer"
              className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all"
            />
          </div>
          <div>
            <label className="block text-slate-400 text-xs font-semibold uppercase tracking-wider mb-2">Email *</label>
            <input
              required type="email" value={form.email}
              onChange={e => handleChange('email', e.target.value)}
              placeholder="you@example.com"
              className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all"
            />
          </div>
          <div>
            <label className="block text-slate-400 text-xs font-semibold uppercase tracking-wider mb-2">Mobile No. *</label>
            <input
              required value={form.mobileNo} maxLength={10}
              onChange={e => handleChange('mobileNo', e.target.value.replace(/\D/, ''))}
              placeholder="10-digit mobile number"
              className="w-full bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all"
            />
          </div>
        </div>

        <div>
          <div className="flex items-center justify-between mb-2">
            <label className="text-slate-400 text-xs font-semibold uppercase tracking-wider">Profile URLs * <span className="text-slate-500 normal-case">(LinkedIn, GitHub, Portfolio…)</span></label>
            <button type="button" onClick={addUrl} className="flex items-center gap-1 text-cyan-400 hover:text-cyan-300 text-xs font-medium transition-colors">
              <PlusIcon /> Add URL
            </button>
          </div>
          <div className="space-y-2">
            {form.urls.map((url, i) => (
              <div key={i} className="flex gap-2">
                <input
                  value={url}
                  onChange={e => handleUrlChange(i, e.target.value)}
                  placeholder="https://linkedin.com/in/yourprofile"
                  className="flex-1 bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-3 text-white placeholder-slate-500 outline-none transition-all text-sm"
                />
                {form.urls.length > 1 && (
                  <button type="button" onClick={() => removeUrl(i)} className="text-slate-500 hover:text-red-400 transition-colors p-3">
                    <TrashIcon />
                  </button>
                )}
              </div>
            ))}
          </div>
        </div>

        <button
          type="submit" disabled={saving}
          className="w-full bg-gradient-to-r from-cyan-500 to-blue-600 hover:from-cyan-400 hover:to-blue-500 disabled:opacity-50 disabled:cursor-not-allowed text-white font-semibold py-3 rounded-xl transition-all shadow-lg shadow-cyan-500/20"
        >
          {saving ? 'Saving…' : exists ? 'Update Profile' : 'Save Profile'}
        </button>
      </form>
    </div>
  );
};

export default ProfileSection;