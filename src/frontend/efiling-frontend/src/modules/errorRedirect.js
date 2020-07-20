export function errorRedirect(errorUrl, error) {
  if (
    errorUrl &&
    error.response &&
    error.response.status &&
    error.response.data &&
    error.response.data.message
  ) {
    return window.open(
      `${errorUrl}?status=${error.response.status}&message=${error.response.data.message}`,
      "_self"
    );
  }
}
