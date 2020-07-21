package ca.bc.gov.open.jag.efilingapi.utils;

import java.io.File;
import java.net.URLConnection;

public class FileUtils {


    public static String guessContentTypeFromName(String fileName){
        File file = new File(fileName);
        return URLConnection.guessContentTypeFromName(file.getName());
    }

}
