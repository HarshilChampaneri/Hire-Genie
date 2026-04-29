import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../components/Navbar';
import ProfileSection from '../components/resume/ProfileSection';
import ProfileSummarySection from '../components/resume/ProfileSummarySection';
import ExperienceSection from '../components/resume/ExperienceSection';
import ProjectSection from '../components/resume/ProjectSection';
import EducationSection from '../components/resume/EducationSection';
import CertificateSection from '../components/resume/CertificateSection';
import { SkillsSection, OtherSection } from '../components/resume/SkillsAndOtherSection';
import { useAuth } from '../context/useAuth';
import { resumeService } from '../api/resumeService';

const tabs = [
  { id: 'profile',     label: 'Profile',     icon: '👤' },
  { id: 'summary',     label: 'Summary',     icon: '📝' },
  { id: 'experience',  label: 'Experience',  icon: '💼' },
  { id: 'projects',    label: 'Projects',    icon: '🚀' },
  { id: 'education',   label: 'Education',   icon: '🎓' },
  { id: 'certificates',label: 'Certificates',icon: '🏆' },
  { id: 'skills',      label: 'Skills',      icon: '⚡' },
  { id: 'other',       label: 'Other',       icon: '✨' },
];

const ResumeBuilder = () => {
  const { token } = useAuth();
  const navigate = useNavigate();
  const [activeTab, setActiveTab] = useState('profile');
  const [downloading, setDownloading] = useState(false);
  const [generating, setGenerating] = useState(false);
  const [downloadError, setDownloadError] = useState('');

  const handleDownload = async () => {
    setDownloading(true); setDownloadError('');
    try {
      const response = await resumeService.downloadResume(token);
      const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
      const a = document.createElement('a');
      a.href = url; a.download = 'Resume.pdf'; a.click();
      window.URL.revokeObjectURL(url);
    } catch { setDownloadError('Failed to download resume. Make sure all sections are filled in.'); }
    finally { setDownloading(false); }
  };

  const handleGenerate = async () => {
    setGenerating(true); setDownloadError('');
    try {
      const response = await resumeService.generateResumePdf(token);
      const url = window.URL.createObjectURL(new Blob([response.data], { type: 'application/pdf' }));
      const a = document.createElement('a');
      a.href = url; a.download = 'Resume.pdf'; a.click();
      window.URL.revokeObjectURL(url);
    } catch { setDownloadError('Failed to generate resume. Please ensure your profile is complete.'); }
    finally { setGenerating(false); }
  };

  const renderSection = () => {
    switch (activeTab) {
      case 'profile':      return <ProfileSection />;
      case 'summary':      return <ProfileSummarySection />;
      case 'experience':   return <ExperienceSection />;
      case 'projects':     return <ProjectSection />;
      case 'education':    return <EducationSection />;
      case 'certificates': return <CertificateSection />;
      case 'skills':       return <SkillsSection />;
      case 'other':        return <OtherSection />;
      default:             return null;
    }
  };

  return (
    <div className="min-h-screen bg-slate-900 text-white">
      <Navbar />

      {/* Header */}
      <div className="border-b border-slate-800 bg-slate-900/80 backdrop-blur sticky top-0 z-10">
        <div className="max-w-6xl mx-auto px-4 sm:px-6 py-4 flex flex-col sm:flex-row sm:items-center justify-between gap-3">
          <div>
            <h1 className="text-2xl font-bold bg-gradient-to-r from-cyan-400 to-blue-500 bg-clip-text text-transparent">
              Resume Builder
            </h1>
            <p className="text-slate-400 text-sm">Build your AI-powered resume step by step</p>
          </div>
          <div className="flex gap-2 flex-wrap">
            {downloadError && <p className="text-red-400 text-xs w-full">{downloadError}</p>}
            <button
              onClick={handleGenerate} disabled={generating}
              className="flex items-center gap-2 bg-violet-600 hover:bg-violet-500 disabled:opacity-50 text-white text-sm font-semibold px-4 py-2.5 rounded-xl transition-all"
            >
              {generating ? (
                <span className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
              ) : '⚡'}
              {generating ? 'Generating…' : 'Generate & Download'}
            </button>
            <button
              onClick={handleDownload} disabled={downloading}
              className="flex items-center gap-2 bg-slate-700 hover:bg-slate-600 disabled:opacity-50 border border-slate-600 text-white text-sm font-semibold px-4 py-2.5 rounded-xl transition-all"
            >
              {downloading ? (
                <span className="w-4 h-4 border-2 border-white border-t-transparent rounded-full animate-spin" />
              ) : '⬇️'}
              {downloading ? 'Downloading…' : 'Download PDF'}
            </button>
            <button
              onClick={() => navigate('/dashboard')}
              className="flex items-center gap-2 text-slate-400 hover:text-white text-sm px-3 py-2.5 rounded-xl transition-all"
            >
              ← Dashboard
            </button>
          </div>
        </div>

        {/* Tabs */}
        <div className="max-w-6xl mx-auto px-4 sm:px-6">
          <div className="flex gap-1 overflow-x-auto pb-0 scrollbar-hide">
            {tabs.map(tab => (
              <button
                key={tab.id}
                onClick={() => setActiveTab(tab.id)}
                className={`flex items-center gap-2 px-4 py-3 text-sm font-medium whitespace-nowrap border-b-2 transition-all
                  ${activeTab === tab.id
                    ? 'border-cyan-400 text-cyan-400'
                    : 'border-transparent text-slate-400 hover:text-slate-200 hover:border-slate-600'
                  }`}
              >
                <span>{tab.icon}</span>
                <span className="hidden sm:inline">{tab.label}</span>
              </button>
            ))}
          </div>
        </div>
      </div>

      {/* Content */}
      <div className="max-w-6xl mx-auto px-4 sm:px-6 py-8">
        <div className="bg-slate-800/60 backdrop-blur border border-slate-700/50 rounded-2xl p-6 sm:p-8 shadow-2xl">
          {renderSection()}
        </div>

        {/* Progress indicator */}
        <div className="mt-6 flex items-center justify-between">
          <button
            onClick={() => {
              const idx = tabs.findIndex(t => t.id === activeTab);
              if (idx > 0) setActiveTab(tabs[idx - 1].id);
            }}
            disabled={tabs.findIndex(t => t.id === activeTab) === 0}
            className="flex items-center gap-2 text-slate-400 hover:text-white disabled:opacity-30 text-sm font-medium transition-all"
          >
            ← Previous
          </button>

          <div className="flex gap-1.5">
            {tabs.map(tab => (
              <button key={tab.id} onClick={() => setActiveTab(tab.id)}
                className={`w-2 h-2 rounded-full transition-all ${activeTab === tab.id ? 'bg-cyan-400 w-6' : 'bg-slate-600 hover:bg-slate-500'}`} />
            ))}
          </div>

          <button
            onClick={() => {
              const idx = tabs.findIndex(t => t.id === activeTab);
              if (idx < tabs.length - 1) setActiveTab(tabs[idx + 1].id);
            }}
            disabled={tabs.findIndex(t => t.id === activeTab) === tabs.length - 1}
            className="flex items-center gap-2 text-slate-400 hover:text-white disabled:opacity-30 text-sm font-medium transition-all"
          >
            Next →
          </button>
        </div>
      </div>
    </div>
  );
};

export default ResumeBuilder;