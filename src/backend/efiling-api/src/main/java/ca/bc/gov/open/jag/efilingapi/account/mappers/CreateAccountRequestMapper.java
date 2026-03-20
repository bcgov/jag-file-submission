package ca.bc.gov.open.jag.efilingapi.account.mappers;

import ca.bc.gov.open.jag.efilingapi.api.model.CreateCsoAccountRequest;
import ca.bc.gov.open.jag.efilingcommons.model.CreateAccountRequest;
import org.mapstruct.Mapper;

@Mapper
public interface CreateAccountRequestMapper {

    CreateAccountRequest toCreateAccountRequest(String universalId, String identityProvider,CreateCsoAccountRequest createCsoAccountRequest);

}
