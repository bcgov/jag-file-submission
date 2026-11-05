package ca.bc.gov.open.jag.efiling;

import java.io.File;

public class Keys {
    protected Keys() {
    }

    // Document Service
    public static final String X_TRANSACTION_ID = "X-Transaction-Id";
    public static final String PARENT_APPLICATION = "FLA";

    // Submission Service
    public static final String X_USER_ID = "X-User-Id";

    // Assertions
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String RESPONSE_NAVIGATION_URL = "http//somewhere.com";
    public static final String APPLICANT = "Applicant";
    public static final String EFILE_SUBMISSION_PAGE_TITLE = "E-File submission";

    // Downloaded Files path
    public static final String DOWNLOADED_FILES_PATH = File.separator + "downloadedFiles";

    // File path
    public static final String BASE_PATH = System.getProperty("user.dir");
    public static final String RESOURCES_PATH = "/src/test/resources/data/";
    public static final String TEST_DOCUMENT_PDF = "test-document.pdf";
    public static final String SECOND_DOCUMENT_PDF = "test-document-additional.pdf";

    // Api endpoint Paths
    public static final String SUBMIT_PATH = "submit";
    public static final String COURTS_PATH = "courts";
    public static final String FILE_NAME_PATH = "document/test-document.pdf";
    public static final String FILING_PACKAGE_PATH = "filing-package";
    public static final String CONFIG_PATH = "config";
    public static final String UPDATE_DOCUMENTS_PATH = "update-documents";
    public static final String DOCUMENTS_PATH = "documents";
    public static final String DOCUMENTS_TYPES_PATH = "documents/types";
    public static final String FILING_PACKAGES_PATH = "filingpackages";
    public static final String SUBMISSION_DOCUMENTS_PATH = "submission/documents";
    public static final String GENERATE_URL_PATH = "generateUrl";
    public static final String SUBMISSION_PATH = "submission";
    public static final String COUNTRIES_PATH = "countries";
    public static final String RUSH_PROCESSING_PATH = "rushProcessing";

    /** Action Document 'Submitted' status */
    public static final String ACTION_STATUS_SUB = "SUB";

    /** Action Document 'Rejected' status */
    public static final String ACTION_STATUS_REJ = "REJ";
    
}
