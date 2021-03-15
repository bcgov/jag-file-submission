package ca.bc.gov.open.efilingdiligenclient.diligen.processor;

import ca.bc.gov.open.jag.efilingdiligenclient.api.model.Field;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.json.JSONException;
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
    public void test() throws IOException, JSONException {


        Path path = Paths.get("src/test/resources/courtDetails.schema.json");

        Path path2 = Paths.get("src/test/resources/diligen.answer.1.json");


        ObjectMapper mapper = new ObjectMapper();

        List<Field> response = mapper.readValue(new String(
                Files.readAllBytes(path2)), new TypeReference<List<Field>>(){});

        ObjectNode actual = sut.getJson(new String(
                Files.readAllBytes(path)), response);

      //  System.out.println(actual);

    }


}