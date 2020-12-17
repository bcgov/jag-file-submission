package ca.bc.gov.open.jag.efilingcsostarter.config;

import ca.bc.gov.open.jag.efilingcommons.model.Clients;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingSoapClientProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Optional;

@ConfigurationProperties(prefix = "jag.efiling.soap")
public class SoapProperties {
    private List<EfilingSoapClientProperties> clients;

    public List<EfilingSoapClientProperties> getClients() {
        return clients;
    }

    public void setClients(List<EfilingSoapClientProperties> clients) {
        this.clients = clients;
    }

    public EfilingSoapClientProperties findByEnum(Clients client) {
        Optional<EfilingSoapClientProperties> efilingSoapClientProperties = clients.stream().filter(c ->
                c.getClient() == client).findFirst();
        return efilingSoapClientProperties.orElse(null);
    }
}
