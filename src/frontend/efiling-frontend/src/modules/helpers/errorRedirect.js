export function errorRedirect(errorUrl, error) {
  if (errorUrl && error) {
    let errorStatus;
    if (error.status) {
      errorStatus = error.status;
    } else if (error.response && error.response.status) {
      errorStatus = error.response.status;
    } else {
      errorStatus = "unknown";
    }

    let errorMessage;
    if (error.response && error.response.data && error.response.data.message) {
      errorMessage = error.response.data.message;
    } else if (error.message) {
      errorMessage = error.message;
    } else {
      errorMessage = "There was an error with your submission.";
    }

    sessionStorage.setItem("validExit", true);
    return window.open(
      `${errorUrl}?status=${errorStatus}&message=${errorMessage}`,
      "_self"
    );
  }

  return null;
}
