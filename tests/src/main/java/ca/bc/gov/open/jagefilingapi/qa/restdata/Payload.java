package ca.bc.gov.open.jagefilingapi.qa.restdata;

public class Payload {

    public static String submitDocumentProperties() {
        return "{\n" +
                "  \"documentProperties\": {\n" +
                "    \"type\": \"string\",\n" +
                "    \"subType\": \"string\",\n" +
                "    \"submissionAccess\": {\n" +
                "      \"url\": \"string\",\n" +
                "      \"verb\": \"GET\",\n" +
                "      \"headers\": {\n" +
                "        \"additionalProp1\": \"string\",\n" +
                "        \"additionalProp2\": \"string\",\n" +
                "        \"additionalProp3\": \"string\"\n" +
                "      }\n" +
                "    }\n" +
                "  },\n" +
                "  \"navigation\": {\n" +
                "    \"success\": {\n" +
                "      \"url\": \"string\"\n" +
                "    },\n" +
                "    \"error\": {\n" +
                "      \"url\": \"string\"\n" +
                "    },\n" +
                "    \"cancel\": {\n" +
                "      \"url\": \"string\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }
}
