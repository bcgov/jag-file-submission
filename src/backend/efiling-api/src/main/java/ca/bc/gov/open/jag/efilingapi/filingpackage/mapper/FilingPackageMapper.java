package ca.bc.gov.open.jag.efilingapi.filingpackage.mapper;

import ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage;
import org.mapstruct.Mapper;

@Mapper
public interface FilingPackageMapper {

    FilingPackage toResponseFilingPackage(ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage filingPackage);
}
