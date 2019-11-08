/*
 * File: FilterElement.java
 */
package se.e2t.xraycalc;

import se.e2t.xraycalc.Inparameters.TubeElement;

/**
 *
 * @author Kent Ericsson, e2t AB
 * 
 * Class to store the data of a tube filter element
 */
public class FilterElement implements Cloneable {
    private TubeElement _selectedElement;
    private double _conc;
    
    public FilterElement() {
        this(null, 0, 0.0d);
    }
    
    public FilterElement(String elementSymbol, int atomicNumber, double percentage) {
        _selectedElement = new TubeElement(elementSymbol, atomicNumber);
        _conc = percentage;
    }

    public TubeElement getSelectedElement() {
        return _selectedElement;
    }

    public void setSelectedElement(TubeElement _selectedElement) {
        this._selectedElement = _selectedElement;
    }

    public double getConc() {
        return _conc;
    }

    public void setConc(double _percentage) {
        this._conc = _percentage;
    }

    
}
