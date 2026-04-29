import axios from 'axios';

const BASE = '/api/resumes';

const authHeader = (token) => ({ headers: { Authorization: `Bearer ${token}` } });

export const resumeService = {
  // Profile
  addProfile: (data, token) => axios.post(`${BASE}/profile`, data, authHeader(token)),
  getProfile: (token) => axios.get(`${BASE}/profile`, authHeader(token)),
  updateProfile: (data, token) => axios.put(`${BASE}/profile`, data, authHeader(token)),
  deleteProfile: (token) => axios.delete(`${BASE}/profile`, authHeader(token)),

  // Profile Summary
  addProfileSummary: (data, token) => axios.post(`${BASE}/profile-summary`, data, authHeader(token)),
  getProfileSummary: (token) => axios.get(`${BASE}/profile-summary`, authHeader(token)),
  updateProfileSummary: (data, token) => axios.put(`${BASE}/profile-summary`, data, authHeader(token)),
  deleteProfileSummary: (token) => axios.delete(`${BASE}/profile-summary`, authHeader(token)),

  // Experience
  addExperiences: (data, token) => axios.post(`${BASE}/experiences`, { experienceRequests: data }, authHeader(token)),
  getAllExperiences: (token) => axios.get(`${BASE}/experiences`, authHeader(token)),
  updateExperience: (id, data, token) => axios.put(`${BASE}/experiences/${id}`, data, authHeader(token)),
  deleteExperience: (id, token) => axios.delete(`${BASE}/experiences/${id}`, authHeader(token)),

  // Projects
  addProjects: (data, token) => axios.post(`${BASE}/projects`, { projectRequests: data }, authHeader(token)),
  getAllProjects: (token) => axios.get(`${BASE}/projects`, authHeader(token)),
  updateProject: (id, data, token) => axios.put(`${BASE}/projects/${id}`, data, authHeader(token)),
  deleteProject: (id, token) => axios.delete(`${BASE}/projects/${id}`, authHeader(token)),

  // Education
  addEducations: (data, token) => axios.post(`${BASE}/educations`, { educationRequests: data }, authHeader(token)),
  getAllEducations: (token) => axios.get(`${BASE}/educations`, authHeader(token)),
  updateEducation: (id, data, token) => axios.put(`${BASE}/educations/${id}`, data, authHeader(token)),
  deleteEducation: (id, token) => axios.delete(`${BASE}/educations/${id}`, authHeader(token)),

  // Certificates
  addCertificates: (data, token) => axios.post(`${BASE}/certificates`, { certificateRequests: data }, authHeader(token)),
  getAllCertificates: (token) => axios.get(`${BASE}/certificates`, authHeader(token)),
  updateCertificate: (id, data, token) => axios.put(`${BASE}/certificates/${id}`, data, authHeader(token)),
  deleteCertificate: (id, token) => axios.delete(`${BASE}/certificates/${id}`, authHeader(token)),

  // Other
  addOther: (data, token) => axios.post(`${BASE}/others`, data, authHeader(token)),
  getOther: (token) => axios.get(`${BASE}/others`, authHeader(token)),
  updateOther: (data, token) => axios.put(`${BASE}/others`, data, authHeader(token)),
  deleteOther: (token) => axios.delete(`${BASE}/others`, authHeader(token)),

  // Skills (read + AI generate)
  getSkills: (token) => axios.get(`${BASE}/get-skill`, authHeader(token)),

  // Resume
  getMyProfile: (token) => axios.get(`${BASE}/get-my-profile`, authHeader(token)),
  downloadResume: (token) => axios.get(`${BASE}/download`, { ...authHeader(token), responseType: 'blob' }),
  generateResumePdf: (token) => axios.post(`${BASE}/generate-pdf`, {}, { ...authHeader(token), responseType: 'blob' }),

  // AI
  generateSkillSummary: (text, token) =>
    axios.put(`${BASE}/provide/skills/summary${text ? `?text=${encodeURIComponent(text)}` : ''}`, {}, authHeader(token)),
  rewriteProjectDescAI: (id, token) => axios.put(`${BASE}/rewirte-pd-with-ai/${id}`, {}, authHeader(token)),
  rewriteExperienceDescAI: (id, token) => axios.put(`${BASE}/rewrite-experiences-description-with-ai/${id}`, {}, authHeader(token)),
  rewriteOtherDescAI: (id, token) => axios.put(`${BASE}/rewrite-other-section-description-with-ai/${id}`, {}, authHeader(token)),
  rewriteProfileSummaryAI: (token) => axios.put(`${BASE}/rewrite-profile-summary-with-ai`, {}, authHeader(token)),
  generateProfileSummaryAI: (token) => axios.post(`${BASE}/generate-profile-summary-with-ai`, {}, authHeader(token)),

  // Job Recommendations
  getRecommendedJobs: (token) => axios.get(`${BASE}/recommend/jobs`, authHeader(token)),
};
