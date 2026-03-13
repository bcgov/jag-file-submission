package ca.bc.gov.open.jag.efilingapi.court.services;

import ca.bc.gov.open.jag.efilingapi.court.models.GetCourtDetailsRequest;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtFileNumberRequest;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtRequest;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingSearchService;
import org.apache.commons.lang3.StringUtils;

import java.util.Optional;

public class CourtServiceImpl implements CourtService {

    private final EfilingCourtService efilingCourtService;
    private final EfilingSearchService efilingSearchService;

    public CourtServiceImpl(EfilingCourtService efilingCourtService, EfilingSearchService efilingSearchService) {
        this.efilingCourtService = efilingCourtService;
        this.efilingSearchService = efilingSearchService;
    }

    @Override
    public boolean isValidCourt(IsValidCourtRequest isValidCourtRequest) {
        return this.efilingCourtService.checkValidLevelClassLocation(
                isValidCourtRequest.getCourtId(),
                isValidCourtRequest.getCourtLevel(),
                isValidCourtRequest.getCourtClassification(),
                isValidCourtRequest.getApplicationCode()
        );
    }

    @Override
    public Optional<CourtDetails> getCourtDetails(GetCourtDetailsRequest getCourtDetailsRequest) {

        return this.efilingCourtService.getCourtDescription(
                getCourtDetailsRequest.getCourtLocation(),
                getCourtDetailsRequest.getCourtLevel(),
                getCourtDetailsRequest.getCourtClassification());

    }

    @Override
    public boolean isValidCourtFileNumber(IsValidCourtFileNumberRequest isValidCourtFileNumberRequest){

        if (StringUtils.equalsIgnoreCase("A", isValidCourtFileNumberRequest.getCourtLevel())) {
            return this.efilingSearchService.caseNumberExists(isValidCourtFileNumberRequest.getFileNumber());
        } else {
            return efilingCourtService.checkValidCourtFileNumber(
                    isValidCourtFileNumberRequest.getFileNumber(),
                    isValidCourtFileNumberRequest.getCourtId(),
                    isValidCourtFileNumberRequest.getCourtLevel(),
                    isValidCourtFileNumberRequest.getCourtClassification(),
                    isValidCourtFileNumberRequest.getApplicationCode()
            );
        }


    }

}
