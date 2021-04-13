import api from "axiosConfig";
import MockAdapter from "axios-mock-adapter";
import { getDocumentTypeConfigurations } from "domain/documents/DocumentService";
import { configurations } from "domain/documents/_tests_/DocumentData";

describe("Document Service test suite", () => {
  let mock;
  beforeEach(() => {
    mock = new MockAdapter(api);
  });

  test("getDocumentTypeConfigurations 200", async () => {
    mock.onGet("/documentTypeConfigurations").reply(200, configurations);

    const response = await getDocumentTypeConfigurations();
    expect(response).toMatchObject(configurations);
  });

  test("getDocumentTypeConfigurations 401", async () => {
    mock.onGet("/documentTypeConfigurations").reply(401, configurations);

    try {
      await getDocumentTypeConfigurations();
    } catch (error) {
      expect(error.message).toEqual("Request failed with status code 401");
      expect(error.response.status).toEqual(401);
    }
  });
});
