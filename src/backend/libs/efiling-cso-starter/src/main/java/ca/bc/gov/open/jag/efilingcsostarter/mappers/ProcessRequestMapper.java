package ca.bc.gov.open.jag.efilingcsostarter.mappers;

import ca.bc.gov.ag.csows.filing.ProcessRequest;
import org.mapstruct.Mapper;

@Mapper
public interface ProcessRequestMapper {


    ProcessRequest toProcessRequest();


}
