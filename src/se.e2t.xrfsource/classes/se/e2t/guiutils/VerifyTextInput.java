/*
 * File: VerifyTextInput.java
 */
package se.e2t.guiutils;

import java.awt.Toolkit;
import java.util.Optional;
import javafx.scene.control.TextField;

/**
 *
 * @author Kent Ericsson, e2t AB
 * 
 * Class has a number of static methods to handle data input to Java FX
 * textfields.
 */
public class VerifyTextInput {

    public static boolean isDoubleOK(TextField textField, Optional<Double> min,
            Optional<Double> max) {
        boolean retval = true;
        double dValue = 0.0f;
        try {
            dValue = Double.valueOf(textField.getText());
        } catch (NumberFormatException nfe) {
            retval = false;
        }
        if (retval && min.isPresent()) {
            retval = dValue >= min.get();
        }
        if (retval && max.isPresent()) {
            retval = dValue <= max.get();
        }
        if (!retval) {
            Toolkit.getDefaultToolkit().beep();
        }
        return retval;
    }
    
    public static boolean isIntOK(TextField textField, Optional<Integer> min,
            Optional<Integer> max) {
        boolean retval = true;
        float iValue = 0;
        try {
            iValue = Integer.valueOf(textField.getText());
        } catch (NumberFormatException nfe) {
            retval = false;
        }
        if (retval && min.isPresent()) {
            retval = iValue >= min.get();
        }
        if (retval && max.isPresent()) {
            retval = iValue <= max.get();
        }
        if (!retval) {
            Toolkit.getDefaultToolkit().beep();
        }
        return retval;
    }

    // Method returns Float.NAN if invalid input
    
    public static double getDoubleInput(TextField textInput, Optional<Double> min,
            Optional<Double> max, String outputFormat) {

        // Verify data input
        if (isDoubleOK(textInput, min, max)) {

            // Move focus to next input field
            FocusMove.transferFocus(textInput);

            // get float value
            double dReturn = Double.valueOf(textInput.getText());

            // Update textfield
            textInput.setText(String.format(outputFormat, dReturn));

            // return converted data
            return dReturn;
        } else {
            return Double.NaN;
        }
    }

    public static Optional<Integer> getIntInput(TextField textInput, Optional<Integer> min,
            Optional<Integer> max) {

        // Verify data input
        if (isIntOK(textInput, min, max)) {

            // get return value
            int iReturn = Integer.valueOf(textInput.getText());

            // Move focus to next input field
            FocusMove.transferFocus(textInput);

            return Optional.of(iReturn);
        } else {
            return Optional.empty();
        }
    }
}
