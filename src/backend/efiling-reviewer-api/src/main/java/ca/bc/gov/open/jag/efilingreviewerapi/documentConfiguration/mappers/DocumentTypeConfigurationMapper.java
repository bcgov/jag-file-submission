package ca.bc.gov.open.jag.efilingreviewerapi.documentConfiguration.mappers;

import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentTypeConfiguration;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring")
public interface DocumentTypeConfigurationMapper {


    DocumentTypeConfiguration toDocumentTypeConfiguration(ca.bc.gov.open.jag.efilingreviewerapi.document.models.DocumentTypeConfiguration documentTypeConfiguration);

}
