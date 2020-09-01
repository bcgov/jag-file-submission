package ca.bc.gov.open.jag.efilingcsostarter.mappers;

import ca.bc.gov.ag.csows.filing.CsoParty;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.Party;
import ca.bc.gov.open.jag.efilingcsostarter.Keys;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CsoPartyMapper {

    @Mapping(target = "partyTypeCd", source = "party.partyTypeCd", defaultValue = Keys.PARTY_TYPE_CD)
    @Mapping(target = "roleTypeCd", source = "party.roleTypeCd", defaultValue = Keys.PARTY_ROLE_TYPE_CD)
    @Mapping(target = "current.entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "current.entUserId", source = "accountDetails.clientId")
    @Mapping(target = "current.firstGivenNm", source = "party.firstName")
    @Mapping(target = "current.identificationDetailSeqNo", source = "sequenceNumber")
    @Mapping(target = "current.nameTypeCd", source= "party.nameTypeCd", defaultValue = Keys.PARTY_NAME_TYPE_CD)
    @Mapping(target = "current.secondGivenNm", source = "party.middleName")
    @Mapping(target = "current.surnameNm", source = "party.lastName")
    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    CsoParty toEfilingParties(Integer sequenceNumber, Party party, AccountDetails accountDetails);

}
