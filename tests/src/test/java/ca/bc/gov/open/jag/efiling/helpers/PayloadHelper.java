package ca.bc.gov.open.jag.efiling.helpers;

import ca.bc.gov.open.jag.efiling.models.FileSpec;

public class PayloadHelper {

    private PayloadHelper() {
    }

    public static String generateUrlPayload(FileSpec... fileSpecs) {

    	StringBuffer sb = new StringBuffer();
        sb.append(
        		"{\n" +
		        "    \"navigationUrls\": {\n" +
		        "        \"success\": \"http//somewhere.com\",\n" +
		        "        \"error\": \"http//somewhere.com\",\n" +
		        "        \"cancel\": \"http//somewhere.com\"\n" +
		        "    },\n" +
		        "    \"clientAppName\": \"my app\",\n" +
		        "    \"filingPackage\": {\n" +
		        "       \"packageIdentifier\": 1,\n" +
		        "        \"court\": {\n" +
		        "            \"location\": \"1211\",\n" +
		        "            \"level\": \"P\",\n" +
		        "            \"courtClass\": \"F\",\n" +
		        "            \"fileNumber\": \"1\"\n" +
		        "        },\n" +
		        "        \"documents\": [\n");
        
        for (int i = 0; i < fileSpecs.length; i++) {
            if (i > 0) {
            	sb.append(",");
            }
            sb.append(
	            "            {\n" +
	            "                \"name\": \"" + fileSpecs[i].getFilename() + "\",\n" +
	            "                \"type\": \"AFF\",\n" +
	            "                \"statutoryFeeAmount\": 0,\n" +
	            "                \"data\": {},\n" +
	            "                \"md5\": \"string\", \n" +
	            "                \"actionDocument\": {\n" +
	            "                   \"id\": 2,\n" +
	            "                   \"status\": \"" + fileSpecs[i].getActionStatus() + "\",\n" +
	            "                   \"type\": \"F\"\n" +
	            "               }\n" +
	            "            }\n");
		}
        
        sb.append(
                "        ],\n" +
                "        \"parties\": [\n" +
                "            {\n" +
                "                \"partyType\": \"IND\",\n" +
                "                \"roleType\": \"APP\",\n" +
                "                \"firstName\": \"first\",\n" +
                "                \"middleName\": \"middle\",\n" +
                "                \"lastName\": \"last\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}");
        
    	return sb.toString();

    }

    public static String generateUrlPayloadWithInvalidFileNo(String documentName) {

        return "{\n" +
                "    \"navigationUrls\": {\n" +
                "        \"success\": \"http//somewhere.com\",\n" +
                "        \"error\": \"http//somewhere.com\",\n" +
                "        \"cancel\": \"http//somewhere.com\"\n" +
                "    },\n" +
                "    \"clientAppName\": \"my app\",\n" +
                "    \"filingPackage\": {\n" +
                "       \"packageIdentifier\": 1,\n" +
                "        \"court\": {\n" +
                "            \"location\": \"1211\",\n" +
                "            \"level\": \"A\",\n" +
                "            \"courtClass\": \"F\",\n" +
                "            \"fileNumber\": \"3\"\n" +
                "        },\n" +
                "        \"documents\": [\n" +
                "            {\n" +
                "                \"name\": \"" + documentName + "\",\n" +
                "                \"type\": \"AFF\",\n" +
                "                \"statutoryFeeAmount\": 0,\n" +
                "                \"data\": {},\n" +
                "                \"md5\": \"string\", \n" +
                "                \"actionDocument\": {\n" +
                "                   \"id\": 2,\n" +
                "                   \"status\": \"SUB\",\n" +
                "                   \"type\": \"F\"\n" +
                "               }\n" +
                "            }\n" +
                "        ],\n" +
                "        \"parties\": [\n" +
                "            {\n" +
                "                \"partyType\": \"IND\",\n" +
                "                \"roleType\": \"APP\",\n" +
                "                \"firstName\": \"first\",\n" +
                "                \"middleName\": \"middle\",\n" +
                "                \"lastName\": \"last\"\n" +
                "            }\n" +
                "        ]\n" +
                "    }\n" +
                "}";

    }

    public static String updateDocumentProperties(String documentName) {

        return "{" +
                "        \"documents\": [" +
                "            {" +
                "                \"name\": \"" + documentName + "\"," +
                "                \"type\": \"AAB\"," +
                "                \"isAmendment\": \"true\"," +
                "                \"isSupremeCourtScheduling\": \"true\"" +
                "            }" +
                "        ]" +
                "}";

    }

   public static String addRushProcessing() {

       return  " {" +
               "      \"rushType\":\"rule\"," +
               "      \"firstName\":\"Efile\"," +
               "      \"lastName\":\"Tester\"," +
               "      \"organization\":\"AG\"," +
               "      \"phoneNumber\":\"2365498745\"," +
               "      \"email\":\"efiletester@test.com\"," +
               "      \"country\":\"Canada\"," +
               "      \"countryCode\":\"1\"," +
               "      \"reason\":\"Urgent need to process\"," +
               "      \"status\":\"Processing\"," +
               "      \"supportingDocuments\":[\n" +
               "                           {\n" +
               "      \"fileName\":\"test-document.pdf\"," +
               "      \"identifier\":\"9b35f5d6-50e9-4cd5-9d46-8ce1f9e484c9\"" +
               "              }\n" +
               "           ]\n" +
               " }";
   }
}
