package ca.bc.gov.open.jag.efilinghubapi.submission;

import ca.bc.gov.open.jag.efilinghub.api.SubmissionApiDelegate;
import ca.bc.gov.open.jag.efilinghub.api.model.GenerateUrlRequest;
import ca.bc.gov.open.jag.efilinghub.api.model.GenerateUrlResponse;
import ca.bc.gov.open.jag.efilinghub.api.model.UploadSubmissionDocumentsResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public class SubmissionApiDelegateImpl implements SubmissionApiDelegate {
    @Override
    public ResponseEntity<GenerateUrlResponse> generateUrl(UUID xAuthUserId, UUID id, GenerateUrlRequest generateUrlRequest) {
        return ResponseEntity.ok(new GenerateUrlResponse());
    }

    @Override
    public ResponseEntity<UploadSubmissionDocumentsResponse> uploadSubmissionDocuments(UUID xAuthUserId, List<MultipartFile> files) {
        return ResponseEntity.ok(new UploadSubmissionDocumentsResponse());
    }
}
