const Pagination = ({ pageIndex, totalPages, isLastPage, onPageChange }) => {
  if (totalPages <= 1) return null;

  const pages = Array.from({ length: totalPages }, (_, i) => i);

  return (
    <div className="flex items-center justify-center gap-2 mt-8">
      {/* Prev */}
      <button
        onClick={() => onPageChange(pageIndex - 1)}
        disabled={pageIndex === 0}
        className="px-3 py-2 rounded-lg bg-slate-700 text-slate-300 text-sm font-medium
                   hover:bg-slate-600 disabled:opacity-30 disabled:cursor-not-allowed transition-all"
      >
        ← Prev
      </button>

      {/* Page Numbers */}
      {pages.map((p) => (
        <button
          key={p}
          onClick={() => onPageChange(p)}
          className={`w-9 h-9 rounded-lg text-sm font-semibold transition-all
            ${p === pageIndex
              ? 'bg-blue-600 text-white shadow-lg shadow-blue-600/30'
              : 'bg-slate-700 text-slate-300 hover:bg-slate-600'
            }`}
        >
          {p + 1}
        </button>
      ))}

      {/* Next */}
      <button
        onClick={() => onPageChange(pageIndex + 1)}
        disabled={isLastPage}
        className="px-3 py-2 rounded-lg bg-slate-700 text-slate-300 text-sm font-medium
                   hover:bg-slate-600 disabled:opacity-30 disabled:cursor-not-allowed transition-all"
      >
        Next →
      </button>
    </div>
  );
};

export default Pagination;