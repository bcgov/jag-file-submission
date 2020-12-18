package ca.bc.gov.open.jag.efilingapi.submission.mappers;

import ca.bc.gov.open.jag.efilingapi.api.model.InitialPackage;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import org.mapstruct.Mapper;

@Mapper
public interface FilingPackageMapper {

    FilingPackage toFilingPackage(InitialPackage filingPackage);

    FilingPackage toFilingPackage(ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage filingPackage);

    ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage toApiFilingPackage(FilingPackage filingPackage);

}
