package ca.bc.gov.open.jag.efilingcsostarter.config;


import ca.bc.gov.open.jag.efilingcommons.service.*;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcsoclient.config.CsoProperties;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.AccountDetailsMapper;
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
                .withPropertyValues("cso.fileServerHost=localhost",
                        "jag.efiling.soap.clients[0].client=ACCOUNT",
                        "jag.efiling.soap.clients[0].userName=username",
                        "jag.efiling.soap.clients[0].password=password",
                        "jag.efiling.soap.clients[0].uri=http://locahost",
                        "jag.efiling.soap.clients[1].client=ROLE",
                        "jag.efiling.soap.clients[1].userName=username",
                        "jag.efiling.soap.clients[1].password=password",
                        "jag.efiling.soap.clients[1].uri=http://locahost",
                        "jag.efiling.soap.clients[2].client=LOOKUP",
                        "jag.efiling.soap.clients[2].userName=username",
                        "jag.efiling.soap.clients[2].password=password",
                        "jag.efiling.soap.clients[2].uri=http://locahost",
                        "jag.efiling.soap.clients[3].client=STATUS",
                        "jag.efiling.soap.clients[3].userName=username",
                        "jag.efiling.soap.clients[3].password=password",
                        "jag.efiling.soap.clients[3].uri=http://locahost",
                        "jag.efiling.soap.clients[4].client=CSOWS",
                        "jag.efiling.soap.clients[4].userName=username",
                        "jag.efiling.soap.clients[4].password=password",
                        "jag.efiling.soap.clients[4].uri=http://locahost",
                        "jag.efiling.soap.clients[5].client=FILING",
                        "jag.efiling.soap.clients[5].userName=username",
                        "jag.efiling.soap.clients[5].password=password",
                        "jag.efiling.soap.clients[5].uri=http://locahost",
                        "jag.efiling.soap.clients[6].client=SERVICE",
                        "jag.efiling.soap.clients[6].userName=username",
                        "jag.efiling.soap.clients[6].password=password",
                        "jag.efiling.soap.clients[6].uri=http://locahost",
                        "jag.efiling.soap.clients[7].client=REPORT",
                        "jag.efiling.soap.clients[7].userName=username",
                        "jag.efiling.soap.clients[7].password=password",
                        "jag.efiling.soap.clients[7].uri=http://locahost",
                        "jag.efiling.soap.clients[8].client=SEARCH",
                        "jag.efiling.soap.clients[8].userName=username",
                        "jag.efiling.soap.clients[8].password=password",
                        "jag.efiling.soap.clients[8].uri=http://locahost")
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
            assertThat(it).hasSingleBean(EfilingReviewService.class);
            assertThat(it).hasSingleBean(EfilingSearchService.class);
            assertThat(it).hasSingleBean(AccountDetailsMapper.class);
        });

    }
}
