import { useState, useRef, useEffect } from 'react';
import { useAuth } from '../context/useAuth';
import { jobService } from '../services/jobService';

const initialsOf = (name) =>
  name
    ?.split(' ')
    .map((n) => n[0])
    .slice(0, 2)
    .join('')
    .toUpperCase() ?? '?';

const ResultCard = ({ profile }) => {
  const urls = [...(profile.urls ?? [])];

  return (
    <div className="bg-slate-800 border border-slate-700 rounded-xl p-4 flex gap-3 hover:border-slate-600 transition-colors">
      <div className="w-11 h-11 rounded-full bg-blue-600/20 border border-blue-500/40 text-blue-400
                      font-bold text-sm flex items-center justify-center shrink-0">
        {initialsOf(profile.fullName)}
      </div>

      <div className="min-w-0 flex-1">
        <div className="flex items-start justify-between gap-2">
          <p className="text-white font-semibold text-sm truncate">{profile.fullName || 'Unnamed candidate'}</p>
        </div>
        {profile.profession && (
          <p className="text-blue-400 text-xs mt-0.5">{profile.profession}</p>
        )}

        <div className="flex flex-wrap gap-x-4 gap-y-1 mt-2">
          {profile.email && (
            <a
              href={`mailto:${profile.email}`}
              className="text-slate-400 hover:text-slate-200 text-xs flex items-center gap-1 transition-colors"
            >
              ✉️ {profile.email}
            </a>
          )}
          {profile.mobileNo && (
            <span className="text-slate-400 text-xs flex items-center gap-1">
              📞 {profile.mobileNo}
            </span>
          )}
        </div>

        {urls.length > 0 && (
          <div className="flex flex-wrap gap-2 mt-2">
            {urls.map((url, i) => (
              <a
                key={i}
                href={url}
                target="_blank"
                rel="noopener noreferrer"
                className="text-xs text-blue-400 hover:text-blue-300 underline underline-offset-2 truncate max-w-50"
              >
                {url}
              </a>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

const EmployeeSearchBar = () => {
  const { token } = useAuth();
  const [query, setQuery] = useState('');
  const [isOpen, setIsOpen] = useState(false);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [results, setResults] = useState(null);
  const containerRef = useRef(null);

  // Close panel on outside click
  useEffect(() => {
    const handleClickOutside = (e) => {
      if (containerRef.current && !containerRef.current.contains(e.target)) {
        setIsOpen(false);
      }
    };
    document.addEventListener('mousedown', handleClickOutside);
    return () => document.removeEventListener('mousedown', handleClickOutside);
  }, []);

  const runSearch = async () => {
    const trimmed = query.trim();
    if (!trimmed) return;

    setLoading(true);
    setError(null);
    setIsOpen(true);

    try {
      const res = await jobService.searchEmployees(trimmed, token);
      setResults(res.data ?? []);
    } catch (err) {
      const backendMessage =
        typeof err.response?.data === 'string'
          ? err.response.data
          : err.response?.data?.message;
      setError(backendMessage || 'Something went wrong while searching. Please try again.');
      setResults(null);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    runSearch();
  };

  const handleClear = () => {
    setQuery('');
    setResults(null);
    setError(null);
    setIsOpen(false);
  };

  return (
    <div ref={containerRef} className="relative w-full max-w-md">
      <form onSubmit={handleSubmit} className="relative">
        <svg
          xmlns="http://www.w3.org/2000/svg"
          className="w-4 h-4 text-slate-500 absolute left-3 top-1/2 -translate-y-1/2 pointer-events-none"
          fill="none"
          viewBox="0 0 24 24"
          stroke="currentColor"
        >
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2}
            d="M21 21l-6-6m2-5a7 7 0 11-14 0 7 7 0 0114 0z" />
        </svg>

        <input
          type="text"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
          onFocus={() => results && setIsOpen(true)}
          placeholder="Search candidates… e.g. Java developers at TCS"
          className="w-full bg-slate-900 border border-slate-700 rounded-lg pl-9 pr-9 py-2
                     text-sm text-white placeholder-slate-500 focus:outline-none
                     focus:border-blue-500/60 focus:ring-1 focus:ring-blue-500/40 transition-all"
        />

        {query && (
          <button
            type="button"
            onClick={handleClear}
            className="absolute right-2.5 top-1/2 -translate-y-1/2 text-slate-500 hover:text-slate-300 transition-colors"
            title="Clear search"
          >
            ✕
          </button>
        )}
      </form>

      {isOpen && (
        <div
          className="absolute left-0 right-0 mt-2 bg-slate-800 border border-slate-700 rounded-xl
                     shadow-2xl shadow-black/40 z-40 max-h-112 overflow-y-auto"
        >
          {/* Loading */}
          {loading && (
            <div className="flex flex-col items-center justify-center py-10 gap-3">
              <div className="w-6 h-6 border-2 border-blue-500 border-t-transparent rounded-full animate-spin" />
              <p className="text-slate-400 text-xs">Searching candidates…</p>
            </div>
          )}

          {/* Error */}
          {!loading && error && (
            <div className="flex flex-col items-center justify-center py-10 gap-2 text-center px-6">
              <span className="text-2xl">⚠️</span>
              <p className="text-slate-300 text-sm font-medium">{error}</p>
              <p className="text-slate-500 text-xs">Try a simpler search, like a name, role, or company.</p>
            </div>
          )}

          {/* Empty results */}
          {!loading && !error && results && results.length === 0 && (
            <div className="flex flex-col items-center justify-center py-10 gap-2 text-center px-6">
              <span className="text-2xl">🔍</span>
              <p className="text-slate-300 text-sm font-medium">No candidates found</p>
              <p className="text-slate-500 text-xs">Try a different name, role, or company.</p>
            </div>
          )}

          {/* Results */}
          {!loading && !error && results && results.length > 0 && (
            <div className="p-3 flex flex-col gap-2">
              <p className="text-xs font-bold uppercase tracking-widest text-slate-500 px-1 mb-1">
                {results.length} candidate{results.length !== 1 ? 's' : ''} found
              </p>
              {results.map((profile) => (
                <ResultCard key={profile.profileId} profile={profile} />
              ))}
            </div>
          )}
        </div>
      )}
    </div>
  );
};

export default EmployeeSearchBar;
