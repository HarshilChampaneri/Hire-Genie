const JOB_TYPE_STYLES = {
  FULL_TIME: 'bg-emerald-500/15 text-emerald-400 border border-emerald-500/30',
  PART_TIME: 'bg-amber-500/15 text-amber-400 border border-amber-500/30',
};

const WORK_MODE_STYLES = {
  REMOTE: 'bg-blue-500/15 text-blue-400 border border-blue-500/30',
  HYBRID: 'bg-purple-500/15 text-purple-400 border border-purple-500/30',
  ON_SITE: 'bg-rose-500/15 text-rose-400 border border-rose-500/30',
};

const formatSalary = (min, max, currency) => {
  if (!min && !max) return null;
  const fmt = (v) => Number(v).toLocaleString();
  const cur = currency || 'USD';
  if (min && max) return `${cur} ${fmt(min)} – ${fmt(max)}`;
  if (min) return `${cur} ${fmt(min)}+`;
  return `Up to ${cur} ${fmt(max)}`;
};

const JobCard = ({ job, onApply, onRoleplay, isRecruiter = false, onEdit, onDelete, onRecommend }) => {
  const salary = formatSalary(job.minSalary, job.maxSalary, job.currency);

  return (
    <div className="bg-slate-800 border border-slate-700 rounded-2xl p-5 flex flex-col gap-4
                    hover:border-slate-500 transition-all duration-200 hover:shadow-lg hover:shadow-black/20">

      {/* Header */}
      <div className="flex items-start justify-between gap-3">
        <div className="flex-1 min-w-0">
          <h3 className="text-white font-bold text-lg leading-tight truncate">{job.jobTitle}</h3>
          <p className="text-slate-400 text-sm mt-0.5 flex items-center gap-1.5">
            <svg xmlns="http://www.w3.org/2000/svg" className="w-3.5 h-3.5 shrink-0" fill="none" viewBox="0 0 24 24" stroke="currentColor">
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
              <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M15 11a3 3 0 11-6 0 3 3 0 016 0z" />
            </svg>
            {job.location}
          </p>
        </div>

        {/* Vacancies */}
        <span className="shrink-0 bg-slate-700 text-slate-300 text-xs font-semibold px-2.5 py-1 rounded-full">
          {job.vacancies} {job.vacancies === 1 ? 'vacancy' : 'vacancies'}
        </span>
      </div>

      {/* Badges */}
      <div className="flex flex-wrap gap-2">
        <span className={`text-xs font-semibold px-2.5 py-1 rounded-full ${JOB_TYPE_STYLES[job.jobType] || ''}`}>
          {job.jobType?.replace('_', ' ')}
        </span>
        <span className={`text-xs font-semibold px-2.5 py-1 rounded-full ${WORK_MODE_STYLES[job.workMode] || ''}`}>
          {job.workMode?.replace('_', ' ')}
        </span>
      </div>

      {/* Description */}
      <p className="text-slate-400 text-sm leading-relaxed line-clamp-3">{job.jobDescription}</p>

      {/* Salary */}
      {salary && (
        <div className="flex items-center gap-1.5 text-slate-300 text-sm font-medium">
          <svg xmlns="http://www.w3.org/2000/svg" className="w-4 h-4 text-emerald-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
          </svg>
          {salary}
        </div>
      )}

      {/* Actions */}
      <div className="flex flex-wrap gap-2 mt-auto pt-2 border-t border-slate-700">
        {!isRecruiter && (
          <>
            <button
              onClick={() => onApply(job.jobId)}
              className="flex-1 min-w-[100px] py-2 bg-blue-600 hover:bg-blue-500 text-white text-sm font-semibold
                         rounded-lg transition-all text-center"
            >
              Apply Now
            </button>
            <button
              onClick={() => onRoleplay(job.jobId)}
              className="flex-1 min-w-[100px] py-2 bg-slate-700 hover:bg-slate-600 text-slate-200 text-sm font-semibold
                         rounded-lg transition-all text-center"
            >
              🎭 Roleplay
            </button>
          </>
        )}

        {isRecruiter && (
          <>
            <button
              onClick={() => onRecommend(job.jobId)}
              className="flex-1 min-w-[100px] py-2 bg-purple-600 hover:bg-purple-500 text-white text-sm font-semibold
                         rounded-lg transition-all text-center"
            >
              🤖 Recommend
            </button>
            <button
              onClick={() => onRoleplay(job.jobId)}
              className="py-2 px-3 bg-slate-700 hover:bg-slate-600 text-slate-200 text-sm font-semibold
                         rounded-lg transition-all"
            >
              🎭
            </button>
            <button
              onClick={() => onEdit(job)}
              className="py-2 px-3 bg-blue-600/20 hover:bg-blue-600/40 text-blue-400 text-sm font-semibold
                         rounded-lg transition-all border border-blue-500/30"
            >
              Edit
            </button>
            <button
              onClick={() => onDelete(job.jobId)}
              className="py-2 px-3 bg-red-600/20 hover:bg-red-600/40 text-red-400 text-sm font-semibold
                         rounded-lg transition-all border border-red-500/30"
            >
              Delete
            </button>
          </>
        )}
      </div>
    </div>
  );
};

export default JobCard;