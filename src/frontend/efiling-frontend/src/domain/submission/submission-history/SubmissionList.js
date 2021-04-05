import React from "react";
import PropTypes from "prop-types";
import moment from "moment";
import "./SubmissionList.scss";
import { isClick, isEnter } from "../../../modules/helpers/eventUtil";

export default function SubmissionList({ submissions }) {
  function handlePackageSelection(packageNumber, e) {
    if (isClick(e) || isEnter(e)) {
      window.open(`/efilinghub/packagereview/${packageNumber}`, "_blank");
    }
  }

  return (
    <div className="ct-submission-list">
      <div className="table-responsive">
        <table className="table">
          <thead>
            <tr>
              <th scope="col">Package #</th>
              <th scope="col">Date Submitted</th>
              <th scope="col">Application</th>
              <th scope="col">Court Location</th>
            </tr>
          </thead>

          <tbody>
            {submissions &&
              submissions.map((submission) => (
                <tr className="table-row" key={submission.packageNumber}>
                  <td className="border-left">
                    <span
                      className="file-href"
                      role="button"
                      tabIndex={0}
                      onKeyDown={(e) => {
                        handlePackageSelection(submission.packageNumber, e);
                      }}
                      onClick={(e) => {
                        handlePackageSelection(submission.packageNumber, e);
                      }}
                    >
                      {submission.packageNumber}
                    </span>
                  </td>
                  <td>
                    {moment(submission.submittedDate).format("DD-MMM-YYYY")}
                  </td>
                  <td>xxxxx</td>
                  <td className="border-right">{submission.court.location}</td>
                </tr>
              ))}
          </tbody>
        </table>
      </div>
    </div>
  );
}

SubmissionList.propTypes = {
  submissions: PropTypes.arrayOf(PropTypes.object),
};

SubmissionList.defaultProps = {
  submissions: null,
};
