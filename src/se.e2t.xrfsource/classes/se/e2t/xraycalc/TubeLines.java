/*
 * TubeLines.java
 */
package se.e2t.xraycalc;

import java.util.HashMap;
import java.util.Map;
import static se.e2t.xraycalc.Inparameters.getAnodeElements;
import se.e2t.xraycalc.LineEnergies;

/**
 *
 * @author Kent Ericsson, e2t AB
 * 
 * Class stores data required for calculation of x-ray tube intensity lines.
 */
public class TubeLines {
    
//    public static enum Lline {
//        L_ALPHA_1, L_ALPHA_2, L_BETA_2, L_IOTA, L_BETA_1, L_BETA_3, L_BETA_4,
//        L_ETA, L_GAMMA_1, L_GAMMA_3, L_GAMMA_2, L_BETA_5, L_BETA_6
//    };

    public static enum XrfLine {
        K_ALPHA_12, K_BETA_1, L_ALPHA_12, L_BETA_1, M_ALPHA_12,
         L_ALPHA_1, L_ALPHA_2, L_BETA_2, L_IOTA, L_BETA_3, L_BETA_4,
        L_ETA, L_GAMMA_1, L_GAMMA_3, L_GAMMA_2, L_BETA_5, L_BETA_6
    };

    private static final Map<Integer, LineInfo> K_ALPHA_12_LINE = new HashMap<>();

//    static {
//        K_ALPHA_12_LINE = new HashMap<>();
//        K_ALPHA_12_LINE.put(24, new LineInfo(5.41d, 1.5d, 5.98902d));
//        K_ALPHA_12_LINE.put(45, new LineInfo(20.21d, 7.8d, 23.22199d));
//        K_ALPHA_12_LINE.put(74, new LineInfo(59.31d, 44.9d, 69.50850d));
//    }
//    ;
    
    private static final Map<Integer, LineInfo> K_BETA_1_LINE = new HashMap<>();

//    static {
//        K_BETA_1_LINE = new HashMap<>();
//        K_BETA_1_LINE.put(24, new LineInfo(5.9468d, 2.220d, 5.98902d));
//        K_BETA_1_LINE.put(45, new LineInfo(22.7236d, 8.020d, 23.22199d));
//        K_BETA_1_LINE.put(74, new LineInfo(67.2450d, 46.500d, 69.50850d));
//    }
//    ;
    
    private static final Map<Integer, LineInfo> L_ALPHA_12_LINE = new HashMap<>();

//    static {
//        L_ALPHA_12_LINE = new HashMap<>();
//        L_ALPHA_12_LINE.put(45, new LineInfo(2.695d, 3.6d, 3.0020700d));
//        L_ALPHA_12_LINE.put(74, new LineInfo(8.36d, 8.5d, 10.2d));
//    }
//    ;
    
    private static final Map<Integer, LineInfo> M_ALPHA_12_LINE = new HashMap<>();

//    static {
//        M_ALPHA_12_LINE = new HashMap<>();
//        M_ALPHA_12_LINE.put(74, new LineInfo(1.774d, 7.4d, 1.816d));
//    }
//    ; 
    
    private static final Map<Integer, LineInfo> L_ALPHA_1_LINE = new HashMap<>();
    
//    static {
//        L_ALPHA_1_LINE = new HashMap<>();
//        L_ALPHA_1_LINE.put(45, new LineInfo(2.697d, 3.433d, 0.0d));
//        L_ALPHA_1_LINE.put(74, new LineInfo(8.398, 8.463d, 0.0d));
//    }
    
    private static final Map<Integer, LineInfo> L_ALPHA_2_LINE = new HashMap<>();
    
//    static {
//        L_ALPHA_2_LINE = new HashMap<>();
//        L_ALPHA_2_LINE.put(45, new LineInfo(2.692d, 3.833d, 0.0d));
//        L_ALPHA_2_LINE.put(74, new LineInfo(8.335d, 8.463d, 0.0d));
//    }  
    
    private static final Map<Integer, LineInfo> L_BETA_2_LINE = new HashMap<>();
    
//    static {
//        L_BETA_2_LINE = new HashMap<>();
//        L_BETA_2_LINE.put(45, new LineInfo(3.004d, 3.223d, 0.0d));
//        L_BETA_2_LINE.put(74, new LineInfo(9.963d, 10.563d, 0.0d));
//    }  
    
    private static final Map<Integer, LineInfo> L_IOTA_LINE = new HashMap<>();
    
//    static {
//        L_IOTA_LINE = new HashMap<>();
//        L_IOTA_LINE.put(45, new LineInfo(2.370d, 10.823d, 0.0d));
//        L_IOTA_LINE.put(74, new LineInfo(7.388d, 21.263d, 0.0d));
//    }  
    
    private static final Map<Integer, LineInfo> L_BETA_1_LINE = new HashMap<>();
    
//    static {
//        L_BETA_1_LINE = new HashMap<>();
//        L_BETA_1_LINE.put(45, new LineInfo(2.834d, 2.933d, 0.0d));
//        L_BETA_1_LINE.put(74, new LineInfo(9.672d, 7.578d, 0.0d));
//    } 
    
    private static final Map<Integer, LineInfo> L_BETA_3_LINE = new HashMap<>();
    
//    static {
//        L_BETA_3_LINE = new HashMap<>();
//        L_BETA_3_LINE.put(45, new LineInfo(2.915d, 6.250d, 0.0d));
//        L_BETA_3_LINE.put(74, new LineInfo(9.820d, 12.700d, 0.0d));
//    } 
    
    private static final Map<Integer, LineInfo> L_BETA_4_LINE = new HashMap<>();
    
//    static {
//        L_BETA_4_LINE = new HashMap<>();
//        L_BETA_4_LINE.put(45, new LineInfo(2.891d, 6.250d, 0.0d));
//        L_BETA_4_LINE.put(74, new LineInfo(9.526d, 14.400d, 0.0d));
//    } 
    
    private static final Map<Integer, LineInfo> L_ETA_LINE = new HashMap<>();
    
//    static {
//        L_ETA_LINE = new HashMap<>();
//        L_ETA_LINE.put(45, new LineInfo(2.513d, 9.923d, 0.0d));
//        L_ETA_LINE.put(74, new LineInfo(8.725d, 20.378d, 0.0d));
//    } 
    
    private static final Map<Integer, LineInfo> L_GAMMA_1_LINE = new HashMap<>();
    
//    static {
//        L_GAMMA_1_LINE = new HashMap<>();
//        L_GAMMA_1_LINE.put(45, new LineInfo(3.145d, 3.323d, 0.0d));
//        L_GAMMA_1_LINE.put(74, new LineInfo(11.285d, 9.978d, 0.0d));
//    } 
    
    private static final Map<Integer, LineInfo> L_GAMMA_3_LINE = new HashMap<>();
    
//    static {
//        L_GAMMA_3_LINE = new HashMap<>();
//        L_GAMMA_3_LINE.put(45, new LineInfo(3.366d, 7.800d, 0.0d));
//        L_GAMMA_3_LINE.put(74, new LineInfo(11.676d, 10.500d, 0.0d));
//    } 
    
    private static final Map<Integer, LineInfo> L_GAMMA_2_LINE = new HashMap<>();
    
//    static {
//        L_GAMMA_2_LINE = new HashMap<>();
//    }  
    
    private static final Map<Integer, LineInfo> L_BETA_5_LINE = new HashMap<>();
    
//    static {
//        L_BETA_5_LINE = new HashMap<>();
//    }  
    
   private static final Map<Integer, LineInfo> L_BETA_6_LINE = new HashMap<>();
    
//    static {
//        L_BETA_6_LINE = new HashMap<>();
//    }    
    
    private static final Map<XrfLine, Map<Integer, LineInfo>> TUBE_L_LINE_INFO;

    static {
        TUBE_L_LINE_INFO = new HashMap<>();
        TUBE_L_LINE_INFO.put(XrfLine.L_ALPHA_1, L_ALPHA_1_LINE);
        TUBE_L_LINE_INFO.put(XrfLine.L_ALPHA_2, L_ALPHA_2_LINE);
        TUBE_L_LINE_INFO.put(XrfLine.L_BETA_2, L_BETA_2_LINE);
        TUBE_L_LINE_INFO.put(XrfLine.L_IOTA, L_IOTA_LINE);
        TUBE_L_LINE_INFO.put(XrfLine.L_BETA_1, L_BETA_1_LINE);
        TUBE_L_LINE_INFO.put(XrfLine.L_BETA_3, L_BETA_3_LINE);
        TUBE_L_LINE_INFO.put(XrfLine.L_BETA_4, L_BETA_4_LINE);
        TUBE_L_LINE_INFO.put(XrfLine.L_ETA, L_ETA_LINE);
        TUBE_L_LINE_INFO.put(XrfLine.L_GAMMA_1, L_GAMMA_1_LINE);
        TUBE_L_LINE_INFO.put(XrfLine.L_GAMMA_3, L_GAMMA_3_LINE);
        TUBE_L_LINE_INFO.put(XrfLine.L_GAMMA_2, L_GAMMA_2_LINE);
        TUBE_L_LINE_INFO.put(XrfLine.L_BETA_5, L_BETA_5_LINE);
        TUBE_L_LINE_INFO.put(XrfLine.L_BETA_6, L_BETA_6_LINE);
    }
    
    private static final Map<XrfLine, Map<Integer, LineInfo>> TUBE_LINE_INFO;

    static {
        TUBE_LINE_INFO = new HashMap<>();
        TUBE_LINE_INFO.put(XrfLine.K_ALPHA_12, K_ALPHA_12_LINE);
        TUBE_LINE_INFO.put(XrfLine.K_BETA_1, K_BETA_1_LINE);
        TUBE_LINE_INFO.put(XrfLine.L_ALPHA_12, L_ALPHA_12_LINE);
        TUBE_LINE_INFO.put(XrfLine.L_BETA_1, L_BETA_1_LINE);
        TUBE_LINE_INFO.put(XrfLine.M_ALPHA_12, M_ALPHA_12_LINE);
    }
    
    public TubeLines() {

        //Initialize major line data
        
        TUBE_LINE_INFO.keySet().stream()
                .forEach(xrfLine -> {
                    getAnodeElements().stream()
                            .map(tubeElement -> tubeElement.getAtomicNumber())
                            .filter(atomZ
                                    -> LineEnergies.getTubeLineEnergy(atomZ, xrfLine).isPresent()
                            && LineWidths.getTubeLineWidth(atomZ, xrfLine).isPresent()
                            && AbsorptionEdges.getEdge(atomZ, xrfLine).isPresent())
                            .forEach(atomZ
                                    -> TUBE_LINE_INFO.get(xrfLine).put(atomZ,
                                    new LineInfo(
                                            LineEnergies.getTubeLineEnergy(atomZ, xrfLine).get(),
                                            LineWidths.getTubeLineWidth(atomZ, xrfLine).get(),
                                            AbsorptionEdges.getEdge(atomZ, xrfLine).get()))
                            );
                });
        
        // Initialize data of L lines
        
        TUBE_L_LINE_INFO.keySet().stream()
                .forEach(xrfLine -> {
                    getAnodeElements().stream()
                            .map(tubeElement -> tubeElement.getAtomicNumber())
                            .filter(atomZ
                                    -> LineEnergies.getTubeLineEnergy(atomZ, xrfLine).isPresent()
                            && LineWidths.getTubeLineWidth(atomZ, xrfLine).isPresent()
                            && AbsorptionEdges.getEdge(atomZ, xrfLine).isPresent())
                            .forEach(atomZ
                                    -> TUBE_L_LINE_INFO.get(xrfLine).put(atomZ,
                                    new LineInfo(
                                            LineEnergies.getTubeLineEnergy(atomZ, xrfLine).get(),
                                            LineWidths.getTubeLineWidth(atomZ, xrfLine).get(),
                                            AbsorptionEdges.getEdge(atomZ, xrfLine).get()))
                            );
                });
    }

    public static class LineInfo {

        private double _energy; //Energy in keV
        private double _lineWidth; //Natural width in eV
        private double _absorptionEdge; // Absorption edge in keV

        public LineInfo(double energy, double lineWidth, double absorptionEdge) {
            _energy = energy;
            _lineWidth = lineWidth;
            _absorptionEdge = absorptionEdge;
        }

        public void setEnergy(double _energy) {
            this._energy = _energy;
        }

        public void setLineWidth(double _lineWidth) {
            this._lineWidth = _lineWidth;
        }

        public void setAbsorptionEdge(double _absorptionEdge) {
            this._absorptionEdge = _absorptionEdge;
        }

        public double getEnergy() {
            return _energy;
        }

        public double getLineWidth() {
            return _lineWidth;
        }

        public double getAbsorptionEdge() {
            return _absorptionEdge;
        }

    }

    public static Map<XrfLine, Map<Integer, LineInfo>> GetMajorLineInfo() {
        return TUBE_LINE_INFO;
    }
    
    public static LineInfo getLlineInfo(XrfLine line, int z) {
        return TUBE_L_LINE_INFO.get(line).get(z);
    }
}
