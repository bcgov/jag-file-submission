package ca.bc.gov.open.jagefilingapi.qa.frontendutils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class JsonReader {

    public static Object[][] searchJsonElement(JsonArray jsonArray, int totalDataRow, int totalColumnEntry) {
        Object[][] matrix = new Object[totalDataRow][totalColumnEntry];
        int i = 0;
        int j = 0;

        if (jsonArray != null) {
            for (JsonElement jsonElement : jsonArray) {
                for (Map.Entry<String, JsonElement> entry : jsonElement.getAsJsonObject().entrySet()) {
                    matrix[i][j] = entry.getValue().toString().replace("\"", "");
                    j++;
                }
                i++;
                j = 0;
            }
        }
        return matrix;
    }

    public static Object[][] getData(String JSON_path, String typeData, int totalDataRow, int totalColumnEntry) throws FileNotFoundException {

        JsonObject jsonObj = JsonParser.parseReader(new FileReader(JSON_path)).getAsJsonObject();
        JsonArray array = (JsonArray) jsonObj.get(typeData);
        return searchJsonElement(array, totalDataRow, totalColumnEntry);
    }
}
