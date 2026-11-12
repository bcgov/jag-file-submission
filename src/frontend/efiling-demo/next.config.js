const webpack = require("webpack");

/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  // Static export — produces out/ directory served by nginx (same pattern as CRA build/)
  output: "export",
  // Treat shared-components as first-party so its CSS imports (react-datepicker)
  // are not blocked by Next.js's node_modules CSS restriction
  transpilePackages: ["shared-components"],

  webpack: (config, { isServer }) => {
    // shared-components' Footer does require("bootstrap/dist/js/bootstrap.bundle.min")
    // at module scope, which touches window/document and crashes during
    // server-side prerender ("Cannot read properties of undefined (reading 'jquery')").
    // Stub it on the server build only; the browser bundle still includes it.
    if (isServer) {
      config.resolve.alias = {
        ...config.resolve.alias,
        "bootstrap/dist/js/bootstrap.bundle.min": false,
      };
    }

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
