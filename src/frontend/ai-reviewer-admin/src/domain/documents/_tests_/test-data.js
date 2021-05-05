export const configurations = [
  {
    id: "0866d655-4c75-419d-a552-849ee3c229bc",
    documentType: { type: "RCC", description: "Response to Civil Claim" },
    projectId: 2,
    documentConfig: {
      properties: {
        court: {
          type: "object",
          fieldId: null,
          properties: {
            location: { type: "string", fieldId: 230, properties: {} },
            level: { type: "string", fieldId: 229, properties: {} },
            courtClass: { type: "string", fieldId: 234, properties: {} },
            division: { type: "string", fieldId: 210, properties: {} },
            fileNumber: { type: "string", fieldId: 228, properties: {} },
            participatingClass: {
              type: "string",
              fieldId: 210,
              properties: {},
            },
            agencyId: { type: "string", fieldId: 210, properties: {} },
            locationDescription: {
              type: "string",
              fieldId: 210,
              properties: {},
            },
            levelDescription: { type: "string", fieldId: 210, properties: {} },
            classDescription: { type: "string", fieldId: 210, properties: {} },
          },
        },
      },
    },
  },
  {
    id: "6550866d-754c-9d41-52a5-c229bc849ee3",
    documentType: { type: "SG1", description: "Stargate One Report" },
    projectId: 2,
    documentConfig: {
      properties: {
        court: {
          type: "object",
          fieldId: null,
          properties: {
            location: {
              type: "string",
              fieldId: 230,
              properties: {},
            },
          },
        },
      },
    },
  },
  {
    id: "927198da-e807-4bb2-b3bd-30dfcb179f5d",
    documentType: {
      type: "DEVRCC",
      description: "Response to Civil Claim"
    },
    projectId: 21,
    documentConfig: {
      properties: {
        document: {
          type: "object",
          fieldId: null,
          properties: {
            documentType: {
              type: "string",
              fieldId: 634,
              properties: {}
            },
            dateFiled: {
              type: "string",
              fieldId: 635,
              properties: {}
            },
            filedBy: {
              type: "string",
              fieldId: 636,
              properties: {}
            }
          }
        },
        court: {
          type: "object",
          fieldId: null,
          properties: {
            location: {
              type: "string",
              fieldId: 633,
              properties: {}
            },
            level: {
              type: "string",
              fieldId: 631,
              properties: {}
            },
            class: {
              type: "string",
              fieldId: 632,
              properties: {}
            },
            division: {
              type: "string",
              fieldId: 210,
              properties: {}
            },
            fileNumber: {
              type: "string",
              fieldId: 630,
              properties: {}
            }
          }
        },
        parties: {
          type: "object",
          fieldId: null,
          properties: {
            defendants: {
              type: "string",
              fieldId: 648,
              properties: {}
            },
            plaintiffs: {
              type: "string",
              fieldId: 649,
              properties: {}
            }
          }
        }
      }
    }
  }
];
