import js from '@eslint/js';
import globals from 'globals';
import pluginReact from 'eslint-plugin-react';
import json from '@eslint/json';
import markdown from '@eslint/markdown';
import css from '@eslint/css';
import { defineConfig } from 'eslint/config';

export default defineConfig([
  {
    files: ['**/*.{js,mjs,cjs,jsx}'],
    plugins: { js },
    extends: ['js/recommended'],
  },
  {
    files: ['**/*.{js,mjs,cjs,jsx}'],
    languageOptions: { globals: { ...globals.browser, ...globals.node } },
  },
  pluginReact.configs.flat.recommended,
  {
    settings: {
      react: {
        version: '19.1.0',
      },
    },
  },
  {
    files: ['**/*.json'],
    plugins: { json },
    language: 'json/json',
    extends: ['json/recommended'],
  },
  {
    files: ['**/*.jsonc'],
    plugins: { json },
    language: 'json/jsonc',
    extends: ['json/recommended'],
  },
  {
    files: ['**/*.md'],
    plugins: { markdown },
    language: 'markdown/gfm',
    extends: ['markdown/recommended'],
  },
  {
    files: ['**/*.css'],
    plugins: { css },
    language: 'css/css',
    extends: ['css/recommended'],
  },
  // React specific rules
  {
    files: ['**/*.{js,jsx}'],
    rules: {
      'react/jsx-uses-react': 'error',
      'react/jsx-uses-vars': 'error',
      'react/prop-types': 'warn',
      'react/no-unescaped-entities': 'error',
      'react/no-direct-mutation-state': 'error',
      'react/jsx-no-duplicate-props': 'error',
      'react/jsx-key': 'error',
      'react/jsx-no-undef': 'error',
      'react/no-unknown-property': 'error',
    },
  },
  // JavaScript/ES6+ Best Practices
  {
    files: ['**/*.{js,jsx}'],
    rules: {
      'no-unused-vars': 'warn',
      'no-console': ['warn', { allow: ['warn', 'error', 'log'] }],
      'prefer-const': 'error',
      'no-var': 'error',
      eqeqeq: 'error',
      'no-duplicate-imports': 'error',
      'no-param-reassign': 'error',
      'no-return-await': 'error',
      'no-throw-literal': 'error',
    },
  },
  // Error prevention
  {
    files: ['**/*.{js,jsx}'],
    rules: {
      'no-debugger': 'warn',
      'no-alert': 'warn',
      'no-unused-expressions': 'error',
      'require-await': 'error',
      'no-use-before-define': 'error',
      'no-shadow': 'error',
    },
  },
]);
