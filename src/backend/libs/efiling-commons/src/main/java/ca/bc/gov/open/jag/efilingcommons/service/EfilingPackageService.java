package ca.bc.gov.open.jag.efilingcommons.service;


import ca.bc.gov.open.jag.efilingcommons.model.SubmissionFee;

public interface EfilingPackageService {

    SubmissionFee getSubmissionFee(String serviceId);

}
