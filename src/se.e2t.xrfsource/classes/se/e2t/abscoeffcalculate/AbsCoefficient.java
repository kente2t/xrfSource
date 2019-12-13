/*
 * AbsCoefficient.java
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
package se.e2t.abscoeffcalculate;

import se.e2t.abscoeffcalculate.Mucal.ErrorCode;
import se.e2t.xraycalc.AbsorptionEdges.AbsEdge;
import se.e2t.xraycalc.Inparameters;

/**
 *
 * @author Kent Ericsson, e2t AB
 * 
 * Class handles x-ray absorption data. Methods in Mucal.java are used.
 */
public class AbsCoefficient {
    
    /**
     * Method returns the mass absorption coefficient in cm2/g.
     * @param Z atoic number.
     * @param wavelength wavelength in Angstrom.
     * @return mass absorption coefficient or NaN if error.
     */
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
    
    /**
     * Method returns the attenuation coefficient (unit /cm)
     * @param Z atoic number.
     * @param wavelength wavelength in Angstrom.
     * @return absorption coefficient or NaN if error.
     */
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
    
    /**
     * Method returns the photoelectric mass absorption coefficient.
     * 
     * @param Z atomic numbet
     * @param wavelength wavelength in Angstrom
     * @return photoelectriv mass absorption coefficient or NaN if error.
     */
    public static double getTau(int Z, double wavelength) {
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
    
    /**
     * Method retunr the density of an element.
     * @param Z atomic number
     * @return density in g/cm3 or NaN if error.
     */
    public static double getDensity(int Z) {
        Mucal mc = new Mucal(null, Z, 0.0d, 'C', true);
        ErrorCode ec = mc.calculate();
        if (ec == ErrorCode.no_error) {
            return mc.getXsec()[7];
        } else {
            return Double.NaN;
        }
    }
    
    /**
     * Method retuns the flourescent yield of an element
     * @param Z atomic number
     * @param lEdge the absorption edge, K, L1, L2, L3 or M5.
     * @return flourescent yield ofr NaN if error.
     */
    public static double getLFlourYield(int Z, AbsEdge lEdge) {
        Mucal mc = new Mucal(null, Z, 0.0d, 'C', true);
        ErrorCode ec = mc.calculate();
        double retval = Double.NaN;
        if (ec == ErrorCode.no_error) {
            switch (lEdge) {
                case L1_EDGE:
                    retval = mc.getFl_yield()[1];
                    break;
                case L2_EDGE:
                    retval = mc.getFl_yield()[2];
                    break;
                case L3_EDGE:
                    retval = mc.getFl_yield()[3];
            }
        }
        return retval;
    }
}
