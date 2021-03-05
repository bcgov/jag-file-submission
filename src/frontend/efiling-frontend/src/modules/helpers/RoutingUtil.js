export function redirectToParentApp() {
  const parentAppUrl = sessionStorage.getItem("cancelUrl");
  window.open(parentAppUrl, "_self");
}
