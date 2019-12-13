/*
 * File Guistart.java
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

import java.util.Locale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import se.e2t.xraycalc.Inparameters;

/**
 * @author Kent Ericsson, e2t AB
 * 
 * Class creates the InParameters object and start the GUI.
 */
public class Guistart extends Application {
    public static final String PROG_VER = "2.0.0";
    public static final String PROG_TITLE_STRING = " - xrfSource";
    private Inparameters _inParameters;
    private Stage _primaryStage;
    private XraySourceGuiMainController _primaryController;
    
    @Override
    public void init() {
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
        
        // Load FXML file of GUI
        FXMLLoader loader = new FXMLLoader(getClass().getResource(
                "/se/e2t/resources/xraySourceGuiMain.fxml"));
        Parent root = loader.load();
        
        // Send reference of this main program to the GUI controller
        _primaryController = ((XraySourceGuiMainController) (loader.getController()));
        _primaryController.setMainProgram((Guistart) this);
  
        // Start the GUI
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    // This is the start of the program
    // the launch method will start the Java FX Application by first calling
    // the init method, then the start method.
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * To get the reference to the Application primary stage.
     */
    public Stage getPrimaryStage() {
        return _primaryStage;
    }
    
    /**
     * To get a reference to the program input parameters.
     */
    public Inparameters getInparameters() {
        return _inParameters;
    }
    
    /**
     * To get a reference to the program primary controller.
     */
    public XraySourceGuiMainController getPrimaryController() {
        return _primaryController;
    }
    
    /**
     * To display the program name in the top of the program window.
     */
    public void setParameterName(String name) {
        _primaryStage.setTitle(name + PROG_TITLE_STRING);
    }
}
