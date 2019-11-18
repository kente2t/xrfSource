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
 * Preliminary values are estimated from
 * M.G. Pia, P Saracco, M. Sudhakar, Validation of flourescence transition probability calculations,
 * arXiv:0912.1717v1 [physics.comp-ph] 9 Dec 2009.

 */
public class TransProbabilities {
    
    private static final Map<Integer, Double> K_ALPHA_12_PROB;

    // Mean of probability of K-L2 and K-L3
    static {
        K_ALPHA_12_PROB = new HashMap<>();
        K_ALPHA_12_PROB.put(24, (0.295d + 0.585d) / 2.0d);
        K_ALPHA_12_PROB.put(45, (0.285d + 0.54d) / 2.0d);
        K_ALPHA_12_PROB.put(74, (0.29d + 0.5d) / 2.0d);
    }
    ;
    
    private static final Map<Integer, Double> K_BETA_1_PROB;

    // Probability of K-M3
    static {
        K_BETA_1_PROB = new HashMap<>();
        K_BETA_1_PROB.put(24, 0.07d); // Uncertain
        K_BETA_1_PROB.put(45, 0.08d); // uncertain
        K_BETA_1_PROB.put(74, 0.0105d);
    }
    ;
    
    private static final Map<Integer, Double> L_ALPHA_12_PROB;

    // Mean of probability of L3-M4 and L3-M5
    static {
        L_ALPHA_12_PROB = new HashMap<>();
        L_ALPHA_12_PROB.put(45, (0.09d + 0.75d) / 2.0d);
        L_ALPHA_12_PROB.put(74, (0.08d + 0.7d) / 2.0d);
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
        L_ALPHA_1_PROB.put(45, 0.75d);
        L_ALPHA_1_PROB.put(74, 0.7d);
    }

    private static final Map<Integer, Double> L_ALPHA_2_PROB;

    // Probability of L3-M4
    static {
        L_ALPHA_2_PROB = new HashMap<>();
        L_ALPHA_2_PROB.put(45, 0.09d);
        L_ALPHA_2_PROB.put(74, 0.08d);
    }

    private static final Map<Integer, Double> L_BETA_2_PROB;

    // Probability of L3-N5
    static {
        L_BETA_2_PROB = new HashMap<>();
        L_BETA_2_PROB.put(45, 0.05d);
        L_BETA_2_PROB.put(74, 0.14d);
    }

    private static final Map<Integer, Double> L_IOTA_PROB;

    // Probability of L3-M1
    static {
        L_IOTA_PROB = new HashMap<>();
        L_IOTA_PROB.put(45, 0.03d);
        L_IOTA_PROB.put(74, 0.03d);
    }

    private static final Map<Integer, Double> L_BETA_1_PROB;

    // Probability of L2-M4
    static {
        L_BETA_1_PROB = new HashMap<>();
        L_BETA_1_PROB.put(45, 0.87d);
        L_BETA_1_PROB.put(74, 0.83d);
    }

    private static final Map<Integer, Double> L_BETA_3_PROB;

    // Probability of L1-M3
    static {
        L_BETA_3_PROB = new HashMap<>();
        L_BETA_3_PROB.put(45, 0.55d);
        L_BETA_3_PROB.put(74, 0.45d);
    }

    private static final Map<Integer, Double> L_BETA_4_PROB;

    // Probability of L1-M2
    static {
        L_BETA_4_PROB = new HashMap<>();
        L_BETA_4_PROB.put(45, 0.31d);
        L_BETA_4_PROB.put(74, 0.3d);
    }

    private static final Map<Integer, Double> L_ETA_PROB;

    // Probability of L2-M1
    static {
        L_ETA_PROB = new HashMap<>();
        L_ETA_PROB.put(45, 0.03d);
        L_ETA_PROB.put(74, 0.025d);
    }

    private static final Map<Integer, Double> L_GAMMA_1_PROB;

    // Probability of L2-N4
    static {
        L_GAMMA_1_PROB = new HashMap<>();
        L_GAMMA_1_PROB.put(45, 0.05d);
        L_GAMMA_1_PROB.put(74, 0.15d);
    }

    private static final Map<Integer, Double> L_GAMMA_3_PROB;

    // Probability of L1-N3
    static {
        L_GAMMA_3_PROB = new HashMap<>();
        L_GAMMA_3_PROB.put(45, 0.083d);
        L_GAMMA_3_PROB.put(74, 0.11d);
    }

    private static final Map<Integer, Double> L_GAMMA_2_PROB;

    // Probabiulity of L1-N2
    static {
        L_GAMMA_2_PROB = new HashMap<>();
        L_GAMMA_2_PROB.put(45, 0.08d); // uncertain
        L_GAMMA_2_PROB.put(74, 0.09d);
    }

    private static final Map<Integer, Double> L_BETA_5_PROB;

    // Pobability of L3-O4 and L3-O5
    static {
        L_BETA_5_PROB = new HashMap<>();
        L_BETA_5_PROB.put(74, 0.005d);
    }

    private static final Map<Integer, Double> L_BETA_6_PROB;

    // Probability of L3-N1
    static {
        L_BETA_6_PROB = new HashMap<>();
        L_BETA_6_PROB.put(45, 0.006d); //uncertain
        L_BETA_6_PROB.put(74, 0.008d);
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
}
