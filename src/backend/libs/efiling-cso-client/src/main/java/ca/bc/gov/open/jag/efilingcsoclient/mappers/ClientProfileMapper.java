package ca.bc.gov.open.jag.efilingcsoclient.mappers;

import ca.bc.gov.ag.csows.accounts.ClientProfile;
import org.mapstruct.Mapper;

@Mapper
public interface ClientProfileMapper {

    ClientProfile toClientProfile(ca.bc.gov.ag.csows.accounts.ClientProfile previewClientProfile);

}
