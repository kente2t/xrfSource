/*
 * AbsorptionEdges.java
 */
package se.e2t.xraycalc;

import java.util.Optional;
import se.e2t.abscoeffcalculate.Mucal;

/**
 *
 * @author Kent
 * 
 * Class uses Mucal.java to deliver the absorption edge energy of
 * a certain characteristic line of an atom.
 */
public class AbsorptionEdges {
    public static Optional<Double> getEdge(int atomZ, TubeLines.XrfLine line) {
        
        // Create Mucal object for the calculations
       
        Mucal mc = new Mucal(null, atomZ, 0.0d, 'C', true);
        Mucal.ErrorCode ec = mc.calculate();
        if (ec != Mucal.ErrorCode.no_error) {
            return Optional.empty();
        }
        
        // Return correct Edge energy
        
        Double edge = null;
        switch (line) {
            case K_ALPHA_12:
            case K_BETA_1:
                edge = mc.getEnergy()[0]; //K edge
                break;
            case L_ALPHA_12:
            case L_ALPHA_1:
            case L_ALPHA_2:
            case L_BETA_5:
            case L_BETA_6:
            case L_IOTA:
            case L_BETA_2:
                edge = mc.getEnergy()[3]; // L3 edge
                break;
            case L_BETA_3:
            case L_BETA_4:
            case L_GAMMA_2:
            case L_GAMMA_3:
                edge =mc.getEnergy()[1]; // L1 edge
                break;
            case L_BETA_1:
            case L_GAMMA_1:
            case L_ETA:
                edge = mc.getEnergy()[2]; // L2
                break;
            case M_ALPHA_12:
                edge = 1.816d; // M5 edge
        }
        return Optional.ofNullable(edge);
    }
}
