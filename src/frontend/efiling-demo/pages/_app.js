import "../src/index.css";
import "@bcgov/bootstrap-theme/dist/css/bootstrap-theme.min.css";
import "../src/components/page/page.css";
import "../src/components/page/home/Home.css";
import "../src/components/page/error/Error.css";
import axios from "axios";
import { useEffect } from "react";

export default function App({ Component, pageProps }) {
  useEffect(() => {
    // window.env is injected at runtime via public/env-config.js for
    // container/OpenShift deployments; fall back to build-time env var.
    axios.defaults.baseURL = window.env
      ? window.env.NEXT_PUBLIC_API_BASE_URL
      : process.env.NEXT_PUBLIC_API_BASE_URL;
  }, []);

  return <Component {...pageProps} />;
}
