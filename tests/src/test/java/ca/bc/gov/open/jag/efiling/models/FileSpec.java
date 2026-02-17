package ca.bc.gov.open.jag.efiling.models;

public class FileSpec {

	private String filename;
	private String actionStatus;
	
	public FileSpec(String filename, String actionStatus) {
		this.filename = filename;
		this.actionStatus = actionStatus;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	public String getActionStatus() {
		return actionStatus;
	}
	
	public void setActionStatus(String actionStatus) {
		this.actionStatus = actionStatus;
	}
	
}
