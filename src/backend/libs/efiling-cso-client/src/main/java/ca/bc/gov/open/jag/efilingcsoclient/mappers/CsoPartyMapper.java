package ca.bc.gov.open.jag.efilingcsoclient.mappers;

import ca.bc.gov.ag.csows.filing.CsoParty;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.Party;
import ca.bc.gov.open.jag.efilingcsoclient.Keys;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CsoPartyMapper {

    @Mapping(target = "partyTypeCd", source = "party.partyTypeCd")
    @Mapping(target = "roleTypeCd", source = "party.roleTypeCd")
    @Mapping(target = "current.entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "current.entUserId", source = "accountDetails.clientId")
    @Mapping(target = "current.firstGivenNm", source = "firstName")
    @Mapping(target = "current.identificationDetailSeqNo", source = "sequenceNumber")
    @Mapping(target = "current.nameTypeCd", source= "party.nameTypeCd", defaultValue = Keys.PARTY_NAME_TYPE_CD)
    @Mapping(target = "current.secondGivenNm", source = "party.middleName")
    @Mapping(target = "current.surnameNm", source = "lastName")
    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    CsoParty toEfilingParties(Integer sequenceNumber, Party party, AccountDetails accountDetails, String firstName, String lastName);

}
