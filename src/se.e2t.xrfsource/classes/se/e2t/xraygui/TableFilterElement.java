/*
 * TbleFilterElement.java
 */
package se.e2t.xraygui;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Class defind for the display of data in the Filter table.
 * 
 * @author Kent
 */
public class TableFilterElement {
    
    private final SimpleStringProperty _element;
    private final SimpleDoubleProperty _conc;
    
    public TableFilterElement(String element, double conc) {
        _element = new SimpleStringProperty(element);
        _conc = new SimpleDoubleProperty(conc);
    }
    
    public StringProperty elementProperty() {
        return _element;
    }
    
    public DoubleProperty concProperty() {
        return _conc;
    }
}
