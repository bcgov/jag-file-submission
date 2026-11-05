package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingapi.fakes.EfilingDocumentServiceFake;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("DocumentConfiguration")
public class DocumentConfigurationTest {

    private DocumentConfiguration sut;

    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(
                    DocumentConfiguration.class)
            .withBean(EfilingDocumentServiceFake.class);


    @Test
    public void checkRegisteredBeans() {


        context.run(it -> {
            assertThat(it).hasSingleBean(DocumentStore.class);
            Assertions.assertEquals(DocumentStoreImpl.class, it.getBean(DocumentStore.class).getClass());

            assertThat(it).hasSingleBean(DocumentService.class);
            Assertions.assertEquals(DocumentServiceImpl.class, it.getBean(DocumentService.class).getClass());


        });

    }



}
