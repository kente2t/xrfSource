/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.e2t.guiutils;

import java.util.Optional;
import javafx.scene.Node;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;

/**
 *
 * @author Kent
 */
public class FocusMove {

    public static void transferFocus(Control textField) { // may use Control class?
        boolean isThisField = false;
        for (Node child : textField.getParent().getChildrenUnmodifiable()) {
            if (isThisField) {

                //This code will only execute after the current Node
                if (child.isFocusTraversable() && !child.isDisabled()) {
                    child.requestFocus();

                    //Reset check to prevent later Node from pulling focus
                    isThisField = false;
                }
            } else {

                //Check if this is the current Node
                isThisField = child.equals(textField);
            }
        }

        //Check if current Node still has focus
        boolean focusChanged = !textField.isFocused();
        if (!focusChanged) {
            for (Node child : textField.getParent().getChildrenUnmodifiable()) {
                if (!focusChanged && child.isFocusTraversable() && !child.isDisabled()) {
                    child.requestFocus();

                    //Update to prevent later Node from pulling focus
                    focusChanged = true;
                }
            }
        }
    }
    public static void keepFocus(TextField textField) { // may use Control class?
        Optional<Node> thisField = textField.getParent().getChildrenUnmodifiable().stream()
                .filter(child -> child.equals((Node) textField))
                .findFirst();
        if (thisField.isPresent()) {
            thisField.get().requestFocus();
            System.out.println("Trying to keep focus");
        }
    }
    
    
    
}
