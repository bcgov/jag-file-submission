package ca.bc.gov.open.jag.efilingworker.service;

import org.springframework.http.ResponseEntity;

import java.io.File;
import java.util.UUID;

public class MockDocumentStoreServiceImpl implements DocumentStoreService {
    @Override
    public String uploadFile(File file) {
        return UUID.randomUUID().toString();
    }
}
