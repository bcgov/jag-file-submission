package ca.bc.gov.open.jag.efilingcsoclient.mappers;

import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import org.joda.time.DateTime;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper
public interface FilePackageMapper {

    @Mapping(target = "court.courtClass", source = "courtClassCd")
    @Mapping(target = "court.fileNumber", source = "courtFileNo")
    @Mapping(target = "court.level", source = "courtLevelCd")
    @Mapping(target = "court.location", source = "courtLocationCd")
    @Mapping(target = "court.locationDescription", source = "courtLocationName")
    FilingPackage toFilingPackage(ca.bc.gov.ag.csows.filing.status.FilePackage filePackage);
}
