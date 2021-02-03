const documents = [
  {
    documentProperties: {
      name: "file name 1",
      type: "file type",
    },
    description: "file description 1",
    statutoryFeeAmount: 40,
    isAmendment: null,
    isSupremeCourtScheduling: null,
  },
  {
    documentProperties: {
      name: "file name 2",
      type: "file type",
    },
    description: "file description 2",
    statutoryFeeAmount: 0,
    isAmendment: false,
    isSupremeCourtScheduling: true,
  },
];

export function getDocumentsData() {
  return documents;
}
