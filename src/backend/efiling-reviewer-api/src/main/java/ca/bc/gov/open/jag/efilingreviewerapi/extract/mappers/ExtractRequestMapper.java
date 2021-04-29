package ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers;

import ca.bc.gov.open.jag.efilingreviewerapi.api.model.DocumentExtractResponse;
import ca.bc.gov.open.jag.efilingreviewerapi.document.mappers.DocumentMapper;
import ca.bc.gov.open.jag.efilingreviewerapi.document.models.Document;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.Extract;
import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.ExtractRequest;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.UUID;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring")
public interface ExtractRequestMapper {

    @Mapping(target = "extract", source = "extract")
    @Mapping(target = "document", source = "multipartFile", qualifiedByName = "multipartFileToDocument")
    @Mapping(target = "receivedTimeMillis", source = "receivedTimeMillis")
    ExtractRequest toExtractRequest(Extract extract, @Context String documentType, MultipartFile multipartFile, long receivedTimeMillis);

    @Named("multipartFileToDocument")
    static Document multipartFileToDocument(MultipartFile multipartFile, @Context String documentType) {
        DocumentMapper documentMapper = Mappers.getMapper( DocumentMapper.class );
        return documentMapper.toDocument(documentType, multipartFile);
    }

    @Mapping(target = "document.documentId", source = "documentId")
    @Mapping(target = "document.type", source = "extractRequest.document.type")
    @Mapping(target = "document.fileName", source = "extractRequest.document.fileName")
    @Mapping(target = "document.size", source = "extractRequest.document.size")
    @Mapping(target = "document.contentType", source = "extractRequest.document.contentType")
    @Mapping(target = "extract.id", source = "extractRequest.extract.id")
    @Mapping(target = "extract.transactionId", source = "extractRequest.extract.transactionId")
    DocumentExtractResponse toDocumentExtractResponse(ExtractRequest extractRequest, BigDecimal documentId);

    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "useWebhook", source = "useWebhook")
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    Extract toExtract(UUID transactionId, Boolean useWebhook);

}
