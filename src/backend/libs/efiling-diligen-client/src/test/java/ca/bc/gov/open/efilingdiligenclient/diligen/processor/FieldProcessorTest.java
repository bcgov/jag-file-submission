package ca.bc.gov.open.efilingdiligenclient.diligen.processor;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.FormData;
import ca.bc.gov.open.jag.efilingdiligenclient.api.model.Field;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class FieldProcessorTest {

    private FieldProcessorImpl sut;

    @BeforeAll
    public void beforeAll() {
        sut = new FieldProcessorImpl();
    }

    @Test
    public void testProduct() throws IOException {

        Path path = Paths.get("src/test/resources/formData.json");

        Path path2 = Paths.get("src/test/resources/diligen.answer.1.json");

        ObjectMapper mapper = new ObjectMapper();

        FormData formData = mapper.readValue(new String(Files.readAllBytes(path)), FormData.class);

        List<Field> response = mapper.readValue(new String(
                Files.readAllBytes(path2)), new TypeReference<List<Field>>(){});

        ObjectNode actual = sut.getJson(formData, response);

        String test = "1";

    }

}
