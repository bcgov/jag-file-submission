package ca.bc.gov.open.jag.efilingapi.account;

import ca.bc.gov.open.jag.efilingapi.api.model.CsoAccount;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import org.mapstruct.Mapper;

@Mapper
public interface CsoAccountMapper {


    CsoAccount toCsoAccount(AccountDetails accountDetails);

}
