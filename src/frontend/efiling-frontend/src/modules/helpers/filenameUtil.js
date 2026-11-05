/**
 * Checks an array of droppedFiles to see if any of their file names match any file in previousFiles.
 * @param droppedFiles An array of file objects containing a name property. Represents new files not previously added.
 * @param previousFiles An array of file objects containing a name property. Represents a list of files previously added, and should not contain duplicate file names.
 * @returns {boolean} true if a name is found in dropped files that is a duplicate of a name in previous files, false if no duplicates are found.
 */
export const checkForDuplicateFilenames = (droppedFiles, previousFiles) => {
  let isDuplicate = false;
  for (let i = 0; i < previousFiles.length; i += 1) {
    isDuplicate = droppedFiles.some(
      (droppedFile) => droppedFile.name === previousFiles[i].name
    );
    if (isDuplicate) return isDuplicate;
  }

  return isDuplicate;
};
