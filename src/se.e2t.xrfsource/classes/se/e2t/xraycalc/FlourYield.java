/*
 * FlourYield.java
 */
package se.e2t.xraycalc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Kent
 * 
 * Data of this static class is according to This paper:
 * Authors: J.H Hubbell, P.N. Trehan, Nirmal Singh, B. Chand, D. Metha, L. Garg,
 * R.R. Garg, Surinder Singh, S. Puri
 * Title: A Review, Bibliographu, and Tabulation of K, L, and higher Atomic
 * Shell X-Ray Flourescence Yields
 * Journal: Journal of Physical and Chemical Refernece Data 23, 339-364 (1994).
 * 
 * The L shell yields tabulated are averaged yields för L1, L2 and L3.
 */
public class FlourYield {
    
      private static final Map<Integer, Double> K_YIELDS;

    // K edge flourescence yields
    static {
        K_YIELDS = new HashMap<>();
        K_YIELDS.put(24, 0.286d);
        K_YIELDS.put(45, 0.792d);
        K_YIELDS.put(74, 0.982d); // interpolated value
    }
    
    private static final Map<Integer, Double> L_YIELDS;

    // L edge average flourescence yields
    static {
        L_YIELDS = new HashMap<>();
        L_YIELDS.put(45, 0.0499d);
        L_YIELDS.put(74, 0.290d);
    }
    
    private static final Map<Integer, Double> M_YIELDS;

    // M edge average flourescence yields
    static {
        M_YIELDS = new HashMap<>();
        M_YIELDS.put(74, 0.0205d); //interpolated value
    }
    
    private static final Map<AbsorptionEdges.AbsEdge, Map<Integer, Double>> FLOURESCENCE_YIELDS;

    static {
        FLOURESCENCE_YIELDS = new HashMap<>();
        FLOURESCENCE_YIELDS.put(AbsorptionEdges.AbsEdge.K_EDGE, K_YIELDS);
        FLOURESCENCE_YIELDS.put(AbsorptionEdges.AbsEdge.L1_EDGE, L_YIELDS);
        FLOURESCENCE_YIELDS.put(AbsorptionEdges.AbsEdge.L2_EDGE, L_YIELDS);
        FLOURESCENCE_YIELDS.put(AbsorptionEdges.AbsEdge.L3_EDGE, L_YIELDS);
        FLOURESCENCE_YIELDS.put(AbsorptionEdges.AbsEdge.M5_EDGE, M_YIELDS);
    }

    public static Optional<Double> getYield(int atomZ, AbsorptionEdges.AbsEdge edge) {
        Optional retval = Optional.empty();
        if (Optional.ofNullable(FLOURESCENCE_YIELDS.get(edge)).isPresent()) {
            retval = Optional.ofNullable(Optional.of(FLOURESCENCE_YIELDS.get(edge)).get().get(atomZ));
        }
        return retval;
    }
}
