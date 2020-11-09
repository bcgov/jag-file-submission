package ca.bc.gov.open.jag.efilingapi.courts.mappers;

import ca.bc.gov.open.jag.efilingapi.api.model.CourtLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface CourtLocationMapper {
    @Mapping(target = "id", source="courtid")
    @Mapping(target = "name", source="courtname")
    @Mapping(target = "code", source="courtcode")
    @Mapping(target = "isSupremeCourt", expression = "java(ca.bc.gov.open.jag.efilingcommons.utils.BooleanUtils.toBoolean(courtLocation.getIssupremecourt()))")
    @Mapping(target = "address.addressLine1", source="addressline1")
    @Mapping(target = "address.addressLine2", source="addressline2")
    @Mapping(target = "address.addressLine3", source="addressline3")
    @Mapping(target = "address.postalCode", source="postalcode")
    @Mapping(target = "address.cityName", source="cityname")
    @Mapping(target = "address.provinceName", source="provincename")
    @Mapping(target = "address.countryName", source="countryname")
    CourtLocation toCourtLocation(ca.bc.gov.open.jag.efilingceisapiclient.api.model.CourtLocation courtLocation);
}
