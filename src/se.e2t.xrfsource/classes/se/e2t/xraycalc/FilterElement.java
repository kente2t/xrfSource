/*
 * File: FilterElement.java
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
