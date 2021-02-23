package ca.bc.gov.open.jag.efiling.helpers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class DeleteFileHelper {

    private DeleteFileHelper() {
        throw new IllegalStateException("Helper class");
    }

    public static void deleteDownloadedFile(Path path) throws IOException {
        Files.delete(path);
    }
}
