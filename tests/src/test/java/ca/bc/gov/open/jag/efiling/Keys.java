package ca.bc.gov.open.jag.efiling;

import java.io.File;

public class Keys {
    protected Keys() {
    }

    // Token Helper
    public static final String CLIENT_ID = "client_id";
    public static final String GRANT_TYPE = "grant_type";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    // Document Service
    public static final String X_TRANSACTION_ID = "X-Transaction-Id";
    public static final String X_DOCUMENT_TYPE = "X-Document-Type";

    // Submission Service
    public static final String X_USER_ID = "X-User-Id";
    public static final String MESSAGE_FORMAT_WITH_SUB_ID_AND_PATH = "{0}/submission/{1}/{2}";

    // Commons
    public static final String TEST_DOCUMENT_PDF = "test-document.pdf";
    public static final String TEST_RCC_DOCUMENT_PDF = "test-rcc-document.pdf";
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String RESPONSE_NAVIGATION_URL = "http//somewhere.com";
    public static final String APPLICANT = "Applicant";

    // Frontend tests
    public static final String EFILE_SUBMISSION_PAGE_TITLE = "E-File submission";

    // Downloaded Files
    public static final String DOWNLOADED_FILES_PATH = File.separator + "downloadedFiles";

    // File path
    public static final String BASE_PATH = System.getProperty("user.dir");
    public static final String SECOND_PDF_PATH = "/src/test/resources/data/test-document-additional.pdf";

    // Path
    public static final String SUBMIT_PATH = "submit";
    public static final String FILE_NAME_PATH = "document/test-document.pdf";
    public static final String FILING_PACKAGE_PATH = "filing-package";
    public static final String PARENT_APPLICATION = "efiling-admin";
    public static final String CONFIG_PATH = "config";
    public static final String UPDATE_DOCUMENTS_PATH = "update-documents";
    public static final String DOCUMENTS_PATH = "documents";

}
