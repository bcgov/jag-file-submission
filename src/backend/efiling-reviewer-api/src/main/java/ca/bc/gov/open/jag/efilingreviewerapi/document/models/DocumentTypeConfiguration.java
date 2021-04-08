package ca.bc.gov.open.jag.efilingreviewerapi.document.models;

import ca.bc.gov.open.efilingdiligenclient.diligen.model.DocumentConfig;
import ca.bc.gov.open.efilingdiligenclient.diligen.model.PropertyConfig;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.annotation.Id;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

public class DocumentTypeConfiguration {

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

    public String getDocumentTypeDescription() { return documentTypeDescription;  }

    public Integer getProjectId() { return projectId; }

    public DocumentConfig getDocumentConfig() {
        return documentConfig;
    }

    public DocumentTypeConfiguration(Builder builder) {
        this();
        this.documentType = builder.documentType;
        this.documentConfig = builder.documentConfig;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private String documentType;
        private DocumentConfig documentConfig;

        public Builder documentType(String documentType) {
            this.documentType = documentType;
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
