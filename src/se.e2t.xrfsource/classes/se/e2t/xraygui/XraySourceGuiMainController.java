/*
 * File: XraySourceGuiMainController.java
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
import se.e2t.guiutils.FocusMove;
import se.e2t.guiutils.VerifyTextInput;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.ServiceLoader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import se.e2t.xraycalc.EbelCalculation;
import se.e2t.xraycalc.FinPavCalculation;
import se.e2t.xraycalc.Inparameters;
import static se.e2t.xraycalc.Inparameters.getAlgorithms;
import se.e2t.xraycalc.Inparameters.CalcModel;
import static se.e2t.xraycalc.Inparameters.getAnodeElements;
import static se.e2t.xraycalc.Inparameters.getWindowElements;
import se.e2t.xraycalc.NistCalculation;
import se.e2t.xraycalc.SpectrumFileWriter;
import static se.e2t.xraygui.Guistart.PROG_TITLE_STRING;
import se.e2t.xrfsource.spectrumclasses.XraySpectrum;
import se.e2t.xraymisc.OpenSaveParameters;
import se.e2t.xrfsource.format.spi.SpectrumFormatSPI;

/**
 * FXML Controller class
 *
 * @author Kent Ericsson, e2t AB
 *
 * Class contains code that cooperates with the definitions of the Screen
 * Builder FXML file XraySourceGuiMain.fxml. Code was initially generated
 * by marking the fxml file in Netbeans and via rightclick selecting
 * Make controller.This file and the fxml file handles the main GUI of the
 * application.
 */
public class XraySourceGuiMainController implements Initializable {

    private Inparameters _inParameters;
    private String _lastParamFilePath;
    private String _lastDirectoryPath;
    private Guistart _mainProgram;
    private final ObservableList<TableFilterElement> _filterElements
            = FXCollections.observableArrayList();
    private static final String[] PAR_ERROR_TEXT = {
        "Electron incident angle",
        "Photon exiting angle",
        "Tube window thickness",
        "Tube filter",
        "Tube filter thickness",
        "Tube voltage",
        "X-ray continuum interval size",
        "X-ray continuum max wavelength"
    };

    @FXML
    private TextField _inangle;
    @FXML
    private ChoiceBox<String> _anodeList;
    @FXML
    private TextField _xrayOutAngle;
    @FXML
    private ChoiceBox<String> _tubeWindowElement;
    @FXML
    private TextField _windowThickness;
    @FXML
    private ChoiceBox<String> _algSelection;
    @FXML
    private Button _modifyFilterButton;
    @FXML
    private TableColumn<TableFilterElement, String> _elementColumn;
    @FXML
    private TableColumn<TableFilterElement, Double> _percentColumn;
    @FXML
    private TableView<TableFilterElement> _filterTable;
    @FXML
    private TextField _filterThickness;
    @FXML
    private TextField _tubeVoltage;
    @FXML
    private TextField _continuumSlice;
    @FXML
    private Button _generateFileButton;
    @FXML
    private TextField _maxWavelength;
    @FXML
    private MenuBar _menuBar;
    @FXML
    private Button _removeFilterButton;
    @FXML
    private CheckBox _splitAtAbsEdge;

    /**
     * Initializes the controller class.
     * @param url not used
     * @param rb not used
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Populate the anode choicebox
        ObservableList<String> anodeAlt = FXCollections.observableArrayList();
        getAnodeElements().stream()
                .forEach(element -> anodeAlt.add(element.getSymbol()));
        _anodeList.setItems(anodeAlt);

        // Make first alternative selected
        _anodeList.getSelectionModel().selectFirst();

        // Add a listener for changes of the anode element list
        _anodeList.getSelectionModel().selectedIndexProperty().addListener(
                (ov, oldIndex, newIndex) -> newAnodeElement((int) oldIndex, (int) newIndex));

        // Populate the tube window choicebox
        ObservableList<String> windowAlt = FXCollections.observableArrayList();
        getWindowElements().stream()
                .forEach(element -> windowAlt.add(element.getSymbol()));
        _tubeWindowElement.setItems(windowAlt);

        // Make first alternative selected
        _tubeWindowElement.getSelectionModel().selectFirst();

        // Add a listener for changes of the window element list
        _tubeWindowElement.getSelectionModel().selectedIndexProperty().addListener(
                (ov, oldIndex, newIndex) -> newWindowElement((int) oldIndex, (int) newIndex));

        // Populate the calculation algorithm choicebox
        ObservableList<String> algAlt = FXCollections.observableArrayList();
        getAlgorithms().stream()
                .forEach(alternative -> algAlt.add(alternative.getDescription()));
        _algSelection.setItems(algAlt);

        // Make first alternative selected
        _algSelection.getSelectionModel().selectFirst();

        // Add a listener for changes of the algorithm choicebox
        _algSelection.getSelectionModel().selectedIndexProperty().addListener(
                (ov, oldIndex, newIndex) -> newAlgorithm((int) oldIndex, (int) newIndex));

        // Associate filter table columns to TableFilterElement properties
        _elementColumn.setCellValueFactory(
                new PropertyValueFactory<>("element"));
        _percentColumn.setCellValueFactory(
                new PropertyValueFactory<>("conc"));
        
        // Define a renderer for the conc column
        _percentColumn.setCellFactory(column -> {
            return new TableCell<TableFilterElement, Double>() {
                @Override
                protected void updateItem(Double item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                        setStyle("");
                    } else {
                        // Format percentage data.
                        setText(String.format("%.1f", item));
                    }
                }
            };
        });
        
        // Connect data to table
        _filterTable.setItems(_filterElements);
        
        // Indicate no input file so far, select current directory
        _lastParamFilePath = null;
        _lastDirectoryPath = System.getProperty("user.dir");
    }

    /**
     * Method called from main to send reference to primary stage
     * and to get a reference to the inparameter object.
     * @param mainProgram
     */
    public void setMainProgram(Guistart mainProgram) {
        _mainProgram = mainProgram;

        // Get reference to parameter storage
        _inParameters = mainProgram.getInparameters();
        
        // Set window title
        _mainProgram.setParameterName("New parameters");

        // Update parameter display
        updateParameterDisplay(_inParameters);
    }
    
    private void updateParameterDisplay(Inparameters parameters) {
        // Anode element ChoiceList
        for (int i = 0; i < Inparameters.getAnodeElements().size(); i++) {
            if (Inparameters.getAnodeElements().get(i).getAtomicNumber() ==
                    parameters.getAnodeElement().getAtomicNumber()) {
                _anodeList.getSelectionModel().select(i);
            }
        }
        
        // Incident electron input angle
        _inangle.setText(String.format("%.1f", parameters.getInAngle()));
        
        // Outgoing photons angle
        _xrayOutAngle.setText(String.format("%.1f", parameters.getOutAngle()));
        
        // Tube window ChoiceList
         for (int i = 0; i < Inparameters.getWindowElements().size(); i++) {
            if (Inparameters.getWindowElements().get(i).getAtomicNumber() ==
                    parameters.getWindowElement().getAtomicNumber()) {
                _tubeWindowElement.getSelectionModel().select(i);
            }
        }
         
        // Tube window thickness
        _windowThickness.setText(String.format("%.0f", parameters.getWindowThickness()));
        
        // Tube filter table
        _filterElements.clear();
        parameters.getFilterElements().stream()
                .forEach(fElement
                        -> _filterElements.add(new TableFilterElement(fElement.getSelectedElement().getSymbol(),
                        100.0d * fElement.getConc())));
        
        // Tube filter thickness
        _filterThickness.setText(String.format("%.0f", parameters.getFilterThickness()));
        
        // Tube voltage
        _tubeVoltage.setText(String.format("%.1f", parameters.getTubeVoltage()));
        
        // Continuum interval size
        _continuumSlice.setText(String.format("%.3f", parameters.getContinuumIntervalSize()));
        
        // Split at absorption edge
        _splitAtAbsEdge.setSelected(parameters.isSplitAtAbsEdge());
        
        // Max wavelength
        _maxWavelength.setText(String.format("%.1f", parameters.getMaxWavelength()));
        
        // Calculation algorithm ChoiceList
        _algSelection.getSelectionModel().select(getAlgorithms().indexOf(parameters.getAlgorithm()));
    }

    /**
     * Listener for selections in the anode element choicebox
     */
    private void newAnodeElement(int oldIndex, int newIndex) {
        _inParameters.setAnodeElement(newIndex);
    }

    /**
     * Listener for selections in the window element choicebox
     * @param oldIndex
     * @param newIndex 
     */
    private void newWindowElement(int oldIndex, int newIndex) {
        _inParameters.setWindowElement(newIndex);
    }

    /**
     * Listener for selections in algoritm choicebox
     * @param oldIndex
     * @param newIndex 
     */
    private void newAlgorithm(int oldIndex, int newIndex) {
        _inParameters.setAlgorithm(getAlgorithms().get(newIndex));
    }

    /**
     * Handler executing when a new angle of ingoing electrons have been input
     */ 
    @FXML
    private void angle_in(ActionEvent event) {
        verifyInAngle();
    }

    /** Handler executing when a tab is entered in ingoing electrons angle text field
     * 
     * @param event 
     */
    @FXML
    private void angle_in_key_pressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            double dInput = verifyInAngle();
            if (Double.isNaN(dInput)) {
                event.consume();
            }
        }
    }

    private double verifyInAngle() {
        double dInput = VerifyTextInput.getDoubleInput(_inangle, Optional.of(0.0d),
                Optional.of(90.0d), "%.1f");
        if (!Double.isNaN(dInput)) {
            _inParameters.setInAngle(dInput);
        }
        return dInput;
    }
    
    /**
     * Handler executing when a new angle of outoing photons have been input.
     * @param event 
     */
    @FXML
    private void angleOut(ActionEvent event) {
        verifyAngleOut();
    }

    /** Handler executing when a tab is entered in outgoing photons angle text field.
     * 
     * @param event 
     */
    @FXML
    private void angleOutKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            double dInput = verifyAngleOut();
            if (Double.isNaN(dInput)) {
                event.consume();
            }
        }
    }

    private double verifyAngleOut() {
        double dInput = VerifyTextInput.getDoubleInput(_xrayOutAngle, Optional.of(0.0d),
                Optional.of(90.0d), "%.1f");
        if (!Double.isNaN(dInput)) {
            _inParameters.setOutAngle(dInput);
        }
        return dInput;
    }

    /** Handler executing when a new tube thickness have been input.
     * 
     * @param event 
     */
    @FXML
    private void windowThicknessKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            Optional<Integer> iInput = verifyWindowThickness();
            if (!iInput.isPresent()) {
                event.consume();
            }
        }
    }
    
    /** Handler executing when a new tube thickness have been input.
     * 
     * @param event 
     */
    @FXML
    private void windowThicknessAction(ActionEvent event) {
        verifyWindowThickness();
    }

    private Optional<Integer> verifyWindowThickness() {
        Optional<Integer> iInput = VerifyTextInput.getIntInput(_windowThickness,
                Optional.of(0), Optional.of(500));
        if (iInput.isPresent()) {
            _inParameters.setWindowThickness((double) iInput.get());
        }
        return iInput;
    }
    
    /**
     * Method called when Modify Filter button is pressed.
     * @param event
     * @throws IOException if fxml file is not found.
     */
    @FXML
    private void modifyFilterButtonPressed(ActionEvent event) throws IOException {
        final Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.initOwner(_mainProgram.getPrimaryStage());
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/se/e2t/resources/filterCreateDialog.fxml"));
        Parent root = loader.load();

        // Send main program address to dialog controller
        ((FilterCreateDialogController) (loader.getController())).setMainProgram(_mainProgram);

        // Load dialog GUI
        Scene scene = new Scene(root);
        dialog.setTitle("Specify filter contents");
        dialog.setScene(scene);
        dialog.show();
    }

    /**
     * Method called from the modify filter popup window
     * 
     */
    public void updateFilterTable() {
        _filterElements.clear();

        _inParameters.getFilterElements().stream()
                .forEach(fElement
                        -> _filterElements.add(new TableFilterElement(fElement.getSelectedElement().getSymbol(),
                        100.0d * fElement.getConc())));
    }
    
    /**
     * Method called when the filter thickness text field is modified.
     * 
     * @param event 
     */
    @FXML
    private void filterThicknessAction(ActionEvent event) {
        verifyFilterThickness();
    }
    
    /**
     * Method called when the filter thickness text field is modified.
     * 
     * @param event 
     */
    @FXML
    private void filterThicknessKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            Optional<Integer> iInput = verifyFilterThickness();
            if (!iInput.isPresent()) {
                event.consume();
            }
        }
    }

    private Optional<Integer> verifyFilterThickness() {
        Optional<Integer> iInput = VerifyTextInput.getIntInput(_filterThickness,
                Optional.of(0), Optional.of(1000));
        if (iInput.isPresent()) {
            _inParameters.setFilterThickness((double) iInput.get());
        }
        return iInput;
    }
    
    /**
     * Method is called when the tube voltage text field is modified.
     * 
     * @param event 
     */
    @FXML
    private void tubeVoltageAction(ActionEvent event) {
        verifyTubeVoltage();
    }
    
    /**
     * Method is called when the tube voltage text field is modified.
     * 
     * @param event 
     */
    @FXML
    private void tubeVoltageKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            double dInput = verifyTubeVoltage();
            if (Double.isNaN(dInput)) {
                event.consume();
            }
        }
    }

    private double verifyTubeVoltage() {
        double dInput = VerifyTextInput.getDoubleInput(_tubeVoltage, Optional.of(10.0d),
                Optional.of(110.0d), "%.1f");
        if (!Double.isNaN(dInput)) {
            _inParameters.setTubeVoltage(dInput);
        }
        return dInput;
    }
    
    /**
     * Method is called when the continuum slice text field is modified.
     * 
     * @param event 
     */
    @FXML
    private void continuumSliceAction(ActionEvent event) {
        verifyContinuumSlice();
    }
    
    /**
     * Method is called when the continuum slice text field is modified.
     * 
     * @param event 
     */
    @FXML
    private void continuumSliceKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            double dInput = verifyContinuumSlice();
            if (Double.isNaN(dInput)) {
                event.consume();
            }
        }
    }

    private double verifyContinuumSlice() {
        double dInput = VerifyTextInput.getDoubleInput(_continuumSlice, Optional.of(0.001d),
                Optional.of(1.0d), "%.3f");
        if (!Double.isNaN(dInput)) {
            _inParameters.setContiniumIntervalSize(dInput);
        }
        return dInput;
    }

    /**
     * Method is called when the max wavelength text field is modified.
     * 
     * @param event 
     */
    @FXML
    private void maxWavelengthAction(ActionEvent event) {
        verifyMaxWavelength();
    }
    /**
     * Method is called when the max wavelength text field is modified.
     * 
     * @param event 
     */
    @FXML
    private void maxWavelengthKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            double dInput = verifyMaxWavelength();
            if (Double.isNaN(dInput)) {
                event.consume();
            }
        }
    }

    private double verifyMaxWavelength() {
        double dInput = VerifyTextInput.getDoubleInput(_maxWavelength, Optional.of(1.0d),
                Optional.of(20.0d), "%.1f");
        if (!Double.isNaN(dInput)) {
            _inParameters.setMaxWavelength(dInput);
        }
        return dInput;
    }
    
    /**
     * Method is called when the Generate Spectrum button is pressed.
     * @param event 
     */
    @FXML
    private void generateFileButtonAction(ActionEvent event) {

        // Verify parameters before doing the calculations
        int retCode = verifyInputParameters();
        if (retCode != 0) {
            Toolkit.getDefaultToolkit().beep();
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Please verify parameter " + PAR_ERROR_TEXT[retCode - 1]);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        // Parameters are OK, use selected algorithm for the calculations
        XraySpectrum outputData = null;
        int index = _algSelection.getSelectionModel().getSelectedIndex();
        CalcModel calcModel = getAlgorithms().get(index).getCalcModel();
        switch (calcModel) {
            case NIST: //NIST algorithm
                outputData = new NistCalculation().calculate(_inParameters);     
                break;
            case EBEL:
                outputData = new EbelCalculation().calculate(_inParameters);
                break;
            case FINPAV:
                outputData = new FinPavCalculation().calculate(_inParameters);
            default:
                Toolkit.getDefaultToolkit().beep();
        }
        
        // Count the number of spectrum intervals
         if (outputData == null) {
             Toolkit.getDefaultToolkit().beep();
             Alert alert = new Alert(Alert.AlertType.ERROR,
                     "No spectrum intervals were produced!");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        int numIntervals = outputData.getTubeLines().size() +
                outputData.getContinuum().size();

        // Inform number of spectrum intervals
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Generated spectrum has " + numIntervals + " intervals.\n" +
                        "Compare this number to the maximum number\n" +
                        "of intervals handled by your calculation program.");
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        Optional<ButtonType> response = alert.showAndWait();
        if (response.isPresent() && (response.get() == ButtonType.CANCEL)) return;
        
        // Ask for a file name
        File file;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Specify spectrum file name");
        fileChooser.setInitialDirectory(new File(_lastDirectoryPath));
        // Locate possible formatter providers and an extension filter for each
        ServiceLoader<SpectrumFormatSPI> loader = ServiceLoader.load(SpectrumFormatSPI.class);
        if (!loader.iterator().hasNext()) {
            Toolkit.getDefaultToolkit().beep();
            alert = new Alert(Alert.AlertType.ERROR,
                    "No spectrum data formatter was found!");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        // Add installed providers
        for (SpectrumFormatSPI service : loader) {
            String description = service.getDescription();
            String extensions = service.getExtensions();
             fileChooser.getExtensionFilters().add(
                new ExtensionFilter(description, extensions));
        }
        file = fileChooser.showSaveDialog(_mainProgram.getPrimaryStage());
        byte[] formattedOutput;
        String extension = null;
        if (file == null) {
            return; // Operator selected Cancel
        } else {
            // Format spectrum info using selected formatter
            String SelDescription = fileChooser.getSelectedExtensionFilter().getDescription();
            formattedOutput = null;
            for (SpectrumFormatSPI service : loader) {
                if (service.getDescription().equals(SelDescription)) {
                    formattedOutput = service.createByteArray(outputData);
                    // Extract service files extension
                    extension = getExtension(service.getExtensions()).get();
                    if (formattedOutput == null) {
                        Toolkit.getDefaultToolkit().beep();
                        alert = new Alert(Alert.AlertType.ERROR,
                                "Formatter reported an error:\n"
                                + "Error code = " + service.getErrorCode() + "\n"
                                + "Description = " + service.getDescription());
                        alert.setHeaderText(null);
                        alert.showAndWait();
                        return;
                    }
                }
            }
        }
        // Save directory path
        _lastDirectoryPath = file.getParent();
        
        // Add extension to filename if necessary (no ext added by Linux)
        Optional<String> fileExtension = getExtension(file.toString());
        if (fileExtension.isEmpty()) {
            file = new File(file.toString() + extension);
        }
        else {
            extension = fileExtension.get();
        }
        
        // Write generated byte buffer to file
        int retval = SpectrumFileWriter.writeToFile(formattedOutput, file);
        if (retval != 0) {
            alert = new Alert(Alert.AlertType.ERROR,
                    "Spectrum write to file returned error code " + retval);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        
        // Message to operator
        alert = new Alert(Alert.AlertType.INFORMATION,
                "Spectrum was successfully written to file!");
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.showAndWait();

        // Output parameters to XML file
        // Write parameters to modified file name if spectrum was saved as xml
        String parameterPath;
        if (extension.equalsIgnoreCase(".xml")) {
            parameterPath = file.toString().replace(extension, "_par.xml");
        }
        else {
            parameterPath = file.toString().replace(extension, ".xml");
        }
        File parameterFile = new File(parameterPath);
        OpenSaveParameters.saveParameters(_inParameters, parameterFile);
        
        // Save parameter file path
        _lastParamFilePath = parameterFile.getPath();
        
        // Update window title
        _mainProgram.setParameterName(parameterFile.getName());
    }
    
    /**
     * Method extracts the extension (including ".") from a file path.
     * @param fileName
     * @return 
     */
    private Optional<String> getExtension(String fileName) {
        Pattern p;
        try {
            p = Pattern.compile("^(.*)\\.(.*)$");
        } catch (PatternSyntaxException pse) {
            System.out.println("Pattern syntax error!");
            return Optional.empty();
        }
        Matcher m = p.matcher(fileName);
        if (!m.matches()) {
            return Optional.empty();
        }
        return Optional.of("." + m.group(2));
    }

    /**
     * Method called before generating tube spectrum.
     * @return 0 if OK, error codes 1-8, see below
     */
    private int verifyInputParameters() {
        // Verify inparameter data
        if (Double.isNaN(verifyInAngle())) {
            return 1;
        }
        if (Double.isNaN(verifyAngleOut())) {
            return 2;
        }
        if (!verifyWindowThickness().isPresent()) {
            return 3;
        }
        double cSum = _inParameters.getFilterElements().stream()
                .map(fElement -> fElement.getConc())
                .reduce(0.0d, (a, b) -> a + b);
        if (cSum != 0.0d && !String.format("%.1f", 100.0f * cSum).equals("100.0")) {
            return 4;
        }
        if (!verifyFilterThickness().isPresent()) {
            return 5;
        }
        if (Double.isNaN(verifyTubeVoltage())) {
            return 6;
        }
        if (Double.isNaN(verifyContinuumSlice())) {
            return 7;
        }
        if (Double.isNaN(verifyMaxWavelength())) {
            return 8;
        }
        return 0;
    }

    /**
     * Main menu Quit was selected.
     * @param event 
     */
    @FXML
    private void quitSelected(ActionEvent event) {
        // Close window
        Stage stage = (Stage) _menuBar.getScene().getWindow();
        stage.close();
    }

    /**
     * Remove filter button was pressed.
     * @param event 
     */
    @FXML
    private void removeFilterButtonPressed(ActionEvent event) {
        _filterElements.clear();
        _inParameters.getFilterElements().clear();
    }

    /**
     * Method transfers focus to the choicebox list if enter is pressed.
     * @param event 
     */
    @FXML
    private void tubeAnodeKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            FocusMove.transferFocus(_anodeList);
        }
    }
    
    /**
     * Method transfers focus to the choicebox list if enter is pressed.
     * @param event 
     */
    @FXML
    private void tubeWindowElementKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            FocusMove.transferFocus(_tubeWindowElement);
        }
    }

     /**
     * Method transfers focus to the choicebox list if enter is pressed.
     * @param event 
     */
    @FXML
    private void algSelectKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            FocusMove.transferFocus(_algSelection);
        }
    }

    /**
     * Activated if eneter is pressed when Generate button is focused.
     * @param event 
     */
    @FXML
    private void generateFileButtonKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            _generateFileButton.fire();
        }
    }

    /**
     * Activated if eneter is pressed when Tube filter button is focused.
     * @param event 
     */
    @FXML
    private void modifyFilterKeyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            _modifyFilterButton.fire();
        }
    }

    /**
     * Method called when open is selected via menu File-Open.
     * @param event 
     */
    @FXML
    private void openSelected(ActionEvent event) {
        
        //Open dialog to choose an input file name
        File file;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Specify parameter file name");
        fileChooser.setInitialDirectory(
                new File(_lastDirectoryPath));
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Xml files", "*.xml"));
        file = fileChooser.showOpenDialog(_mainProgram.getPrimaryStage());
        
        // Exit if user pressed cancel
        if (file == null) return;
        
        // Save file path and directory
        _lastParamFilePath = file.getPath();
        _lastDirectoryPath = file.getParent();
        
        // Read parameter data into InParameter object
        _inParameters.getFilterElements().clear();
        OpenSaveParameters.openParameters(_inParameters, file);
        
        // Update parameter display
        _mainProgram.setParameterName(file.getName());
        updateParameterDisplay(_inParameters);
    }

    /**
     * Method called when save is selected bia menu File-Save.
     * @param event 
     */
    @FXML
    private void saveSelected(ActionEvent event) {

        // Go to SaveAs if no input file was read
        String path = _lastParamFilePath;
        if (_lastParamFilePath == null) {
            saveAsSelected(event);
        } else {

            // Output parameters to XML
            OpenSaveParameters.saveParameters(_inParameters, new File(path));
        }
    }

    /**
     * Method called when save is selected bia menu File-Save As.
     * @param event 
     */
    @FXML
    private void saveAsSelected(ActionEvent event) {

        // Ask for a file name
        File file;
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Specify parameter file name");
        fileChooser.setInitialDirectory(
                new File(_lastDirectoryPath));
        fileChooser.getExtensionFilters().add(
                new ExtensionFilter("XML Files", "*.xml"));
        file = fileChooser.showSaveDialog(_mainProgram.getPrimaryStage());

        if (file != null) {

            // Output parameters to xml file
            OpenSaveParameters.saveParameters(_inParameters, file);

            // Save file and directory path
            _lastParamFilePath = file.getPath();
            _lastDirectoryPath = file.getParent();

            // Update window title
            _mainProgram.setParameterName(file.getName());
        }
    }
    
    /**
     * Help-About selected from main menu.
     * @param event 
     */
    @FXML
    private void aboutProgramSelected(ActionEvent event) {
         Alert alert = new Alert(Alert.AlertType.INFORMATION,
                "xrfSource version " + Guistart.PROG_VER + "\n" +
                        "Copyright e2t AB 2019\n" +
                        "xrfSource is licenced under The MIT License");
        alert.setTitle("About xrfSource");
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Checkbox "split interval at absorption edge" was changed.
     * @param event 
     */
    @FXML
    private void splitCheckBoxPressed(ActionEvent event) {
         _inParameters.setSplitAtAbsEdge(_splitAtAbsEdge.isSelected());
    }
}
