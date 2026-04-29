// Shared utility components for the Resume Builder

// YearMonth picker — sends "MM-yyyy" string as required by backend JacksonDateConfig
import { useState, useEffect } from 'react';

export const YearMonthPicker = ({ label, value, onChange, required, disabled }) => {
  const months = ['Jan','Feb','Mar','Apr','May','Jun','Jul','Aug','Sep','Oct','Nov','Dec'];
  const currentYear = new Date().getFullYear();
  const years = Array.from({ length: 40 }, (_, i) => currentYear - i);

  const parseValue = (v) => {
    if (!v) return ['', ''];
    const parts = v.split('-');
    return [parts[0] || '', parts[1] || ''];
  };

  const [localMonth, setLocalMonth] = useState(() => parseValue(value)[0]);
  const [localYear, setLocalYear] = useState(() => parseValue(value)[1]);

  // Sync local state if parent resets/changes the value externally
  useEffect(() => {
    const [m, y] = parseValue(value);
    setLocalMonth(m);
    setLocalYear(y);
  }, [value]);

  const handleMonthChange = (m) => {
    setLocalMonth(m);
    if (m && localYear) onChange(`${m}-${localYear}`);
  };

  const handleYearChange = (y) => {
    setLocalYear(y);
    if (localMonth && y) onChange(`${localMonth}-${y}`);
  };

  return (
    <div className="relative z-20">
      {label && (
        <label className="block text-slate-400 text-xs font-semibold uppercase tracking-wider mb-2">
          {label}{required && ' *'}
        </label>
      )}
      <div className="flex gap-2">
        <select
          value={localMonth}
          disabled={disabled}
          onChange={(e) => handleMonthChange(e.target.value)}
          className="flex-1 bg-slate-700 border border-slate-600 focus:border-cyan-500 rounded-xl px-3 py-3 text-white outline-none cursor-pointer relative z-20"
        >
          <option value="">Month</option>
          {months.map((m, i) => (
            <option key={m} value={String(i + 1).padStart(2, '0')}>{m}</option>
          ))}
        </select>

        <select
          value={localYear}
          disabled={disabled}
          onChange={(e) => handleYearChange(e.target.value)}
          className="flex-1 bg-slate-700 border border-slate-600 focus:border-cyan-500 rounded-xl px-3 py-3 text-white outline-none cursor-pointer relative z-20"
        >
          <option value="">Year</option>
          {years.map((y) => (
            <option key={y} value={y}>{y}</option>
          ))}
        </select>
      </div>
    </div>
  );
};

// Dynamic list input (for description bullets, tech stacks etc.)
export const DynamicListInput = ({ label, items, onChange, placeholder }) => {
  const addItem = () => onChange([...items, '']);
  const updateItem = (i, val) => { const n = [...items]; n[i] = val; onChange(n); };
  const removeItem = (i) => onChange(items.filter((_, idx) => idx !== i));

  return (
    <div>
      <div className="flex items-center justify-between mb-2">
        <label className="text-slate-400 text-xs font-semibold uppercase tracking-wider">{label}</label>
        <button type="button" onClick={addItem} className="text-cyan-400 hover:text-cyan-300 text-xs font-medium flex items-center gap-1 transition-colors">
          <svg className="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 4v16m8-8H4" />
          </svg>
          Add
        </button>
      </div>
      <div className="space-y-2">
        {items.map((item, i) => (
          <div key={i} className="flex gap-2 items-start">
            <span className="text-cyan-400 mt-3.5 text-xs">▸</span>
            <input
              value={item}
              onChange={e => updateItem(i, e.target.value)}
              placeholder={placeholder}
              className="flex-1 bg-slate-700/50 border border-slate-600 focus:border-cyan-500 rounded-xl px-4 py-2.5 text-white placeholder-slate-500 outline-none transition-all text-sm"
            />
            {items.length > 1 && (
              <button type="button" onClick={() => removeItem(i)} className="text-slate-500 hover:text-red-400 transition-colors mt-2">
                <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                  <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
                </svg>
              </button>
            )}
          </div>
        ))}
      </div>
    </div>
  );
};

// Card wrapper for list items (experience, project, etc.)
export const SectionCard = ({ children, onDelete, title, subtitle }) => (
  <div className="bg-slate-700/30 border border-slate-600/50 rounded-2xl p-5 relative group">
    {(title || subtitle) && (
      <div className="mb-4 pr-8">
        {title && <h4 className="font-semibold text-white">{title}</h4>}
        {subtitle && <p className="text-slate-400 text-sm">{subtitle}</p>}
      </div>
    )}
    {onDelete && (
      <button
        onClick={onDelete}
        className="absolute top-4 right-4 text-slate-500 hover:text-red-400 transition-colors opacity-0 group-hover:opacity-100"
      >
        <svg className="w-4 h-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
          <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
        </svg>
      </button>
    )}
    {children}
  </div>
);

export const SparkleIcon = () => (
  <svg className="w-3.5 h-3.5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 3v4M3 5h4M6 17v4m-2-2h4m5-16l2.286 6.857L21 12l-5.714 2.143L13 21l-2.286-6.857L5 12l5.714-2.143L13 3z" />
  </svg>
);

export const AIButton = ({ onClick, loading, label }) => (
  <button
    type="button" onClick={onClick} disabled={loading}
    className="flex items-center gap-1.5 bg-violet-600/20 hover:bg-violet-600/30 border border-violet-500/40 hover:border-violet-400 text-violet-300 px-3 py-1.5 rounded-lg text-xs font-medium transition-all disabled:opacity-50"
  >
    <SparkleIcon />
    {loading ? 'Writing…' : label || 'Rewrite with AI'}
  </button>
);