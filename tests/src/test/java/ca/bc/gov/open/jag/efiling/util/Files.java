package ca.bc.gov.open.jag.efiling.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Files {
	
    private static final Logger logger = LoggerFactory.getLogger(Files.class);

    private Files() { // private constructor to restrict instantiating this object.
	}
    
    /**
     * Code-climate approved  method to use Files.delete(File) instead of File.delete()
     */
	public static boolean delete(File f) {
		try {
			java.nio.file.Files.delete(f.toPath());
		} catch (IOException e) {
			logger.error("IOException", e);
			return false;
		}
		return true;
	}
	
}
