package ca.bc.gov.open.jag.efilingreviewerapi.document.models;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.DocumentConfig;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.PropertyConfig;

public class DocumentTypeConfiguration extends Auditable {

    @Id
    private UUID id;

    private String documentType;

    private String documentTypeDescription;

    private Integer projectId;

    private DocumentConfig documentConfig;

    public DocumentTypeConfiguration() {
        id = UUID.randomUUID();
    }

    public DocumentTypeConfiguration(
            @JsonProperty("id") UUID id,
            @JsonProperty("documentType") String documentType,
            @JsonProperty("documentTypeDescription") String documentTypeDescription,
            @JsonProperty("projectId") Integer projectId,
            @JsonProperty("formData") DocumentConfig formData) {
        this.id = id;
        this.documentType = documentType;
        this.documentTypeDescription = documentTypeDescription;
        this.projectId = projectId;
        this.documentConfig = formData;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getDocumentType() {
        return documentType;
    }

	public void setDocumentType(String documentType) {
		this.documentType = documentType;
	}

    public String getDocumentTypeDescription() { return documentTypeDescription;  }
    
    public void setDocumentTypeDescription(String documentTypeDescription) {
		this.documentTypeDescription = documentTypeDescription;
	}

    public Integer getProjectId() { return projectId; }

    public DocumentConfig getDocumentConfig() {
        return documentConfig;
    }
    
    public void setDocumentConfig(DocumentConfig documentConfig) {
		this.documentConfig = documentConfig;
	}

    public DocumentTypeConfiguration(Builder builder) {
        this();
        this.documentType = builder.documentType;
        this.documentTypeDescription = builder.documentTypeDescription;
        this.projectId = builder.projectId;
        this.documentConfig = builder.documentConfig;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String documentType;
        private String documentTypeDescription;
        private Integer projectId;
        private DocumentConfig documentConfig;

        public Builder documentType(String documentType) {
            this.documentType = documentType;
            return this;
        }

        public Builder documentTypeDescription(String documentTypeDescription) {
            this.documentTypeDescription = documentTypeDescription;
            return this;
        }

        public Builder projectId(Integer projectId) {
            this.projectId = projectId;
            return this;
        }

        public Builder documentConfig(DocumentConfig documentConfig) {
            this.documentConfig = documentConfig;
            return this;
        }

        public Builder documentConfig(LinkedHashMap<String, Object> documentConfigMap) {

            documentConfig = new DocumentConfig();

            for(Map.Entry<String, Object> entry: documentConfigMap.entrySet()) {

                if(StringUtils.equals("properties", entry.getKey())) {
                    documentConfig.setProperties(convertProperty((LinkedHashMap<String, Object>) entry.getValue()));
                }

            }

            return this;

        }

        private Map<String, PropertyConfig> convertProperty(LinkedHashMap<String, Object> propertyMap) {

            Map<String, PropertyConfig> propertyConfigMap = new LinkedHashMap<>();

            for(Map.Entry<String, Object> entry: propertyMap.entrySet()) {

                PropertyConfig propertyConfig = new PropertyConfig();

                LinkedHashMap<String, Object> propertyConfigMapInternal = (LinkedHashMap<String, Object>)entry.getValue();

                propertyConfig.setType(propertyConfigMapInternal.get("type").toString());

                if(StringUtils.equals("object", propertyConfig.getType())) {
                    propertyConfig.setProperties(convertProperty((LinkedHashMap<String, Object>)propertyConfigMapInternal.get("properties")));
                } else if (StringUtils.equals("string", propertyConfig.getType())) {
                    propertyConfig.setFieldId(Integer.parseInt(propertyConfigMapInternal.get("fieldId").toString()));
                }

                propertyConfigMap.put(entry.getKey(), propertyConfig);

            }

            return propertyConfigMap;
        }

        public DocumentTypeConfiguration create() {
            return new DocumentTypeConfiguration(this);
        }

    }

}
