package ca.bc.gov.open.jag.efilingcsoclient.mappers;

import ca.bc.gov.ag.csows.services.FinancialTransaction;
import ca.bc.gov.ag.csows.services.Service;
import ca.bc.gov.open.jag.efilingcommons.model.PaymentTransaction;
import ca.bc.gov.open.jag.efilingcommons.utils.DateUtils;
import ca.bc.gov.open.jag.efilingcsoclient.Keys;
import org.joda.time.DateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.XMLGregorianCalendar;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FinancialTransactionMapper {

    @Mapping(target = "approvalCd", source = "efilingTransaction.approvalCd")
    @Mapping(target = "ecommerceTransactionId", source = "efilingTransaction.ecommerceTransactionId")
    @Mapping(target = "transactionAmt", source = "efilingTransaction.transactionAmt")
    @Mapping(target = "transactonDtm", source = "efilingTransaction.transactonDtm" , qualifiedByName = "toXmlGregorianDate")
    @Mapping(target = "transactionStateCd", source = "efilingTransaction.transactionStateCd")
    @Mapping(target = "transactionTypeCd", constant = Keys.TRANSACTION_TYPE_CD)
    @Mapping(target = "transactionSubtypeCd", constant = Keys.TRANSACTION_SUB_TYPE_CD)
    @Mapping(target = "creditCardTypeCd", source = "efilingTransaction.creditCardTypeCd")
    @Mapping(target = "processDt", source = "efilingTransaction.processDt" , qualifiedByName = "toXmlGregorianDate")
    @Mapping(target = "invoiceNo", source = "efilingTransaction.invoiceNo")
    @Mapping(target = "entDtm", source = "efilingTransaction.entDtm", qualifiedByName = "toXmlGregorianDate")
    @Mapping(target = "entUserId", source = "service.entUserId")
    @Mapping(target = "serviceId", source = "service.serviceId")
    @Mapping(target = "updDtm", ignore = true)
    @Mapping(target = "updUserId", ignore = true)
    FinancialTransaction toTransaction(PaymentTransaction efilingTransaction, Service service);


    @Named("toXmlGregorianDate")
    public static XMLGregorianCalendar toXmlGregorianDate(DateTime dateTime) throws DatatypeConfigurationException {
        return DateUtils.getXmlDate(dateTime);
    }


}
