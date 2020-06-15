package ca.bc.gov.open.jag.efilingworker.service;

import org.springframework.http.ResponseEntity;

import java.io.File;

public interface DocumentStoreService {
    String uploadFile(File file);
}
