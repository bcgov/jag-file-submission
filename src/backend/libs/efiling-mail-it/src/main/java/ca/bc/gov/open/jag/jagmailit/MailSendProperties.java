package ca.bc.gov.open.jag.jagmailit;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "mailsend")
public class MailSendProperties {

	private String hostName;
	private Integer port;

	public String getHostName() {
		return hostName;
	}
	
	public Integer getPort() {
		return port;
	}
	
	public void setHostName(String hostName) {
		this.hostName = hostName;
	}
	
	public void setPort(Integer port) {
		this.port = port;
	}

	public String getServer() {
		return getHostName() + ":" + getPort();
	}

}
