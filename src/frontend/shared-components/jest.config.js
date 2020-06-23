module.exports = {
  watchPathIgnorePatterns: ["<rootDir>/node_modules/"],
  collectCoverageFrom: [
    "src/**/*.js",
    "!src/index.js",
    "!**/node_modules/**",
    "!**/vendor/**"
  ],
  transform: {
    "^.+\\.[t|j]sx?$": "babel-jest"
  },
  transformIgnorePatterns: ["./node_modules/(?!shared-components).+\\.js$"],
  setupFilesAfterEnv: ["<rootDir>/src/setupTests.js", "./src/setupTests.js"],
  moduleNameMapper: {
    "\\.(jpg|jpeg|png|PNG|gif|eot|otf|webp|svg|ttf|woff|woff2|mp4|webm|wav|mp3|m4a|aac|oga|pdf)$":
      "<rootDir>/src/AssetsTransformer.js",
    "\\.(css|less)$": "<rootDir>/src/AssetsTransformer.js"
  },
  verbose: true,
  testResultsProcessor: "jest-sonar-reporter",
  collectCoverage: false,
  coverageReporters: ["text", ["lcov", {"projectRoot": "../../../"}]],
  coverageDirectory: "coverage"
};
