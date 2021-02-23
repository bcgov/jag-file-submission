package ca.bc.gov.open.jag.efilingreviewerapi.extract.mappers;

import ca.bc.gov.open.jag.efilingreviewerapi.extract.models.Extract;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(injectionStrategy = InjectionStrategy.CONSTRUCTOR, componentModel = "spring")
public interface ExtractMapper {

    @Mapping(target = "transactionId", source = "transactionId")
    @Mapping(target = "id", expression = "java(java.util.UUID.randomUUID())")
    Extract toExtract(UUID transactionId);

}
