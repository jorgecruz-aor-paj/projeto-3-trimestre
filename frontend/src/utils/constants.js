// constants.js
// Centralized regex patterns and magic values for validation

export const REGEX = {
  EMAIL: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
  EMAIL_STRICT:
    /^(?=.{1,64}@.{1,255}$)([A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,})$/,
  PHONE: /^\d{9}$/,
  TRAINING_AREA_NAME: /^[a-zA-Z0-9\s-]+$/,
  PASSWORD_LOGIN: /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*#?&.\-_])\S{8,}$/,
  PASSWORD_REGISTER:
    /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*.])(?!.*\s).{8,}$/,
  PASSWORD_SET_NEW:
    /^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!*.])(?!.*\s).{8,}$/,
  NUMBER_ONLY: /^[0-9]+$/,
};

export const VALIDATION_LIMITS = {
  PASSWORD_MIN_LENGTH: 8,
  PASSWORD_MAX_LENGTH: 255,
  NAME_MIN_LENGTH: 2,
  NAME_MAX_LENGTH: 50,
  ADDRESS_MAX_LENGTH: 200,
  BIOGRAPHY_MAX_LENGTH: 500,
  TRAINING_TITLE_MIN: 3,
  TRAINING_TITLE_MAX: 100,
  TRAINING_DESC_MIN: 10,
  TRAINING_DESC_MAX: 2000,
  TRAINING_DURATION_MIN: 15,
  TRAINING_DURATION_MAX: 3000,
  EVALUATION_TITLE_MIN: 5,
  EVALUATION_TITLE_MAX: 150,
  EVALUATION_FEEDBACK_MIN: 3,
  EVALUATION_FEEDBACK_MAX: 2000,
  MESSAGE_MAX_LENGTH: 5000,
  EMAIL_MAX_LENGTH: 255,
  BIRTHDATE_MIN_AGE: 13,
  BIRTHDATE_MAX_AGE: 120,
};
