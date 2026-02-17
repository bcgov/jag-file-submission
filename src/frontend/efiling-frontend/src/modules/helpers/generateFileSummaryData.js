import Dinero from "dinero.js";
import React from "react";

function calculateTotalStatFee(files) {
  let totalStatFee = Dinero({ amount: 0 });
  files.forEach((file) => {
    totalStatFee = totalStatFee.add(
      Dinero({
        amount: parseInt((file.statutoryFeeAmount * 100).toFixed(0), 10),
      })
    );
  });

  return totalStatFee;
}

function calculateTotalFee(totalStatFee, submissionFee) {
  const total = totalStatFee.add(
    Dinero({
      amount: parseInt((submissionFee * 100).toFixed(0), 10),
    })
  );

  return total;
}

function generateTableContent(
  isRush,
  numDocuments,
  totalStatFee,
  submissionFee,
  hasPorDocument
) {
  const rushFeatureFlag = window.env
    ? window.env.REACT_APP_RUSH_TAB_FEATURE_FLAG
    : process.env.REACT_APP_RUSH_TAB_FEATURE_FLAG;
  if (rushFeatureFlag === "true") {
    return [
      {
        name: "Rush Processing:",
        value:
          isRush || hasPorDocument ? (
            <span style={{ color: "red" }}>
              <b>Yes</b>
            </span>
          ) : (
            <span>
              <b>No</b>
            </span>
          ),
        isValueBold: true,
      },
      {
        name: "Number of Documents in Package:",
        value: `${numDocuments}`,
        isValueBold: true,
      },
      {
        name: "Statutory Fees:",
        value: totalStatFee.toFormat("$0,0.00"),
        isValueBold: true,
      },
      {
        name: "Submission Fee:",
        value: Dinero({
          amount: parseInt((submissionFee * 100).toFixed(0), 10),
        }).toFormat("$0,0.00"),
        isValueBold: true,
      },
    ];
  }
  return [
    {
      name: "Number of Documents in Package:",
      value: `${numDocuments}`,
      isValueBold: true,
    },
    {
      name: "Statutory Fees:",
      value: totalStatFee.toFormat("$0,0.00"),
      isValueBold: true,
    },
    {
      name: "Submission Fee:",
      value: Dinero({
        amount: parseInt((submissionFee * 100).toFixed(0), 10),
      }).toFormat("$0,0.00"),
      isValueBold: true,
    },
  ];
}

export function generateFileSummaryData(
  isRush,
  files,
  submissionFee,
  withTotal,
  hasPorDocument
) {
  const totalStatFee = calculateTotalStatFee(files);
  const totalOverallFee = calculateTotalFee(totalStatFee, submissionFee);
  const numDocuments = files.length;
  const result = generateTableContent(
    isRush,
    numDocuments,
    totalStatFee,
    submissionFee,
    hasPorDocument
  );

  if (withTotal) {
    result.push(
      { name: "", value: "", isEmptyRow: true },
      {
        name: "Total Fee:",
        value: totalOverallFee.toFormat("$0,0.00"),
        isValueBold: true,
      }
    );
  }

  return {
    data: result,
    totalFee: totalOverallFee.getAmount(),
  };
}
