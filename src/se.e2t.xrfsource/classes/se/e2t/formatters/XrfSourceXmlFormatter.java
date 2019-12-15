/*
 * XrfSourceXmlFormatter.java
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
package se.e2t.formatters;
import java.io.StringWriter;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import se.e2t.xrfsource.spectrumclasses.SpectrumPart;
import se.e2t.xrfsource.spectrumclasses.XraySpectrum;
import se.e2t.xrfsource.format.spi.SpectrumFormatSPI;
import javax.xml.transform.Transformer;
import org.w3c.dom.Document;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Class formats spectrum data according to an xrfSource xml format.
 * Class implements the interface of the SpectrumFormatSPI.

 *
 * @author Kent Ericsson, e2t AB
 */
public class XrfSourceXmlFormatter implements SpectrumFormatSPI {
    private static final String ROOT_TAG = "XrfSourceSpectrum";
    
    private static final String TUBE_LINES_ELEMENT_TAG = "TubeLines";
    private static final String TUBE_LINE_ELEMENT_TAG = "TubeLine";
    private static final String ATTR_WAVELENGTH = "wavelength";
    private static final String ATTR_WIDTH = "width";
    private static final String ATTR_INTENSITY = "intensity";
    
    private static final String CONTINUUM_ELEMENT_TAG = "Continuum";
    private static final String CONTINUUMSLICE_ELEMENT_TAG = "ContinuumSlice";
    
    private static final String DESCRIPTION = "xrfSource xml format";
    private static final String EXTENSIONS = "*.xml";
    private int _errorCode;
    private String _errorDescription;
    
    public void XrfSourceFormatter() {
        _errorCode = 0;
        _errorDescription = null;
    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    public String getExtensions() {
        return EXTENSIONS;
    }

    @Override
    public byte[] createByteArray(XraySpectrum spectrum) {
        Transformer trans = null;
        Document doc = null;
        StringWriter sw = null;
        String xmlString;
        /*
         * Get a DOM document
         */
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            _errorCode = 1;
            _errorDescription = "Create document builder failed";
        }
        if (_errorCode == 0) {
            doc = docBuilder.newDocument();
            doc.setXmlStandalone(true); // To be able to control validation later

            // Add a comment
            Comment comm = doc.createComment("This parameter file "
                    + "was created by program xrfSource " + new Date());
            doc.appendChild(comm);

            // Add the root element
            Element root = doc.createElement(ROOT_TAG);
            doc.appendChild(root);
            
            // Add TubeLines element tag
            Element tubelinesElement = doc.createElement(TUBE_LINES_ELEMENT_TAG);
            root.appendChild(tubelinesElement);
            // Add child elements for each tube line
            for (SpectrumPart tubeLine : spectrum.getTubeLines()) {
                Element tElement = doc.createElement(TUBE_LINE_ELEMENT_TAG);
                tElement.setAttribute(ATTR_WAVELENGTH, String.format("%.3f",
                        tubeLine.getWavelength()));
                tElement.setAttribute(ATTR_WIDTH, String.format("%.2e",
                        tubeLine.getWindow()));
                 tElement.setAttribute(ATTR_INTENSITY, String.format("%.2e",
                        tubeLine.getIntensity()));
                tubelinesElement.appendChild(tElement);
            }
            
            // Continuum TubeLines element tag
            Element continuumElement = doc.createElement(CONTINUUM_ELEMENT_TAG);
            root.appendChild(continuumElement);
            // Add child elements for each continuum slice
            for (SpectrumPart cSlice : spectrum.getContinuum()) {
                Element csElement = doc.createElement(CONTINUUMSLICE_ELEMENT_TAG);
                csElement.setAttribute(ATTR_WAVELENGTH, String.format("%.3f",
                        cSlice.getWavelength()));
                csElement.setAttribute(ATTR_WIDTH, String.format("%.2e",
                        cSlice.getWindow()));
                csElement.setAttribute(ATTR_INTENSITY, String.format("%.2e",
                        cSlice.getIntensity()));
                continuumElement.appendChild(csElement);
            }  
            
            // Output the DOM object to a string and feed this string
            // through a transformer to adjust output
            TransformerFactory transfac = TransformerFactory.newInstance();
            transfac.setAttribute("indent-number", 3);
            try {
                trans = transfac.newTransformer();
            } catch (TransformerConfigurationException ex) {
                _errorCode = 2;
                _errorDescription = "Create transformer failed";
            }
        }
        if (_errorCode == 0) {

            // Adjust transformer parameters
            trans.setOutputProperty(OutputKeys.INDENT, "yes");
            trans.setOutputProperty(OutputKeys.ENCODING, "ISO-8859-1");
            trans.setOutputProperty(OutputKeys.STANDALONE, "yes");
            trans.setOutputProperty(OutputKeys.METHOD, "xml");

            // Create streamWriter object from the DOM object after passing transformer
            sw = new StringWriter();
            StreamResult result = new StreamResult(sw);
            DOMSource source = new DOMSource(doc);
            try {
                trans.transform(source, result);
            } catch (TransformerException ex) {
                _errorCode = 3;
                _errorDescription = "transform of DOM document failed";
            }
        }
        if (_errorCode == 0) {

            // Convert transformer output to String
            xmlString = sw.toString();
            xmlString = xmlString.replace("><", ">\n<"); // To get comment on own line

            // Convert String to byte array
            
            return xmlString.getBytes();
        }
        else {
            return null;
        }
    }

    @Override
    public int getErrorCode() {
        return _errorCode;
    }

    @Override
    public String getErrorDescription() {
        return _errorDescription;
    }
}
