package ca.bc.gov.open.jag.efilingdiligenclientstarter;


import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Represents the Diligen Configuration
 */
@ConfigurationProperties(prefix = "jag.efiling.diligen")
public class DiligenProperties {

    private String basePath;
    private String username;
    private String password;
    private Integer projectIdentifier;

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public String getUsername() {return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public Integer getProjectIdentifier() { return projectIdentifier; }

    public void setProjectIdentifier(Integer projectIdentifier) { this.projectIdentifier = projectIdentifier; }

}
