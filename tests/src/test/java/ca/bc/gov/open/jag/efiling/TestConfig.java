package ca.bc.gov.open.jag.efiling;

import ca.bc.gov.open.jag.efiling.config.BrowserScopePostProcessor;
import ca.bc.gov.open.jag.efiling.page.AuthenticationPage;
import ca.bc.gov.open.jag.efiling.page.DocumentUploadPage;
import ca.bc.gov.open.jag.efiling.page.PackageConfirmationPage;
import ca.bc.gov.open.jag.efiling.page.PackageReviewPage;
import ca.bc.gov.open.jag.efiling.services.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class TestConfig {

    @Value("${default.timeout:30}")
    private int timeout;

    @Bean
    public OauthService oauthService() {
        return new OauthService();
    }

    @Bean
    public SubmissionService submissionService() {
        return new SubmissionService();
    }

    @Bean
    public CourtService courtService() {
        return new CourtService();
    }

    @Bean
    public GenerateUrlService generateUrlService() {
        return new GenerateUrlService(oauthService(), submissionService());
    }

    @Bean
    public FilingPackageService filingPackageService() {
        return new FilingPackageService();
    }

    @Bean
    public DocumentService documentService() {
        return new DocumentService();
    }

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new BrowserScopePostProcessor();
    }

    @Bean
    @Scope("browserscope")
    public WebDriver chromeDriver() {
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true);
        options.addArguments("--window-size=1920,1080");
        return new ChromeDriver(options);

    }

    @Bean
    @Scope("prototype")
    public WebDriverWait webDriverWait(WebDriver driver) {
        return new WebDriverWait(driver, this.timeout);
    }

    @Bean
    @Scope("prototype")
    public AuthenticationPage authenticationPage() {
        return new AuthenticationPage();
    }

    @Bean
    @Scope("prototype")
    public PackageConfirmationPage packageConfirmationPage() {
        return new PackageConfirmationPage();
    }

    @Bean
    @Scope("prototype")
    public DocumentUploadPage documentUploadPage() {
        return new DocumentUploadPage();
    }

    @Bean
    @Scope("prototype")
    public PackageReviewPage packageReviewPage() {
        return new PackageReviewPage();
    }

}
