/*
 * SpectrumFileWriter.java
 *
 * Copyright 2019 e2t AB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package se.e2t.xraycalc;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Class writes a byte buffer formatted by formatter code to a file.
 * 
 * @author Kent Ericsson, e2t AB
 */
public class SpectrumFileWriter {

    /**
     * Method writes formatted data in byte buffer to a file.
     * 
     * @param spectrumData byte buffer
     * @param file output file
     * @return 0 if OK, error code 1-5, se below.
     */
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
