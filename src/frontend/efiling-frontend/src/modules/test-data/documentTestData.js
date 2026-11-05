const documents = [
  {
    description: "file description 1",
    documentProperties: {
      name: "file name 1",
      type: "file type",
    },
    name: "file name 1",
    isAmendment: null,
    isSupremeCourtScheduling: null,
    mimeType: "application/pdf",
    statutoryFeeAmount: 40,
    actionDocument: {
      status: "SUB",
    },
  },
  {
    description: "file description 2",
    documentProperties: {
      name: "file name 2",
      type: "file type",
    },
    name: "file name 2",
    isAmendment: false,
    isSupremeCourtScheduling: true,
    mimeType: "application/pdf",
    statutoryFeeAmount: 0,
    actionDocument: {
      status: "REJ",
    },
  },
];

const duplicateDocuments = [
  {
    description: "file description 1",
    documentProperties: {
      name: "file name 1",
      type: "file type",
    },
    name: "file name 1",
    isAmendment: null,
    isSupremeCourtScheduling: null,
    mimeType: "application/pdf",
    statutoryFeeAmount: 40,
  },
  {
    description: "file description 1",
    documentProperties: {
      name: "file name 1",
      type: "file type",
    },
    name: "file name 1",
    isAmendment: null,
    isSupremeCourtScheduling: null,
    mimeType: "application/pdf",
    statutoryFeeAmount: 40,
  },
];

const jsonDocuments = [
  {
    description: "file description 1",
    documentProperties: {
      name: "ping3.json",
      type: "file type",
    },
    name: "ping3.json",
    isAmendment: null,
    isSupremeCourtScheduling: null,
    mimeType: "application/json",
    statutoryFeeAmount: 40,
  },
];

const porDocuments = [
  {
    description: "file description 1",
    documentProperties: {
      name: "file name 1",
      type: "POR",
    },
    name: "file name 1",
    isAmendment: null,
    isSupremeCourtScheduling: null,
    mimeType: "application/pdf",
    statutoryFeeAmount: 40,
    rushRequired: true,
    actionDocument: {
      status: "SUB",
    },
  },
  {
    description: "file description 2",
    documentProperties: {
      name: "file name 2",
      type: "POR",
    },
    name: "file name 2",
    isAmendment: false,
    isSupremeCourtScheduling: true,
    mimeType: "application/pdf",
    statutoryFeeAmount: 0,
    rushRequired: true,
    actionDocument: {
      status: "REJ",
    },
  },
];

export function getDocumentsData() {
  return documents;
}

export function getDuplicateDocumentsData() {
  return duplicateDocuments;
}

export function getJsonDocumentsData() {
  return jsonDocuments;
}

export function getPorDocumentsData() {
  return porDocuments;
}
