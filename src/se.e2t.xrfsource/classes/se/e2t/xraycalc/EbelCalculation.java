/*
 * EbelCalculation.java
 */
package se.e2t.xraycalc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import se.e2t.abscoeffcalculate.AbsCoefficient;
import se.e2t.xraycalc.AbsorptionEdges.AbsEdge;
import se.e2t.xraycalc.TubeLines.LineInfo;
import se.e2t.xrfsource.spectrumclasses.SpectrumPart;
import se.e2t.xrfsource.spectrumclasses.XraySpectrum;

/**
 *
 * @author Kent
 * 
 * * Class calculates an x-ray tube spectrum accordinbg to an algorith published
 * by Horst Ebel working at The Technical University in Vienna.
 *
 * Publications:
 * 1. X-ray Tube Spectra. Horst Ebel, X-ray Spectrometry, Bol 28,pages 255-266.
 * 2. Lt, La12, Ln, Lb123456, Ly123 spectra of x-ray tubes. Horst Ebel,
 * X-ray Spectrometry 32, åpages 46-51.
 */
public class EbelCalculation extends SourceCalculation {
    
    public EbelCalculation() {
        super();
    }

    @Override
    protected double getContiniumIntensity(Inparameters inParameters, double wavelength) {

        // dE in the contiuum intensity formula has been replaced by
        // (12.4 / (wavelenght * wavelength)) * dLambda.
        // and (E0/E - 1) has been replaced by (wavelength/wavelength0 -1).
        // Get tube anode atomic number
        int z = inParameters.getAnodeElement().getAtomicNumber();

        // Calculate the x exponent
        double energy0 = inParameters.getTubeVoltage();
        double energy = Inparameters.CONV_KEV_ANGSTROM / wavelength;
        double xExponent = 1.109d - (0.00435 * (double) z) + 0.00175 * energy0;
        double tauEj = AbsCoefficient.getTau(z, wavelength);
        double sinPhi = Math.sin(inParameters.getInAngle() * Inparameters.ANGLE_CONV);
        double sinEpsilon = Math.sin(inParameters.getOutAngle() * Inparameters.ANGLE_CONV);
        double longExpression = tauEj * 2.0d
                * getRouZ(inParameters.getTubeVoltage(), wavelength, z)
                * sinPhi / sinEpsilon;
        double intensity = 1.35e9d * (double) z
                * Math.pow(((energy0 / energy) - 1.0d), xExponent)
                * ((1.0d - Math.exp(-longExpression)) / (tauEj * longExpression))
                * inParameters.CONV_KEV_ANGSTROM / (wavelength * wavelength);
        return intensity;
    }

    /**
     * Method calculates the rouz variable as described in Ebels paper
     *
     * @param tubeVoltage = X-ray tube voltage in keV.
     * @param wavelength = When method is called during calculation of continuum
     * intensity this is the wavelength of the continuum slice. When method is
     * called during calculation of tube line intensity then this is the
     * wavelength of the absorption edge associated with the line.
     * @param atomZ = atomic number of x-reay tube target material.
     * @return = value of rouz variable.
     */
    private double getRouZ(double tubeVoltage, double wavelength, int atomZ) {

        double j = 0.0135d * (double) atomZ;
        double rouZm = (AtomicWeights.getRelAtomicWeight(atomZ) / (double) atomZ)
                * (0.787e-5d * Math.sqrt(j) * Math.pow(tubeVoltage, 1.5d)
                + 0.735e-6d * tubeVoltage * tubeVoltage);
        double lnZ = Math.log((double) atomZ);
        double m = 0.1382d - (0.9211d / Math.sqrt((double) atomZ));
        double eta = Math.pow(tubeVoltage, m) * (0.1904d - 0.2236d * lnZ
                + 0.1292d * lnZ * lnZ - 0.0149d + lnZ * lnZ * lnZ);
        double lnU0 = Math.log(tubeVoltage / (Inparameters.CONV_KEV_ANGSTROM / wavelength));
        double rouZ = rouZm * lnU0 * ((0.49269d - 1.0987d * eta + 0.78557d * eta * eta)
                / (0.70256d - 1.09865d * eta + 1.0046d * eta * eta + lnU0));
        return rouZ;
    }

    @Override
    protected void calculateTubeLineIntensities(Inparameters inParameters,
            XraySpectrum outputData) {

        // Create a temoprary storage for the lines
        List<SpectrumPart> allLines = new ArrayList<>();

        // Get tube target atomic number
        int z = inParameters.getAnodeElement().getAtomicNumber();
        double zD = (double) z;

        // Calculate K line intensities according to Ebels first paper
        final Map<TubeLines.XrfLine, Map<Integer, TubeLines.LineInfo>> majorLineInfo
                = TubeLines.getMajorLineInfo();
        final double zK = 2.0d;
        final double bK = 0.35d;
        final double constK = 5.0e13d;

        double energy0 = inParameters.getTubeVoltage();

        majorLineInfo.keySet().stream()
                .filter(xrfLine -> TubeLines.isKline(xrfLine)) // Major lines includes some L and a M line
                .filter(xrfLine -> AbsorptionEdges.getEdge(xrfLine).isPresent())
                .filter(xrfLine -> FlourYield.getYield(z, AbsorptionEdges.getEdge(xrfLine).get()).isPresent())
                .filter(xrfLine -> TransProbabilities.getTransProb(z, xrfLine).isPresent())
                .forEach(xrfLine -> {
                    LineInfo lineInfo = majorLineInfo.get(xrfLine).get(z);
                    double wavelength = Inparameters.CONV_KEV_ANGSTROM
                            / lineInfo.getEnergy();
                    double edgeEnergy = lineInfo.getAbsorptionEdge();
                    // Calculate U0, the overvoltage ratio
                    double u0 = energy0 / edgeEnergy;
                    // Line exists if tube voltage is above absorption edge
                    if (u0 > 1) {
                        // Calculate components of stopping power factor
                        double firstParantesis = u0 * Math.log(u0) + 1.0d - u0;
                        double bigRoot = Math.sqrt(0.0135d * zD / edgeEnergy);
                        double nominator = Math.sqrt(u0) * Math.log(u0) + 2.0d * (1.0d - Math.sqrt(u0));
                        double squareBracket = 1.0d + 16.05d * bigRoot * (nominator / firstParantesis);
                        double sPowFactor = ((zK * bK) / zD) * firstParantesis * squareBracket;
                        // Calculate the f function
                        double tau = AbsCoefficient.getTau(z, wavelength);
                        double sinPhi = Math.sin(inParameters.getInAngle() * Inparameters.ANGLE_CONV);
                        double sinEpsilon = Math.sin(inParameters.getOutAngle() * Inparameters.ANGLE_CONV);
                        double rouZ = getRouZ(inParameters.getTubeVoltage(),
                                Inparameters.CONV_KEV_ANGSTROM / edgeEnergy, z);
                        double longExpression = tau * 2.0d * rouZ
                                * sinPhi / sinEpsilon;
                        double fFunction = (1.0d - Math.exp(-longExpression)) / longExpression;
                        // Calculate r
                        double r = 1.0d - 0.0081517d * zD + 3.613e-5d * zD * zD
                                + 0.009583d * zD * Math.exp(-u0) + 0.001141 * energy0;
                        double omegaJK = FlourYield.getYield(z, AbsorptionEdges.getEdge(xrfLine).get()).get();
                        double pJKL = TransProbabilities.getTransProb(z, xrfLine).get();
                        double evwidth = lineInfo.getLineWidth();
                        // Calculate line width in Angstrom
                        double lineWidth = getLineWidth(lineInfo.getEnergy(), evwidth);
                        // Convert line integrated intensity to per Angstrom value
                        double intensity = (constK * sPowFactor * r * omegaJK * pJKL * fFunction) / lineWidth;
                        allLines.add(new SpectrumPart(wavelength, lineWidth, intensity));
                    }
                });

        // Calculate L line intensities according to Ebels second paper
        final Map<TubeLines.XrfLine, Map<Integer, TubeLines.LineInfo>> lLineInfo
                = TubeLines.getLlineInfo();
        final double zL = 8.0d;
        final double bL = 0.25d;
        final double fCorr = -0.4814d + 0.03781 * zD - 2.413e-4d * zD * zD;

        lLineInfo.keySet().stream()
                .filter(xrfLine -> AbsorptionEdges.getEdge(xrfLine).isPresent())
                .filter(xrfLine -> FlourYield.getYield(z, AbsorptionEdges.getEdge(xrfLine).get()).isPresent())
                .filter(xrfLine -> TransProbabilities.getTransProb(z, xrfLine).isPresent())
                .forEach(xrfLine -> {
                    LineInfo lineInfo = lLineInfo.get(xrfLine).get(z);
                    double wavelength = Inparameters.CONV_KEV_ANGSTROM
                            / lineInfo.getEnergy();
                    double edgeEnergy = lineInfo.getAbsorptionEdge();
                    // Calculate U0, the overvoltage ratio
                    double u0 = energy0 / edgeEnergy;
                    // Line exists if tube voltage is above absorption edge
                    if (u0 > 1) {
                        // Calculate components of stopping power factor
                        double firstParantesis = u0 * Math.log(u0) + 1.0d - u0;
                        double bigRoot = Math.sqrt(0.0135d * zD / edgeEnergy);
                        double nominator = Math.sqrt(u0) * Math.log(u0) + 2.0d * (1.0d - Math.sqrt(u0));
                        double squareBracket = 1.0d + 16.05d * bigRoot * (nominator / firstParantesis);
                        double sPowFactor = ((zL * bL) / zD) * firstParantesis * squareBracket;
                        // Calculate the f function
                        double tau = AbsCoefficient.getTau(z, wavelength);
                        double sinPhi = Math.sin(inParameters.getInAngle() * Inparameters.ANGLE_CONV);
                        double sinEpsilon = Math.sin(inParameters.getOutAngle() * Inparameters.ANGLE_CONV);
                        double rouZ = getRouZ(inParameters.getTubeVoltage(),
                                Inparameters.CONV_KEV_ANGSTROM / edgeEnergy, z);
                        double longExpression = tau * 2.0d * rouZ
                                * sinPhi / sinEpsilon;
                        double fFunction = (1.0d - Math.exp(-longExpression)) / longExpression;
                        // Calculate r
                        double r = 1.0d - 0.0081517d * zD + 3.613e-5d * zD * zD
                                + 0.009583d * zD * Math.exp(-u0) + 0.001141 * energy0;
                        double omegaJK = FlourYield.getYield(z, AbsorptionEdges.getEdge(xrfLine).get()).get();
                        double pJKL = TransProbabilities.getTransProb(z, xrfLine).get();
                        double evwidth = lineInfo.getLineWidth();
                        // Calculate line width in Angstrom
                        double lineWidth = getLineWidth(lineInfo.getEnergy(), evwidth);
                        // Calculate the Const factor
                        double constX = 4.94e13d;
                        AbsEdge absEdgeType = AbsorptionEdges.getEdge(xrfLine).get();
                        switch (absEdgeType) {
                            case L1_EDGE:
                                constX = fCorr * 0.71e13d;
                                break;
                            case L2_EDGE:
                                constX = fCorr * 2.70E13d;
                        }
                        // Convert line integrated intensity to per Angstrom value
                        double intensity = (constX * sPowFactor * r * omegaJK * pJKL * fFunction) / lineWidth;
                        allLines.add(new SpectrumPart(wavelength, lineWidth, intensity));
                    }
                });
        // Sort lines in wavelength order and add to output data
        allLines.stream()
                .sorted(Comparator.comparing(SpectrumPart::getWavelength))
                .forEach(specPart -> outputData.getTubeLines().add(specPart));
    }
}
