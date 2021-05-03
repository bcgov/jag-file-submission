package ca.bc.gov.open.jag.efilingreviewerapi.document.models;

import java.util.Date;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Version;

/**
 * An abstract Auditable class that auto-populates <code>createdDate</code>, <code>modifiedDate</code>, and
 * <code>version</code> fields. Class need only to extend this class to add auditing fields to a model object.
 */
public abstract class Auditable {

	@CreatedDate
	protected Date createdDate;

	@LastModifiedDate
	protected Date modifiedDate;

	@Version
	private Integer version;

	public Date getCreatedDate() {
		return createdDate == null ? new Date() : createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

}
