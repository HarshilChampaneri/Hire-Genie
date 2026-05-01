// const CompanyCard = ({ company, onEdit, onDelete, onViewJobs }) => {
//   return (
//     <div className="bg-slate-800 border border-slate-700 rounded-2xl p-5 flex flex-col gap-4
//                     hover:border-slate-500 transition-all duration-200">

//       {/* Header */}
//       <div className="flex items-start justify-between gap-3">
//         <div className="flex items-center gap-3 flex-1 min-w-0">
//           {/* Company icon */}
//           <div className="shrink-0 w-10 h-10 bg-blue-600/20 border border-blue-500/30 rounded-xl
//                           flex items-center justify-center text-blue-400 font-bold text-lg">
//             {company.companyName?.charAt(0).toUpperCase()}
//           </div>
//           <div className="min-w-0">
//             <h3 className="text-white font-bold text-base truncate">{company.companyName}</h3>
//             <a
//               href={company.companyUrl}
//               target="_blank"
//               rel="noopener noreferrer"
//               className="text-blue-400 text-xs hover:text-blue-300 transition-colors truncate block"
//               onClick={(e) => e.stopPropagation()}
//             >
//               {company.companyUrl}
//             </a>
//           </div>
//         </div>
//         <span className="shrink-0 text-slate-500 text-xs font-mono">#{company.companyId}</span>
//       </div>

//       {/* Actions */}
//       <div className="flex gap-2 pt-2 border-t border-slate-700">
//         <button
//           onClick={() => onViewJobs(company.companyId, company.companyName)}
//           className="flex-1 py-2 bg-slate-700 hover:bg-slate-600 text-slate-200 text-xs font-semibold
//                      rounded-lg transition-all"
//         >
//           📋 View Jobs
//         </button>
//         <button
//           onClick={() => onEdit(company)}
//           className="py-2 px-3 bg-blue-600/20 hover:bg-blue-600/40 text-blue-400 text-xs font-semibold
//                      rounded-lg transition-all border border-blue-500/30"
//         >
//           Edit
//         </button>
//         <button
//           onClick={() => onDelete(company.companyId)}
//           className="py-2 px-3 bg-red-600/20 hover:bg-red-600/40 text-red-400 text-xs font-semibold
//                      rounded-lg transition-all border border-red-500/30"
//         >
//           Delete
//         </button>
//       </div>
//     </div>
//   );
// };

// export default CompanyCard;


import { useNavigate } from 'react-router-dom';

const CompanyCard = ({ company, onEdit, onDelete, onViewJobs }) => {
  const navigate = useNavigate();

  const handleCardClick = () => {
    navigate(`/recruiter/companies/${company.companyId}`, { state: { company } });
  };

  return (
    <div
      onClick={handleCardClick}
      className="bg-slate-800 border border-slate-700 rounded-2xl p-5 flex flex-col gap-4
                 hover:border-slate-500 transition-all duration-200 cursor-pointer group"
    >
      {/* Header */}
      <div className="flex items-start justify-between gap-3">
        <div className="flex items-center gap-3 flex-1 min-w-0">
          {/* Avatar */}
          <div className="shrink-0 w-10 h-10 bg-blue-600/20 border border-blue-500/30 rounded-xl
                          flex items-center justify-center text-blue-400 font-bold text-lg">
            {company.companyName?.charAt(0).toUpperCase()}
          </div>
          <div className="min-w-0">
            <h3 className="text-white font-bold text-base truncate group-hover:text-blue-400 transition-colors">
              {company.companyName}
            </h3>
            <a
              href={company.companyUrl}
              target="_blank"
              rel="noopener noreferrer"
              onClick={(e) => e.stopPropagation()}
              className="text-blue-400 text-xs hover:text-blue-300 transition-colors truncate block"
            >
              {company.companyUrl}
            </a>
          </div>
        </div>
        <span className="shrink-0 text-slate-500 text-xs font-mono">#{company.companyId}</span>
      </div>

      {/* Click hint */}
      <p className="text-slate-600 text-xs group-hover:text-slate-400 transition-colors">
        Click card to view full details →
      </p>

      {/* Actions — stopPropagation so card click doesn't also fire */}
      <div
        onClick={(e) => e.stopPropagation()}
        className="flex gap-2 pt-2 border-t border-slate-700"
      >
        <button
          onClick={() => onViewJobs(company.companyId, company.companyName)}
          className="flex-1 py-2 bg-slate-700 hover:bg-slate-600 text-slate-200 text-xs font-semibold
                     rounded-lg transition-all"
        >
          📋 View Jobs
        </button>
        <button
          onClick={() => onEdit(company)}
          className="py-2 px-3 bg-blue-600/20 hover:bg-blue-600/40 text-blue-400 text-xs font-semibold
                     rounded-lg transition-all border border-blue-500/30"
        >
          Edit
        </button>
        <button
          onClick={() => onDelete(company.companyId)}
          className="py-2 px-3 bg-red-600/20 hover:bg-red-600/40 text-red-400 text-xs font-semibold
                     rounded-lg transition-all border border-red-500/30"
        >
          Delete
        </button>
      </div>
    </div>
  );
};

export default CompanyCard;