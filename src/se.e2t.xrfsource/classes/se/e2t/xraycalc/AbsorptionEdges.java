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
    
     public static enum AbsEdge {K_EDGE, L1_EDGE, L2_EDGE, L3_EDGE, M5_EDGE};
     
      public static Optional<AbsEdge> getEdge(TubeLines.XrfLine line) {
        
        // Return correct Edge
        
        Optional<AbsEdge> retval = Optional.empty();
        switch (line) {
            case K_ALPHA_12:
            case K_BETA_1:
                retval = Optional.of(AbsEdge.K_EDGE);
                break;
            case L_ALPHA_12:
            case L_ALPHA_1:
            case L_ALPHA_2:
            case L_BETA_5:
            case L_BETA_6:
            case L_IOTA:
            case L_BETA_2:
                retval = Optional.of(AbsEdge.L3_EDGE);
                break;
            case L_BETA_3:
            case L_BETA_4:
            case L_GAMMA_2:
            case L_GAMMA_3:
                retval = Optional.of(AbsEdge.L1_EDGE);
                break;
            case L_BETA_1:
            case L_GAMMA_1:
            case L_ETA:
                 retval = Optional.of(AbsEdge.L2_EDGE);
                break;
            case M_ALPHA_12:
                retval = Optional.of(AbsEdge.M5_EDGE);
        }
        return retval;
    }
     
    public static Optional<Double> getEdgeEnergy(int atomZ, AbsEdge edge) {
        
        // Create Mucal object for the calculations
       
        Mucal mc = new Mucal(null, atomZ, 0.0d, 'C', true);
        Mucal.ErrorCode ec = mc.calculate();
        if (ec != Mucal.ErrorCode.no_error) {
            return Optional.empty();
        }
        
        // Return correct Edge energy
        
        Optional<Double> retval = Optional.empty();
        switch (edge) {
            case K_EDGE:
                retval = Optional.of(mc.getEnergy()[0]); //K edge
                break;
            case L1_EDGE:
                retval = Optional.of(mc.getEnergy()[1]); //L1 edge
                break;
            case L2_EDGE:
                retval = Optional.of(mc.getEnergy()[2]); //L2 edge
                break;
            case L3_EDGE:
                retval = Optional.of(mc.getEnergy()[3]); //L3 edge
                break;
            case M5_EDGE:
                if (atomZ == 74) {
                    retval = Optional.of(1.816d);
                }
        }
        return retval;
    }
}
