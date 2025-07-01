// src/utils/dateUtils.js

/**
 * Converts an ISO instant string or epoch seconds to dd/mm/yyyy format.
 * @param {string|number} instant - ISO string or epoch seconds
 * @returns {string} - Formatted date string
 */
export function formatDateToDDMMYYYY(instant) {
  if (!instant) return '-';
  let date;
  if (typeof instant === 'number') {
    date = new Date(instant * 1000);
  } else {
    date = new Date(instant);
  }
  const day = String(date.getDate()).padStart(2, '0');
  const month = String(date.getMonth() + 1).padStart(2, '0');
  const year = date.getFullYear();
  return `${day}/${month}/${year}`;
}
