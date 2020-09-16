export function errorRedirect(errorUrl, error) {
  if (errorUrl && error.response && error.response.status) {
    const errorMessage =
      error.response.data && error.response.data.message
        ? error.response.data.message
        : "There was an error with your submission.";

    sessionStorage.setItem("validExit", true);
    return window.open(
      `${errorUrl}?status=${error.response.status}&message=${errorMessage}`,
      "_self"
    );
  }

  return null;
}
