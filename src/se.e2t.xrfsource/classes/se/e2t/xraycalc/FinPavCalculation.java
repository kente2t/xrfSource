/*
 * FinPavCalculation.java
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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import se.e2t.abscoeffcalculate.AbsCoefficient;
import se.e2t.xraycalc.AbsorptionEdges.AbsEdge;
import static se.e2t.xraycalc.SourceCalculation.getLineWidth;
import se.e2t.xrfsource.spectrumclasses.SpectrumPart;
import se.e2t.xrfsource.spectrumclasses.XraySpectrum;

/**
 *
 * Class calculates an x-ray tube spectrum according to an algorith published
 * by A.L. Finkelshtein and T.O. Pavlova, Institute of Geochemistry,
 * Siberian Barnch of Academy of Science, Irkutsk, Russia.
 *
 * Publications:
 * 1. Calculation of X-ray Tube Spectral Distributions,
 * A.L. Finkelshtein and T.O. Pavlova, X-ray Spectrometry,
 * Vol 28,pages 27-32 (1999).
 
 * @author Kent Ericsson, e2t AB
 */
public class FinPavCalculation extends SourceCalculation {
    
    private static final Map<Integer, Double> A_EXPONENT;

    // Exponent values according to page 28 in the paper (1.)
    static {
        A_EXPONENT = new HashMap<>();
        A_EXPONENT.put(24, 0.23d);
        A_EXPONENT.put(45, 0.17d);
        A_EXPONENT.put(74, 0.15d);
    }

    public FinPavCalculation() {
        super();
    }
    
    /**
     * Method produces an intensity per Angstrom value for a certain wavelength.
     * @param inParameters reference to parameters input via GUI.
     * @param wavelength wavelength in Angstrom.
     * @param wavelengthWidth width of the wavelegth slice to be calculated (Angstrom).
     * @return total calculated intensity within wavelength interval.
     */
    @Override
    protected double getContiniumIntensity(Inparameters inParameters,
            double wavelength, double wavelengthWidth) {
        
        double energy0 = inParameters.getTubeVoltage();
        double energy = Inparameters.CONV_KEV_ANGSTROM /wavelength;
        double wavelength0 = Inparameters.CONV_KEV_ANGSTROM / energy0;
        int z = inParameters.getAnodeElement().getAtomicNumber();
        double zD = (double) z;
        var aExponent = Optional.ofNullable(A_EXPONENT.get(z));
        if (aExponent.isEmpty()) {
            System.out.println("In FinPavCalculation, no a exponent found for element " + z);
            return 0.0d;
        }
        double b = Math.pow(wavelength / (2.0d * wavelength0), aExponent.get());
        double t = Math.PI / Math.sqrt(3.0d);
        double j = 11.5d * zD;
        double lBracket = (1166.0d / j) * ((2.0d * energy0 + energy) / 3.0d);
        double lCont = Math.log(lBracket);
        double sigma = 4.0e5d / (Math.pow(energy0, 1.65d) - Math.pow(energy, 1.65d));
        double fFactor = getFfactor(z, inParameters.getOutAngle(), energy0,
                energy, sigma);
        double rFactor = getRfactor(z, energy0, energy);
        // Calculate according to the formula of the paper
        double nPhotonsPerAngstrom = 7.52e-5d * zD *(1.0d / wavelength0 - 1.0d / wavelength) *
                (1.0d / wavelength) * b * (t / lCont) * fFactor * rFactor * (1.0d / (4.0d * Math.PI));
        double integratedIntensity = nPhotonsPerAngstrom * wavelengthWidth;
        return integratedIntensity;
    }
    
    /**
     * Method calculates the f factor or function according to page 28 in the paper.
     * 
     * @param z atomic number
     * @param outAngle angle of photons existing the anode, relative to anode
     *        surface (degrees). 
     * @param energy0 Incoming electron energy (keV)
     * @param energy  energy of outgoing photon (KeV).
     * @param sigma sigma calculated according to formulas on papge 28 in
     *        the paper (different for continuum and lines).
     * @return factor value.
     */
    private double getFfactor(int z, double outAngle, double energy0,
            double energy, double sigma) {
        double h = 1.2d * AtomicWeights.getRelAtomicWeight(z) / (double) (z * z);
        double chi = AbsCoefficient.getMassAbsCoefficient(z, Inparameters.CONV_KEV_ANGSTROM / energy) /
                Math.sin(outAngle * Inparameters.ANGLE_CONV);
        double fFactor = 1.0d / (
                (1.0d + chi / sigma) * 
                (1.0d + ((h / (1.0d + h)) * (chi / sigma)))
                );
        return fFactor;
    }
    
    /**
     * Method calculates the backscattering factor according to page 379 in the
     * book, reference 14 in the paper. Note that the formula of R2 in the book
     * is incorrect. The formula has an offset value of +0.1836. Shall be -0.1836.
     * 
     * @param z atomic number
     * @param energy0 Incoming electron energy (keV)
     * @param energy energy of outgoing photon (KeV) for the continuum,
     *        the energy of absorption edge of the current line when factor
     *        is used in the line intensity calculations.
     * @return calculated factor.
     */
    private double getRfactor(int z, double energy0, double energy) {
        // Calculation of the backscattering factor R according to page 379
        // in the book referenced (no. 14) in the paper by Finkelshtein and Pavlova.
        double u = energy0 / energy;
        u = Math.min(10.0d, u);
        double r1 = 8.73e-3d * u * u * u - 0.1669d * u *u + 0.9662d * u + 0.4523d;
        double r2 = 2.703e-3d * u *u * u - 5.182e-2d * u * u + 0.302d * u - 0.1836d;
        double r3 = (0.887d * u * u * u -3.44d * u *u + 9.33d * u - 6.43d) /
                (u* u * u);
        double rFactor = r1 - r2 * Math.log(r3 * (double) z + 25.0d);
        return rFactor;
    }
    
    /**
     * Method calculates intensities of the characteristic lines of the x-ray tube.
     * Intensity is returned as the total intensity of the line together with
     * a width which is the natural width of the line.
     * @param inParameters reference to parameters input via GUI.
     * @param outputData an XraySpectrum object containing the calculated values.
     */
    @Override
    protected void calculateTubeLineIntensities(Inparameters inParameters, XraySpectrum outputData) {
        
        // Create a temporary storage for the lines
        List<SpectrumPart> allLines = new ArrayList<>();

        // Get tube target atomic number
        int z = inParameters.getAnodeElement().getAtomicNumber();
        double zD = (double) z;

        // First calculate K line intensities
        final Map<TubeLines.XrfLine, Map<Integer, TubeLines.LineInfo>> majorLineInfo
                = TubeLines.getMajorLineInfo();
        final double neK = 2.0d;
        final double bK = 0.35d * 1.73d;
        double energy0 = inParameters.getTubeVoltage();

        majorLineInfo.keySet().stream()
                .filter(xrfLine -> TubeLines.isKline(xrfLine)) // Major lines includes some L and a M line
                .filter(xrfLine -> Optional.ofNullable(majorLineInfo.get(xrfLine).get(z)).isPresent())                .filter(xrfLine -> AbsorptionEdges.getEdge(xrfLine).isPresent())
                .filter(xrfLine -> FlourYield.getYield(z, AbsorptionEdges.getEdge(xrfLine).get()).isPresent())
                .filter(xrfLine -> TransProbabilities.getTransProb(z, xrfLine).isPresent())
                .forEach(xrfLine -> {
                    TubeLines.LineInfo lineInfo = majorLineInfo.get(xrfLine).get(z);
                    double energy = lineInfo.getEnergy();
                    double wavelength = Inparameters.CONV_KEV_ANGSTROM
                            / energy;
                    double edgeEnergy = lineInfo.getAbsorptionEdge();
                    // Calculate U0, the overvoltage ratio
                    double u = energy0 / edgeEnergy;
                    // Line exists if tube voltage is above absorption edge
                    if (u > 1) {
                        double firstParantesis = u * Math.log(u) + 1.0d - u;
                        double j = 11.5d * zD;
                        double lnArgument = (1166.0d / j) * ((2.0d * energy0 + edgeEnergy) / 3.0d);
                        double lChar = Math.log(lnArgument);
                        // delta K calculation
                        double rK = 0.88d;
                        double lFunction = 0.75d;
                        double deltaK = 1.098e-5d * zD * zD * rK *
                                (edgeEnergy / ( neK * bK)) * lFunction;
                        // Start calculating final expression
                        double omegaK = FlourYield.getYield(z, AbsorptionEdges.getEdge(xrfLine).get()).get();
                        double pKL = TransProbabilities.getTransProb(z, xrfLine).get();
                        double sigma = 4.5e5d / (Math.pow(energy0, 1.65d) - Math.pow(edgeEnergy, 1.65d));
                        double fFactor = getFfactor(z, inParameters.getOutAngle(),
                                energy0, energy, sigma);
                        double rFactor = getRfactor(z, energy0, edgeEnergy);
                        double nPhotons = omegaK * pKL * ((neK * bK) / (2.0d * zD)) *
                                firstParantesis * (1.0d / lChar) * fFactor * rFactor *
                                (1.0d / (4.0d * Math.PI)) * (1.0d + deltaK);
                        double evwidth = lineInfo.getLineWidth();
                        // Calculate line width in Angstrom
                        double lineWidth = getLineWidth(lineInfo.getEnergy(), evwidth);
                        
                        // store calculated intensity
                        allLines.add(new SpectrumPart(wavelength, lineWidth, nPhotons));
                    }
                });

        // Calculate L line intensities
        final Map<TubeLines.XrfLine, Map<Integer, TubeLines.LineInfo>> lLineInfo
                = TubeLines.getLlineInfo();
        final double bL = (2.519d / (zD - 26.6)) - 0.0968d + 0.0103d * zD;

        lLineInfo.keySet().stream()
                .filter(xrfLine -> AbsorptionEdges.getEdge(xrfLine).isPresent())
                .filter(xrfLine -> Optional.ofNullable(lLineInfo.get(xrfLine).get(z)).isPresent())
                .filter(xrfLine -> FlourYield.getYield(z, AbsorptionEdges.getEdge(xrfLine).get()).isPresent())
                .filter(xrfLine -> TransProbabilities.getTransProb(z, xrfLine).isPresent())
                .forEach(xrfLine -> {
                    TubeLines.LineInfo lineInfo = lLineInfo.get(xrfLine).get(z);
                    double energy = lineInfo.getEnergy();
                    double wavelength = Inparameters.CONV_KEV_ANGSTROM
                            / energy;
                    double edgeEnergy = lineInfo.getAbsorptionEdge();
                    double omegaK = FlourYield.getYield(z, AbsorptionEdges.getEdge(xrfLine).get()).get();
                    AbsEdge lEdge = AbsorptionEdges.getEdge(xrfLine).get();
                    double neL = 2.0d;
                    if (lEdge == AbsEdge.L3_EDGE) {
                        neL = 4.0d;
                    }
                    // Calculate U0, the overvoltage ratio
                    double u = energy0 / edgeEnergy;
                    // Line exists if tube voltage is above absorption edge
                    if (u > 1) {
                        double firstParantesis = u * Math.log(u) + 1.0d - u;
                        double j = 11.5d * zD;
                        double lnArgument = (1166.0d / j) * ((2.0d * energy0 + edgeEnergy) / 3.0d);
                        double l = Math.log(lnArgument);
                        // Start calculating final expression
                        double pKL = TransProbabilities.getTransProb(z, xrfLine).get();
                         double sigma = 4.5e5d / (Math.pow(energy0, 1.65d) - Math.pow(edgeEnergy, 1.65d));
                        double fFactor = getFfactor(z, inParameters.getOutAngle(),
                                energy0, energy, sigma);
                        double rFactor = getRfactor(z, energy0, edgeEnergy);
                        double nPhotons = omegaK * pKL * ((neL * bL) / (2.0d * zD)) *
                                firstParantesis * (1.0d / l) * fFactor * rFactor *
                                (1.0d / (4.0d * Math.PI)) ;
                        double evwidth = lineInfo.getLineWidth();
                        // Calculate line width in Angstrom
                        double lineWidth = getLineWidth(lineInfo.getEnergy(), evwidth);
                        
                        // Store calculated intensity
                        allLines.add(new SpectrumPart(wavelength, lineWidth, nPhotons));
                    }
                });
        // Sort lines in wavelength order and add to output data
        allLines.stream()
                .sorted(Comparator.comparing(SpectrumPart::getWavelength))
                .forEach(specPart -> outputData.getTubeLines().add(specPart));  
    }
}
