/* eslint-disable react/function-component-definition */
import React from "react";
import PropTypes from "prop-types";
import { MdPermIdentity } from "react-icons/md";
import { FcOrganization } from "react-icons/fc";
import { formatFullName } from "../../../modules/helpers/StringUtil";
import "./PartyList.scss";

const hash = require("object-hash");

export default function PartyList({ parties, organizationParties }) {
  return (
    <div className="ct-party-list">
      <div className="header">
        <span className="d-none d-lg-inline col-lg-6">Name</span>
        <span className="d-none d-lg-inline col-lg-3">Party Role</span>
        <span className="d-none d-lg-inline col-lg-3">Party Type</span>
      </div>
      <ul>
        {parties &&
          parties.map((party) => (
            <li key={hash(party)}>
              <span className="col-sm-12 col-lg-6">
                <MdPermIdentity size={30} color="#FCBA19" />
                {formatFullName(party)}
              </span>
              <span className="label col-sm-4 d-lg-none">Party Role:</span>
              <span className="col-sm-8 col-lg-3">{party.roleDescription}</span>
              <span className="label col-sm-4 d-lg-none">Party Type:</span>
              <span className="col-sm-8 col-lg-3">
                {party.partyDescription}
              </span>
            </li>
          ))}
        {organizationParties &&
          organizationParties.map((party) => (
            <li key={hash(party)}>
              <span className="col-sm-12 col-lg-6">
                <FcOrganization size={30} />
                {party.name}
              </span>
              <span className="label col-sm-4 d-lg-none">Party Role:</span>
              <span className="col-sm-8 col-lg-3">{party.roleDescription}</span>
              <span className="label col-sm-4 d-lg-none">Party Type:</span>
              <span className="col-sm-8 col-lg-3">
                {party.partyDescription}
              </span>
            </li>
          ))}
      </ul>
    </div>
  );
}

PartyList.propTypes = {
  parties: PropTypes.arrayOf(
    PropTypes.shape({
      partyType: PropTypes.string,
      partyDescription: PropTypes.string,
      roleDescription: PropTypes.string,
      firstName: PropTypes.string,
      middleName: PropTypes.string,
      lastName: PropTypes.string,
    })
  ),
  organizationParties: PropTypes.arrayOf(
    PropTypes.shape({
      name: PropTypes.string,
      partyDescription: PropTypes.string,
      roleDescription: PropTypes.string,
    })
  ),
};

PartyList.defaultProps = {
  parties: [],
  organizationParties: [],
};
