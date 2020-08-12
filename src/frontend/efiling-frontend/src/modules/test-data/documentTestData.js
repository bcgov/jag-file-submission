const documents = [
  {
    name: "file name 1",
    description: "file description 1",
    type: "file type",
    statutoryFeeAmount: 40,
    isAmendment: null,
    isSupremeCourtScheduling: null,
  },
  {
    name: "file name 2",
    description: "file description 2",
    type: "file type",
    statutoryFeeAmount: 0,
    isAmendment: false,
    isSupremeCourtScheduling: true,
  },
];

export function getDocumentsData() {
  return documents;
}
