/*
 * LineWidths.java
 */
package se.e2t.xraycalc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Kent
 * 
 * Data of this static class are calculated using the natural width of the
 * atomic levels as tabulated in the paper "widths of the atomic K.N7 levels",
 * by J.H. Campbell and T. Papp. published in Atomic Data and Nuclear Data
 * Tables, Vol. 77, No. 1. January 2001.
 * The line width of a flourescent line in eV is calculated as the sum of the natural
 * width of the two atomic levels involved.
 * If a value of an element is missing in the tables then a value is calculated
 * using the atomic number in linear interpolation from adjacent values.
 */
public class LineWidths {

    private static final Map<Integer, Double> K_ALPHA_12_LINEWIDTH;

    // Width of K level width plus mean of L2 and L3 widths
    static {
        K_ALPHA_12_LINEWIDTH = new HashMap<>();
        K_ALPHA_12_LINEWIDTH.put(24, 1.02d + (0.76d + 0.32d) / 2.0d);
        K_ALPHA_12_LINEWIDTH.put(45, 1.62d + (15.0d / 17.0d) * (6.8d - 1.62d)
                + (2.13d + 1.96d) / 2.0d);
        K_ALPHA_12_LINEWIDTH.put(74, 37.9d + (49.5d - 37.9d) / 5.0d
                + (4.82d + 4.81d) / 2.0d);
    }
    ;
    
    private static final Map<Integer, Double> K_BETA_1_LINEWIDTH;

    // Width of K level width plus M3 width
    static {
        K_BETA_1_LINEWIDTH = new HashMap<>();
        K_BETA_1_LINEWIDTH.put(24, 1.02d + 1.2d);
        K_BETA_1_LINEWIDTH.put(45, 1.62d + (15.0d / 17.0d) * (6.8d - 1.62d) + 2.25d);
        K_BETA_1_LINEWIDTH.put(74, 37.9d + (49.5d - 37.9d) / 5.0d + 6.4d);
    }
    ;
    
    private static final Map<Integer, Double> L_ALPHA_12_LINEWIDTH;

    // Width of L3 level width plus mean of M4 and M5 widths
    static {
        L_ALPHA_12_LINEWIDTH = new HashMap<>();
        L_ALPHA_12_LINEWIDTH.put(45, 1.96d + (0.21d + 0.21d) / 2.0d);
        L_ALPHA_12_LINEWIDTH.put(74, 4.81d + (1.7d + 1.7d) / 2.0d);
    }
    ;
    
    private static final Map<Integer, Double> M_ALPHA_12_LINEWIDTH;

    // Width of M5 width plus mean of N6 and N7 widths 
    static {
        M_ALPHA_12_LINEWIDTH = new HashMap<>();
        M_ALPHA_12_LINEWIDTH.put(74, 1.7d + (0.1d + 0.06d) / 2.0d);
    }
    ; 
    
    private static final Map<Integer, Double> L_ALPHA_1_LINEWIDTH;

    // Width of L3 with plus M5 width
    static {
        L_ALPHA_1_LINEWIDTH = new HashMap<>();
        L_ALPHA_1_LINEWIDTH.put(45, 1.96d + 0.21d);
        L_ALPHA_1_LINEWIDTH.put(74, 4.81d + 1.7d);
    }

    private static final Map<Integer, Double> L_ALPHA_2_LINEWIDTH;

    // Width of L3 plus M4 width
    static {
        L_ALPHA_2_LINEWIDTH = new HashMap<>();
        L_ALPHA_2_LINEWIDTH.put(45, 1.96d + 0.61d);
        L_ALPHA_2_LINEWIDTH.put(74, 4.81d + 1.7d);
    }

    private static final Map<Integer, Double> L_BETA_2_LINEWIDTH;

    // Width of L3 plus N5 width
    static {
        L_BETA_2_LINEWIDTH = new HashMap<>();
        L_BETA_2_LINEWIDTH.put(45, 1.96d + 0.05d);
        L_BETA_2_LINEWIDTH.put(74, 4.81d + 3.8d);
    }

    private static final Map<Integer, Double> L_IOTA_LINEWIDTH;

    // Width of L3 plus M1 width
    static {
        L_IOTA_LINEWIDTH = new HashMap<>();
        L_IOTA_LINEWIDTH.put(45, 1.96d + 7.2d + (8.0d - 7.2d) / 2.0d);
        L_IOTA_LINEWIDTH.put(74, 4.81d + 13.8d + (5.0d / 8.0d) * (14.8d - 13.8d));
    }

    private static final Map<Integer, Double> L_BETA_1_LINEWIDTH;

    // Width of L2 plus M4 width
    static {
        L_BETA_1_LINEWIDTH = new HashMap<>();
        L_BETA_1_LINEWIDTH.put(45, 2.13d + 0.61d);
        L_BETA_1_LINEWIDTH.put(74, 4.82d + 1.7d);
    }

    private static final Map<Integer, Double> L_BETA_3_LINEWIDTH;

    // Width of L1 plus M3 width
    static {
        L_BETA_3_LINEWIDTH = new HashMap<>();
        L_BETA_3_LINEWIDTH.put(45, 4.0d + 2.25d);
        L_BETA_3_LINEWIDTH.put(74, 6.3d + 6.4d);
    }

    private static final Map<Integer, Double> L_BETA_4_LINEWIDTH;

    // Width of L1 plus M2 width
    static {
        L_BETA_4_LINEWIDTH = new HashMap<>();
        L_BETA_4_LINEWIDTH.put(45, 4.0d + 2.25d);
        L_BETA_4_LINEWIDTH.put(74, 6.3d + 8.5d);
    }

    private static final Map<Integer, Double> L_ETA_LINEWIDTH;

    // Width of L2 plus M1 width
    static {
        L_ETA_LINEWIDTH = new HashMap<>();
        L_ETA_LINEWIDTH.put(45, 2.13d + 7.2d + (8.0d - 7.2d) / 2.0d);
        L_ETA_LINEWIDTH.put(74, 4.82d + 13.8d + (5.0d / 8.0d) * (14.8 - 13.8));
    }

    private static final Map<Integer, Double> L_GAMMA_1_LINEWIDTH;

    // Width of L2 plus N4 width
    static {
        L_GAMMA_1_LINEWIDTH = new HashMap<>();
        L_GAMMA_1_LINEWIDTH.put(45, 2.13d + 0.05d);
        L_GAMMA_1_LINEWIDTH.put(74, 4.82d + 4.1d);
    }

    private static final Map<Integer, Double> L_GAMMA_3_LINEWIDTH;

    // Width of L1 plus N3 width
    static {
        L_GAMMA_3_LINEWIDTH = new HashMap<>();
        L_GAMMA_3_LINEWIDTH.put(45, 4.0d + 3.8d);
        L_GAMMA_3_LINEWIDTH.put(74, 6.3d + 4.2d);
    }

    private static final Map<Integer, Double> L_GAMMA_2_LINEWIDTH;

    // Width of L1 plus N2 width
    static {
        L_GAMMA_2_LINEWIDTH = new HashMap<>();
        L_GAMMA_2_LINEWIDTH.put(74, 6.3d + 5.8d);
    }

    private static final Map<Integer, Double> L_BETA_5_LINEWIDTH;

    // Width of L3 plus mean of O4 and O5 widths (O level widths missing)
    static {
        L_BETA_5_LINEWIDTH = new HashMap<>();
        L_BETA_5_LINEWIDTH.put(74, 4.81d);
    }

    private static final Map<Integer, Double> L_BETA_6_LINEWIDTH;

    // Width of L3 plus N1 width
    static {
        L_BETA_6_LINEWIDTH = new HashMap<>();
        L_BETA_6_LINEWIDTH.put(45, 1.96d + 4.2d);
        L_BETA_6_LINEWIDTH.put(74, 4.81d + 7.3d);
    }

    private static final Map<TubeLines.XrfLine, Map<Integer, Double>> TUBE_LINE_WIDTHS;

    static {
        TUBE_LINE_WIDTHS = new HashMap<>();
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_ALPHA_1, L_ALPHA_1_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_ALPHA_2, L_ALPHA_2_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_BETA_2, L_BETA_2_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_IOTA, L_IOTA_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_BETA_1, L_BETA_1_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_BETA_3, L_BETA_3_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_BETA_4, L_BETA_4_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_ETA, L_ETA_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_GAMMA_1, L_GAMMA_1_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_GAMMA_3, L_GAMMA_3_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_GAMMA_2, L_GAMMA_2_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_BETA_5, L_BETA_5_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_BETA_6, L_BETA_6_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.K_ALPHA_12, K_ALPHA_12_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.K_BETA_1, K_BETA_1_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.L_ALPHA_12, L_ALPHA_12_LINEWIDTH);
        TUBE_LINE_WIDTHS.put(TubeLines.XrfLine.M_ALPHA_12, M_ALPHA_12_LINEWIDTH);
    }

    public static Optional<Double> getTubeLineWidth(int atomZ, TubeLines.XrfLine line) {
        return Optional.ofNullable(TUBE_LINE_WIDTHS.get(line).get(atomZ));
    }
}
