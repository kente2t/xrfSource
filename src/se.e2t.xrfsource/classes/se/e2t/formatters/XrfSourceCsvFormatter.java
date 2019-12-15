/*
 * XrfSourceCsvFormatter
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
package se.e2t.formatters;

import java.io.StringWriter;
import se.e2t.xrfsource.spectrumclasses.SpectrumPart;
import se.e2t.xrfsource.spectrumclasses.XraySpectrum;
import se.e2t.xrfsource.format.spi.SpectrumFormatSPI;

/**
 * Class formats spectrum data according to an xrfSource csv format.
 * Class implements the interface of the SpectrumFormatSPI.
 * 
 * @author Kent Ericsson, e2t AB
 */
public class XrfSourceCsvFormatter implements SpectrumFormatSPI {
    private static final String DESCRIPTION = "xrfSource csv format";
    private static final String EXTENSION = "*.csv";
    private int _errorCode;
    private String _errorDescription;
    
    public void XrfSourceCsvFormatter() {
        _errorCode = 0;
        _errorDescription = null;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getExtensions() {
        return EXTENSION;
    }

    @Override
    public byte [] createByteArray(XraySpectrum spectrum) {

        // Get a String writer for the file
        StringWriter strW;
        strW = new StringWriter();

        // Print spectrum data
        // First the tube lines
        for (SpectrumPart sp : spectrum.getTubeLines()) {
            strW.write(String.format("%.3f, ", sp.getWavelength()));
            strW.write(String.format("%.2e, ", sp.getIntensity()));
            strW.write(String.format("%.2e, L\n", sp.getWindow()));
        }

        // Then the continum slices
        for (SpectrumPart sp : spectrum.getContinuum()) {
            strW.write(String.format("%.3f, ", sp.getWavelength()));
            strW.write(String.format("%.2e, ", sp.getIntensity()));
            strW.write(String.format("%.2e, C\n", sp.getWindow()));
        }

        // Copy output to byte array
        strW.flush();
        String output = strW.toString();
        return output.getBytes();
    }

    @Override
    public int getErrorCode() {
        return _errorCode;
    }

    @Override
    public String getErrorDescription() {
        return _errorDescription;
    }
}
