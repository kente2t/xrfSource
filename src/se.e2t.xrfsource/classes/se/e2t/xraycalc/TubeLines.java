/*
 * TubeLines.java
 *
 * Copyright 2019 e2t AB
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the Software
 * is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package se.e2t.xraycalc;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import static se.e2t.xraycalc.Inparameters.getAnodeElements;
import se.e2t.xraycalc.TubeLines.LineInfo;
import static se.e2t.xraycalc.TubeLines.XrfLine.K_ALPHA_12;
import static se.e2t.xraycalc.TubeLines.XrfLine.K_BETA_1;
import static se.e2t.xraycalc.TubeLines.XrfLine.L_ALPHA_1;
import static se.e2t.xraycalc.TubeLines.XrfLine.L_ALPHA_2;
import static se.e2t.xraycalc.TubeLines.XrfLine.L_BETA_1;
import static se.e2t.xraycalc.TubeLines.XrfLine.L_BETA_2;
import static se.e2t.xraycalc.TubeLines.XrfLine.L_BETA_3;
import static se.e2t.xraycalc.TubeLines.XrfLine.L_BETA_4;
import static se.e2t.xraycalc.TubeLines.XrfLine.L_BETA_5;
import static se.e2t.xraycalc.TubeLines.XrfLine.L_BETA_6;
import static se.e2t.xraycalc.TubeLines.XrfLine.L_ETA;
import static se.e2t.xraycalc.TubeLines.XrfLine.L_GAMMA_1;
import static se.e2t.xraycalc.TubeLines.XrfLine.L_GAMMA_2;
import static se.e2t.xraycalc.TubeLines.XrfLine.L_GAMMA_3;
import static se.e2t.xraycalc.TubeLines.XrfLine.L_IOTA;

/**
 *
 * @author Kent Ericsson, e2t AB
 * 
 * Class stores data required for calculation of x-ray tube intensity lines.
 */
public class TubeLines {
    
    /**
     * Enum constants to reference the different characteristic lines handled
     * by the program..
     */
    public static enum XrfLine {
        K_ALPHA_12, K_BETA_1, L_ALPHA_12, L_BETA_1, M_ALPHA_12,
         L_ALPHA_1, L_ALPHA_2, L_BETA_2, L_IOTA, L_BETA_3, L_BETA_4,
        L_ETA, L_GAMMA_1, L_GAMMA_3, L_GAMMA_2, L_BETA_5, L_BETA_6
    };
    
    private static final Set<XrfLine> K_LINE_SET = new HashSet<>();
    static {
        K_LINE_SET.add(K_ALPHA_12);
        K_LINE_SET.add(K_BETA_1);
    }
    
    private static final Set<XrfLine> L_LINE_SET = new HashSet<>();
    static {
        L_LINE_SET.add(L_BETA_1);
        L_LINE_SET.add(L_ALPHA_1);
        L_LINE_SET.add(L_ALPHA_2);
        L_LINE_SET.add(L_BETA_2);
        L_LINE_SET.add(L_IOTA);
        L_LINE_SET.add(L_ETA);
        L_LINE_SET.add(L_BETA_3);
        L_LINE_SET.add(L_BETA_4);
        L_LINE_SET.add(L_GAMMA_1);
        L_LINE_SET.add(L_GAMMA_3);
        L_LINE_SET.add(L_GAMMA_2);
        L_LINE_SET.add(L_BETA_5);
        L_LINE_SET.add(L_BETA_6);
    }

    private static final Map<Integer, LineInfo> K_ALPHA_12_LINE = new HashMap<>();

    private static final Map<Integer, LineInfo> K_BETA_1_LINE = new HashMap<>();

    private static final Map<Integer, LineInfo> L_ALPHA_12_LINE = new HashMap<>();

    private static final Map<Integer, LineInfo> M_ALPHA_12_LINE = new HashMap<>();

    private static final Map<Integer, LineInfo> L_ALPHA_1_LINE = new HashMap<>();
    
    private static final Map<Integer, LineInfo> L_ALPHA_2_LINE = new HashMap<>();
    
    private static final Map<Integer, LineInfo> L_BETA_2_LINE = new HashMap<>();
    
    private static final Map<Integer, LineInfo> L_IOTA_LINE = new HashMap<>();
    
    private static final Map<Integer, LineInfo> L_BETA_1_LINE = new HashMap<>();
    
    private static final Map<Integer, LineInfo> L_BETA_3_LINE = new HashMap<>();
    
    private static final Map<Integer, LineInfo> L_BETA_4_LINE = new HashMap<>();
    
    private static final Map<Integer, LineInfo> L_ETA_LINE = new HashMap<>();
    
    private static final Map<Integer, LineInfo> L_GAMMA_1_LINE = new HashMap<>();
    
    private static final Map<Integer, LineInfo> L_GAMMA_3_LINE = new HashMap<>();
    
    private static final Map<Integer, LineInfo> L_GAMMA_2_LINE = new HashMap<>();
    
    private static final Map<Integer, LineInfo> L_BETA_5_LINE = new HashMap<>();
    
   private static final Map<Integer, LineInfo> L_BETA_6_LINE = new HashMap<>();
    
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
    
    // This class has just static methods and is never instatiated-
    // Data is therefore loaded into the data structues by this initializer.
    static {

        //Initialize major line data
        initializeLineData(getMajorLineInfo());
        
        // Initialize data of L lines
        initializeLineData(getLlineInfo());
        
        // Print data for verification purposes
//        System.out.println("TUBE_LINE_INFO");
//        printLineData(getMajorLineInfo());
//        System.out.println("\nTUBE_L_LINE_INFO");
//        printLineData(getLlineInfo());
    }
    
    private static void initializeLineData(Map<XrfLine, Map<Integer, LineInfo>> lines) {
        lines.keySet().stream()
                .forEach(xrfLine -> {
                    getAnodeElements().stream()
                            .map(tubeElement -> tubeElement.getAtomicNumber())
                            .filter(atomZ
                                    -> LineEnergies.getTubeLineEnergy(atomZ, xrfLine).isPresent()
                            && LineWidths.getTubeLineWidth(atomZ, xrfLine).isPresent()
                                            && AbsorptionEdges.getEdge(xrfLine).isPresent()
                            && AbsorptionEdges.getEdgeEnergy(atomZ, AbsorptionEdges.getEdge(xrfLine).get()).isPresent())
                            .forEach(atomZ
                                    -> lines.get(xrfLine).put(atomZ,
                                    new LineInfo(
                                            LineEnergies.getTubeLineEnergy(atomZ, xrfLine).get(),
                                            LineWidths.getTubeLineWidth(atomZ, xrfLine).get(),
                                            AbsorptionEdges.getEdgeEnergy(atomZ,
                                                    AbsorptionEdges.getEdge(xrfLine).get()).get()))
                            );
                });
    }
    
    private static void printLineData(Map<XrfLine, Map<Integer, LineInfo>> lines) {
         lines.keySet().stream()
                .forEach(xrfLine -> {
                    System.out.println("\n" + xrfLine.toString());
                    lines.get(xrfLine).keySet().stream()
                            .forEach(atomZ -> {
                                LineInfo lInfo = lines.get(xrfLine).get(atomZ);
                                System.out.println(atomZ + " "
                                        + String.format("%.3f", lInfo.getEnergy()) + " "
                                        + String.format("%.2f", lInfo.getLineWidth()) + " "
                                        + String.format("%.3f", lInfo.getAbsorptionEdge()));
                            });
                });
    }

    /**
     * Class has data of a characteristic x-ray line
     */
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

    public static Map<XrfLine, Map<Integer, LineInfo>> getMajorLineInfo() {
        return TUBE_LINE_INFO;
    }
    
     public static Map<XrfLine, Map<Integer, LineInfo>> getLlineInfo() {
        return TUBE_L_LINE_INFO;
    }
    
    public static LineInfo getLlineData(XrfLine line, int z) {
        return TUBE_L_LINE_INFO.get(line).get(z);
    }
    
    public static Set<XrfLine> getKlineSet() {
        return K_LINE_SET;
    }
    
    public static Set<XrfLine> getLlineSet() {
        return L_LINE_SET;
    }
    
    public static boolean isKline(XrfLine line) {
        return K_LINE_SET.contains(line);
    }
    
    public static boolean isLline(XrfLine line) {
        return L_LINE_SET.contains(line);
    }
}
