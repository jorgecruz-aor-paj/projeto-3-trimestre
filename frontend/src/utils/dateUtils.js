/**
 * Converts a date string (yyyy-mm-dd) or Date object to ISO8601 string in UTC (e.g. 2025-06-01T00:00:00Z).
 * If isEndOfDay is true, sets time to 23:59:59, else to 00:00:00.
 * @param {string|Date} date - Date string (yyyy-mm-dd) or Date object
 * @param {boolean} isEndOfDay - Whether to set time to end of day
 * @returns {string} - ISO8601 string in UTC
 */
export function toApiDateTime(date, isEndOfDay = false) {
  if (!date) return '';
  let d = typeof date === 'string' ? new Date(date) : new Date(date);
  if (isNaN(d.getTime())) return '';
  if (isEndOfDay) {
    d.setUTCHours(23, 59, 59, 0);
  } else {
    d.setUTCHours(0, 0, 0, 0);
  }
  return d.toISOString().replace('.000', '');
}
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
