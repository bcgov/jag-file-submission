package ca.bc.gov.open.efilingdiligenclient.diligen.processor;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.DocumentConfig;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.Field;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.List;

public interface FieldProcessor {

    ObjectNode getJson(DocumentConfig formData, List<Field> fields);

}
