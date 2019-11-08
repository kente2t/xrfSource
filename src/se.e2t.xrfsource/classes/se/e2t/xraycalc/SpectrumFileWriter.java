/*
 * SpectrumFileWriter.java
 */
package se.e2t.xraycalc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Kent
 */
public class SpectrumFileWriter {

    public static int writeToFile(byte [] spectrumData, File file) {
        int retval = 0;
        
        // Create file if not replace
        
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ioe) {
                retval = 1;
            }
        }
        
        // Get a FileOutputStream for the file
        
        FileOutputStream streamWriter = null;
        if (retval == 0) {
            try {
                streamWriter = new FileOutputStream(file);
            }
            catch (FileNotFoundException fnfe) {
                retval = 2;
            }
            catch (SecurityException se) {
                retval = 3;
            }
        }
        
        // Output spectrum data
        if (retval == 0) {
            try {
                streamWriter.write(spectrumData);
            }
            catch (IOException ioe) {
                retval = 4;
            }
        }
        
        // Close the file
        
        if (streamWriter != null) {
            try {
                streamWriter.close();
            }
            catch (IOException ioe) {
                retval = 5;
            }
        }
        return retval;
    }
}
