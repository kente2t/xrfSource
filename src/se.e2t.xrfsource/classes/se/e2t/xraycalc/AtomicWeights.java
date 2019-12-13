/*
 * AtomicWeights.java
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

/**
 *
 * @author Kent Ericsson, e2t AB.
 * 
 * Data for this static class has been copied from the NIST database:
 * Atomic Weights and Isotopic Compositions with Relative Atomic Masses
 * Developers and Contributors of the database:
 * J.S. Coursey, D. J. Schwab, J. J. Tsai, and R. A. Dragoset
 * NIST Physical Measurement Laboratory
 * 
 */
public class AtomicWeights {
    
    final static double REL_WEIGHTS[] = {
        1.00782503223d, //1
        3.0160293201d, //2
        6.0151228874d, //3
        9.012183065d, //4
        10.01293695d, //5
        12.0000000d, //6
        14.00307400443d, //7
        15.99491461957d, //8
        18.99840316273d, //9
        19.9924401762d, //10
        22.9897692820d,  //11
        23.985041697d, //12
        26.98153853d, //13
        27.97692653465d, //14
        30.97376199842d, //15
        31.9720711744d, //16
        34.968852682d, //17
        35.967545105d, //18
        38.9637064864d, //19
        39.962590863d, //20
        44.95590828d, //21
        45.95262772d, //22
        49.94715601d, //23
        49.94604183d, //24
        54.93804391d, //25
        53.93960899d, //26
        58.93319429d, //27
        57.93534241d, //28
        62.92959772d, //29
        63.92914201d, //30
        68.9255735d, //31
        69.92424875d, //32
        74.92159457d, //33
        73.922475934d, //34
        78.9183376d, //35
        77.92036494d, //36
        84.9117897379d, //37
        83.9134191d, //38
        88.9058403d,  //39
        89.9046977d, //40
        92.9063730d, //41
        91.90680796d, //42
        96.9063667d, //43
        95.90759025d, //44
        102.9054980d, //45
        101.9056022d, //46
        106.9050916d, //47
        105.9064599d, //48
        112.90406184d, //49
        111.90482387d, //50
        120.9038120d, //51
        119.9040593d, //52
        126.9044719d, //53
        123.9058920d, //54
        132.9054519610d,  //55
        129.9063207d, //56
        137.9071149d, //57
        135.90712921d, //58
        140.9076576d, //59
        141.9077290d, //60
        144.9127559d, //61
        143.9120065d, //62
        150.9198578d, //63
        151.9197995d, //64
        158.9253547d, //65
        155.9242847d, //66
        164.9303288d, //67
        161.9287884d, //68
        168.9342179d, //69
        167.9338896d, //70
        174.9407752d, //71
        173.9400461d, //72
        179.9474648d, //73
        179.9467108d, //74
        184.9529545d, //75
        183.9524885d, //76
        190.9605893d, ////
        189.9599297d, //78
        196.96656879d, //79
        195.9658326d, //80
        202.9723446d, //81
        203.9730440d, //82
        208.9803991d, //83
        208.9824308d, //84
        209.9871479d, //85
        210.9906011d, //86
        223.0197360d, //87
        223.0185023d, //88
        227.0277523d,  //89
        230.0331341d, //90
        231.0358842d, //91
        233.0396355d, //92
        236.046570d, //93
        238.0495601d, //94
        241.0568293d, //95
        243.0613893d, //96
        247.0703073d, //97
        249.0748539d, //98
        252.082980d, //99
        257.0951061d /100  
    };
    
    static double getRelAtomicWeight(int atomZ) {
        return REL_WEIGHTS[atomZ - 1];
    }
}
