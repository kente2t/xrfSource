/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

module se.e2t.xrfsource {
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.fxml;
    requires java.base;
    requires java.desktop;
    requires se.e2t.xrfsource.spectrumclasses;
    requires se.e2t.xrfsource.format;
    opens se.e2t.xraygui to javafx.fxml, javafx.graphics;
    opens se.e2t.resources to javafx.fxml, javafx.graphics;
    exports se.e2t.xraygui;
    exports se.e2t.xraycalc;
    uses se.e2t.xrfsource.format.spi.SpectrumFormatSPI;
    provides se.e2t.xrfsource.format.spi.SpectrumFormatSPI
            with se.e2t.formatters.XrfSourceCsvFormatter,
            se.e2t.formatters.XrfSourceXmlFormatter;
}
