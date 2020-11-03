package ca.bc.gov.open.jag.efilingapi.court.services;

import ca.bc.gov.open.jag.efilingapi.court.models.GetCourtDetailsRequest;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtRequest;
import ca.bc.gov.open.jag.efilingapi.court.models.IsValidCourtRequestRequest;
import ca.bc.gov.open.jag.efilingcommons.model.CourtDetails;
import ca.bc.gov.open.jag.efilingcommons.service.EfilingCourtService;

public class CourtServiceImpl implements CourtService {

    private final EfilingCourtService efilingCourtService;

    public CourtServiceImpl(EfilingCourtService efilingCourtService) {
        this.efilingCourtService = efilingCourtService;
    }

    @Override
    public Boolean isValidCourtLocation(IsValidCourtRequest isValidCourtRequest) {
        return this.efilingCourtService.checkValidLevelClassLocation(
                isValidCourtRequest.getCourtId(),
                isValidCourtRequest.getCourtLevel(),
                isValidCourtRequest.getCourtClassification(),
                isValidCourtRequest.getApplicationCode()
        );
    }

    @Override
    public CourtDetails getCourtDetails(GetCourtDetailsRequest getCourtDetailsRequest) {

        return this.efilingCourtService.getCourtDescription(
                getCourtDetailsRequest.getCourtLocation(),
                getCourtDetailsRequest.getCourtLevel(),
                getCourtDetailsRequest.getCourtClassification());

    }

    @Override
    public boolean IsValidCourt(IsValidCourtRequestRequest isValidCourtRequestRequest) {

        return this.efilingCourtService.checkValidLevelClassLocation(
                isValidCourtRequestRequest.getCourtId(),
                isValidCourtRequestRequest.getCourtLevel(),
                isValidCourtRequestRequest.getCourtClassification(),
                isValidCourtRequestRequest.getApplicationCode()
        );

    }

}
