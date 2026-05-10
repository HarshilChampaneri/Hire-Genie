import axios from 'axios';

const BASE = '/api/jobs';

const authHeader = (token) => ({ headers: { Authorization: `Bearer ${token}` } });

export const jobService = {

  // ─── Companies (Recruiter Only) ─────────────────────────────────────────────

  addCompany: (data, token) =>
    axios.post(`${BASE}/companies`, data, authHeader(token)),

  getAllCompanies: (token, page = 0, size = 10, sortBy = 'companyName', sortDir = 'asc') =>
    axios.get(`${BASE}/companies`, {
      ...authHeader(token),
      params: { page, size, sortBy, sortDir },
    }),

  updateCompany: (companyId, data, token) =>
    axios.put(`${BASE}/companies/${companyId}`, data, authHeader(token)),

  deleteCompany: (companyId, token) =>
    axios.delete(`${BASE}/companies/${companyId}`, authHeader(token)),

  // ─── Jobs (Read: Both Roles | Write: Recruiter Only) ────────────────────────

  getAllJobs: (token, page = 0, size = 10, sortBy = 'jobType', sortDir = 'asc') =>
    axios.get(`${BASE}`, {
      ...authHeader(token),
      params: { page, size, sortBy, sortDir },
    }),

  getAllJobsByCompany: (companyId, token, page = 0, size = 10, sortBy = 'jobTitle', sortDir = 'asc') =>
    axios.get(`${BASE}/${companyId}`, {
      ...authHeader(token),
      params: { page, size, sortBy, sortDir },
    }),

  addJob: (companyId, data, token) =>
    axios.post(`${BASE}/${companyId}`, data, authHeader(token)),

  updateJob: (jobId, data, token) =>
    axios.put(`${BASE}/${jobId}`, data, authHeader(token)),

  deleteJob: (jobId, token) =>
    axios.delete(`${BASE}/${jobId}`, authHeader(token)),

  // ─── Job Application (Employee) ─────────────────────────────────────────────

  applyForJob: (jobId, token) =>
    axios.post(`${BASE}/apply/${jobId}`, {}, authHeader(token)),

  // ─── Employee Recommendation (Recruiter Only) ────────────────────────────────

  recommendEmployees: (jobId, token) =>
    axios.post(`${BASE}/recommend/employees/${jobId}`, {}, authHeader(token)),

  // ─── Roleplay (Both Roles) ───────────────────────────────────────────────────

  startRoleplay: (jobId, token) =>
    axios.post(`${BASE}/start/roleplay/${jobId}`, {}, authHeader(token)),

  // ─── Pending Job Applications (Recruiter Only) ───────────────────────────────

  getPendingApplications: (token, page = 0, size = 10, sortBy = 'jobTitle', sortDir = 'asc') =>
    axios.get(`${BASE}/get/pending/applications`, {
      ...authHeader(token),
      params: { page, size, sortBy, sortDir },
    }),

  acceptApplicant: (jobApplicationId, data, token) =>
    axios.post(`${BASE}/accept/applicant/${jobApplicationId}`, data, authHeader(token)),

  rejectApplicant: (jobApplicationId, token) =>
    axios.post(`${BASE}/reject/applicant/${jobApplicationId}`, {}, authHeader(token)),

};