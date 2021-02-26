const documents = [
  {
    description: "file description 1",
    documentProperties: {
      name: "file name 1",
      type: "file type",
    },
    isAmendment: null,
    isSupremeCourtScheduling: null,
    mimeType: "application/pdf",
    statutoryFeeAmount: 40,
  },
  {
    description: "file description 2",
    documentProperties: {
      name: "file name 2",
      type: "file type",
    },
    isAmendment: false,
    isSupremeCourtScheduling: true,
    mimeType: "application/pdf",
    statutoryFeeAmount: 0,
  },
];

export function getDocumentsData() {
  return documents;
}
