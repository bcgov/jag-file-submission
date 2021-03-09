package ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers;

import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentExtractResponse;
import ca.bc.gov.open.jag.efilingreviewerapi.document.mappers.DocumentMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.Document;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, uses = {ExtractMapper.class}, componentModel = "spring")
public interface ExtractRequestMapper {

    @Mapping(target = "extract", source = "transactionId")
    @Mapping(target = "document", source = "multipartFile", qualifiedByName = "multipartFileToDocument")
    @Mapping(target = "receivedTimeMillis", source = "receivedTimeMillis")
    ExtractRequest toExtractRequest(UUID transactionId, @Context String documentType, MultipartFile multipartFile, long receivedTimeMillis);

    @Named("multipartFileToDocument")
    static Document multipartFileToDocument(MultipartFile multipartFile, @Context String documentType) {
        DocumentMapper documentMapper = Mappers.getMapper( DocumentMapper.class );
        return documentMapper.toDocument(documentType, multipartFile);
    }

    DocumentExtractResponse toDocumentExtractResponse(ExtractRequest extractRequest);

}
