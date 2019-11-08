/*
 * SpectrumPart.java
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
