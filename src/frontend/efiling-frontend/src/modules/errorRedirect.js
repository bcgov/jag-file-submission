export function errorRedirect(errorUrl, status, message) {
  return window.open(
    `${errorUrl}?status=${status}&message=${message}`,
    "_self"
  );
}
