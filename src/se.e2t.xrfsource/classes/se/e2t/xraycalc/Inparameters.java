/*
 * File: Inparameters.java
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

import java.util.ArrayList;
import java.util.List;
import static se.e2t.xraycalc.Inparameters.CalcModel.EBEL;
import static se.e2t.xraycalc.Inparameters.CalcModel.FINPAV;
import static se.e2t.xraycalc.Inparameters.CalcModel.NIST;

/**
 *
 * @author Kent Ericsson, e2t AB
 * 
 * Class stores the parameters to be used for the x-ray spectrum calculations.
 * 
 */
public class Inparameters {
    
    public static final double CONV_KEV_ANGSTROM = 12.398d;
    public static final double ANGLE_CONV = Math.PI / 180.0d;
    public static final String THIS_PROGRAM_VERSION = "2.0.0";
    public static final int NUM_AVERAGE = 100;
    private String _programVersion;
    private String _algDescription;
    private TubeElement _anodeElement;
    private double _inAngle;
    private double _outAngle;
    private TubeElement _windowElement;
    private double _windowThickness; // micrometers
    private final List<FilterElement> _filterElements;
    private double _filterThickness;
    private double _tubeVoltage;
    private double _continuumIntervalSize;
    private double _maxWavelength;
    private Algorithm _algorithm;
    private boolean _splitAtAbsEdge;
    
    private static final List<TubeElement> ANODE_ELEMENTS;
    static {
        ANODE_ELEMENTS = new ArrayList<>();
        ANODE_ELEMENTS.add(new TubeElement("Rh", 45));
        ANODE_ELEMENTS.add(new TubeElement("Cr", 24));
        ANODE_ELEMENTS.add(new TubeElement("W", 74));
    }
    private static final List<TubeElement> WINDOW_ELEMENTS;
    static {
        WINDOW_ELEMENTS = new ArrayList<>();
        WINDOW_ELEMENTS.add(new TubeElement("Be", 4));
    }
    
     public static enum CalcModel {
        NIST, EBEL, FINPAV
    };
    
    private static final List<Algorithm> ALG_ALTERNATIVES;
    static {
        ALG_ALTERNATIVES = new ArrayList<>();
        ALG_ALTERNATIVES.add(new Algorithm("NIST (Pella et al.)", NIST));
        ALG_ALTERNATIVES.add(new Algorithm("Horst Ebel's algorithms", EBEL));
        ALG_ALTERNATIVES.add(new Algorithm("Finkelshtein and Pavlova", FINPAV));
    }
    
    public static final List<TubeElement> FILTER_ALTERNATIVES;
    static {
        FILTER_ALTERNATIVES = new ArrayList<>();
        FILTER_ALTERNATIVES.add(new TubeElement("Al", 13));
        FILTER_ALTERNATIVES.add(new TubeElement("Cu", 29));
        FILTER_ALTERNATIVES.add(new TubeElement("Zn", 30));
        FILTER_ALTERNATIVES.add(new TubeElement("Pb", 82));
    }
    
    public Inparameters() {
        
        // Allocate variables
        
        _filterElements = new ArrayList<>();
        _anodeElement = new TubeElement();
        _windowElement = new TubeElement();
        
        // Load default values
        
        _programVersion = THIS_PROGRAM_VERSION;
        _algDescription = ALG_ALTERNATIVES.get(0).getDescription();
        _anodeElement.setAtomicNumber(ANODE_ELEMENTS.get(0).getAtomicNumber());
        _anodeElement.setSymbol(ANODE_ELEMENTS.get(0).getSymbol());
        _inAngle = 90.0d;
        _outAngle = 90.0d;
        _windowElement.setAtomicNumber(WINDOW_ELEMENTS.get(0).getAtomicNumber());
        _windowElement.setSymbol(WINDOW_ELEMENTS.get(0).getSymbol());
        _windowThickness = 50.0d;
        _filterThickness = 0.0d;
        _tubeVoltage = 50.0d;
        _continuumIntervalSize = 0.1d;
        _maxWavelength = 12.0d;
        _algorithm = getAlgorithms().get(0);
        _splitAtAbsEdge = true;
    }

    public static List<TubeElement> getAnodeElements() {
        return ANODE_ELEMENTS;
    }
    
    public static List<TubeElement> getWindowElements() {
        return WINDOW_ELEMENTS;
    }
    
    public static List<Algorithm> getAlgorithms() {
        return ALG_ALTERNATIVES;
    }
    
    public static List<TubeElement> getFilterAlternatives() {
        return FILTER_ALTERNATIVES;
    }
    
    public TubeElement getAnodeElement() {
        return _anodeElement;
    }

    public double getInAngle() {
        return _inAngle;
    }

    public double getOutAngle() {
        return _outAngle;
    }

    public TubeElement getWindowElement() {
        return _windowElement;
    }

    public double getWindowThickness() {
        return _windowThickness;
    }

    public List<FilterElement> getFilterElements() {
        return _filterElements;
    }

    public double getFilterThickness() {
        return _filterThickness;
    }

    public double getTubeVoltage() {
        return _tubeVoltage;
    }

    public double getContinuumIntervalSize() {
        return _continuumIntervalSize;
    }

    public double getMaxWavelength() {
        return _maxWavelength;
    }

    public Algorithm getAlgorithm() {
        return _algorithm;
    }

    public void setAnodeElement(int anodeElement) {
        _anodeElement.setAtomicNumber(ANODE_ELEMENTS.get(anodeElement).getAtomicNumber());
        _anodeElement.setSymbol(ANODE_ELEMENTS.get(anodeElement).getSymbol());
    }

    public void setInAngle(double inAngle) {
        this._inAngle = inAngle;
    }

    public void setOutAngle(double outAngle) {
        this._outAngle = outAngle;
    }

    public void setWindowElement(int windowElement) {
        _windowElement.setAtomicNumber(WINDOW_ELEMENTS.get(windowElement).getAtomicNumber());
        _windowElement.setSymbol(WINDOW_ELEMENTS.get(windowElement).getSymbol());
    }

    public void setWindowThickness(double windowThickness) {
        this._windowThickness = windowThickness;
    }

    public void setFilterThickness(double filterThickness) {
        this._filterThickness = filterThickness;
    }

    public void setTubeVoltage(double tubeVoltage) {
        this._tubeVoltage = tubeVoltage;
    }

    public void setContiniumIntervalSize(double continiumIntervalSize) {
        this._continuumIntervalSize = continiumIntervalSize;
    }

    public void setMaxWavelength(double maxWavelength) {
        this._maxWavelength = maxWavelength;
    }

    public void setAlgorithm(Algorithm algorithm) {
        this._algorithm = algorithm;
    }
    
    public TubeElement getAnodeElementData(int index) {
        return ANODE_ELEMENTS.get(index);
    }
    
     public TubeElement getWindowElementData(int index) {
        return WINDOW_ELEMENTS.get(index);
    }

    public String getProgramVersion() {
        return _programVersion;
    }

    public void setProgramVersion(String programVersion) {
        this._programVersion = programVersion;
    }

    public boolean isSplitAtAbsEdge() {
        return _splitAtAbsEdge;
    }

    public void setSplitAtAbsEdge(boolean _splitAtAbsEdge) {
        this._splitAtAbsEdge = _splitAtAbsEdge;
    }
   
    /**
     * Class stoes tube element data
     */
    public static class TubeElement implements Cloneable {
    private String _symbol;
    private int _atomicNumber;
    
    public TubeElement() {
        this(null, 0);
    }
    
    public TubeElement(String symbol, int atomicNumber) {
        _symbol = symbol;
        _atomicNumber = atomicNumber;
    }

    public String getSymbol() {
        return _symbol;
    }

    public void setSymbol(String _symbol) {
        this._symbol = _symbol;
    }

    public int getAtomicNumber() {
        return _atomicNumber;
    }

    public void setAtomicNumber(int _atomicNumber) {
        this._atomicNumber = _atomicNumber;
    }
}
    
    /**
     * Class stores data of a tube spectrum calculation algorithm.
     */
    public static class Algorithm {
        private final String _description;
        private final CalcModel _calcModel;
        
        public Algorithm(String description, CalcModel calcModel) {
            _description = description;
            _calcModel = calcModel;
        }

        public String getDescription() {
            return _description;
        }

        public CalcModel getCalcModel() {
            return _calcModel;
        }
    }
}
