package ca.bc.gov.open.jag.efilingcsostarter.mappers;

import ca.bc.gov.ag.csows.services.FinancialTransaction;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingTransaction;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FinancialTransactionMapper {

    @Mapping(target = "approvalCd", source = "approvalCd")
    @Mapping(target = "ecommerceTransactionId", source = "ecommerceTransactionId")
    FinancialTransaction toTransaction(EfilingTransaction efilingTransaction);
}
