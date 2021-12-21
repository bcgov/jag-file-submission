import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { getDocumentTypes } from "../DocumentService";

describe("Document service test suite", () => {
  let mock;

  beforeEach(() => {
    mock = new MockAdapter(axios);

    const params = {
      courtLevel: "A",
      courtClassification: "B",
    };

    mock.onGet("/documents/types", params).reply(200, [
      { type: "AFF", description: "Affidavit" },
      { type: "AAS", description: "Affidavit of Attempted Service" },
      { type: "CCB", description: "Case Conference Brief" },
    ]);
  });

  test("OK: service should return a list of documents", async () => {
    const actual = await getDocumentTypes("A", "B");

    expect(actual.length).toEqual(3);
    expect(actual[0].type).toEqual("AFF");
    expect(actual[0].description).toEqual("Affidavit");
  });
});
