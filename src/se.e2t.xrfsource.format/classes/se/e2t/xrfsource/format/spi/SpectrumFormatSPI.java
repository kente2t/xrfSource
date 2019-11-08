/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.e2t.xrfsource.format.spi;
import se.e2t.xrfsource.spectrumclasses.XraySpectrum;

/**
 *
 * @author Kent
 */
public interface SpectrumFormatSPI {
    String getDescription();
    String getExtensions();
    byte [] createByteArray(XraySpectrum spectrum);
    int getErrorCode();
    String getErrorDescription();
}
