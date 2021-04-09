module.exports = {
  env: {
    browser: true,
    es6: true,
    jest: true,
  },
  extends: ["plugin:react/recommended", "airbnb", "prettier"],
  globals: {
    Atomics: "readonly",
    SharedArrayBuffer: "readonly",
  },
  parser: "babel-eslint",
  parserOptions: {
    ecmaFeatures: {
      jsx: true,
    },
    ecmaVersion: 2018,
    sourceType: "module",
  },
  plugins: ["react", "prettier"],
  ignorePatterns: ["node_modules/", "build/"],
  rules: {
    "import/no-unresolved": "off",
    "react/forbid-prop-types": "off",
    "react/jsx-filename-extension": "off",
    "react/jsx-curly-brace-presence": "off",
    "import/no-extraneous-dependencies": "off",
    "import/prefer-default-export": "off",
    "prettier/prettier": [
      "error",
      {
        endOfLine: "auto",
      },
    ],
  },
};
