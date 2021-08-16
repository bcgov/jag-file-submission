import React from "react";

import Login from "./Login";

const keycloak = {};
keycloak.login = () => {};

export const Default = () => <Login keycloak={keycloak} />;

export const Mobile = () => <Login keycloak={keycloak} />;

Mobile.parameters = {
  viewport: {
    defaultViewport: "mobile2",
  },
};
