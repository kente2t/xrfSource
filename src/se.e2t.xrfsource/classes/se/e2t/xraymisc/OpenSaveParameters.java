/*
 * OpenSaveParameters.java
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
package se.e2t.xraymisc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;
import java.util.Optional;
import javafx.scene.control.Alert;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import se.e2t.xraycalc.FilterElement;
import se.e2t.xraycalc.Inparameters;
import se.e2t.xraycalc.Inparameters.Algorithm;

/**
 * Class handles open and save of xrfSource parameters from/to an XML file.
 *
 * @author Kent Ericsson, e2t AB
 */
public class OpenSaveParameters {

    private static final String ROOT_TAG = "XraySourceParameters";
    private static final String ATTR_VERS = "programVersion";
    private static final String ATTR_INANGLE = "inAngle";
    private static final String ATTR_OUTANGLE = "outAngle";
    private static final String ATTR_WINDOW_THICKNESS = "windowThickness";
    private static final String ATTR_FILTER_THICKNESS = "filterThickness";
    private static final String ATTR_ANODE_VOLTAGE = "anodeVoltage";
    private static final String ATTR_INTERVAL_SIZE = "intervalSize";
    private static final String ATTR_SPLIT_INTERVAL_AT_EDGE = "splitAtEdge";
    private static final String ATTR_MAX_WAVELENGTH = "maxWavelength";

    private static final String ANODE_ELEMENT_TAG = "AnodeElement";
    private static final String ATTR_SYMBOL = "symbol";
    private static final String ATTR_ATOMIC_NUMBER = "atomicNumber";

    private static final String WINDOW_ELEMENT_TAG = "WindowElement";

    private static final String FILTER_ELEMENTS_TAG = "FilterElements";
    private static final String FILTER_ELEMENT_TAG = "FilterElement";
    private static final String ATTR_CONC = "conc";

    private static final String ALGORITHM_ELEMENT_TAG = "Algorithm";
    private static final String ATTR_CALCMODEL = "calcModel";
    private static final String ATTR_DESCRIPTION = "description";

    /**
     * Method reads parameters from an xml parameter file into an InParameter
     * object. Most of the code aws produced atomatically via a dcd file produced 
     * by Netbeans from an xml file and then the dcd file was used to produce
     * input scanner code.
     * 
     *@param file input file
     * @param parameters reference to parameter storage.
     */
    @SuppressWarnings("null")
    public static void openParameters(Inparameters parameters, File file) {
        DocumentBuilder builder;
        Document document;

        // Verify file root tag
        XMLRoot fileRoot;
        fileRoot = new XMLRoot();
        if (fileRoot.readFile(file) != 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Sorry, could not verify file type, an error occured!");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        String rootName;
        rootName = fileRoot.getRootTagName();
        if (rootName.compareTo(ROOT_TAG) != 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Sorry, root tag " + rootName + " is not correct for an xrfSource parameter file!");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        // Verify version of program that created file
        String pgmVersion;
        pgmVersion = fileRoot.getAttributeValue(ATTR_VERS);
        if (pgmVersion == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Sorry, could not verify version of program that created file!");
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        
        // If file produced by version 1.0.0 then preset new version 2.0 parameter value
        if (pgmVersion.equals("1.0.0")) {
            parameters.setSplitAtAbsEdge(true);
        }

        // Read XML into DOM object
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();

        try {
            builder = builderFactory.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Create document builder failed!\n"
                    + ex);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }
        try {
            document = builder.parse(new InputSource(new FileInputStream(file)));
        } catch (SAXException | IOException ex) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "XML parse failed!\n" + ex);
            alert.setHeaderText(null);
            alert.showAndWait();
            return;
        }

        // read DOM info into parameter object
        visitDocument(document, parameters);
    }

    private static void visitDocument(Document document, Inparameters parameters) {
        org.w3c.dom.Element element = document.getDocumentElement();
        if ((element != null) && element.getTagName().equals(ROOT_TAG)) {
            visitElement_XraySourceParameters(element, parameters);
        }
        if ((element != null) && element.getTagName().equals(ANODE_ELEMENT_TAG)) {
            visitElement_AnodeElement(element, parameters);
        }
        if ((element != null) && element.getTagName().equals(WINDOW_ELEMENT_TAG)) {
            visitElement_WindowElement(element, parameters);
        }
        if ((element != null) && element.getTagName().equals(FILTER_ELEMENTS_TAG)) {
            visitElement_FilterElements(element, parameters);
        }
        if ((element != null) && element.getTagName().equals(FILTER_ELEMENT_TAG)) {
            FilterElement fElement = new FilterElement();
            visitElement_FilterElement(element, fElement);
            parameters.getFilterElements().add(fElement);
        }
        if ((element != null) && element.getTagName().equals(ALGORITHM_ELEMENT_TAG)) {
            visitElement_Algorithm(element, parameters);
        }
    }

    /**
     * Scan through org.w3c.dom.Element named XraySourceParameters.
     */
    private static void visitElement_XraySourceParameters(org.w3c.dom.Element element,
            Inparameters parameters) {
        // <XraySourceParameters>
        // element.getValue();
        org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals(ATTR_VERS)) {
                parameters.setProgramVersion(attr.getValue());
            }
            if (attr.getName().equals(ATTR_INANGLE)) {
                parameters.setInAngle(Double.valueOf(attr.getValue()));
            }
            if (attr.getName().equals(ATTR_OUTANGLE)) {
                parameters.setOutAngle(Double.valueOf(attr.getValue()));
            }
            if (attr.getName().equals(ATTR_WINDOW_THICKNESS)) {
                parameters.setWindowThickness(Double.valueOf(attr.getValue()));
            }
            if (attr.getName().equals(ATTR_FILTER_THICKNESS)) {
                parameters.setFilterThickness(Double.valueOf(attr.getValue()));
            }
            if (attr.getName().equals(ATTR_ANODE_VOLTAGE)) {
                parameters.setTubeVoltage(Double.valueOf(attr.getValue()));
            }
            if (attr.getName().equals(ATTR_INTERVAL_SIZE)) {
                parameters.setContiniumIntervalSize(Double.valueOf(attr.getValue()));
            }
             if (attr.getName().equals(ATTR_SPLIT_INTERVAL_AT_EDGE)) {
                parameters.setSplitAtAbsEdge(Boolean.valueOf(attr.getValue()));
            }
            if (attr.getName().equals(ATTR_MAX_WAVELENGTH)) {
                parameters.setMaxWavelength(Double.valueOf(attr.getValue()));
            }
        }
        org.w3c.dom.NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            switch (node.getNodeType()) {
                case org.w3c.dom.Node.CDATA_SECTION_NODE:
                    // ((org.w3c.dom.CDATASection)node).getData();
                    break;
                case org.w3c.dom.Node.ELEMENT_NODE:
                    org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
                    if (nodeElement.getTagName().equals(ANODE_ELEMENT_TAG)) {
                        visitElement_AnodeElement(nodeElement, parameters);
                    }
                    if (nodeElement.getTagName().equals(WINDOW_ELEMENT_TAG)) {
                        visitElement_WindowElement(nodeElement, parameters);
                    }
                    if (nodeElement.getTagName().equals(FILTER_ELEMENTS_TAG)) {
                        visitElement_FilterElements(nodeElement, parameters);
                    }
                    if (nodeElement.getTagName().equals("Algorithm")) {
                        visitElement_Algorithm(nodeElement, parameters);
                    }
                    break;
                case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                    // ((org.w3c.dom.ProcessingInstruction)node).getTarget();
                    // ((org.w3c.dom.ProcessingInstruction)node).getData();
                    break;
            }
        }
    }

    /**
     * Scan through org.w3c.dom.Element named AnodeElement.
     */
    private static void visitElement_AnodeElement(org.w3c.dom.Element element,
            Inparameters parameters) {
        // <AnodeElement>
        // element.getValue();
        org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals(ATTR_SYMBOL)) {
                parameters.getAnodeElement().setSymbol(attr.getValue());
            }
            if (attr.getName().equals(ATTR_ATOMIC_NUMBER)) {
                parameters.getAnodeElement().setAtomicNumber(Integer.valueOf(attr.getValue()));
            }
        }
        org.w3c.dom.NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            switch (node.getNodeType()) {
                case org.w3c.dom.Node.CDATA_SECTION_NODE:
                    // ((org.w3c.dom.CDATASection)node).getData();
                    break;
                case org.w3c.dom.Node.ELEMENT_NODE:
                    org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
                    break;
                case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                    // ((org.w3c.dom.ProcessingInstruction)node).getTarget();
                    // ((org.w3c.dom.ProcessingInstruction)node).getData();
                    break;
            }
        }
    }

    /**
     * Scan through org.w3c.dom.Element named WindowElement.
     */
    private static void visitElement_WindowElement(org.w3c.dom.Element element,
            Inparameters parameters) {
        // <WindowElement>
        // element.getValue();
        org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals(ATTR_SYMBOL)) {
                parameters.getWindowElement().setSymbol(attr.getValue());
            }
            if (attr.getName().equals(ATTR_ATOMIC_NUMBER)) {
                parameters.getWindowElement().setAtomicNumber(Integer.valueOf(attr.getValue()));
            }
        }
        org.w3c.dom.NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            switch (node.getNodeType()) {
                case org.w3c.dom.Node.CDATA_SECTION_NODE:
                    // ((org.w3c.dom.CDATASection)node).getData();
                    break;
                case org.w3c.dom.Node.ELEMENT_NODE:
                    org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
                    break;
                case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                    // ((org.w3c.dom.ProcessingInstruction)node).getTarget();
                    // ((org.w3c.dom.ProcessingInstruction)node).getData();
                    break;
            }
        }
    }

    /**
     * Scan through org.w3c.dom.Element named FilterElements.
     */
    private static void visitElement_FilterElements(org.w3c.dom.Element element,
            Inparameters parameters) {
        // <FilterElements>
        // element.getValue();
        org.w3c.dom.NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            switch (node.getNodeType()) {
                case org.w3c.dom.Node.CDATA_SECTION_NODE:
                    // ((org.w3c.dom.CDATASection)node).getData();
                    break;
                case org.w3c.dom.Node.ELEMENT_NODE:
                    org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
                    if (nodeElement.getTagName().equals(FILTER_ELEMENT_TAG)) {
                        FilterElement fElement = new FilterElement();
                        visitElement_FilterElement(nodeElement, fElement);
                        parameters.getFilterElements().add(fElement);
                    }
                    break;
                case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                    // ((org.w3c.dom.ProcessingInstruction)node).getTarget();
                    // ((org.w3c.dom.ProcessingInstruction)node).getData();
                    break;
            }
        }
    }

    /**
     * Scan through org.w3c.dom.Element named FilterElement.
     */
    private static void visitElement_FilterElement(org.w3c.dom.Element element,
            FilterElement fElement) {
        // <FilterElement>
        // element.getValue();
        org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals(ATTR_SYMBOL)) {
                fElement.getSelectedElement().setSymbol(attr.getValue());
            }
            if (attr.getName().equals(ATTR_ATOMIC_NUMBER)) {
                fElement.getSelectedElement().setAtomicNumber(Integer.valueOf(attr.getValue()));
            }
            if (attr.getName().equals(ATTR_CONC)) {
                fElement.setConc(Double.valueOf(attr.getValue()));
            }
        }
        org.w3c.dom.NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            switch (node.getNodeType()) {
                case org.w3c.dom.Node.CDATA_SECTION_NODE:
                    // ((org.w3c.dom.CDATASection)node).getData();
                    break;
                case org.w3c.dom.Node.ELEMENT_NODE:
                    org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
                    break;
                case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                    // ((org.w3c.dom.ProcessingInstruction)node).getTarget();
                    // ((org.w3c.dom.ProcessingInstruction)node).getData();
                    break;
            }
        }
    }

    /**
     * Scan through org.w3c.dom.Element named Algorithm.
     */
    private static void visitElement_Algorithm(org.w3c.dom.Element element,
            Inparameters parameters) {
        // <Algorithm>
        // element.getValue();
        parameters.setAlgorithm(Inparameters.getAlgorithms().get(0)); // Compatibility with version 1.0
        org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals(ATTR_CALCMODEL)) {
//                Algorithm alg = Inparameters.getAlgorithms().get(0);
                Optional<Algorithm> alg = Inparameters.getAlgorithms().stream()
                        .filter(algAlt -> algAlt.getCalcModel().toString().equals(attr.getValue()))
                        .findFirst();
                alg.ifPresent(algAlt -> parameters.setAlgorithm(algAlt));
            }
        }
        org.w3c.dom.NodeList nodes = element.getChildNodes();
        for (int i = 0; i < nodes.getLength(); i++) {
            org.w3c.dom.Node node = nodes.item(i);
            switch (node.getNodeType()) {
                case org.w3c.dom.Node.CDATA_SECTION_NODE:
                    // ((org.w3c.dom.CDATASection)node).getData();
                    break;
                case org.w3c.dom.Node.ELEMENT_NODE:
                    org.w3c.dom.Element nodeElement = (org.w3c.dom.Element) node;
                    break;
                case org.w3c.dom.Node.PROCESSING_INSTRUCTION_NODE:
                    // ((org.w3c.dom.ProcessingInstruction)node).getTarget();
                    // ((org.w3c.dom.ProcessingInstruction)node).getData();
                    break;
            }
        }
    }

    // Method to save parameters to XML.
    @SuppressWarnings("null")
    public static void saveParameters(Inparameters parameters, File file) {
        // Add file extension if not .xml
        FileFilter filter = new FileNameExtensionFilter("xml files", "xml");
        if (!filter.accept(file)) {
            String filePath = file.getAbsolutePath() + ".xml";
            file = new File(filePath);
        }
        // Write parameters to XML
        int retv = outputXML(parameters, file);
        if (retv != 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR,
                    "Error saving XML file!\nretval = " + retv);
            alert.setHeaderText(null);
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION,
                    "Parameters have been saved to xml file!");
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    /**
     * Method stores xrfSource parameters in an xml file.
     * @param parameters reference to parameters
     * @param file output file
     * @return = 0 if OK, error code 1-6 if error, se below.
     */
    @SuppressWarnings("null")
    private static int outputXML(Inparameters parameters, File file) {
        FileWriter writer = null;
        Transformer trans = null;
        Document doc = null;
        StringWriter sw = null;
        String xmlString = null;
        int retval = 0;
        
        // Get a DOM document
        DocumentBuilderFactory dbfac = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = null;
        try {
            docBuilder = dbfac.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            retval = 1;
            System.out.println("Create document builder failed" + ex);
        }
        if (retval == 0) {
            doc = docBuilder.newDocument();
            doc.setXmlStandalone(true); // To be able to control validation later

            // Add a comment
            Comment comm = doc.createComment("This parameter file "
                    + "was created by program xrfSource " + new Date());
            doc.appendChild(comm);

            // Add the root element with its attributes
            Element root = doc.createElement(ROOT_TAG);
            root.setAttribute(ATTR_VERS, Inparameters.THIS_PROGRAM_VERSION);
            root.setAttribute(ATTR_INANGLE, String.valueOf(parameters.getInAngle()));
            root.setAttribute(ATTR_OUTANGLE, String.valueOf(parameters.getOutAngle()));
            root.setAttribute(ATTR_WINDOW_THICKNESS, String.valueOf(parameters.getWindowThickness()));
            root.setAttribute(ATTR_FILTER_THICKNESS, String.valueOf(parameters.getFilterThickness()));
            root.setAttribute(ATTR_ANODE_VOLTAGE, String.valueOf(parameters.getTubeVoltage()));
            root.setAttribute(ATTR_INTERVAL_SIZE, String.valueOf(parameters.getContinuumIntervalSize()));
            root.setAttribute(ATTR_SPLIT_INTERVAL_AT_EDGE, String.valueOf(parameters.isSplitAtAbsEdge()));
            root.setAttribute(ATTR_MAX_WAVELENGTH, String.valueOf(parameters.getMaxWavelength()));
            doc.appendChild(root);

            // Add anode element info
            Element aElement = doc.createElement(ANODE_ELEMENT_TAG);
            aElement.setAttribute(ATTR_SYMBOL, parameters.getAnodeElement().getSymbol());
            aElement.setAttribute(ATTR_ATOMIC_NUMBER, String.valueOf(parameters.getAnodeElement().getAtomicNumber()));
            root.appendChild(aElement);

            // Add window element info
            Element wElement = doc.createElement(WINDOW_ELEMENT_TAG);
            wElement.setAttribute(ATTR_SYMBOL, parameters.getWindowElement().getSymbol());
            wElement.setAttribute(ATTR_ATOMIC_NUMBER, String.valueOf(parameters.getWindowElement().getAtomicNumber()));
            root.appendChild(wElement);

            // Add Filter elements
            Element fElements = doc.createElement(FILTER_ELEMENTS_TAG);
            root.appendChild(fElements);

            // Add a child element for each of the filter elements
            for (FilterElement fElem : parameters.getFilterElements()) {
                Element fElement = doc.createElement(FILTER_ELEMENT_TAG);
                fElement.setAttribute(ATTR_SYMBOL, fElem.getSelectedElement().getSymbol());
                fElement.setAttribute(ATTR_ATOMIC_NUMBER,
                        String.valueOf(fElem.getSelectedElement().getAtomicNumber()));
                fElement.setAttribute(ATTR_CONC, String.valueOf(fElem.getConc()));
                fElements.appendChild(fElement);
            }

            // Add algorithm element info
            Element algElement = doc.createElement(ALGORITHM_ELEMENT_TAG);
            algElement.setAttribute(ATTR_CALCMODEL, parameters.getAlgorithm().getCalcModel().toString());
            algElement.setAttribute(ATTR_DESCRIPTION,
                    parameters.getAlgorithm().getDescription());
            root.appendChild(algElement);

            // Output the DOM object to a string and feed this string
            // through a transformer to adjust output
            TransformerFactory transfac = TransformerFactory.newInstance();
            transfac.setAttribute("indent-number", 3);
            try {
                trans = transfac.newTransformer();
            } catch (TransformerConfigurationException ex) {
                retval = 2;
                System.out.println("Create transformer failed" + ex);
            }
        }
        if (retval == 0) {

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
                retval = 3;
                System.out.println("transform of DOM document failed" + ex);
            }
        }
        if (retval == 0) {

            // Convert transformer output to String
            xmlString = sw.toString();
            xmlString = xmlString.replace("><", ">\n<"); // To get comment on own line

            // Create and open the FileWriter
            try {
                writer = new FileWriter(file);
            } catch (IOException ex) {
                retval = 4;
                System.out.println("Creating a FileWriter failed" + ex);
            }
        }
        if (retval == 0) {

            // Output the XML to the file
            try {
                writer.append((CharSequence) xmlString);
            } catch (IOException ex) {
                retval = 5;
                System.out.println("Append of XML to FileWriter failed" + ex);
            }
        }
        if (retval == 0) {

            //* Close file
            try {
                writer.close();
            } catch (IOException ex) {
                retval = 6;
                System.out.println("Close of the FileWriter failed" + ex);
            }
        }

        //* Exit
        return retval;
    }
}
