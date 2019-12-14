/*
 * SpectrumFormatSPI.java
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

package se.e2t.xrfsource.format.spi;
import se.e2t.xrfsource.spectrumclasses.XraySpectrum;

/**
 *
 * This is an interface to be implemented by code that handles the spectrum
 * formatting service, i.e. code that stores formatted data in a byte arry.
 * 
 * @author Kent Ericsson, e2t AB
 */
public interface SpectrumFormatSPI {
    // Returns the description of the format
    String getDescription();
    // returns ectension to be used by the program FileChooser call, format *.ext.
    String getExtensions();
    // Method that creates a byte array from spectrum data.
    byte [] createByteArray(XraySpectrum spectrum);
    // Method to return an error code.
    int getErrorCode();
    // Method returns an error description.
    String getErrorDescription();
}
