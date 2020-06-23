module.exports = {
  env: {
    test: {
      presets: ["@babel/preset-env", "@babel/preset-react"],
      only: ["./**/*.js", "node_modules/jest-runtime"]
    }
  }
};
