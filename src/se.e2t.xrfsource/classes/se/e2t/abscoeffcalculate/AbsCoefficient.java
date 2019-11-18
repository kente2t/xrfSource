/*
 * AbsCoefficient.java
 */
package se.e2t.abscoeffcalculate;

import se.e2t.abscoeffcalculate.Mucal.ErrorCode;
import se.e2t.xraycalc.Inparameters;

/**
 *
 * @author Kent Ericsson, e2t AB
 */
public class AbsCoefficient {
    
    // Method returns the mass absorption coefficient in cm2/g.
    
    public static double getMassAbsCoefficient(int Z, double wavelength) {
        double energy = Inparameters.CONV_KEV_ANGSTROM / wavelength;
        Mucal mc = new Mucal(null, Z, energy, 'C', true);
        ErrorCode ec = mc.calculate();
        if (ec == Mucal.ErrorCode.within_edge) {
            // Decrease energy 3 eV if to close to absorption edge
            energy = energy - 0.003d;
            mc.setEphot(energy);
        }
        ec = mc.calculate();

        if (ec == ErrorCode.no_error) {
            return mc.getXsec()[3];
        } else {
            return Double.NaN;
        }
    }
    
    // Method returns the attenuation coefficient (unit /cm)
    
    public static double getAttenuationCoefficient(int Z, double wavelength) {
        double energy = Inparameters.CONV_KEV_ANGSTROM / wavelength;
        Mucal mc = new Mucal(null, Z, energy, 'C', true);
        ErrorCode ec = mc.calculate();
        if (ec == Mucal.ErrorCode.within_edge) {
            // Decrease energy 3 eV if to close to absorption edge
            energy = energy - 0.003d;
            mc.setEphot(energy);
        }
        ec = mc.calculate();
        if (ec == ErrorCode.no_error) {
            return mc.getXsec()[5];
        } else {
            return Double.NaN;
        }
    }
    
    public static double getThau(int Z, double wavelength) {
        double energy = Inparameters.CONV_KEV_ANGSTROM / wavelength;
        Mucal mc = new Mucal(null, Z, energy, 'C', true);
        ErrorCode ec = mc.calculate();
        if (ec == Mucal.ErrorCode.within_edge) {
            // Decrease energy 3 eV if to close to absorption edge
            energy = energy - 0.003d;
            mc.setEphot(energy);
        }
        ec = mc.calculate();

        if (ec == ErrorCode.no_error) {
            return mc.getXsec()[0];
        } else {
            return Double.NaN;
        }
    }
}
