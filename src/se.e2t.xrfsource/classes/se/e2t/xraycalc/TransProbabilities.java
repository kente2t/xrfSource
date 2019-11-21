/*
 * TransProbabilities.java
 */
package se.e2t.xraycalc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 *
 * @author Kent
 * 
 * Data of this static class are according to
 * 1. J.H. Scofield, Radiative Decay Rates of Vacancies in the K and L Shells,
 * Phys. Rev. vol. 179, no. 1, pp- 9-16, 1969.

 */
public class TransProbabilities {
    
    private static final Map<Integer, Double> K_ALPHA_12_PROB;

    // Mean of probability of K-L2 and K-L3
    static {
        K_ALPHA_12_PROB = new HashMap<>();
        K_ALPHA_12_PROB.put(24, (0.0563d + 0.1107d + (2.0d / 3.0d) *
                (0.1003d - 0.0563d + 0.1967d - 0.1107d)) /
                (0.1860d + (2.0d / 3.0d) * (0.332d - 0.1860d)));
        K_ALPHA_12_PROB.put(45, (0.970d + 1.848d + (3.0d / 5.0d) *
                (1.571d - 0.970d + 2.961d - 1.848d)) /
                (3.33d + (3.0d / 5.0d) * (5.42d - 3.33d)));
        K_ALPHA_12_PROB.put(74, (10.88d + 18.88d) / 37.4d);
    }
    ;
    
    private static final Map<Integer, Double> K_BETA_1_PROB;

    // Probability of K-M3
    static {
        K_BETA_1_PROB = new HashMap<>();
        K_BETA_1_PROB.put(24, (0.0126d + (2.0d / 3.0d) *
                (0.0235d - 0.0126d)) /
                (0.1860d + (2.0d / 3.0d) * (0.332d - 0.1860d)));
        K_BETA_1_PROB.put(45, (0.2930d + (3.0d / 5.0d) *
                (0.5017d - 0.2930d)) /
                (3.33d + (3.0d / 5.0d) * (5.42d - 3.33d)));
        K_BETA_1_PROB.put(74, 3.92d / 37.4d);
    }
    ;
    
    private static final Map<Integer, Double> L_ALPHA_12_PROB;

    // Mean of probability of L3-M4 and L3-M5
    static {
        L_ALPHA_12_PROB = new HashMap<>();
        L_ALPHA_12_PROB.put(45, (0.0058d + 0.0513d + (3.0d / 5.0d) *
                (0.0107d - 0.0058d + 0.0946d - 0.0513d)) /
                (0.0616d + (3.0d / 5.0d) * (0.1196d - 0.0616d)));
        L_ALPHA_12_PROB.put(74, (0.0102d + 0.898d) / 1.244d);
    }
    ;
    
    private static final Map<Integer, Double> M_ALPHA_12_PROB;

    // Mean of probability M5-N6 and M5-N7 
    static {
        M_ALPHA_12_PROB = new HashMap<>();
//        M_ALPHA_12_PROB.put(74, (0.0d + 0.0d) / 2.0d); // Unknown
    }
    ; 
    
    private static final Map<Integer, Double> L_ALPHA_1_PROB;

    // Probability of L3-M5
    static {
        L_ALPHA_1_PROB = new HashMap<>();
        L_ALPHA_1_PROB.put(45, (0.0513d + (3.0d / 5.0d) *
                (0.0946d - 0.0513d)) /
                (0.0616d + (3.0d / 5.0d) * (0.1196d - 0.0616d)));
        L_ALPHA_1_PROB.put(74, 0.898d / 1.244d);
    }

    private static final Map<Integer, Double> L_ALPHA_2_PROB;

    // Probability of L3-M4
    static {
        L_ALPHA_2_PROB = new HashMap<>();
        L_ALPHA_2_PROB.put(45, (0.0058d + (3.0d / 5.0d) *
                (0.0107d - 0.0058d)) /
                (0.0616d + (3.0d / 5.0d) * (0.1196d - 0.0616d)));
        L_ALPHA_2_PROB.put(74, 0.102d / 1.244d);
    }

    private static final Map<Integer, Double> L_BETA_2_PROB;

    // Probability of L3-N4 + L3-N5
    static {
        L_BETA_2_PROB = new HashMap<>();
        L_BETA_2_PROB.put(45, (0.00020d + 0.00173d + (3.0d / 5.0d) *
                (0.00098d - 0.00020d + 0.0086d - 0.00173d)) /
                (0.0616d + (3.0d / 5.0d) * (0.1196d - 0.0616d)));
        L_BETA_2_PROB.put(74, (0.0178d + 0.159d) / 1.244d);
    }

    private static final Map<Integer, Double> L_IOTA_PROB;

    // Probability of L3-M1
    static {
        L_IOTA_PROB = new HashMap<>();
        L_IOTA_PROB.put(45, (0.00217d + (3.0d / 5.0d) *
                (0.0039d - 0.00217d)) /
                (0.0616d + (3.0d / 5.0d) * (0.1196d - 0.0616d)));
        L_IOTA_PROB.put(74, 0.047d / 1.244d);
    }

    private static final Map<Integer, Double> L_BETA_1_PROB;

    // Probability of L2-M4
    static {
        L_BETA_1_PROB = new HashMap<>();
        L_BETA_1_PROB.put(45, (0.0595d + (3.0d / 5.0d) *
                (0.1109d - 0.0595d)) /
                (0.0638d + (3.0d / 5.0d) * (0.1253d - 0.0638d)));
        L_BETA_1_PROB.put(74, 1.138d / 1.397d);
    }

    private static final Map<Integer, Double> L_BETA_3_PROB;

    // Probability of L1-M3
    static {
        L_BETA_3_PROB = new HashMap<>();
        L_BETA_3_PROB.put(45, (0.0222d + (3.0d / 5.0d) *
                (0.0396d - 0.0222d)) /
                (0.0412d + (3.0d / 5.0d) * (0.0761d - 0.0412d)));
        L_BETA_3_PROB.put(74, 0.330d / 0.804d);
    }

    private static final Map<Integer, Double> L_BETA_4_PROB;

    // Probability of L1-M2
    static {
        L_BETA_4_PROB = new HashMap<>();
        L_BETA_4_PROB.put(45, (0.0129d + (3.0d / 5.0d) *
                (0.0238d - 0.0129d)) /
                (0.0412d + (3.0d / 5.0d) * (0.0761d - 0.0412d)));
        L_BETA_4_PROB.put(74, 0.264d / 0.804d);
    }

    private static final Map<Integer, Double> L_ETA_PROB;

    // Probability of L2-M1
    static {
        L_ETA_PROB = new HashMap<>();
        L_ETA_PROB.put(45, (0.00188d + (3.0d / 5.0d) *
                (0.0033d - 0.00188d)) /
                (0.0638d + (3.0d / 5.0d) * (0.1253d - 0.0638d)));
        L_ETA_PROB.put(74, 0.031d / 1.397d);
    }

    private static final Map<Integer, Double> L_GAMMA_1_PROB;

    // Probability of L2-N4
    static {
        L_GAMMA_1_PROB = new HashMap<>();
        L_GAMMA_1_PROB.put(45, (0.0020d + (3.0d / 5.0d) *
                (0.0104d - 0.0020d)) /
                (0.0638d + (3.0d / 5.0d) * (0.1253d - 0.0638d)));
        L_GAMMA_1_PROB.put(74, 0.212d / 1.397d);
    }

    private static final Map<Integer, Double> L_GAMMA_3_PROB;

    // Probability of L1-N3
    static {
        L_GAMMA_3_PROB = new HashMap<>();
        L_GAMMA_3_PROB.put(45, (0.00349d + (3.0d / 5.0d) *
                (0.0071d - 0.00349d)) /
                (0.0412d + (3.0d / 5.0d) * (0.0761d - 0.0412d)));
        L_GAMMA_3_PROB.put(74, 0.086d / 0.804d);
    }

    private static final Map<Integer, Double> L_GAMMA_2_PROB;

    // Probabiulity of L1-N2
    static {
        L_GAMMA_2_PROB = new HashMap<>();
        L_GAMMA_2_PROB.put(45, (0.00201d + (3.0d / 5.0d) *
                (0.0043d - 0.00201d)) /
                (0.0412d + (3.0d / 5.0d) * (0.0761d - 0.0412d)));
        L_GAMMA_2_PROB.put(74, 0.065d / 0.804d);
    }

    private static final Map<Integer, Double> L_BETA_5_PROB;

    // Pobability of L3-O4 and L3-O5
    static {
        L_BETA_5_PROB = new HashMap<>();
        L_BETA_5_PROB.put(74, 0.0047d / 1.244d);
    }

    private static final Map<Integer, Double> L_BETA_6_PROB;

    // Probability of L3-N1
    static {
        L_BETA_6_PROB = new HashMap<>();
        L_BETA_6_PROB.put(45, (0.00037d + (3.0d / 5.0d) *
                (0.00075d - 0.00037d)) /
                (0.0616d + (3.0d / 5.0d) * (0.1196d - 0.0616d)));
        L_BETA_6_PROB.put(74, 0.0112d / 1.244d);
    }

    private static final Map<TubeLines.XrfLine, Map<Integer, Double>> TRANS_PROB_DATA;

    static {
        TRANS_PROB_DATA = new HashMap<>();
        TRANS_PROB_DATA.put(TubeLines.XrfLine.L_ALPHA_1, L_ALPHA_1_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.L_ALPHA_2, L_ALPHA_2_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.L_BETA_2, L_BETA_2_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.L_IOTA, L_IOTA_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.L_BETA_1, L_BETA_1_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.L_BETA_3, L_BETA_3_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.L_BETA_4, L_BETA_4_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.L_ETA, L_ETA_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.L_GAMMA_1, L_GAMMA_1_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.L_GAMMA_3, L_GAMMA_3_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.L_GAMMA_2, L_GAMMA_2_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.L_BETA_5, L_BETA_5_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.L_BETA_6, L_BETA_6_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.K_ALPHA_12, K_ALPHA_12_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.K_BETA_1, K_BETA_1_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.L_ALPHA_12, L_ALPHA_12_PROB);
        TRANS_PROB_DATA.put(TubeLines.XrfLine.M_ALPHA_12, M_ALPHA_12_PROB);
    }

    public static Optional<Double> getTransProb(int atomZ, TubeLines.XrfLine line) {
        return Optional.ofNullable(TRANS_PROB_DATA.get(line).get(atomZ));
    }
    
    static {
         TRANS_PROB_DATA.keySet().stream()
                .forEach(xrfLine -> {
                    System.out.println("\n" + xrfLine.toString());
                    TRANS_PROB_DATA.get(xrfLine).keySet().stream()
                            .forEach(atomZ -> {
                                System.out.println(atomZ + " "
                                        + String.format("%.3f", TRANS_PROB_DATA.get(xrfLine).get(atomZ)));
                            });
                });
    }
}
