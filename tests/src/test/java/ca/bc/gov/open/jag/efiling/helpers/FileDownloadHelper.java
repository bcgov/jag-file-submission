package ca.bc.gov.open.jag.efiling.helpers;

import ca.bc.gov.open.jag.efiling.error.EfilingTestException;

import java.io.File;

public class FileDownloadHelper {

    public File downloadFile(String downloadedFilesPath) {

        File folder = new File(downloadedFilesPath);
        File[] listOfFiles = folder.listFiles();

        File actualFile = null;

        if (listOfFiles == null) throw new EfilingTestException("Downloaded file is not present");
        for (File file : listOfFiles) {
            // Grabbing the first file
            if (file.isFile()) {
                actualFile = file;
                break;
            }
        }
        return actualFile;
    }
}
