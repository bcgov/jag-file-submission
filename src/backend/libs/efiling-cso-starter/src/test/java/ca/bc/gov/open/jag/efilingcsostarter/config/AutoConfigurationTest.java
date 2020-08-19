package ca.bc.gov.open.jag.efilingcsostarter.config;


import ca.bc.gov.open.jag.efilingcommons.service.*;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.AccountDetailsMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AutoConfiguration Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class AutoConfigurationTest {

    ApplicationContextRunner context;

    private AutoConfiguration sut;

    @BeforeAll
    public void setup() {
        context = new ApplicationContextRunner()
                .withUserConfiguration(AutoConfiguration.class)
                .withPropertyValues("cso.fileServerHost=localhost")
                .withPropertyValues("jag.efiling.soap.clients[0].client=ACCOUNT")
                .withPropertyValues("jag.efiling.soap.clients[0].userName=username")
                .withPropertyValues("jag.efiling.soap.clients[0].password=password")
                .withPropertyValues("jag.efiling.soap.clients[0].uri=http://locahost")
                .withPropertyValues("jag.efiling.soap.clients[1].client=ROLE")
                .withPropertyValues("jag.efiling.soap.clients[1].userName=username")
                .withPropertyValues("jag.efiling.soap.clients[1].password=password")
                .withPropertyValues("jag.efiling.soap.clients[1].uri=http://locahost")
                .withPropertyValues("jag.efiling.soap.clients[2].client=LOOKUP")
                .withPropertyValues("jag.efiling.soap.clients[2].userName=username")
                .withPropertyValues("jag.efiling.soap.clients[2].password=password")
                .withPropertyValues("jag.efiling.soap.clients[2].uri=http://locahost")
                .withPropertyValues("jag.efiling.soap.clients[3].client=STATUS")
                .withPropertyValues("jag.efiling.soap.clients[3].userName=username")
                .withPropertyValues("jag.efiling.soap.clients[3].password=password")
                .withPropertyValues("jag.efiling.soap.clients[3].uri=http://locahost")
                .withPropertyValues("jag.efiling.soap.clients[4].client=CSOWS")
                .withPropertyValues("jag.efiling.soap.clients[4].userName=username")
                .withPropertyValues("jag.efiling.soap.clients[4].password=password")
                .withPropertyValues("jag.efiling.soap.clients[4].uri=http://locahost")
                .withPropertyValues("jag.efiling.soap.clients[5].client=FILING")
                .withPropertyValues("jag.efiling.soap.clients[5].userName=username")
                .withPropertyValues("jag.efiling.soap.clients[5].password=password")
                .withPropertyValues("jag.efiling.soap.clients[5].uri=http://locahost")
                .withPropertyValues("jag.efiling.soap.clients[6].client=SERVICE")
                .withPropertyValues("jag.efiling.soap.clients[6].userName=username")
                .withPropertyValues("jag.efiling.soap.clients[6].password=password")
                .withPropertyValues("jag.efiling.soap.clients[6].uri=http://locahost")
                .withUserConfiguration(CsoProperties.class);
    }

    @Test
    @DisplayName("Test that beans are created")
    public void testBeansAreGenerated() {

        context.run(it -> {
            assertThat(it).hasSingleBean(EfilingAccountService.class);
            assertThat(it).hasSingleBean(EfilingDocumentService.class);
            assertThat(it).hasSingleBean(EfilingLookupService.class);
            assertThat(it).hasSingleBean(EfilingCourtService.class);
            assertThat(it).hasSingleBean(EfilingSubmissionService.class);
            assertThat(it).hasSingleBean(AccountDetailsMapper.class);
        });

    }
}
