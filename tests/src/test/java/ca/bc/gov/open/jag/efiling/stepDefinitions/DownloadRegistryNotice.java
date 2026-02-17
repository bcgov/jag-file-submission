package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.helpers.FileDownloadHelper;
import ca.bc.gov.open.jag.efiling.page.PackageReviewPage;
import ca.bc.gov.open.jag.efiling.util.Files;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

public class DownloadRegistryNotice {

    private final PackageReviewPage packageReviewPage;

    private final Logger logger = LoggerFactory.getLogger(DownloadRegistryNotice.class);

    public DownloadRegistryNotice(PackageReviewPage packageReviewPage) {
        this.packageReviewPage = packageReviewPage;
    }

    @Then("registry notice can be downloaded")
    public void downloadRegistryNotice() throws InterruptedException {
        packageReviewPage.clickToDownloadRegistryNotice();

        FileDownloadHelper fileDownloadHelper = new FileDownloadHelper();
        File downloadedFile = fileDownloadHelper.downloadFile(Keys.BASE_PATH + Keys.DOWNLOADED_FILES_PATH);

        logger.info("Downloaded file name is: {}", downloadedFile.getName());
        Assert.assertEquals("RegistryNotice.pdf", downloadedFile.getName());

        Assert.assertTrue(downloadedFile.length() > 0);
        logger.info("Files successfully downloaded");
        
        logger.info("Files deleted after validation: {}", Boolean.valueOf(Files.delete(downloadedFile)));
        Assert.assertEquals(0, downloadedFile.length());
    }
}
