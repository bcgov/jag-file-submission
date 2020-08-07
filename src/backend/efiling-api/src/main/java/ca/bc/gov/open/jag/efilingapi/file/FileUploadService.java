package ca.bc.gov.open.jag.efilingapi.file;


public interface FileUploadService {
    void upload(byte[] file, String fileName);
}
