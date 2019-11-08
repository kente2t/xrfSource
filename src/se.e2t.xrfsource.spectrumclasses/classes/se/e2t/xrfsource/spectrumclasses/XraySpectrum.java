/*
 * XraySpectrum.java
 */
package se.e2t.xrfsource.spectrumclasses;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Kent Ericsson, e2t AB
 * 
 * Class stores results from x-ray spectrum calculations.
 */
public class XraySpectrum {
    private final List<SpectrumPart> _tubeLines;
    private final List<SpectrumPart> _continium;
    
    public XraySpectrum() {
        _tubeLines = new ArrayList<>();
        _continium = new ArrayList<>();
    }

    public List<SpectrumPart> getTubeLines() {
        return _tubeLines;
    }

    public List<SpectrumPart> getContinium() {
        return _continium;
    }
    
    public void addTubeLine(SpectrumPart lineInfo) {
        _tubeLines.add(lineInfo);
    }
    
    public void addContiniumSlice(SpectrumPart contInfo) {
        _continium.add(contInfo);
    }
}
