module.exports = {
  env: {
    browser: true,
    es6: true,
    jest: true,
  },
  extends: ["plugin:react/recommended"],
  globals: {
    Atomics: "readonly",
    SharedArrayBuffer: "readonly",
  },
  parserOptions: {
    ecmaFeatures: {
      jsx: true,
    },
    ecmaVersion: 2018,
    sourceType: "module",
  },
  plugins: ["react"],
  ignorePatterns: ["node_modules/", "build/"],
  rules: {
    "import/no-cycle": "off",
    "import/no-extraneous-dependencies": "off",
    "import/no-unresolved": "off",
    "import/prefer-default-export": "off",
    "react/forbid-prop-types": "off",
    "react/jsx-curly-brace-presence": "off",
    "react/jsx-filename-extension": "off",
    "react/jsx-wrap-multilines": "off",
  },
};
