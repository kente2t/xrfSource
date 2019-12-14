/*
 * SpectrumPart.java
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
package se.e2t.xrfsource.spectrumclasses;

/**
 *
 * @author Kent Ericsson, e2t AB
 * 
 * Class stores info of a part of an x-ray spectrum
 */
public class SpectrumPart {

    private final double _wavelength; // Center wavelength
    private final double _window;     // Window centered at _wavelength
    private double _intensity;  // Intensity at center wavelength

    public SpectrumPart(double wavelength, double window, double intensity) {
        _wavelength = wavelength;
        _window = window;
        _intensity = intensity;
    }

    public double getWavelength() {
        return _wavelength;
    }

    public double getWindow() {
        return _window;
    }

    public double getIntensity() {
        return _intensity;
    }

    public void setIntensity(double _intensity) {
        this._intensity = _intensity;
    }
}
