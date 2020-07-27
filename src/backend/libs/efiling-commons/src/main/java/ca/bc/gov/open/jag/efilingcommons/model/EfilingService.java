package ca.bc.gov.open.jag.efilingcommons.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;

public class EfilingService {
    private BigDecimal accountId;
    private BigDecimal clientId;
    private String clientReferenceTxt;
    private String courtFileNumber;
    private String documentsProcessed;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar entryDateTime;
    private String entryUserId;
    private BigDecimal serviceId;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar serviceReceivedDateTime;
    private String serviceReceivedDtmText;
    private BigDecimal serviceSessionId;
    private String serviceSubtypeCd;
    private String serviceTypeCd;
    private String serviceTypeDesc;
    private String styleOfCause;
    @XmlSchemaType(name = "dateTime")
    private XMLGregorianCalendar updateDateTime;
    private String updateUserId;
    private BigDecimal userSessionId;

    public BigDecimal getAccountId() {
        return accountId;
    }

    public void setAccountId(BigDecimal accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getClientId() {
        return clientId;
    }

    public void setClientId(BigDecimal clientId) {
        this.clientId = clientId;
    }

    public String getClientReferenceTxt() {
        return clientReferenceTxt;
    }

    public void setClientReferenceTxt(String clientReferenceTxt) {
        this.clientReferenceTxt = clientReferenceTxt;
    }

    public String getCourtFileNumber() {
        return courtFileNumber;
    }

    public void setCourtFileNumber(String courtFileNumber) {
        this.courtFileNumber = courtFileNumber;
    }

    public String getDocumentsProcessed() {
        return documentsProcessed;
    }

    public void setDocumentsProcessed(String documentsProcessed) {
        this.documentsProcessed = documentsProcessed;
    }

    public XMLGregorianCalendar getEntryDateTime() {
        return entryDateTime;
    }

    public void setEntryDateTime(XMLGregorianCalendar entryDateTime) {
        this.entryDateTime = entryDateTime;
    }

    public String getEntryUserId() {
        return entryUserId;
    }

    public void setEntryUserId(String entryUserId) {
        this.entryUserId = entryUserId;
    }

    public BigDecimal getServiceId() {
        return serviceId;
    }

    public void setServiceId(BigDecimal serviceId) {
        this.serviceId = serviceId;
    }

    public XMLGregorianCalendar getServiceReceivedDateTime() {
        return serviceReceivedDateTime;
    }

    public void setServiceReceivedDateTime(XMLGregorianCalendar serviceReceivedDateTime) {
        this.serviceReceivedDateTime = serviceReceivedDateTime;
    }

    public String getServiceReceivedDtmText() {
        return serviceReceivedDtmText;
    }

    public void setServiceReceivedDtmText(String serviceReceivedDtmText) {
        this.serviceReceivedDtmText = serviceReceivedDtmText;
    }

    public BigDecimal getServiceSessionId() {
        return serviceSessionId;
    }

    public void setServiceSessionId(BigDecimal serviceSessionId) {
        this.serviceSessionId = serviceSessionId;
    }

    public String getServiceSubtypeCd() {
        return serviceSubtypeCd;
    }

    public void setServiceSubtypeCd(String serviceSubtypeCd) {
        this.serviceSubtypeCd = serviceSubtypeCd;
    }

    public String getServiceTypeCd() {
        return serviceTypeCd;
    }

    public void setServiceTypeCd(String serviceTypeCd) {
        this.serviceTypeCd = serviceTypeCd;
    }

    public String getServiceTypeDesc() {
        return serviceTypeDesc;
    }

    public void setServiceTypeDesc(String serviceTypeDesc) {
        this.serviceTypeDesc = serviceTypeDesc;
    }

    public String getStyleOfCause() {
        return styleOfCause;
    }

    public void setStyleOfCause(String styleOfCause) {
        this.styleOfCause = styleOfCause;
    }

    public XMLGregorianCalendar getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(XMLGregorianCalendar updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getUpdateUserId() {
        return updateUserId;
    }

    public void setUpdateUserId(String updateUserId) {
        this.updateUserId = updateUserId;
    }

    public BigDecimal getUserSessionId() {
        return userSessionId;
    }

    public void setUserSessionId(BigDecimal userSessionId) {
        this.userSessionId = userSessionId;
    }
    @JsonCreator
    public EfilingService(@JsonProperty("accountId") BigDecimal accountId,
                          @JsonProperty("clientId") BigDecimal clientId,
                          @JsonProperty("clientReferenceTxt") String clientReferenceTxt,
                          @JsonProperty("courtFileNumber") String courtFileNumber,
                          @JsonProperty("documentsProcessed") String documentsProcessed,
                          @JsonProperty("entryDateTime") XMLGregorianCalendar entryDateTime,
                          @JsonProperty("entryUserId") String entryUserId,
                          @JsonProperty("serviceId") BigDecimal serviceId,
                          @JsonProperty("serviceReceivedDateTime") XMLGregorianCalendar serviceReceivedDateTime,
                          @JsonProperty("serviceReceivedDtmText") String serviceReceivedDtmText,
                          @JsonProperty("serviceSessionId") BigDecimal serviceSessionId,
                          @JsonProperty("serviceSubtypeCd") String serviceSubtypeCd,
                          @JsonProperty("serviceTypeCd") String serviceTypeCd,
                          @JsonProperty("serviceTypeDesc") String serviceTypeDesc,
                          @JsonProperty("styleOfCause") String styleOfCause,
                          @JsonProperty("updateDateTime") XMLGregorianCalendar updateDateTime,
                          @JsonProperty("updateUserId") String updateUserId,
                          @JsonProperty("userSessionId") BigDecimal userSessionId) {
        this.accountId = accountId;
        this.clientId = clientId;
        this.clientReferenceTxt = clientReferenceTxt;
        this.courtFileNumber = courtFileNumber;
        this.documentsProcessed = documentsProcessed;
        this.entryDateTime = entryDateTime;
        this.entryUserId = entryUserId;
        this.serviceId = serviceId;
        this.serviceReceivedDateTime = serviceReceivedDateTime;
        this.serviceReceivedDtmText = serviceReceivedDtmText;
        this.serviceSessionId = serviceSessionId;
        this.serviceSubtypeCd = serviceSubtypeCd;
        this.serviceTypeCd = serviceTypeCd;
        this.serviceTypeDesc = serviceTypeDesc;
        this.styleOfCause = styleOfCause;
        this.updateDateTime = updateDateTime;
        this.updateUserId = updateUserId;
        this.userSessionId = userSessionId;
    }
    public EfilingService() { }
}
