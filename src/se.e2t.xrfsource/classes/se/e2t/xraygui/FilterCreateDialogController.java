/*
 * File: FilterCreateDialogController.java
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
     * Initializes the controller class.
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

    @FXML
    private void addFilterElement(ActionEvent event) {

        // Calculate max concentration for new element
        double cSum = _filterElements.stream()
                .map(fElement -> fElement.getConc())
                .reduce(0.0d, (a, b) -> a + b);

        // Verify data input
        if (VerifyTextInput.isDoubleOK(_filterElementConc, Optional.of(0.0d),
                Optional.of(100.0d - 100.0d * cSum))) {

            int selectedIndex = _filterElementSelect.getSelectionModel().getSelectedIndex();

            _filterElements.add(new FilterElement(
                    Inparameters.FILTER_ALTERNATIVES.get(selectedIndex).getSymbol(),
                    Inparameters.FILTER_ALTERNATIVES.get(selectedIndex).getAtomicNumber(),
                    0.01d * Double.valueOf(_filterElementConc.getText())));

            cSum = _filterElements.stream()
                    .map(fElement -> fElement.getConc())
                    .reduce(0.0d, (a, b) -> a + b);

            _concSum.setText(String.format("%.1f", 100.0d * cSum));
            
            // Preset conc of next element to the remaining to 100%
            
            _filterElementConc.setText(String.format("%.1f", 100.0d * (1.0d -cSum)));
        }

    }

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
        }
        else Toolkit.getDefaultToolkit().beep();
    }

    @FXML
    private void cancelKeyPressed(ActionEvent event) {
        // Close window
        Stage stage = (Stage) _cancelKey.getScene().getWindow();
        stage.close();
    }

    // Method called by controller of parent stage to set the main program reference
    public void setMainProgram(Guistart mainProgram) {
        _mainProgram = mainProgram;

    }
}
