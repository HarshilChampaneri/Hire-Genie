import { useState } from 'react';
import { authService } from '../api/authService';

const Register = () => {
  const [formData, setFormData] = useState({
    email: '',
    password: '',
    roles: ['ROLE_EMPLOYEE'],
    adminSecret: ''
  });

  const handleRoleChange = (role) => {
    setFormData(prev => ({
      ...prev,
      roles: [role],
      adminSecret: role === 'ROLE_ADMIN' ? prev.adminSecret : ''
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await authService.register(formData);
      alert('Registration Successful! Please login.');
    } catch (err) {
      console.error(err.response?.data?.message || 'Registration failed');
    }
  };

  return (
    <div className="min-h-screen flex items-center justify-center bg-slate-900 text-white p-6">
      <div className="w-full max-w-md bg-slate-800 rounded-2xl shadow-xl p-8 border border-slate-700">
        <h2 className="text-3xl font-bold text-center mb-6 text-blue-400">Join Hire-Genie</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <input 
            type="email" placeholder="Email" 
            className="w-full p-3 rounded bg-slate-700 border border-slate-600 focus:outline-none focus:border-blue-500"
            onChange={(e) => setFormData({...formData, email: e.target.value})}
            required
          />
          <input 
            type="password" placeholder="Password" 
            className="w-full p-3 rounded bg-slate-700 border border-slate-600 focus:outline-none focus:border-blue-500"
            onChange={(e) => setFormData({...formData, password: e.target.value})}
            required
          />
          
          <div className="flex gap-4 py-2">
            {['ROLE_EMPLOYEE', 'ROLE_RECRUITER', 'ROLE_ADMIN'].map(role => (
              <label key={role} className="flex items-center text-sm cursor-pointer">
                <input 
                  type="radio" name="role" className="mr-2"
                  checked={formData.roles.includes(role)}
                  onChange={() => handleRoleChange(role)}
                />
                {role.split('_')[1]}
              </label>
            ))}
          </div>

          {formData.roles.includes('ROLE_ADMIN') && (
            <input 
              type="password" placeholder="Admin Secret Key" 
              className="w-full p-3 rounded bg-slate-700 border border-red-500 focus:outline-none"
              onChange={(e) => setFormData({...formData, adminSecret: e.target.value})}
              required
            />
          )}

          <button className="w-full bg-blue-600 hover:bg-blue-500 py-3 rounded-lg font-semibold transition-all">
            Create Account
          </button>
        </form>
      </div>
    </div>
  );
};

export default Register;
