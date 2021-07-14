package ca.bc.gov.open.jag.efilingcsoclient.mappers;

import ca.bc.gov.ag.csows.filing.status.File;
import ca.bc.gov.ag.csows.filing.status.PackageParties;
import ca.bc.gov.ag.csows.filing.status.RushOrderRequestItem;
import ca.bc.gov.open.jag.efilingcommons.model.Individual;
import ca.bc.gov.open.jag.efilingcommons.model.Organization;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewDocument;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper
public interface FilePackageMapper {

    @Mapping(target = "court.courtClass", source = "filePackage.courtClassCd")
    @Mapping(target = "court.fileNumber", source = "filePackage.courtFileNo")
    @Mapping(target = "court.level", source = "filePackage.courtLevelCd")
    @Mapping(target = "court.locationCd", source = "filePackage.courtLocationCd")
    @Mapping(target = "court.locationName", source = "filePackage.courtLocationName")
    @Mapping(target = "documents", source = "filePackage.files")
    @Mapping(target = "packageLinks.packageHistoryUrl", source = "csoHistoryLink")
    @Mapping(target = "parties", source = "individuals")
    @Mapping(target = "organizations", source = "organizations")
    @Mapping(target = "rushOrder", source = "rushOrderRequestItem")
    ReviewFilingPackage toFilingPackage(ca.bc.gov.ag.csows.filing.status.FilePackage filePackage, String csoHistoryLink, List<Individual> individuals, List<Organization> organizations, RushOrderRequestItem rushOrderRequestItem);

    List<ReviewDocument> toDocuments(List<File> file);

    //TODO: extend document to add additional fields
    ReviewDocument toDocument(File file);


    @Mapping(target = "middleName", source = "secondName")
    @Mapping(target = "lastName", source = "surname")
    Individual toParty(PackageParties packageParties);


    @Mapping(target = "name", source = "organizationName")
    Organization toOrganization(PackageParties packageParties);

}
