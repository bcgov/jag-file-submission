package ca.bc.gov.open.jag.efilingapi.utils;

import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;

public class FileUtils {

    private FileUtils() {}

    /**
     * Guess the mime type from filename
     * @param fileName
     * @return
     */
    public static String guessContentTypeFromName(String fileName){
        File file = new File(fileName);
        return URLConnection.guessContentTypeFromName(file.getName());
    }

    public static MultipartFile createMultipartFile(String name, ContentType contentType, InputStream bytes) {

        try {
            return new MockMultipartFile(name, name, "application/pdf", bytes);
        } catch (IOException e) {
            return null;
        }

    }
}
