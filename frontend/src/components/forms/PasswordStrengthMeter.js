import React from 'react';
import { ProgressBar } from 'react-bootstrap';
import 'styles/components/PasswordStrengthMeter.css';

/**
 * A component that displays password strength as the user types
 * @param {Object} props
 * @param {string} props.password - The current password value
 */
export const PasswordStrengthMeter = ({ password }) => {
  // Password requirements checks
  const hasMinLength = password?.length >= 8;
  const hasUppercase = /[A-Z]/.test(password);
  const hasLowercase = /[a-z]/.test(password);
  const hasNumber = /[0-9]/.test(password);
  const hasSpecial = /[^A-Za-z0-9]/.test(password);

  // Calculate strength score (0-4)
  let strengthScore = 0;
  if (hasMinLength) strengthScore++;
  if (hasUppercase) strengthScore++;
  if (hasLowercase) strengthScore++;
  if (hasNumber) strengthScore++;
  if (hasSpecial) strengthScore++;

  // Normalize to 0-100 for progress bar
  const strengthPercentage = Math.min(100, (strengthScore / 5) * 100);

  // Determine strength level
  let strengthLevel = '';
  let variantColor = 'danger';

  if (strengthScore === 0) {
    strengthLevel = '';
    variantColor = 'danger';
  } else if (strengthScore < 3) {
    strengthLevel = 'Weak';
    variantColor = 'danger';
  } else if (strengthScore < 4) {
    strengthLevel = 'Medium';
    variantColor = 'warning';
  } else if (strengthScore < 5) {
    strengthLevel = 'Strong';
    variantColor = 'success';
  } else {
    strengthLevel = 'Very Strong';
    variantColor = 'success';
  }

  if (!password) {
    return null;
  }

  return (
    <div className="password-strength-container">
      <div className="d-flex justify-content-between align-items-center mb-1">
        <small>Password Strength</small>
        <small className={`text-${variantColor}`}>{strengthLevel}</small>
      </div>
      <ProgressBar
        variant={variantColor}
        now={strengthPercentage}
        style={{ height: '8px' }}
      />

      <ul className="requirements-list">
        <li
          className={`requirement-item ${
            hasMinLength ? 'requirement-fulfilled' : 'requirement-unfulfilled'
          }`}
        >
          At least 8 characters
        </li>
        <li
          className={`requirement-item ${
            hasUppercase ? 'requirement-fulfilled' : 'requirement-unfulfilled'
          }`}
        >
          Uppercase letter
        </li>
        <li
          className={`requirement-item ${
            hasLowercase ? 'requirement-fulfilled' : 'requirement-unfulfilled'
          }`}
        >
          Lowercase letter
        </li>
        <li
          className={`requirement-item ${
            hasNumber ? 'requirement-fulfilled' : 'requirement-unfulfilled'
          }`}
        >
          Number
        </li>
        <li
          className={`requirement-item ${
            hasSpecial ? 'requirement-fulfilled' : 'requirement-unfulfilled'
          }`}
        >
          Special character
        </li>
      </ul>
    </div>
  );
};
