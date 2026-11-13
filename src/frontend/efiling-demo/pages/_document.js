import { Html, Head, Main, NextScript } from "next/document";

export default function Document() {
  return (
    <Html lang="en">
      <Head>
        <link rel="icon" href="/favicon.ico" />
        <meta name="theme-color" content="#000000" />
        <meta name="description" content="eFiling Demo Client" />
        <link rel="manifest" href="/manifest.json" />
        {/* Runtime env injection for container/OpenShift deployments */}
        <script src="/env-config.js" />
      </Head>
      <body>
        <Main />
        <NextScript />
      </body>
    </Html>
  );
}
