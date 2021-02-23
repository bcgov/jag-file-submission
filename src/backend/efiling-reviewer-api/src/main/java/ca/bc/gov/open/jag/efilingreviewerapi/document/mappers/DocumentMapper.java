package ca.bc.gov.open.jag.efilingreviewerapi.document.mappers;

import ca.bc.gov.open.jag.efilingreviewerapi.document.models.Document;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.web.multipart.MultipartFile;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring")
public interface DocumentMapper {

    @Mapping(target = "type", source = "documentType")
    @Mapping(target = "fileName", source = "multipartFile.originalFilename")
    @Mapping(target = "size", source = "multipartFile.size")
    @Mapping(target = "contentType", source = "multipartFile.contentType")
    Document toDocument(String documentType, MultipartFile multipartFile);

}
