package ca.bc.gov.open.jag.efilingcsoclient.mappers;

import ca.bc.gov.ag.csows.filing.status.File;
import ca.bc.gov.ag.csows.filing.status.PackageParties;
import ca.bc.gov.open.jag.efilingcommons.model.Party;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewDocument;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper
public interface FilePackageMapper {

    @Mapping(target = "court.courtClass", source = "courtClassCd")
    @Mapping(target = "court.fileNumber", source = "courtFileNo")
    @Mapping(target = "court.level", source = "courtLevelCd")
    @Mapping(target = "court.locationCd", source = "courtLocationCd")
    @Mapping(target = "court.locationName", source = "courtLocationName")
    @Mapping(target = "documents", source = "files")
    ReviewFilingPackage toFilingPackage(ca.bc.gov.ag.csows.filing.status.FilePackage filePackage);

    List<ReviewDocument> toDocuments(List<File> file);

    //TODO: extend document to add additional fields
    ReviewDocument toDocument(File file);

    List<Party> toParties(List<PackageParties> packageParties);

    @Mapping(target = "middleName", source = "secondName")
    @Mapping(target = "lastName", source = "surname")
    Party toParty(PackageParties packageParties);
}
