package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.court.EfilingCourtLocationService;
import ca.bc.gov.open.jag.efilingcommons.model.Address;
import ca.bc.gov.open.jag.efilingcommons.model.InternalCourtLocation;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class EfilingCourtLocationServiceDemoImpl implements EfilingCourtLocationService {

    @Override
    public List<InternalCourtLocation> getCourtLocations(String courtType) {

        InternalCourtLocation courtLocationOne = new InternalCourtLocation();
        courtLocationOne.setId(BigDecimal.valueOf(10264));
        courtLocationOne.setIdentifierCode("5871");
        courtLocationOne.setName("Campbell River");
        courtLocationOne.setCode("OMH");
        courtLocationOne.setIsSupremeCourt(true);
        Address addressOne = new Address();
        addressOne.setAddressLine1("500 - 13th Avenue");
        addressOne.setPostalCode("V9W 6P1");
        addressOne.setCityName("Campbell River");
        addressOne.setProvinceName("British Columbia");
        addressOne.setCountryName("Canada");
        courtLocationOne.setAddress(addressOne);

        InternalCourtLocation courtLocationTwo = new InternalCourtLocation();
        courtLocationTwo.setId(BigDecimal.valueOf(9393));
        courtLocationTwo.setIdentifierCode("3561");
        courtLocationTwo.setName("Chilliwack");
        courtLocationTwo.setCode("ABB");
        courtLocationTwo.setIsSupremeCourt(true);
        Address addressTwo = new Address();
        addressTwo.setAddressLine1("46085 Yale Road");
        addressTwo.setAddressLine2("  ");
        addressTwo.setAddressLine3("  ");
        addressTwo.setPostalCode("V2P 2L8");
        addressTwo.setCityName("Chilliwack");
        addressTwo.setProvinceName("British Columbia");
        addressTwo.setCountryName("Canada");
        courtLocationTwo.setAddress(addressTwo);

        return Arrays.asList(courtLocationOne,courtLocationTwo);
    }
}
