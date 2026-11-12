const webpack = require("webpack");

/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  // Treat shared-components as first-party so its CSS imports (react-datepicker)
  // are not blocked by Next.js's node_modules CSS restriction
  transpilePackages: ["shared-components"],

  webpack: (config) => {
    // Port polyfills from config-overrides.js — needed for packages
    // that reference Node built-ins (jsonwebtoken, crypto, etc.)
    const fallback = config.resolve.fallback || {};
    Object.assign(fallback, {
      crypto: require.resolve("crypto-browserify"),
      stream: require.resolve("stream-browserify"),
      assert: require.resolve("assert"),
      http: require.resolve("stream-http"),
      https: require.resolve("https-browserify"),
      os: require.resolve("os-browserify"),
      url: require.resolve("url"),
      vm: require.resolve("vm-browserify"),
    });
    config.resolve.fallback = fallback;

    config.plugins = (config.plugins || []).concat([
      new webpack.ProvidePlugin({
        process: "process/browser",
        Buffer: ["buffer", "Buffer"],
      }),
    ]);

    return config;
  },
};

module.exports = nextConfig;
