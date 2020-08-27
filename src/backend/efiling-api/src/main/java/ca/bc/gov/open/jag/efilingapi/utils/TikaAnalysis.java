package ca.bc.gov.open.jag.efilingapi.utils;

import org.apache.commons.lang3.StringUtils;
import org.apache.tika.detect.DefaultDetector;
import org.apache.tika.detect.Detector;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.mime.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

public class TikaAnalysis {

    private TikaAnalysis() { }

    public static Boolean isPdf(MultipartFile multipartFile) {

        Detector detector = new DefaultDetector();
        Metadata metadata = new Metadata();

        try {
            MediaType mediaType = detector.detect(new ByteArrayInputStream(multipartFile.getBytes()), metadata);
            return StringUtils.equals("application/pdf", mediaType.toString());
        } catch (IOException e) {
            return false;
        }


    }

}
