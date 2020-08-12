package ca.bc.gov.open.jag.efilingcsostarter.mappers;

import ca.bc.gov.ag.csows.services.FinancialTransaction;
import ca.bc.gov.ag.csows.services.Service;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface FinancialTransactionMapper {

    @Mapping(target = "approvalCd", source = "efilingTransaction.approvalCd")
    @Mapping(target = "ecommerceTransactionId", source = "efilingTransaction.ecommerceTransactionId")
    @Mapping(target = "transactionAmt", source = "efilingTransaction.transactionAmt")
    @Mapping(target = "transactonDtm", source = "efilingTransaction.transactonDtm")
    @Mapping(target = "responseCd", source = "efilingTransaction.responseCd")
    @Mapping(target = "invoiceNo", source = "efilingTransaction.invoiceNo")
    @Mapping(target = "entDtm", source = "efilingTransaction.entDtm")
    @Mapping(target = "entUserId", source = "service.entUserId")
    @Mapping(target = "serviceId", source = "service.serviceId")
    @Mapping(target = "updDtm", ignore = true)
    @Mapping(target = "updUserId", ignore = true)
    FinancialTransaction toTransaction(EfilingTransaction efilingTransaction, Service service);
}
