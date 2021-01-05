package ca.bc.gov.open;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.MessageFormat;
import java.util.Map;
import java.util.HashMap;

public class PayloadHelper {


    public static String generateUrlPayload(String documentName) {

        return "{\n" +
                "    \"navigationUrls\": {\n" +
                "        \"success\": \"http//somewhere.com\",\n" +
                "        \"error\": \"http//somewhere.com\",\n" +
                "        \"cancel\": \"http//somewhere.com\"\n" +
                "    },\n" +
                "    \"clientAppName\": \"my app\",\n" +
                "    \"filingPackage\": {\n" +
                "        \"court\": {\n" +
                "            \"location\": \"1211\",\n" +
                "            \"level\": \"P\",\n" +
                "            \"courtClass\": \"F\"\n" +
                "        },\n" +
                "        \"documents\": [\n" +
                "            {\n" +
                "                \"name\": \"" + documentName + "\",\n" +
                "                \"type\": \"AFF\",\n" +
                "                \"statutoryFeeAmount\": 0,\n" +
                "                \"data\": {},\n" +
                "                \"md5\": \"string\"\n" +
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


}
