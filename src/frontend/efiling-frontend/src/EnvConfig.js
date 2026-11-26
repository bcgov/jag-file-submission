export const IDP_BCEID = "bceid";
export const IDP_BCSC = "bcsc";
export const FLA_PARENT_APP_CODE = "FLA";

export const BAMBORA_REDIRECT_URL = window.env
  ? window.env.NEXT_PUBLIC_BAMBORA_REDIRECT_URL
  : process.env.NEXT_PUBLIC_BAMBORA_REDIRECT_URL;

export const NEXT_PUBLIC_API_BASE_URL = window.env
  ? window.env.NEXT_PUBLIC_API_BASE_URL
  : process.env.NEXT_PUBLIC_API_BASE_URL;
