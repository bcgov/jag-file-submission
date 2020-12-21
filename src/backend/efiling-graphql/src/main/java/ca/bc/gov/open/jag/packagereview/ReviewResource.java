package ca.bc.gov.open.jag.packagereview;

import ca.bc.gov.open.jag.efilingcommons.submission.EfilingStatusService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackage;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.packagereview.mapper.FilingPackageMapper;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Optional;


@GraphQLApi
public class ReviewResource {

    Logger logger = LoggerFactory.getLogger(this.getClass());


    private final EfilingStatusService efilingStatusService;

    private final FilingPackageMapper filingPackageMapper;

    public ReviewResource(EfilingStatusService efilingStatusService, FilingPackageMapper filingPackageMapper) {
        this.efilingStatusService = efilingStatusService;
        this.filingPackageMapper = filingPackageMapper;
    }

    @Query
    @Description("Query a package")
    public ca.bc.gov.open.jag.packagereview.model.FilingPackage getSubmission(@Name("clientId") BigDecimal clientId, @Name("packageNo") BigDecimal packageNo) {
        logger.info("GraphQl Request received");

        Optional<FilingPackage> filePackage = efilingStatusService.findStatusByPackage(new FilingPackageRequest(clientId, packageNo));

        ca.bc.gov.open.jag.packagereview.model.FilingPackage responseFilingPackage = new ca.bc.gov.open.jag.packagereview.model.FilingPackage();
        if (filePackage.isPresent()) {
            responseFilingPackage = filingPackageMapper.toResponseFilingPackage(filePackage.get());
            responseFilingPackage.setClientFileNo(clientId.toPlainString());
            responseFilingPackage.setPackageNo(packageNo.toPlainString());
        }

        return responseFilingPackage;
    }
}
