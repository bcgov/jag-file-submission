package ca.bc.gov.open.jag.efiling;

import ca.bc.gov.open.jag.efiling.config.BrowserScopePostProcessor;
import ca.bc.gov.open.jag.efiling.page.*;
import ca.bc.gov.open.jag.efiling.services.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class TestConfig {

    @Value("${default.timeout:30}")
    private int timeout;

    @Value("${AUTH_PROVIDER:keycloak}")
    private String provider;

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
    public CountryService countryService() {
        return new CountryService();
    }

    @Bean
    public static BeanFactoryPostProcessor beanFactoryPostProcessor() {
        return new BrowserScopePostProcessor();
    }

    @Bean
    @Scope("browserscope")
    public WebDriver chromeDriver() {

        //File file = new File("bin/chromedriver.exe");

        //System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());

        WebDriverManager.chromedriver().setup();
        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("profile.default_content_settings.popups", Integer.valueOf(0));
        prefs.put("download.default_directory", Keys.BASE_PATH + Keys.DOWNLOADED_FILES_PATH);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--headless=new");

        options.addArguments("--window-size=1920,1080");
        //options.setBinary(file.getAbsolutePath());

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
        List<AuthenticationPage> authenticationPages = new ArrayList<>();
        authenticationPages.add(bceidAuthenticationPageImpl());
        authenticationPages.add(bcscAuthenticationPageImpl());
        authenticationPages.add(keycloakAuthenticationPageImpl());

        return authenticationPages.stream().filter(x -> StringUtils.equals(provider,
                x.getName())).findFirst().get();
    }

    public AuthenticationPage bceidAuthenticationPageImpl() {
        return new BceidAuthenticationPageImpl();
    }

    public AuthenticationPage bcscAuthenticationPageImpl() {
        return new BcscAuthenticationPageImpl();
    }

    public AuthenticationPage keycloakAuthenticationPageImpl() {
        return new KeycloakAuthenticationPageImpl();
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

    @Bean
    @Scope("prototype")
    public EfilingAdminHomePage efilingAdminHomePage() {
        return new EfilingAdminHomePage();
    }

    @Bean
    @Scope("prototype")
    public SubmissionHistoryPage submissionHistoryPage() {
        return new SubmissionHistoryPage();
    }

}
