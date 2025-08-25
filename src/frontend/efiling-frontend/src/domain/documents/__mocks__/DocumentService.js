/* eslint-disable prefer-promise-reject-errors */
const data = [
  { type: "AFF", description: "Affidavit" },
  { type: "AAS", description: "Affidavit of Attempted Service" },
  { type: "CCB", description: "Case Conference Brief" },
];

/**
 * Retrieves an array of document types
 */
export const getDocumentTypes = async (courtLevel, courtClassification) => {
  const success = courtLevel === "P" && courtClassification === "F";

  return new Promise((resolve, reject) => {
    process.nextTick(() =>
      success
        ? resolve(data)
        : reject({
            status: 400,
            message: "There was an error.",
          })
    );
  });
};
