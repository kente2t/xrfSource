/*
 * File: Inparameters.java
 */
package se.e2t.xraycalc;

import java.util.ArrayList;
import java.util.List;

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
    public static final String THIS_PROGRAM_VERSION = "1.0.0";
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
    private double _continiumIntervalSize;
    private double _maxWavelength;
    private int _algorithm;
    
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
    
       private static final List<String> ALGORITHMS;
    static {
        ALGORITHMS = new ArrayList<>();
        ALGORITHMS.add("NIST (Pella et al.)");
        ALGORITHMS.add("ALG 2");
        ALGORITHMS.add("ALG 3");
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
        _algDescription = ALGORITHMS.get(0);
        _anodeElement.setAtomicNumber(ANODE_ELEMENTS.get(0).getAtomicNumber());
        _anodeElement.setSymbol(ANODE_ELEMENTS.get(0).getSymbol());
        _inAngle = 45.0d;
        _outAngle = 90.0d;
        _windowElement.setAtomicNumber(WINDOW_ELEMENTS.get(0).getAtomicNumber());
        _windowElement.setSymbol(WINDOW_ELEMENTS.get(0).getSymbol());
        _windowThickness = 50.0d;
        _filterThickness = 0.0d;
        _tubeVoltage = 50.0d;
        _continiumIntervalSize = 0.1d;
        _maxWavelength = 12.0d;
        _algorithm = 0;
    }

    public static List<TubeElement> getAnodeElements() {
        return ANODE_ELEMENTS;
    }
    
    public static List<TubeElement> getWindowElements() {
        return WINDOW_ELEMENTS;
    }
    
    public static List<String> getAlgorithms() {
        return ALGORITHMS;
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

    public double getContiniumIntervalSize() {
        return _continiumIntervalSize;
    }

    public double getMaxWavelength() {
        return _maxWavelength;
    }

    public int getAlgorithm() {
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
        this._continiumIntervalSize = continiumIntervalSize;
    }

    public void setMaxWavelength(double maxWavelength) {
        this._maxWavelength = maxWavelength;
    }

    public void setAlgorithm(int algorithm) {
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

    public String getAlgDescription() {
        return _algDescription;
    }

    public void setAlgDescription(String algDescription) {
        this._algDescription = algDescription;
    }
     
     
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
    
    
}
