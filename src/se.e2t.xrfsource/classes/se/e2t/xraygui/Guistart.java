/*
 * File Guistart.java
 */
package se.e2t.xraygui;

import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.e2t.xraycalc.Inparameters;
import se.e2t.xraycalc.TubeLines;

/**
 *
 * @author Kent Ericsson, e2t AB
 * 
 * Class creates the InParameters object and start the GUI.
 */
public class Guistart extends Application {
    public static final String PROG_VER = "2.0.0";
    public static final String PROG_TITLE_STRING = " - XraySource";
    private Inparameters _inParameters;
    private Stage _primaryStage;
    private XraySourceGuiMainController _primaryController;
    
    @Override
    public void init() {
        int kalle = 1;
    }
    @Override
    public void start(Stage stage) throws Exception {
        
        // Select GB locales
        
        Locale.setDefault(new Locale("en", "GB"));
        
        // Save reference to primaryStage
        
        _primaryStage = stage;
        
        // Create object to store parameters
        
        _inParameters = new Inparameters();
        
        // Force static initializer of TubeLInes to be run
        
        Class.forName("se.e2t.xraycalc.TubeLines");
        
        // Load FXML file
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/se/e2t/resources/xraySourceGuiMain.fxml"));
        Parent root = loader.load();
        
        // Send reference to this main program to the top controller
        
        _primaryController = ((XraySourceGuiMainController) (loader.getController()));
        _primaryController.setMainProgram((Guistart) this);
        // Start the GUI
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    public Stage getPrimaryStage() {
        return _primaryStage;
    }
    
    public Inparameters getInparameters() {
        return _inParameters;
    }

    public XraySourceGuiMainController getPrimaryController() {
        return _primaryController;
    }
    
    public void setParameterName(String name) {
        _primaryStage.setTitle(name + PROG_TITLE_STRING);
    }
    
}
