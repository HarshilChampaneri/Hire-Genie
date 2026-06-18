import { useState, useEffect } from 'react';
import { useAuth } from '../context/useAuth';

const Section = ({ title, icon, children }) => (
  <div className="mb-6">
    <h3 className="text-xs font-bold uppercase tracking-widest text-slate-400 mb-3 flex items-center gap-2">
      <span>{icon}</span> {title}
    </h3>
    {children}
  </div>
);

const Tag = ({ children }) => (
  <span className="text-xs px-2 py-1 bg-slate-700 text-slate-300 rounded-full border border-slate-600">
    {children}
  </span>
);

const formatYearMonth = (ym) => {
  if (!ym) return null;
  const [year, month] = String(ym).split('-');
  const months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
  return `${months[parseInt(month, 10) - 1]} ${year}`;
};

const ProfileModal = ({ onClose }) => {
  const { token } = useAuth();
  const [profile, setProfile] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const base = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';
        const res = await fetch(`${base}/api/resumes/get-my-profile`, {
          headers: { Authorization: `Bearer ${token}` },
        });
        if (!res.ok) throw new Error(`Server returned ${res.status}`);
        setProfile(await res.json());
      } catch (err) {
        setError(err.message);
      } finally {
        setLoading(false);
      }
    };
    fetchProfile();
  }, [token]);

  const handleBackdrop = (e) => { if (e.target === e.currentTarget) onClose(); };

  const initials = profile?.profile?.fullName
    ?.split(' ').map(n => n[0]).slice(0, 2).join('').toUpperCase() ?? '?';

  const isEmpty =
    !profile?.summary?.profileSummary &&
    !profile?.experiences?.experiences?.length &&
    !profile?.projects?.projects?.length &&
    !Object.keys(profile?.skills?.technicalSkills ?? {}).length &&
    !profile?.educations?.educations?.length &&
    !profile?.certificates?.certificates?.length &&
    !profile?.others?.description?.length;

  return (
    <div
      className="fixed inset-0 z-50 flex justify-end bg-black/50 backdrop-blur-sm"
      onClick={handleBackdrop}
    >
      <div className="w-full max-w-lg h-full bg-slate-900 border-l border-slate-700 overflow-y-auto shadow-2xl flex flex-col animate-slide-in">

        {/* Sticky header */}
        <div className="sticky top-0 bg-slate-900 border-b border-slate-700 px-6 py-4 flex items-center justify-between z-10">
          <h2 className="text-white font-bold text-lg">My Profile</h2>
          <button
            onClick={onClose}
            className="w-8 h-8 flex items-center justify-center rounded-lg text-slate-400 hover:text-white hover:bg-slate-700 transition-all"
          >
            ✕
          </button>
        </div>

        <div className="flex-1 p-6">

          {/* Loading */}
          {loading && (
            <div className="flex flex-col items-center justify-center h-64 gap-3">
              <div className="w-8 h-8 border-2 border-blue-500 border-t-transparent rounded-full animate-spin" />
              <p className="text-slate-400 text-sm">Loading your profile…</p>
            </div>
          )}

          {/* Error */}
          {error && (
            <div className="flex flex-col items-center justify-center h-64 gap-3 text-center">
              <span className="text-3xl">⚠️</span>
              <p className="text-slate-300 font-medium">Couldn't load profile</p>
              <p className="text-slate-500 text-sm">{error}</p>
            </div>
          )}

          {/* Profile content */}
          {profile && (
            <>
              {/* Identity card */}
              <div className="flex items-center gap-4 mb-8 pb-6 border-b border-slate-700">
                <div className="w-16 h-16 rounded-full bg-blue-600/20 border-2 border-blue-500/40 flex items-center justify-center text-blue-400 font-bold text-xl shrink-0">
                  {initials}
                </div>
                <div className="min-w-0">
                  <h3 className="text-white font-bold text-xl truncate">{profile.profile?.fullName || '—'}</h3>
                  <p className="text-blue-400 text-sm font-medium">{profile.profile?.profession || '—'}</p>
                  <p className="text-slate-400 text-sm mt-0.5">{profile.profile?.email || '—'}</p>
                  {profile.profile?.mobileNo && (
                    <p className="text-slate-400 text-sm">{profile.profile.mobileNo}</p>
                  )}
                  {[...(profile.profile?.urls ?? [])].length > 0 && (
                    <div className="flex flex-wrap gap-2 mt-2">
                      {[...profile.profile.urls].map((url, i) => (
                        <a key={i} href={url} target="_blank" rel="noopener noreferrer"
                          className="text-xs text-blue-400 hover:text-blue-300 underline underline-offset-2 truncate max-w-55">
                          {url}
                        </a>
                      ))}
                    </div>
                  )}
                </div>
              </div>

              {/* Summary */}
              {profile.summary?.profileSummary && (
                <Section title="Summary" icon="📝">
                  <p className="text-slate-300 text-sm leading-relaxed">{profile.summary.profileSummary}</p>
                </Section>
              )}

              {/* Experience */}
              {profile.experiences?.experiences?.length > 0 && (
                <Section title="Experience" icon="💼">
                  <div className="flex flex-col gap-3">
                    {profile.experiences.experiences.map((exp) => (
                      <div key={exp.experienceId} className="bg-slate-800 border border-slate-700 rounded-xl p-4">
                        <div className="flex items-start justify-between gap-2 mb-1">
                          <p className="text-white font-semibold text-sm">{exp.position}</p>
                          <span className="text-slate-500 text-xs shrink-0">
                            {formatYearMonth(exp.startDate)} — {exp.isWorkingInCompany ? 'Present' : formatYearMonth(exp.endDate)}
                          </span>
                        </div>
                        <p className="text-blue-400 text-xs mb-2">{exp.companyName}</p>
                        {exp.description?.length > 0 && (
                          <ul className="flex flex-col gap-1">
                            {exp.description.map((d, i) => (
                              <li key={i} className="text-slate-400 text-xs leading-relaxed flex gap-2">
                                <span className="text-slate-600 shrink-0 mt-0.5">•</span>{d}
                              </li>
                            ))}
                          </ul>
                        )}
                      </div>
                    ))}
                  </div>
                </Section>
              )}

              {/* Projects */}
              {profile.projects?.projects?.length > 0 && (
                <Section title="Projects" icon="🚀">
                  <div className="flex flex-col gap-3">
                    {profile.projects.projects.map((proj) => (
                      <div key={proj.projectId} className="bg-slate-800 border border-slate-700 rounded-xl p-4">
                        <div className="flex items-start justify-between gap-2 mb-1">
                          <p className="text-white font-semibold text-sm">{proj.projectName}</p>
                          <span className="text-slate-500 text-xs shrink-0">
                            {formatYearMonth(proj.projectStartDate)} — {proj.isProjectInProgress ? 'Present' : formatYearMonth(proj.projectEndDate)}
                          </span>
                        </div>
                        {proj.projectUrl && (
                          <a href={proj.projectUrl} target="_blank" rel="noopener noreferrer"
                            className="text-blue-400 text-xs hover:text-blue-300 underline underline-offset-2 block mb-2 truncate">
                            {proj.projectUrl}
                          </a>
                        )}
                        {proj.projectTechStacks?.length > 0 && (
                          <div className="flex flex-wrap gap-1.5 mb-2">
                            {proj.projectTechStacks.map((t, i) => <Tag key={i}>{t}</Tag>)}
                          </div>
                        )}
                        {proj.projectDescription?.length > 0 && (
                          <ul className="flex flex-col gap-1">
                            {proj.projectDescription.map((d, i) => (
                              <li key={i} className="text-slate-400 text-xs leading-relaxed flex gap-2">
                                <span className="text-slate-600 shrink-0 mt-0.5">•</span>{d}
                              </li>
                            ))}
                          </ul>
                        )}
                      </div>
                    ))}
                  </div>
                </Section>
              )}

              {/* Skills */}
              {Object.keys(profile.skills?.technicalSkills ?? {}).length > 0 && (
                <Section title="Skills" icon="⚡">
                  <div className="flex flex-col gap-3">
                    {Object.entries(profile.skills.technicalSkills).map(([category, items]) => (
                      <div key={category}>
                        <p className="text-slate-400 text-xs font-semibold mb-1.5">{category}</p>
                        <div className="flex flex-wrap gap-1.5">
                          {items.map((item, i) => <Tag key={i}>{item}</Tag>)}
                        </div>
                      </div>
                    ))}
                  </div>
                </Section>
              )}

              {/* Education */}
              {profile.educations?.educations?.length > 0 && (
                <Section title="Education" icon="🎓">
                  <div className="flex flex-col gap-3">
                    {profile.educations.educations.map((edu) => (
                      <div key={edu.educationId} className="bg-slate-800 border border-slate-700 rounded-xl p-4">
                        <div className="flex items-start justify-between gap-2 mb-1">
                          <p className="text-white font-semibold text-sm">{edu.educationTitle}</p>
                          <span className="text-slate-500 text-xs shrink-0">
                            {formatYearMonth(edu.startDate)} — {edu.isEducationInProgress ? 'Present' : formatYearMonth(edu.endDate)}
                          </span>
                        </div>
                        <p className="text-blue-400 text-xs">{edu.fieldOfStudy}</p>
                        {edu.location && <p className="text-slate-500 text-xs">{edu.location}</p>}
                        {edu.grades != null && (
                          <p className="text-slate-400 text-xs mt-1">{edu.gradeTitle}: {edu.grades}</p>
                        )}
                      </div>
                    ))}
                  </div>
                </Section>
              )}

              {/* Certificates */}
              {profile.certificates?.certificates?.length > 0 && (
                <Section title="Certificates" icon="🏅">
                  <div className="flex flex-col gap-2">
                    {profile.certificates.certificates.map((cert) => (
                      <div key={cert.certificateId}
                        className="flex items-center justify-between bg-slate-800 border border-slate-700 rounded-xl px-4 py-3">
                        <p className="text-slate-300 text-sm">{cert.certificateTitle}</p>
                        {cert.certificateUrl && (
                          <a href={cert.certificateUrl} target="_blank" rel="noopener noreferrer"
                            className="text-blue-400 text-xs hover:text-blue-300 underline underline-offset-2 shrink-0 ml-3">
                            View →
                          </a>
                        )}
                      </div>
                    ))}
                  </div>
                </Section>
              )}

              {/* Others */}
              {profile.others?.description?.length > 0 && (
                <Section title="Others" icon="📌">
                  <ul className="flex flex-col gap-1">
                    {profile.others.description.map((d, i) => (
                      <li key={i} className="text-slate-400 text-sm leading-relaxed flex gap-2">
                        <span className="text-slate-600 shrink-0 mt-0.5">•</span>{d}
                      </li>
                    ))}
                  </ul>
                </Section>
              )}

              {/* Empty state */}
              {isEmpty && (
                <div className="text-center py-16">
                  <p className="text-3xl mb-3">📄</p>
                  <p className="text-slate-400 text-sm">Your profile is empty.</p>
                  <p className="text-slate-500 text-xs mt-1">Head to Resume Builder to fill it out!</p>
                </div>
              )}
            </>
          )}
        </div>
      </div>
    </div>
  );
};

export default ProfileModal;