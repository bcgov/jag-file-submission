package ca.bc.gov.open.jag.efilingapi.config;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.keycloak.adapters.KeycloakConfigResolver;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class KeycloakSecurityConfigTest {

    ApplicationContextRunner context = new ApplicationContextRunner()
            .withUserConfiguration(KeycloakSecurityConfig.class);

    @Test
    public void testConfigure() throws Exception {

        context.run(it -> {
            assertThat(it).hasSingleBean(SessionAuthenticationStrategy.class);
            assertThat(it).hasSingleBean(KeycloakConfigResolver.class);
        });

    }

}
