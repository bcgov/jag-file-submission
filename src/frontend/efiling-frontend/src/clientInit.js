import axios from "axios";
import { BAMBORA_REDIRECT_URL, NEXT_PUBLIC_API_BASE_URL } from "./EnvConfig";

// Client-side initialisation that used to live in index.js.
// This module touches window/sessionStorage, so it must only ever be loaded
// from client-only code (AppShell is imported with `dynamic(..., {ssr: false})`).

axios.defaults.baseURL = NEXT_PUBLIC_API_BASE_URL;

sessionStorage.setItem("bamboraRedirectUrl", BAMBORA_REDIRECT_URL);

// prevent user from leaving site and losing saved data
window.addEventListener("beforeunload", (e) => {
  if (sessionStorage.getItem("validExit")) return false;

  const confirmationMessage = "";
  (e || window.event).returnValue = confirmationMessage;
  return confirmationMessage;
});

window.addEventListener("unload", () => {
  sessionStorage.removeItem("listenerExists");
  sessionStorage.removeItem("validExit");
});
