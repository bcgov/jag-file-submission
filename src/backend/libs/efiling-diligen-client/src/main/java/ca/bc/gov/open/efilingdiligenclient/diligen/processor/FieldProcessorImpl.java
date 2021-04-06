package ca.bc.gov.open.efilingdiligenclient.diligen.processor;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.FormData;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.FormDataProperty;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.Field;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class FieldProcessorImpl implements FieldProcessor {

    public ObjectNode getJson(FormData formData, List<Field> fields) {

        ObjectMapper objectMapper = new ObjectMapper();

        final ObjectNode objectNode = objectMapper.createObjectNode();

        for (Map.Entry<String, FormDataProperty> property : formData.getProperties().entrySet()) {

            if(StringUtils.equals("object", property.getValue().getType())) {
                objectNode.set(property.getKey(), parseSchema(property.getValue(), fields));
            }

            if(StringUtils.equals("string", property.getValue().getType())) {
                objectNode.put(property.getKey(), extractStringValue(property.getValue(), fields));
            }
        }

        return objectNode;

    }

    private ObjectNode parseSchema(FormDataProperty formData, List<Field> fields) {

        ObjectMapper objectMapper = new ObjectMapper();

        final ObjectNode objectNode = objectMapper.createObjectNode();

        for (Map.Entry<String, FormDataProperty> property : formData.getProperties().entrySet()) {

            if(StringUtils.equals("object", property.getValue().getType())) {
                objectNode.set(property.getKey(), parseSchema(property.getValue(), fields));
            }

            if(StringUtils.equals("string", property.getValue().getType())) {
                objectNode.put(property.getKey(), extractStringValue(property.getValue(), fields));
            }
        }

        return objectNode;

    }

    private String extractStringValue(FormDataProperty formDataProperty, List<Field> fields) {

        Optional<List<String>> values = fields.stream().filter(x -> x.getId().equals(formDataProperty.getFieldId())).findFirst().map(x -> x.getValues());

        if(!values.isPresent()) return "";

        return values.get().stream().findFirst().orElse("");

    }


}
