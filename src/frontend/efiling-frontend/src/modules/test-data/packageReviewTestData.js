export const documents = [
  {
    identifier: "1",
    documentProperties: {
      type: "AFF",
      name: "test-document.pdf",
    },
    status: {
      description: "Submitted",
      code: "SUB",
    },
    filingDate: "2020-05-05T00:00:00.000Z",
  },
];

export const protectionOrderDocuments = [
  {
    identifier: "1",
    documentProperties: {
      type: "POR",
      name: "test-POR-document.pdf",
    },
    status: {
      description: "Submitted",
      code: "SUB",
    },
    rushRequired: true,
    filingDate: "2020-05-05T00:00:00.000Z",
  },
];

export const parties = [
  {
    partyType: "IND",
    roleType: "APP",
    firstName: "Bob",
    middleName: "Q",
    lastName: "Ross",
    roleDescription: "Applicant",
    partyDescription: "Individual",
  },
];

export const organizationParties = [
  {
    name: "The Q Continuum",
    roleDescription: "Applicant",
    partyDescription: "Organization",
  },
];

export const payments = [
  {
    feeExempt: false,
    paymentCategory: 1,
    processedAmount: 7,
    submittedAmount: 7,
    serviceIdentifier: 0,
    transactionDate: null,
  },
];
