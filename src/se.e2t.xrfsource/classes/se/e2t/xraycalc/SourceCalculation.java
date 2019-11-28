/*
 * SourceCalculation.java
 */
package se.e2t.xraycalc;

import se.e2t.abscoeffcalculate.AbsCoefficient;
import se.e2t.abscoeffcalculate.Mucal;
import java.util.List;
import java.util.Map;
import se.e2t.xrfsource.spectrumclasses.SpectrumPart;
import se.e2t.xrfsource.spectrumclasses.XraySpectrum;

/**
 *
 * @author Kent Ericsson, e2t AB
 * 
 * This is the super class for calculation of tube spectrum intensities.
 * This class is extended by the classes that actually calculate the intensities.
 */
public abstract class SourceCalculation {

    protected static final double MINIMUM_SLICE = 0.001d;

    public SourceCalculation() {
    }

    public XraySpectrum calculate(Inparameters inParameters) {

        XraySpectrum outputData = new XraySpectrum();

        // Find continium start wavelength
        double startWavelength = Inparameters.CONV_KEV_ANGSTROM / inParameters.getTubeVoltage();

        // Calculate wavelengths of plus/minus 2 eV from absorption edges
        int z = inParameters.getAnodeElement().getAtomicNumber();
        Mucal mc = new Mucal(null, z, 0.0d, 'C', true);
        mc.calculate();
        double wlBelowKedge = Inparameters.CONV_KEV_ANGSTROM / (mc.getEnergy()[0] + 0.002d);
        double wlAboveKedge = Inparameters.CONV_KEV_ANGSTROM / (mc.getEnergy()[0] - 0.002d);
        double wlBelowL1edge = Inparameters.CONV_KEV_ANGSTROM / (mc.getEnergy()[1] + 0.002d);
        double wlAboveL1edge = Inparameters.CONV_KEV_ANGSTROM / (mc.getEnergy()[1] - 0.002d);
        double wlBelowL2edge = Inparameters.CONV_KEV_ANGSTROM / (mc.getEnergy()[2] + 0.002d);
        double wlAboveL2edge = Inparameters.CONV_KEV_ANGSTROM / (mc.getEnergy()[2] - 0.002d);
        double wlBelowL3edge = Inparameters.CONV_KEV_ANGSTROM / (mc.getEnergy()[3] + 0.002d);
        double wlAboveL3edge = Inparameters.CONV_KEV_ANGSTROM / (mc.getEnergy()[3] - 0.002d);

        // Find center wavelength of first full spectrum slice
        double normalSlice = inParameters.getContiniumIntervalSize();
        double centerWavelength = startWavelength + (normalSlice / 2.0d);
        double sliceUpper = centerWavelength + (normalSlice / 2.0d);
        double maxWavelength = inParameters.getMaxWavelength();

        // Add continium slices until slice upper wavelength is above K edge
        // or max ordered wavelength
        do {
            outputData.addContiniumSlice(
                    new SpectrumPart(centerWavelength, normalSlice,
                            getContiniumIntensity(inParameters,
                                    centerWavelength, normalSlice)));
            centerWavelength += normalSlice;
            sliceUpper += normalSlice;
        } while ((sliceUpper < maxWavelength) && (sliceUpper < wlBelowKedge));

        if (sliceUpper < maxWavelength) {
            // Go up to K edge
            double slice = wlBelowKedge - (centerWavelength - (normalSlice / 2.0d));
            centerWavelength = wlBelowKedge - (slice / 2.0d);
            if (slice > MINIMUM_SLICE) {
                outputData.addContiniumSlice(
                        new SpectrumPart(centerWavelength, slice,
                                getContiniumIntensity(inParameters,
                                        centerWavelength, slice)));
            }
            // Restart at K edge
            centerWavelength = wlAboveKedge + (normalSlice / 2.0d);
            sliceUpper = centerWavelength + (normalSlice / 2.0d);
            // Add continium slices until slice upper wavelength is above L1 edge
            // or max ordered wavelength
            do {
                outputData.addContiniumSlice(
                        new SpectrumPart(centerWavelength, normalSlice,
                                getContiniumIntensity(inParameters,
                                        centerWavelength, normalSlice)));
                centerWavelength += normalSlice;
                sliceUpper += normalSlice;
            } while ((sliceUpper < maxWavelength) && (sliceUpper < wlBelowL1edge));
        }
        if (sliceUpper < maxWavelength) {
            // Go up to L1 edge
            double slice = wlBelowL1edge - (centerWavelength - (normalSlice / 2.0d));
            centerWavelength = wlBelowL1edge - (slice / 2.0d);
            if (slice > MINIMUM_SLICE) {
                outputData.addContiniumSlice(
                        new SpectrumPart(centerWavelength, slice,
                                getContiniumIntensity(inParameters,
                                        centerWavelength, slice)));
            }
            // Restart at L1 edge
            centerWavelength = wlAboveL1edge + (normalSlice / 2.0d);
            sliceUpper = centerWavelength + (normalSlice / 2.0d);
            // Add continium slices until slice upper wavelength is above L2 edge
            // or max ordered wavelength
            if (sliceUpper < wlBelowL2edge) {
                do {
                    outputData.addContiniumSlice(
                            new SpectrumPart(centerWavelength, normalSlice,
                                    getContiniumIntensity(inParameters,
                                            centerWavelength, normalSlice)));
                    centerWavelength += normalSlice;
                    sliceUpper += normalSlice;
                } while ((sliceUpper < maxWavelength) && (sliceUpper < wlBelowL2edge));
            }
        }
        if (sliceUpper < maxWavelength) {
            // Go up to L2 edge
            double slice = wlBelowL2edge - (centerWavelength - (normalSlice / 2.0d));
            centerWavelength = wlBelowL2edge - (slice / 2.0d);
            if (slice > MINIMUM_SLICE) {
                outputData.addContiniumSlice(
                        new SpectrumPart(centerWavelength, slice,
                                getContiniumIntensity(inParameters,
                                        centerWavelength, slice)));
            }
            // Restart at L2 edge
            centerWavelength = wlAboveL2edge + (normalSlice / 2.0d);
            sliceUpper = centerWavelength + (normalSlice / 2.0d);
            // Add continium slices until slice upper wavelength is above L3 edge
            // or max ordered wavelength
            if (sliceUpper < wlBelowL3edge) {
                do {
                    outputData.addContiniumSlice(
                            new SpectrumPart(centerWavelength, normalSlice,
                                    getContiniumIntensity(inParameters,
                                            centerWavelength, normalSlice)));
                    centerWavelength += normalSlice;
                    sliceUpper += normalSlice;
                } while ((sliceUpper < maxWavelength) && (sliceUpper < wlBelowL3edge));
            }
        }
        if (sliceUpper < maxWavelength) {
            // Go up to L3 edge
            double slice = wlBelowL3edge - (centerWavelength - (normalSlice / 2.0d));
            centerWavelength = wlBelowL3edge - (slice / 2.0d);
            if (slice > MINIMUM_SLICE) {
                outputData.addContiniumSlice(
                        new SpectrumPart(centerWavelength, slice,
                                getContiniumIntensity(inParameters,
                                        centerWavelength, slice)));
            }
            // Restart at L3 edge
            centerWavelength = wlAboveL3edge + (normalSlice / 2.0d);
            sliceUpper = centerWavelength + (normalSlice / 2.0d);
            // Add continium slices until slice upper wavelength max ordered wavelength
            do {
                outputData.addContiniumSlice(
                        new SpectrumPart(centerWavelength, normalSlice,
                                getContiniumIntensity(inParameters,
                                        centerWavelength, normalSlice)));
                centerWavelength += normalSlice;
                sliceUpper += normalSlice;
            } while (sliceUpper < maxWavelength);
        }

        // Calculate tube line intensities
        calculateTubeLineIntensities(inParameters, outputData);

        // Adjust intensities depending on window and filter attenuation
        windowFilterAdjustment(inParameters, outputData);

        // Normalize calculated intensities
        normalizeIntensities(outputData, 1.0d);

        return outputData;
    }
    
    // This method is implemented by the classes extending this class
    protected abstract double getContiniumIntensity(Inparameters inParameters,
            double wavelength, double wavelengthWidth);

//    private double getContiniumIntensity(Inparameters inParameters,
//            double wavelength, double slice) {
//        // Get a mean value of the intensity inside the slice
//        int numAverage = Inparameters.NUM_AVERAGE;
//        double dLambda = slice / (double) (numAverage -1);
//        double aWavelength = wavelength - (slice / 2.0d);
//        double aSum = 0.0d;
//        for (int i = 0; i < numAverage; i++) {
//                aSum += SourceCalculation.this.getContiniumIntensity(inParameters, aWavelength);
//                aWavelength += dLambda;
//            }
//        return (aSum / (double) numAverage);
//    }
    
    // This method is implemented by the classes extending this class
    protected abstract void calculateTubeLineIntensities(
            Inparameters inParameters,
            XraySpectrum outputData);
    
    protected static double getLineWidth(double lineEnergy, double eWidth) {
        // get half of line width in keV
        double halfEwidth = 0.5d * 0.001d * eWidth;
        // Calculate line width in Angstrom
        return ((Inparameters.CONV_KEV_ANGSTROM
                / (lineEnergy - halfEwidth))
                - (Inparameters.CONV_KEV_ANGSTROM
                / (lineEnergy + halfEwidth)));
    }
    
    private static double getWindowTransferFactor(
            int atomicNumber,
            double wavelength, //Angstrom
            double thickness) {  // micrometer
        // Get thickness in cm
        double cmThickness = 0.0001d * thickness;
        double attCoeff = AbsCoefficient
                .getAttenuationCoefficient(atomicNumber, wavelength);
//        double mAbsC = AbsCoefficient.getMassAbsCoefficient(atomicNumber, wavelength);
//        double density = AbsCoefficient.getDensity(atomicNumber);
//        System.out.println("w = " + wavelength + " aAbsC = " + mAbsC +
//                " dens = " + density + " thick = " + cmThickness +
//                " attC = " + attCoeff);
        return Math.exp(-attCoeff * cmThickness);
    }
    
    private static double getFilterTransferFactor(
            List<FilterElement> filterElements,
            double wavelength,
            double thickness) {  // micrometer
        // Calculate attenuation coefficient
        double attCoeff = filterElements.stream()
                .map(fElement -> {
                    int atomicNumber = fElement.getSelectedElement().getAtomicNumber();
                    double conc = fElement.getConc();
                    double attC = AbsCoefficient.getAttenuationCoefficient(atomicNumber, wavelength);
                    return attC * conc;
                })
                .reduce(0.0d, (a, b) -> a + b);
        double cmThickness = 0.0001d * thickness;
        return Math.exp(-attCoeff * cmThickness);
    }
    
    private static void windowFilterAdjustment(
            Inparameters inParameters,
            XraySpectrum outputData) {
        // Find out if there is a filter
        boolean isFilter = true;
        List<FilterElement> filterElems = inParameters.getFilterElements();
        double filterThickness = inParameters.getFilterThickness();
        if (filterElems.isEmpty() || (filterThickness == 0)) isFilter = false;
        final boolean isFilt = isFilter;
        // Get tube window atomic number and thickness
        int windowZ = inParameters.getWindowElement().getAtomicNumber();
        double windowThickness = inParameters.getWindowThickness();
        // First adjust the tube lines
        outputData.getTubeLines().stream()
                .forEach(sPart ->{
                    double waveLength = sPart.getWavelength();
                    double wTrans = getWindowTransferFactor(windowZ, waveLength, windowThickness);
                    double fTrans = 1.0d;
                    if (isFilt) {
                        fTrans = getFilterTransferFactor(filterElems, waveLength, filterThickness);
                    }
//                    System.out.printf("w = %.3f wT = %.3f %.4e\n", waveLength, wTrans, fTrans);
                    sPart.setIntensity(sPart.getIntensity() * wTrans * fTrans);
                });
        // Adjust continium slices
         outputData.getContinium().stream()
                .forEach(sPart ->{
                    double waveLength = sPart.getWavelength();
                    double wTrans = getWindowTransferFactor(windowZ, waveLength, windowThickness);
                    double fTrans = 1.0d;
                    if (isFilt) {
                        fTrans = getFilterTransferFactor(filterElems, waveLength, filterThickness);
                    }
//                    System.out.printf("w = %.3f wT = %.3f %.4e\n", waveLength, wTrans, fTrans);
                    sPart.setIntensity(sPart.getIntensity() * wTrans * fTrans);
                });
    }
    
    private static void normalizeIntensities(XraySpectrum outputData, double maxPeak) {
        
        // Get max integrated tube line intensity
        
        double mPeak = outputData.getTubeLines().stream()
                .map(sPart -> sPart.getIntensity() * sPart.getWindow())
                .max(Double::compare)
                .get();
        
        // Get factor for normalizing
        
        double normFac = maxPeak / mPeak;
     
//        System.out.println("normFac = " + normFac);
//        outputData.getTubeLines().stream()
//                .forEach(sPart -> {
//                    double wavelength = sPart.getWavelength();
//                    double intensity = sPart.getIntensity();
//                    double linewidth = sPart.getWindow();
//                    System.out.println("w = " + wavelength + " lw = " + linewidth
//                            + " i = " + intensity + " total = " + intensity * linewidth);
//                });
        
        // Normalize peaks
        
        outputData.getTubeLines().stream()
                .forEach(sPart -> sPart.setIntensity(normFac * sPart.getIntensity()));
        
        // Normalize continium slices
        
         outputData.getContinium().stream()
                .forEach(sPart -> sPart.setIntensity(normFac * sPart.getIntensity()));
    }
}
