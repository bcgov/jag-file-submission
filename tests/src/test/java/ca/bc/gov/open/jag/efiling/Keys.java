package ca.bc.gov.open.jag.efiling;

import java.io.File;

public class Keys {
    protected Keys() {
    }

    // Document Service
    public static final String X_TRANSACTION_ID = "X-Transaction-Id";
    public static final String X_DOCUMENT_TYPE = "X-Document-Type";
    public static final String ACTUAL_X_TRANSACTION_ID = "1d4e38ba-0c88-4c92-8367-c8eada8cca19";

    public static final String PARENT_APPLICATION = "FLA";

    // Submission Service
    public static final String X_USER_ID = "X-User-Id";

    // Commons
    public static final String TEST_DOCUMENT_PDF = "test-document.pdf";
    public static final String TEST_RCC_DOCUMENT_PDF = "test-rcc-document.pdf";
    public static final String TEST_VALID_DOCUMENT_PDF = "test-valid-document.pdf";
    public static final String TEST_INVALID_DOCUMENT_PDF = "test-invalid-document.pdf";

    // Assertions
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String RESPONSE_NAVIGATION_URL = "http//somewhere.com";
    public static final String APPLICANT = "Applicant";
    public static final String EFILE_SUBMISSION_PAGE_TITLE = "E-File submission";
    public static final String ID_INDEX_FROM_RESPONSE = "id[0]";

    // Downloaded Files path
    public static final String DOWNLOADED_FILES_PATH = File.separator + "downloadedFiles";

    // File path
    public static final String BASE_PATH = System.getProperty("user.dir");
    public static final String SECOND_PDF_PATH = "/src/test/resources/data/test-document-additional.pdf";
    public static final String DOCUMENT_TYPE_CONFIG_PAYLOAD = "document-type-config-payload.json";
    public static final String RESTRICTED_DOCUMENT_TYPE_PAYLOAD = "restricted-document-type-payload.json";
    public static final String ADDITIONAL_RESTRICTED_DOCUMENT_TYPE_PAYLOAD = "additional-restricted-document-type-payload.json";
    public static final String RESTRICTED_DOCUMENT_TYPE_UPDATE_PAYLOAD = "restricted-document-type-update-payload.json";
    public static final String DOCUMENT_TYPE_CONFIG_UPDATE_PAYLOAD = "document-type-config-update-payload.json";

    // Api endpoint Paths
    public static final String SUBMIT_PATH = "submit";
    public static final String COURTS_PATH = "courts";
    public static final String FILE_NAME_PATH = "document/test-document.pdf";
    public static final String FILING_PACKAGE_PATH = "filing-package";
    public static final String CONFIG_PATH = "config";
    public static final String UPDATE_DOCUMENTS_PATH = "update-documents";
    public static final String DOCUMENTS_PATH = "documents";
    public static final String DOCUMENTS_TYPES_PATH = "documents/types";
    public static final String EXTRACT_DOCUMENTS_PATH = "documents/extract";
    public static final String FILING_PACKAGES_PATH = "filingpackages";
    public static final String SUBMISSION_DOCUMENTS_PATH = "submission/documents";
    public static final String GENERATE_URL_PATH = "generateUrl";
    public static final String SUBMISSION_PATH = "submission";
    public static final String DOCUMENT_TYPE_CONFIGURATION_PATH = "documentTypeConfigurations";
    public static final String RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH = "restrictedDocumentTypes";
    public static final String DOCUMENTS_PROCESSED_PATH = "documents/processed";
}
