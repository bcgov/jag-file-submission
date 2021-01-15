import React, { useEffect, useState } from "react";
import moment from "moment";
import PropTypes from "prop-types";
import { Header, Footer, Button, Table, Alert } from "shared-components";
import { MdCancel } from "react-icons/md";
import axios from "axios";
import { propTypes } from "../../../types/propTypes";

export default function PackageReview({ page: { header, packageId } }) {
  const [error, setError] = useState(false);
  const [packageDetails, setPackageDetails] = useState([
    {
      name: "Package Number:",
      value: "",
      isNameBold: false,
      isValueBold: true,
    },
    { name: "Submitted By:", value: "", isNameBold: false, isValueBold: true },
    {
      name: "Submitted Date:",
      value: "",
      isNameBold: false,
      isValueBold: true,
    },
  ]);
  const [courtFileDetails, setCourtFileDetails] = useState([
    {
      name: "Court File Number:",
      value: "",
      isNameBold: false,
      isValueBold: true,
    },
    { name: "Submitted To:", value: "", isNameBold: false, isValueBold: true },
    {
      name: "Filing Comments:",
      value: "",
      isNameBold: false,
      isValueBold: true,
    },
  ]);

  useEffect(() => {
    axios
      .get(`filingpackage/${packageId}`)
      .then((response) => {
        try {
          const packageNo = response.data.packageNumber || "";
          let submittedBy = "";
          if (response.data.submittedBy.firstName) {
            if (response.data.submittedBy.lastName) {
              submittedBy = `${response.data.submittedBy.firstName} ${response.data.submittedBy.lastName}`;
            }
          }
          let submittedDt = "";
          if (response.data.submittedDate) {
            submittedDt = moment(response.data.submittedDate).format(
              "DD-MMM-YYYY HH:MM"
            );
          }
          const fileNumber = response.data.court.fileNumber || "";
          const submittedTo = response.data.court.location || "";
          const filingComments = response.data.filingComments || "";
          setPackageDetails([
            {
              name: "Package Number:",
              value: `${packageNo}`,
              isNameBold: false,
              isValueBold: true,
            },
            {
              name: "Submitted By:",
              value: submittedBy,
              isNameBold: false,
              isValueBold: true,
            },
            {
              name: "Submitted Date:",
              value: submittedDt,
              isNameBold: false,
              isValueBold: true,
            },
          ]);
          setCourtFileDetails([
            {
              name: "Court File Number:",
              value: `${fileNumber}`,
              isNameBold: false,
              isValueBold: true,
            },
            {
              name: "Submitted To:",
              value: `${submittedTo}`,
              isNameBold: false,
              isValueBold: true,
            },
            {
              name: "Filing Comments:",
              value: `${filingComments}`,
              isNameBold: false,
              isValueBold: true,
            },
          ]);
        } catch (err) {
          setError(true);
        }
      })
      .catch(() => {
        setError(true);
      });
  }, []);

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-12">
          <h1>View Submitted Package</h1>
          {error && (
            <div className="col-md-12">
              <Alert
                icon={<MdCancel size={24} />}
                type="error"
                styling="bcgov-error-background"
                element="There was a problem with your request."
              />
            </div>
          )}
          <br />
          <h2>Package Details</h2>
          <br />
          <div className="row">
            <div className="col-sm-12 col-lg-6">
              <Table elements={packageDetails} />
            </div>
            <div className="col-sm-12 col-lg-6">
              <Table elements={courtFileDetails} />
            </div>
          </div>
          <br />
          <br />
          <section className="buttons pt-2">
            <Button
              label="Cancel and Return to Parent App"
              onClick={() => window.open("http://google.com", "_self")}
              styling="normal-white btn"
            />
          </section>
        </div>
      </div>
      <Footer />
    </main>
  );
}

PackageReview.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
    packageId: PropTypes.string.isRequired,
  }).isRequired,
};
