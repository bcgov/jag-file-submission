package ca.bc.gov.open.jag.efilingapi.document;

import ca.bc.gov.open.jag.efilingapi.submission.SubmissionKey;
import ca.bc.gov.open.jag.efilingcommons.model.DocumentTypeDetails;

import java.util.List;

public interface DocumentStore {

    byte[] put(SubmissionKey submissionKey, String fileName, byte[] content);

    byte[] get(SubmissionKey submissionKey, String fileName);

    void evict(SubmissionKey submissionKey, String fileName);

    DocumentTypeDetails getDocumentDetails(String courtLevel, String courtClass, String documentType);

    List<DocumentTypeDetails> getDocumentTypes(String courtLevel, String courtClass);

    byte[] putRushDocument(SubmissionKey submissionKey, String fileName, byte[] content);

    byte[] getRushDocument(SubmissionKey submissionKey, String fileName);

    void evictRushDocument(SubmissionKey submissionKey, String fileName);

}
