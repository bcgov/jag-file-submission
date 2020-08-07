package ca.bc.gov.open.jag.efilingapi.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    void upload(MultipartFile file);
}
