/*
 * NistCalculation.java
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

import se.e2t.abscoeffcalculate.AbsCoefficient;
import se.e2t.abscoeffcalculate.Mucal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import se.e2t.xraycalc.TubeLines.LineInfo;
import se.e2t.xraycalc.TubeLines.XrfLine;
import se.e2t.xrfsource.spectrumclasses.SpectrumPart;
import se.e2t.xrfsource.spectrumclasses.XraySpectrum;

/**
 *
 * @author Kent Ericsson, e2t AB
 *
 * Class calculates an x-ray tube spectrum accordinbg to an algorith published
 * by Pella, Feng and Small, working at US National Institute of Standards.
 *
 * Publications:
 * 1. An Analytical Algorith for Calculation of Spectral
 * Distributions of X-Ray Tubes for Quantative X-Ray Flourescence Analysis. P.A-
 * Pella, I. Feng, J.A. Small, X-RAY SPECTROMETRY, VOL 14, 1985, pages 125-135.
 * 2. Addition of M- and L-Series Lines to NIST Algorithm for Calculation of
 * X-Ray Tube Output Spectral Distributions. P.A- Pella, I. Feng, J.A. Small,
 * X-RAY SPECTROMETRY, VOL 20, 1991, pages 109-110.
 */
public class NistCalculation extends SourceCalculation {

    // Constants from page 131 in the first paper and from page 109 in the 
    // second paper.
    private static final Map<XrfLine, NistInfo> NIST_PAR;

    static {
        NIST_PAR = new HashMap<>();
        NIST_PAR.put(XrfLine.K_ALPHA_12, new NistInfo(3.22e6d, 9.76e4d, -0.39d));
        NIST_PAR.put(XrfLine.K_BETA_1, new NistInfo(5.13e5d, 2.05e5d, -0.014d));
        NIST_PAR.put(XrfLine.L_ALPHA_12, new NistInfo(2.02e7d, 2.65e6d, 0.21d));
//        NIST_PAR.put(XrfLine.L_B1, new NistInfo(1.76e7d, 6.05e6d, -0.09d));
        NIST_PAR.put(XrfLine.M_ALPHA_12, new NistInfo(1.76e8d, 1.02e6d, 0.0d));
    }

    public NistCalculation() {
        super();
    }

    public static class NistInfo {

        private final double _a;
        private final double _b;
        private final double _d;

        public NistInfo(double a, double b, double d) {
            _a = a;
            _b = b;
            _d = d;
        }

        public double getA() {
            return _a;
        }

        public double getB() {
            return _b;
        }

        public double getD() {
            return _d;
        }
    }

    public static Map<XrfLine, NistInfo> getNIST_PAR() {
        return NIST_PAR;
    }

    /**
     * Method produces an intensity per Angstrom value for a certain wavelength.
     * @param inParameters reference to parameters input via GUI.
     * @param wavelength wavelength in Angstrom.
     * @param wavelengthWidth width of the wavelegth slice to be calculated (Angstrom).
     * @return total calculated intensity within the wavelength interval.
     */
    @Override
    protected double getContiniumIntensity(Inparameters inParameters,
            double wavelength, double wavelengthWidth) {
        // Get tube anode atomic number
        int z = inParameters.getAnodeElement().getAtomicNumber();

        // Calculate f
        double takeOffAngle = inParameters.getOutAngle() * Inparameters.ANGLE_CONV;
        double minWl = Inparameters.CONV_KEV_ANGSTROM / inParameters.getTubeVoltage();
        double f = getPellaF(z, wavelength, takeOffAngle, minWl);

        // Calculate intensity
        double intensity = f * 2.72e-6d * (double) z * ((wavelength / minWl - 1.0d) / (wavelength * wavelength));
        double integratedIntensity = intensity * wavelengthWidth;
        return integratedIntensity;
    }

    private static double getPellaXi(int z, double wavelength, double takeOffAngle,
            double minWl) {
        return (AbsCoefficient.getTau(z, wavelength) / Math.sin(takeOffAngle))
                * ((1.0d / Math.pow(minWl, 1.65d)) - (1.0d / Math.pow(wavelength, 1.65d)));
    }

    private static double getPellaC(double minWl, int z, double xi) {
        return (1.0d + (1.0d / (1.0d + 2.56e-3d * (double) (z * z))))
                / ((1.0d + ((2.56e3d * minWl) / (double) (z * z)))
                * (0.25d * xi + 1.0e4d));
    }

    private static double getPellaF(int z, double wavelength,
            double takeOffAngle, double minWl) {
        double xi = getPellaXi(z, wavelength, takeOffAngle, minWl);
        double c = getPellaC(minWl, z, xi);
        return 1.0d / ((1.0d + c * xi) * (1.0d + c * xi));
    }

    private static double getPellaNproduct(int n) {
        double product = 1.0d;
        for (int i = 1; i <= n; i++) {
            product *= (double) i / (double) ((1 + i) * (1 + i));
        }
        return product;
    }

    private static double getPellaEi(double x) {
        double result = x + Math.log(x);
        int n = 1;
        double tN;
        do {
            tN = Math.pow(x, (double) (n + 1)) * getPellaNproduct(n);
            result += tN;
            n++;
        } while (((double) n <= 2.0d * x) || tN >= 1.0e-6d);
        return result;
    }

    private static double getPellaEz(double absEdgeEnergy, int z) {
        return ((1166.0d * absEdgeEnergy) / (9.76d * (double) z))
                + 58.5d * Math.pow((double) z, -0.19d);
    }

    private static double getPellaUshell(int n, double u0, double eZ, double lYield) {
        double elogEz = Math.log(eZ);
        double squareBracket = getPellaEi(Math.log(u0 * eZ)) - getPellaEi(elogEz);
        double curledBracket = u0 - 1.0d - (elogEz / eZ) * squareBracket;
        return (double) n * lYield * curledBracket;

    }
    
    /**
     * Method calculates intensities of the characteristic lines of the x-ray tube.
     * Intensity is returned as a per Angstrom. The natural width of the line is
     * used to get this per Angstrom value.
     * @param inParameters reference to parameters input via GUI.
     * @param outputData an XarySpectrum object containing the calculated values.
     */
    @Override
    protected void calculateTubeLineIntensities(
            Inparameters inParameters, XraySpectrum outputData) {
        
        Map<TubeLines.XrfLine, Map<Integer, TubeLines.LineInfo>> tubeLineInfo = TubeLines.getMajorLineInfo();

        Map<TubeLines.XrfLine, NistInfo> nistPar = getNIST_PAR();

        // Get tube target atomic number
        int z = inParameters.getAnodeElement().getAtomicNumber();

        // Get tube anode voltage
        double tubeVoltage = inParameters.getTubeVoltage();

        // Process all tube lines
        List<SpectrumPart> tempOut = new ArrayList<>(); // Temporary storage
        nistPar.keySet().stream()
                .filter(xrfLine -> tubeLineInfo.get(xrfLine).get(z) != null) // No such line?
                .filter(xrfLine -> xrfLine != XrfLine.L_BETA_1) // L_B1 added later
                .filter(xrfLine -> Inparameters.CONV_KEV_ANGSTROM
                / tubeLineInfo.get(xrfLine).get(z).getEnergy()
                <= inParameters.getMaxWavelength()) // Wavelength too high?
                .forEach(xrfLine -> {
                    // Get NIST parameter record of this line
                    NistInfo nistInfo = nistPar.get(xrfLine);
                    // Get Lineinfo of this line
                    LineInfo lineInfo = tubeLineInfo.get(xrfLine).get(z);
                    // Get line wavelength
                    double wavelength = Inparameters.CONV_KEV_ANGSTROM
                            / lineInfo.getEnergy();
                    // Calculate U0, the overvoltage ratio
                    double u0 = tubeVoltage / lineInfo.getAbsorptionEdge();
                    // Line exists if tube voltage is above absorption edge
                    if (u0 > 1) {
                        // Calculate the NIST expression factors
                        double fac1 = (nistInfo.getA() / (nistInfo.getB() + (double) (z * z * z * z)))
                                + nistInfo.getD();
                        double exponent = -0.5d * ((u0 - 1.0d) / (1.17d * u0 + 3.2d))
                                * ((u0 - 1.0d) / (1.17d * u0 + 3.2d));
                        double fac2 = Math.exp(exponent);
                        double fac3 = ((u0 * Math.log(u0)) / (u0 - 1.0d)) - 1.0d;
                        // Calculate ratio Nline / Ncontinium
                        double ratio = fac1 * fac2 * fac3;
                         // Get line energy
                        double lineEnergy = lineInfo.getEnergy();
                        // get line width in eV
                        double evwidth = lineInfo.getLineWidth();
                        // Calculate line width in Angstrom
                        double lineWidth = getLineWidth(lineEnergy, evwidth);
                        // Get line integrated intensity
                        double lineIntegralInt = ratio
                                * getContiniumIntensity(inParameters, wavelength, lineWidth) /
                                lineWidth;
                       
                        // Store line intensity
                        tempOut.add(new SpectrumPart(wavelength, lineWidth, lineIntegralInt));
                    }
                });

        // If L_A12 intensity has been generated then additional  L lines will be
        // added according to the second Pella et al. reference (no. 2)
        // Check if L_A12 line was generated
        List<SpectrumPart> allLines = tempOut;
        // Assume no L_A12 line
        Map<Integer, LineInfo> lineInfoMap = tubeLineInfo.get(XrfLine.L_ALPHA_12);
        Optional<LineInfo> optLineInfo
                = Optional.ofNullable(lineInfoMap.get(z));
        if (optLineInfo.isPresent()) {

            // Check if La12 wavelength is less than max wavelength
            double lA12wavelength = Inparameters.CONV_KEV_ANGSTROM
                    / optLineInfo.get().getEnergy();
            if (lA12wavelength <= inParameters.getMaxWavelength()) {

                // Add L lines according to Pella's second paper
                SpectrumPart lA12Info = tempOut.stream()
                        .filter(sPart -> sPart.getWavelength() == lA12wavelength)
                        .findFirst().get(); // L_A12 extracted
                List<SpectrumPart> kmLines = tempOut.stream()
                        .filter(sPart -> !sPart.equals(lA12Info))
                        .collect(Collectors.toList()); // L_A12 removed
                List<SpectrumPart> lLines = getLlines(inParameters,
                        lA12Info.getIntensity(), z);

                // Add lines
                allLines.clear();
                allLines.addAll(kmLines);
                allLines.addAll(lLines);
            }
        }

        // Sort lines in wavelength order and add to output data
        allLines.stream()
                .sorted(Comparator.comparing(SpectrumPart::getWavelength))
                .forEach(specPart -> outputData.getTubeLines().add(specPart));
    }

    /**
     * Method calculates the intensities of the L lines according to the second paper
     * @param inParameters parameters input by the operator
     * @param lA12Intensity total intensity of L-alpha12 used as a reference
     * @param z atomic number
     * @return List of SpectrumPart objects describinbg the line intensities.
     */
    private static List<SpectrumPart> getLlines(
            Inparameters inParameters,
            Double lA12Intensity,
            int z) {
        List<SpectrumPart> result = new ArrayList<>();

        // Some often used parameters
        double takeOffAngle = inParameters.getOutAngle()
                * Inparameters.ANGLE_CONV; // in radians
        double minWl = Inparameters.CONV_KEV_ANGSTROM / inParameters.getTubeVoltage();
        Mucal mc = new Mucal(null, z, 0.0d, 'C', true);
        mc.calculate();
        double tubeVoltage = inParameters.getTubeVoltage();
        double meanYield = (mc.getFl_yield()[1] + mc.getFl_yield()[2]
                + mc.getFl_yield()[3]) / 3.0d;

        // Calculate L_A12 f factor
        double lA12wavelength = Inparameters.CONV_KEV_ANGSTROM
                / ((TubeLines.getLlineData(XrfLine.L_ALPHA_1, z).getEnergy()
                + TubeLines.getLlineData(XrfLine.L_ALPHA_2, z).getEnergy()) / 2.0d);

        double fA12 = getPellaF(z, lA12wavelength, takeOffAngle, minWl);

        // Calculate L subshells ionization cross-sections
        //L1
        double l1Edge = mc.getEnergy()[1];
        double u0L1 = tubeVoltage / l1Edge;
        double ezL1 = getPellaEz(l1Edge, z);
        double uL1 = getPellaUshell(2, u0L1, ezL1, meanYield);
        //L2
        double l2Edge = mc.getEnergy()[2];
        double u0L2 = tubeVoltage / l2Edge;
        double ezL2 = getPellaEz(l2Edge, z);
        double uL2 = getPellaUshell(2, u0L2, ezL2, meanYield);
        //L3
        double l3Edge = mc.getEnergy()[3];
        double u0L3 = tubeVoltage / l3Edge;
        double ezL3 = getPellaEz(l3Edge, z);
        double uL3 = getPellaUshell(4, u0L3, ezL3, meanYield);

        // Calculate and store L line data
        //L_A1
        LineInfo lInfo = TubeLines.getLlineData(XrfLine.L_ALPHA_1, z);
        double wavelength = Inparameters.CONV_KEV_ANGSTROM / lInfo.getEnergy();
        double lineWidth = getLineWidth(lInfo.getEnergy(), lInfo.getLineWidth());
        double intLa1Int = lA12Intensity / 1.1d; // This is the reference intensity 
        result.add(new SpectrumPart(wavelength, lineWidth, intLa1Int));
        //L_A2
        lInfo = TubeLines.getLlineData(XrfLine.L_ALPHA_2, z);
        wavelength = Inparameters.CONV_KEV_ANGSTROM / lInfo.getEnergy();
        lineWidth = getLineWidth(lInfo.getEnergy(), lInfo.getLineWidth());
        double lA2int = 0.1d * intLa1Int;
        result.add(new SpectrumPart(wavelength, lineWidth, lA2int));
        // L_B2
        lInfo = TubeLines.getLlineData(XrfLine.L_BETA_2, z);
        wavelength = Inparameters.CONV_KEV_ANGSTROM / lInfo.getEnergy();
        lineWidth = getLineWidth(lInfo.getEnergy(), lInfo.getLineWidth());
        double fT = getPellaF(z, wavelength, takeOffAngle, minWl) / fA12;
        double relP = 0.2575d * Math.log((double) z) - 0.8845d;
        double lB2int = relP * intLa1Int;
        result.add(new SpectrumPart(wavelength, lineWidth, lB2int * fT));
        // L_L
        lInfo = TubeLines.getLlineData(XrfLine.L_IOTA, z);
        wavelength = Inparameters.CONV_KEV_ANGSTROM / lInfo.getEnergy();
        lineWidth = getLineWidth(lInfo.getEnergy(), lInfo.getLineWidth());
        fT = getPellaF(z, wavelength, takeOffAngle, minWl) / fA12;
        double lLint = 0.044d * intLa1Int;
        result.add(new SpectrumPart(wavelength, lineWidth, lLint * fT));
        // L_B1
        lInfo = TubeLines.getLlineData(XrfLine.L_BETA_1, z);
        wavelength = Inparameters.CONV_KEV_ANGSTROM / lInfo.getEnergy();
        lineWidth = getLineWidth(lInfo.getEnergy(), lInfo.getLineWidth());
        fT = getPellaF(z, wavelength, takeOffAngle, minWl) / fA12;
        relP = (0.565d * Math.log((double) z) - 0.9445d) * (uL2 / uL3);
        double lB1int = relP * intLa1Int;
        result.add(new SpectrumPart(wavelength, lineWidth, lB1int * fT));
        // L_B3
        lInfo = TubeLines.getLlineData(XrfLine.L_BETA_3, z);
        wavelength = Inparameters.CONV_KEV_ANGSTROM / lInfo.getEnergy();
        lineWidth = getLineWidth(lInfo.getEnergy(), lInfo.getLineWidth());
        fT = getPellaF(z, wavelength, takeOffAngle, minWl) / fA12;
        relP = (0.5632d * Math.log((double) z) - 1.9501d) * (uL1 / uL3);
        double lB3int = relP * intLa1Int;
        result.add(new SpectrumPart(wavelength, lineWidth, lB3int * fT));
        // L_B4
        lInfo = TubeLines.getLlineData(XrfLine.L_BETA_4, z);
        wavelength = Inparameters.CONV_KEV_ANGSTROM / lInfo.getEnergy();
        lineWidth = getLineWidth(lInfo.getEnergy(), lInfo.getLineWidth());
        fT = getPellaF(z, wavelength, takeOffAngle, minWl) / fA12;
        double lB4int = 0.626d * lB3int;
        result.add(new SpectrumPart(wavelength, lineWidth, lB4int * fT));
        // L_E
        lInfo = TubeLines.getLlineData(XrfLine.L_ETA, z);
        wavelength = Inparameters.CONV_KEV_ANGSTROM / lInfo.getEnergy();
        lineWidth = getLineWidth(lInfo.getEnergy(), lInfo.getLineWidth());
        fT = getPellaF(z, wavelength, takeOffAngle, minWl) / fA12;
        double lEint = 0.024d * lB1int;
        result.add(new SpectrumPart(wavelength, lineWidth, lEint * fT));
        // L_G1
        lInfo = TubeLines.getLlineData(XrfLine.L_GAMMA_1, z);
        wavelength = Inparameters.CONV_KEV_ANGSTROM / lInfo.getEnergy();
        lineWidth = getLineWidth(lInfo.getEnergy(), lInfo.getLineWidth());
        fT = getPellaF(z, wavelength, takeOffAngle, minWl) / fA12;
        relP = (0.3749d * Math.log((double) z) - 1.2873d) * (uL2 / uL3);
        double lG1int = relP * intLa1Int;
        result.add(new SpectrumPart(wavelength, lineWidth, lG1int * fT));
        // L_G3
        lInfo = TubeLines.getLlineData(XrfLine.L_GAMMA_3, z);
        wavelength = Inparameters.CONV_KEV_ANGSTROM / lInfo.getEnergy();
        lineWidth = getLineWidth(lInfo.getEnergy(), lInfo.getLineWidth());
        fT = getPellaF(z, wavelength, takeOffAngle, minWl) / fA12;
        relP = 0.068d * (uL1 / uL3);
        double lG3int = relP * intLa1Int;
        result.add(new SpectrumPart(wavelength, lineWidth, lG3int * fT));
        // Done
        return result;
    }
}
