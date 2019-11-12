/*
 * LineEnergies.java
 */
package se.e2t.xraycalc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Kent
 * 
 * Values in this class are according to the NIST database
 * X-ray Transition Energies Database
 */
public class LineEnergies {

    private static final Map<Integer, Double> K_ALPHA_12_LINE_ENERGY;

    // Mean of K-L2 and K-L3
    static {
        K_ALPHA_12_LINE_ENERGY = new HashMap<>();
        K_ALPHA_12_LINE_ENERGY.put(24, (5.406d + 5.415d) / 2.0d);
        K_ALPHA_12_LINE_ENERGY.put(45, (20.074d + 20.216d) / 2.0d);
        K_ALPHA_12_LINE_ENERGY.put(74, (57.982d + 59.319d) / 2.0d);
    }
    ;
    
    private static final Map<Integer, Double> K_BETA_1_LINE_ENERGY;

    // K-M3
    static {
        K_BETA_1_LINE_ENERGY = new HashMap<>();
        K_BETA_1_LINE_ENERGY.put(24, 5.947d);
        K_BETA_1_LINE_ENERGY.put(45, 22.274d);
        K_BETA_1_LINE_ENERGY.put(74, 67.245d);
    }
    ;
    
    private static final Map<Integer, Double> L_ALPHA_12_LINE_ENERGY;

    // Mean of L3-M4 and L3-M5
    static {
        L_ALPHA_12_LINE_ENERGY = new HashMap<>();
        L_ALPHA_12_LINE_ENERGY.put(45, (2.693d + 2.697d) / 2.0d);
        L_ALPHA_12_LINE_ENERGY.put(74, (8.335d + 8.398d) / 2.0d);
    }
    ;
    
    private static final Map<Integer, Double> M_ALPHA_12_LINE_ENERGY;

    // Mean of M5-N6 and M5-N7 
    static {
        M_ALPHA_12_LINE_ENERGY = new HashMap<>();
        M_ALPHA_12_LINE_ENERGY.put(74, 1.774d); // This is not A NIST value
    }
    ; 
    
    private static final Map<Integer, Double> L_ALPHA_1_LINE_ENERGY;

    // L3-M5
    static {
        L_ALPHA_1_LINE_ENERGY = new HashMap<>();
        L_ALPHA_1_LINE_ENERGY.put(45, 2.697d);
        L_ALPHA_1_LINE_ENERGY.put(74, 8.398d);
    }

    private static final Map<Integer, Double> L_ALPHA_2_LINE_ENERGY;

    // L3-M4
    static {
        L_ALPHA_2_LINE_ENERGY = new HashMap<>();
        L_ALPHA_2_LINE_ENERGY.put(45, 2.692d);
        L_ALPHA_2_LINE_ENERGY.put(74, 8.335d);
    }

    private static final Map<Integer, Double> L_BETA_2_LINE_ENERGY;

    // L3-N5
    static {
        L_BETA_2_LINE_ENERGY = new HashMap<>();
        L_BETA_2_LINE_ENERGY.put(45, 3.001d);
        L_BETA_2_LINE_ENERGY.put(74, 9.964d);
    }

    private static final Map<Integer, Double> L_IOTA_LINE_ENERGY;

    // L3-M1
    static {
        L_IOTA_LINE_ENERGY = new HashMap<>();
        L_IOTA_LINE_ENERGY.put(45, 2.377d);
        L_IOTA_LINE_ENERGY.put(74, 7.388d);
    }

    private static final Map<Integer, Double> L_BETA_1_LINE_ENERGY;

    // L2-M4
    static {
        L_BETA_1_LINE_ENERGY = new HashMap<>();
        L_BETA_1_LINE_ENERGY.put(45, 2.834d);
        L_BETA_1_LINE_ENERGY.put(74, 9.673d);
    }

    private static final Map<Integer, Double> L_BETA_3_LINE_ENERGY;

    // L1-M3
    static {
        L_BETA_3_LINE_ENERGY = new HashMap<>();
        L_BETA_3_LINE_ENERGY.put(45, 2.916d);
        L_BETA_3_LINE_ENERGY.put(74, 9.819d);
    }

    private static final Map<Integer, Double> L_BETA_4_LINE_ENERGY;

    // L1-M2
    static {
        L_BETA_4_LINE_ENERGY = new HashMap<>();
        L_BETA_4_LINE_ENERGY.put(45, 2.891d);
        L_BETA_4_LINE_ENERGY.put(74, 9.525d);
    }

    private static final Map<Integer, Double> L_ETA_LINE_ENERGY;

    // L2-M1
    static {
        L_ETA_LINE_ENERGY = new HashMap<>();
        L_ETA_LINE_ENERGY.put(45, 2.519d);
        L_ETA_LINE_ENERGY.put(74, 8.724d);
    }

    private static final Map<Integer, Double> L_GAMMA_1_LINE_ENERGY;

    // L2-N4
    static {
        L_GAMMA_1_LINE_ENERGY = new HashMap<>();
        L_GAMMA_1_LINE_ENERGY.put(45, 3.144d);
        L_GAMMA_1_LINE_ENERGY.put(74, 11.286d);
    }

    private static final Map<Integer, Double> L_GAMMA_3_LINE_ENERGY;

    // L1-N3
    static {
        L_GAMMA_3_LINE_ENERGY = new HashMap<>();
        L_GAMMA_3_LINE_ENERGY.put(45, 3.364d);
        L_GAMMA_3_LINE_ENERGY.put(74, 11.680d);
    }

    private static final Map<Integer, Double> L_GAMMA_2_LINE_ENERGY;

    // L1-N2
    static {
        L_GAMMA_2_LINE_ENERGY = new HashMap<>();
        L_GAMMA_2_LINE_ENERGY.put(45, 3.364d);
        L_GAMMA_2_LINE_ENERGY.put(74, 11.611d);
    }

    private static final Map<Integer, Double> L_BETA_5_LINE_ENERGY;

    // L3-O4, L3-O5
    static {
        L_BETA_5_LINE_ENERGY = new HashMap<>();
//        L_BETA_5_LINE_ENERGY.put(74, 0.0d); //Energy not found yet
    }

    private static final Map<Integer, Double> L_BETA_6_LINE_ENERGY;

    // /L3-N1
    static {
        L_BETA_6_LINE_ENERGY = new HashMap<>();
        L_BETA_6_LINE_ENERGY.put(45, 2.923d);
        L_BETA_6_LINE_ENERGY.put(74, 9.608d);
    }

    private static final Map<TubeLines.XrfLine, Map<Integer, Double>> TUBE_LINE_WIDTHS;

    static {
        TUBE_LINE_WIDTHS = new HashMap<>();
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_ALPHA_1, L_ALPHA_1_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_ALPHA_2, L_ALPHA_2_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_BETA_2, L_BETA_2_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_IOTA, L_IOTA_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_BETA_1, L_BETA_1_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_BETA_3, L_BETA_3_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_BETA_4, L_BETA_4_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_ETA, L_ETA_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_GAMMA_1, L_GAMMA_1_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_GAMMA_3, L_GAMMA_3_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_GAMMA_2, L_GAMMA_2_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_BETA_5, L_BETA_5_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_BETA_6, L_BETA_6_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.K_ALPHA_12, K_ALPHA_12_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.K_BETA_1, K_BETA_1_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_ALPHA_12, L_ALPHA_12_LINE_ENERGY);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.M_ALPHA_12, M_ALPHA_12_LINE_ENERGY);
    }

    public static Optional<Double> getTubeLineEnergy(int atomZ, TubeLines.XrfLine line) {
        return Optional.ofNullable(TUBE_LINE_WIDTHS.get(line).get(atomZ));
    }
}
