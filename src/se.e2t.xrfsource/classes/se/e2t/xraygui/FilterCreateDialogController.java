/*
 * File: FilterCreateDialogController.java
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
package se.e2t.xraygui;

import java.awt.Toolkit;
import se.e2t.guiutils.VerifyTextInput;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import se.e2t.xraycalc.FilterElement;
import se.e2t.xraycalc.Inparameters;

/**
 * FXML Controller class
 *
 * @author Kent Ericsson, e2t AB
 * 
 * This class handles the GUI interactions of the define filter pop-up window.
 */
public class FilterCreateDialogController implements Initializable {

    private Guistart _mainProgram;
    private List<FilterElement> _filterElements;

    @FXML
    private TextField _filterElementConc;
    @FXML
    private Button _addFilterElementButton;
    @FXML
    private Button _storeKey;
    @FXML
    private Button _cancelKey;
    @FXML
    private ChoiceBox<String> _filterElementSelect;
    @FXML
    private TextField _concSum;

    /**
     * Methos initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Populate the filter element choicebox
        ObservableList<String> filterAlt = FXCollections.observableArrayList();
        Inparameters.getFilterAlternatives().stream()
                .forEach(element -> filterAlt.add(element.getSymbol()));
        _filterElementSelect.setItems(filterAlt);

        // Make first alternative selected
        _filterElementSelect.getSelectionModel().selectFirst();

        // Preset the percentage text window and sum
        _filterElementConc.setText("100.0");
        _concSum.setText("0.0");

        // Allocate local storage for added filter elements
        _filterElements = new ArrayList<>();
    }

    /**
     * Keyboard pressed in element concentration text field.
     * @param event 
     */
    @FXML
    private void filterElementConcKeyPressed(KeyEvent event) {

        // Calculate max concentration for new element
        double cSum = _filterElements.stream()
                .map(fElement -> fElement.getConc())
                .reduce(0.0d, (a, b) -> a + b);

        // Verify data input
        if (event.getCode() == KeyCode.TAB) {
            if (Double.isNaN( VerifyTextInput.getDoubleInput(_filterElementConc, Optional.of(0.0d),
                Optional.of(100.0d - 100.0d * cSum), "%.1f"))) {
                event.consume();
            }
        }
    }

    /** Operator exits conc text field.
     * 
     * @param event 
     */
    @FXML
    private void filterElementConcOnAction(ActionEvent event) {

        // Calculate max concentration for new element
        double cSum = _filterElements.stream()
                .map(fElement -> fElement.getConc())
                .reduce(0.0d, (a, b) -> a + b);

        // Verify data input
        VerifyTextInput.getDoubleInput(_filterElementConc, Optional.of(0.0d),
                Optional.of(100.0d - 100.0d * cSum), "%.1f");
    }

    /**
     * Add filter element button pressed.
     * @param event 
     */
    @FXML
    private void addFilterElement(ActionEvent event) {

        // Calculate max concentration for new element
        double cSum = _filterElements.stream()
                .map(fElement -> fElement.getConc())
                .reduce(0.0d, (a, b) -> a + b);

        // Verify data input
        if (VerifyTextInput.isDoubleOK(_filterElementConc, Optional.of(0.0d),
                Optional.of(100.0d - 100.0d * cSum))) {

            // Add selected filter element
            int selectedIndex = _filterElementSelect.getSelectionModel().getSelectedIndex();
            _filterElements.add(new FilterElement(
                    Inparameters.FILTER_ALTERNATIVES.get(selectedIndex).getSymbol(),
                    Inparameters.FILTER_ALTERNATIVES.get(selectedIndex).getAtomicNumber(),
                    0.01d * Double.valueOf(_filterElementConc.getText())));

            // Update sum text field
            cSum = _filterElements.stream()
                    .map(fElement -> fElement.getConc())
                    .reduce(0.0d, (a, b) -> a + b);
            _concSum.setText(String.format("%.1f", 100.0d * cSum));
            
            // Preset conc of next element to the remaining to 100%
            _filterElementConc.setText(String.format("%.1f", 100.0d * (1.0d -cSum)));
        }
    }

    /**
     * Store button was pressed.
     * @param event 
     */
    @FXML
    private void storeKeyPressed(ActionEvent event) {

        // Check if concentration sum is 100%
        double cSum = _filterElements.stream()
                .map(fElement -> fElement.getConc())
                .reduce(0.0d, (a, b) -> a + b);
        if (String.format("%.1f", 100.0f * cSum).equals("100.0")) {

            // Copy selected data to the Inparameter object
            _mainProgram.getInparameters().getFilterElements().clear();
            _filterElements.stream()
                    .forEach(fElement
                            -> _mainProgram.getInparameters().getFilterElements()
                            .add(fElement));

            // Order parent to update filter table
            _mainProgram.getPrimaryController().updateFilterTable();

            // close window
            Stage stage = (Stage) _storeKey.getScene().getWindow();
            stage.close();
        } else {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    /** Cancel was pressed.
     * 
     * @param event 
     */
    @FXML
    private void cancelKeyPressed(ActionEvent event) {
        // Close window
        Stage stage = (Stage) _cancelKey.getScene().getWindow();
        stage.close();
    }

    /**
     * This method is called from the GUI main controller to give this
     * class a reference to the parameter storage and to the controller
     * of the parent window.
     * .
     * @param mainProgram 
     */
    // Method called by controller of parent stage to set the main program reference
    public void setMainProgram(Guistart mainProgram) {
        _mainProgram = mainProgram;

    }
}
