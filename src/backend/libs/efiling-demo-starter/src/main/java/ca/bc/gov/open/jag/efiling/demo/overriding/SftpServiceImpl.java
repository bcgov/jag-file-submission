package ca.bc.gov.open.jag.efiling.demo.overriding;

import ca.bc.gov.open.sftp.starter.SftpService;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class SftpServiceImpl implements SftpService {


    @Override
    @CachePut(cacheNames = "demoDocument", key = "#s", cacheManager = "demoDocumentCacheManager", unless = "#result == null")
    public ByteArrayInputStream getContent(String s) {
        return null;
    }

    @Override
    public void moveFile(String s, String s1) {
        // do nothing
    }

    @Override
    @Cacheable(cacheNames = "demoDocument", key = "#s", cacheManager = "demoDocumentCacheManager", unless = "#result == null")
    public void put(InputStream inputStream, String s) {

    }

    @Override
    public List<String> listFiles(String s) {
        // do nothing
        return null;
    }
}
