package ca.bc.gov.open.efilingdiligenclient.diligen.processor;

import ca.bc.gov.open.jag.efilingdiligenclient.api.model.Field;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.everit.json.schema.ObjectSchema;
import org.everit.json.schema.Schema;
import org.everit.json.schema.StringSchema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FieldProcessorImpl implements FieldProcessor {

    public ObjectNode getJson(String jsonSchema, List<Field> fields) {

        JSONObject jsonSubject = new JSONObject(new JSONTokener(jsonSchema));

        ObjectSchema schema = (ObjectSchema) SchemaLoader.load(jsonSubject);

        return parseSchema(schema, fields);

    }

    private ObjectNode parseSchema(ObjectSchema jsonSchema, List<Field> fields) {

        ObjectMapper objectMapper = new ObjectMapper();

        final ObjectNode objectNode = objectMapper.createObjectNode();

        for (Map.Entry<String, Schema> objectSchemaMap : jsonSchema.getPropertySchemas().entrySet()) {

            final Schema schema = objectSchemaMap.getValue();

            if(schema instanceof ObjectSchema) {
                objectNode.set(objectSchemaMap.getKey(), parseSchema((ObjectSchema) schema, fields));
            }

            if(schema instanceof StringSchema) {
                objectNode.put(objectSchemaMap.getKey(), extractStringValue(schema, fields));
            }

        }

        return objectNode;

    }

    private String extractStringValue(Schema schema, List<Field> fields) {

        int id = Integer.parseInt(schema.getUnprocessedProperties().get("fieldId").toString());

        Optional<List<String>> values = fields.stream().filter(x -> x.getId().equals(id)).findFirst().map(x -> x.getValues());

        if(!values.isPresent()) return "";

        return values.get().stream().findFirst().orElse("");

    }


}
