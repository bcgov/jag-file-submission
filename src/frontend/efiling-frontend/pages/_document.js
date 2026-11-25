import { Html, Head, Main, NextScript } from "next/document";

// next.config.js `basePath` does not prefix plain <link>/<script> tags,
// so the prefix is spelled out here. Keep in sync with next.config.js.
const BASE_PATH = "/efilinghub";

export default function Document() {
  return (
    <Html lang="en">
      <Head>
        <meta charSet="utf-8" />
        <link rel="icon" href={`${BASE_PATH}/favicon.ico`} />
        <meta name="theme-color" content="#000000" />
        <link rel="manifest" href={`${BASE_PATH}/manifest.json`} />
        {/* Runtime env injection for container/OpenShift deployments (env.sh) */}
        <script src={`${BASE_PATH}/env-config.js`} />
        <script src="https://libs.na.bambora.com/customcheckout/1/customcheckout.js" />
        <title>E-File submission</title>
      </Head>
      <body>
        <noscript>You need to enable JavaScript to run this app.</noscript>
        <Main />
        <NextScript />
      </body>
    </Html>
  );
}
