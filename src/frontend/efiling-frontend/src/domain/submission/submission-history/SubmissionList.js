import React from "react";
import PropTypes from "prop-types";
import "./SubmissionList.scss";

export default function SubmissionList({ submissions }) {
  return (
    <div className="ct-submission-list">
      <div className="table-responsive">
        <table className="table">
          <thead>
            <tr>
              <th scope="col">Package #</th>
              <th scope="col">Date Submitted</th>
              <th scope="col">Application</th>
              <th scope="col">Doc. Type</th>
              <th scope="col">Status</th>
              <th scope="col">Court Location</th>
            </tr>
          </thead>

          <tbody>
            {submissions &&
              submissions.map((submission) => (
                <tr className="table-row">
                  <td className="border-left">
                    <span className="file-href" role="button" tabIndex={0}>
                      {submission.packageNumber}
                    </span>
                  </td>
                  <td>{submission.submittedDate}</td>
                  <td>xxxxx</td>
                  <td>xxxxx</td>
                  <td>{submission.status.description}</td>
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
