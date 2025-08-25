function getDefaultErrorStatus(error) {
  if (error.status) {
    return error.status;
  }
  if (error.response && error.response.status) {
    return error.response.status;
  }
  return "unknown";
}

function getDefaultErrorMsg(error) {
  if (error.response && error.response.data && error.response.data.message) {
    return error.response.data.message;
  }
  if (error.message) {
    return error.message;
  }
  return "There was an error with your submission.";
}

export function errorRedirect(errorUrl, error) {
  if (errorUrl && error) {
    const status = getDefaultErrorStatus(error);
    const msg = getDefaultErrorMsg(error);

    sessionStorage.setItem("validExit", true);
    return window.open(`${errorUrl}?status=${status}&message=${msg}`, "_self");
  }

  return null;
}
