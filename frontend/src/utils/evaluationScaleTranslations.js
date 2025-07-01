// src/utils/evaluationScaleTranslations.js

export const evaluationScaleTranslations = {
  en: {
    LOW_CONTRIBUTION: 'Low contribution',
    PARCIAL_CONTRIBUTION: 'Partial contribution',
    EXPECTED_CONTRIBUTION: 'Expected contribution',
    EXCEED_CONTRIBUTION: 'Exceeded contribution',
  },
  pt: {
    LOW_CONTRIBUTION: 'Contribuição baixa',
    PARCIAL_CONTRIBUTION: 'Contribuição parcial',
    EXPECTED_CONTRIBUTION: 'Contribuição conforme o esperado',
    EXCEED_CONTRIBUTION: 'Contribuição excedida',
  },
};

export function translateEvaluationScale(scale, lang = 'en') {
  return evaluationScaleTranslations[lang]?.[scale] || scale;
}
