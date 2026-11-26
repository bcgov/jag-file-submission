/** @type {import('next').NextConfig} */
const nextConfig = {
  reactStrictMode: true,
  basePath: "/efilinghub",
  output: "export",
  transpilePackages: ["shared-components"],
  eslint: { ignoreDuringBuilds: true },
  webpack: (config, { isServer, webpack }) => {
    if (isServer) {
      config.resolve.alias = {
        ...config.resolve.alias,
        "bootstrap/dist/js/bootstrap.bundle.min": false,
      };
      return config;
    }

    config.resolve.fallback = {
      ...config.resolve.fallback,
      crypto: require.resolve("crypto-browserify"),
      stream: require.resolve("stream-browserify"),
      assert: require.resolve("assert"),
      http: require.resolve("stream-http"),
      https: require.resolve("https-browserify"),
      os: require.resolve("os-browserify"),
      url: require.resolve("url"),
      vm: require.resolve("vm-browserify"),
    };

    config.plugins.push(
      new webpack.ProvidePlugin({
        process: "process/browser",
        Buffer: ["buffer", "Buffer"],
      })
    );

    return config;
  },
};

module.exports = nextConfig;
