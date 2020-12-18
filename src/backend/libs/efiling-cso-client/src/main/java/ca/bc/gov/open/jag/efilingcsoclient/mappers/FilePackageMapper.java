package ca.bc.gov.open.jag.efilingcsoclient.mappers;

import ca.bc.gov.open.jag.efilingcommons.model.FilePackage;
import org.mapstruct.Mapper;

@Mapper
public interface FilePackageMapper {

    FilePackage toFilePackage(ca.bc.gov.ag.csows.filing.status.FilePackage filePackage);
}
