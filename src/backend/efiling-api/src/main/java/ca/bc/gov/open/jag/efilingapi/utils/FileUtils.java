package ca.bc.gov.open.jag.efilingapi.utils;

import java.io.File;
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

}
