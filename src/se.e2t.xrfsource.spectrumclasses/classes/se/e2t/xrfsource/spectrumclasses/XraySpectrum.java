/*
 * XraySpectrum.java
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

    public List<SpectrumPart> getContinuum() {
        return _continium;
    }
    
    public void addTubeLine(SpectrumPart lineInfo) {
        _tubeLines.add(lineInfo);
    }
    
    public void addContiniumSlice(SpectrumPart contInfo) {
        _continium.add(contInfo);
    }
}
