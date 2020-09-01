package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingapi.submission.SubmissionKey;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentDetails;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentType;

import java.util.List;

public interface DocumentStore {

    byte[] put(SubmissionKey submissionKey, String fileName, byte[] content);

    byte[] get(SubmissionKey submissionKey, String fileName);

    void evict(SubmissionKey submissionKey, String fileName);

    DocumentDetails getDocumentDetails(String courtLevel, String courtClass, String documentType);

    List<DocumentType> getDocumentTypes(String courtLevel, String courtClass);
}
