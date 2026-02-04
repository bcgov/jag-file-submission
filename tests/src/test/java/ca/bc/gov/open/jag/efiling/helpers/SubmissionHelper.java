package ca.bc.gov.open.jag.efiling.helpers;

import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.specification.MultiPartSpecification;

import java.io.File;

public class SubmissionHelper {

    private SubmissionHelper() {}

    public static MultiPartSpecification fileSpecBuilder(File file, String fileName, String mimeType) {

       return new MultiPartSpecBuilder(file).
                fileName(fileName).
                controlName("files").
                mimeType(mimeType).
                build();

    }

    // Different controlName and without mimeType
    public static MultiPartSpecification fileSpecBuilder(File file, String fileName) {

        return new MultiPartSpecBuilder(file).
                fileName(fileName).
                controlName("file").
                build();

    }

}
