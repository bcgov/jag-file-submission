package ca.bc.gov.open.jag.efilingapi.filingpackage.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "parents")
public class ParentProperties {

    private List<ParentAppProperties> parents;

    public List<ParentAppProperties> getParents() { return parents; }

    public void setParents(List<ParentAppProperties> parents) { this.parents = parents; }

}
