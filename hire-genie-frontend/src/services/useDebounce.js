import { useEffect, useState } from 'react';

/**
 * Returns a debounced copy of `value` that only updates after `delay` ms
 * of no further changes. Used to avoid firing the AI-backed search endpoint
 * on every keystroke.
 *
 * @param {*} value
 * @param {number} delay - ms
 */
export const useDebounce = (value, delay = 400) => {
  const [debounced, setDebounced] = useState(value);

  useEffect(() => {
    const timer = setTimeout(() => setDebounced(value), delay);
    return () => clearTimeout(timer);
  }, [value, delay]);

  return debounced;
};