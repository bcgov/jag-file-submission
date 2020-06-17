package ca.bc.gov.open.jag.efilingworker.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.util.UUID;

@Service
public class MockDocumentStoreServiceImpl implements DocumentStoreService {
    @Override
    public String uploadFile(File file) {
        return UUID.randomUUID().toString();
    }
}
