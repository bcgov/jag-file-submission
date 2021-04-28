package ca.bc.gov.open.jag.efilingreviewerapi.extract.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.UUID;

public class Extract {

    private UUID id;
    private UUID transactionId;
    private Boolean useWebhook;

    public Extract(
            @JsonProperty("id") UUID id,
            @JsonProperty("transactionId") UUID transactionId,
            @JsonProperty("useWebhook") Boolean useWebhook) {
        this.id = id;
        this.transactionId = transactionId;
        this.useWebhook = useWebhook;
    }

    public Extract(Builder builder) {
        this.id = builder.id;
        this.transactionId = builder.transactionId;
        this.useWebhook = builder.useWebhook;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private UUID id;

        public Builder id(UUID id) {
            this.id = id;
            return this;
        }

        private UUID transactionId;

        public Builder transactionId(UUID transactionId) {
            this.transactionId = transactionId;
            return this;
        }

        private Boolean useWebhook;

        public Builder useWebhook(Boolean useWebhook) {
            this.useWebhook = useWebhook;
            return this;
        }

        public Extract create() {
            return new Extract(this);
        }

    }

    public UUID getId() {
        return id;
    }

    public UUID getTransactionId() {
        return transactionId;
    }

    public Boolean getUseWebhook() { return useWebhook; }
}
