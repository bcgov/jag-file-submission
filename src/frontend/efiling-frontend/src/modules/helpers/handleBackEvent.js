export function onBackButtonEvent(e, stateChange) {
  e.stopPropagation();

  if (sessionStorage.getItem("currentPage") !== "packageConfirmation") {
    sessionStorage.setItem("currentPage", "packageConfirmation");
    stateChange();
  }
}
