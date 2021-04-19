import { IDP_BCEID, IDP_BCSC } from "../../EnvConfig";
import { getIdentityProviderAlias } from "./authentication-helper/authenticationHelper";

export function translateApplicantInfo({
  bceid,
  lastName,
  firstName,
  middleName,
  email,
}) {
  const fullName = middleName
    ? `${firstName} ${middleName} ${lastName}`
    : `${firstName} ${lastName}`;
  const idp = getIdentityProviderAlias();

  const data = [];
  if (idp === IDP_BCEID) {
    data.push({
      name: "BCeID:",
      value: bceid,
    });
  } else if (idp === IDP_BCSC) {
    data.push({
      name: "BCSC:",
      value: "",
    });
  }

  data.push({
    name: "Full Name:",
    value: fullName,
  });

  // don't show missing email, we'll show input fields instead.
  if (email) {
    data.push({
      name: "Email Address:",
      value: email,
    });
  }
  return data;
}
