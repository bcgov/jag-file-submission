package ca.bc.gov.open.jag.efilingapi.submission.mappers;

import ca.bc.gov.open.jag.efilingapi.api.model.InitialPackage;
import ca.bc.gov.open.jag.efilingapi.api.model.SubmissionDocument;
import ca.bc.gov.open.jag.efilingcommons.model.Document;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FilingPackageMapper {

    FilingPackage toFilingPackage(InitialPackage filingPackage);

    FilingPackage toFilingPackage(ca.bc.gov.open.jag.efilingapi.api.model.FilingPackage filingPackage);

    ca.bc.gov.open.jag.efilingapi.api.model.SubmissionFilingPackage toApiFilingPackage(FilingPackage filingPackage);

    SubmissionDocument documentToSubmissionDocument(Document file);
}
