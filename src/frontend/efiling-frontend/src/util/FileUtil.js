export const checkRejectedFiles = (files, setHasRejectedDocuments) => {
  let hasRejectedDoc = false;
  if (files && files.length > 0) {
    files.forEach((file) => {
      if (file.actionDocument && file.actionDocument.status === "REJ") {
        hasRejectedDoc = true;
      }
    });
  }
  setHasRejectedDocuments(hasRejectedDoc);
};
