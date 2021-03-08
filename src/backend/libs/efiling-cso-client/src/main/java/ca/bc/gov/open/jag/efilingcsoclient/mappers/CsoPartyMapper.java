package ca.bc.gov.open.jag.efilingcsoclient.mappers;

import ca.bc.gov.ag.csows.filing.CsoParty;
import ca.bc.gov.open.jag.efilingcommons.model.AccountDetails;
import ca.bc.gov.open.jag.efilingcommons.model.Individual;
import ca.bc.gov.open.jag.efilingcommons.model.Organization;
import ca.bc.gov.open.jag.efilingcsoclient.Keys;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CsoPartyMapper {

    @Mapping(target = "partyTypeCd", constant = Keys.INDIVIDUAL_ROLE_TYPE_CD)
    @Mapping(target = "roleTypeCd", source = "individual.roleTypeCd")
    @Mapping(target = "current.entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "current.entUserId", source = "accountDetails.clientId")
    @Mapping(target = "current.firstGivenNm", source = "firstName")
    @Mapping(target = "current.identificationDetailSeqNo", source = "sequenceNumber")
    @Mapping(target = "current.nameTypeCd", source= "individual.nameTypeCd", defaultValue = Keys.PARTY_NAME_TYPE_CD)
    @Mapping(target = "current.secondGivenNm", source = "individual.middleName")
    @Mapping(target = "current.surnameNm", source = "lastName")
    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    CsoParty toEfilingParties(Integer sequenceNumber, Individual individual, AccountDetails accountDetails, String firstName, String lastName);

    @Mapping(target = "partyTypeCd", constant = Keys.ORGANIZATION_ROLE_TYPE_CD)
    @Mapping(target = "roleTypeCd", source = "organization.roleTypeCd")
    @Mapping(target = "current.entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    @Mapping(target = "current.entUserId", source = "accountDetails.clientId")
    @Mapping(target = "current.organizationNm", source = "organization.name")
    @Mapping(target = "current.identificationDetailSeqNo", source = "sequenceNumber")
    @Mapping(target = "current.nameTypeCd", source= "organization.nameTypeCd", defaultValue = Keys.PARTY_NAME_TYPE_CD)
    @Mapping(target = "entUserId", source = "accountDetails.clientId")
    @Mapping(target = "entDtm",  expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.DateUtils.getCurrentXmlDate())")
    CsoParty toEfilingOrganization(Integer sequenceNumber, Organization organization, AccountDetails accountDetails);

}
