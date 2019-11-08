/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package se.e2t.xraymisc;

/**
 *
 * @author Kent
 */
public class XraySourceScanner {

    /**
     * org.w3c.dom.Document document
     */
    org.w3c.dom.Document document;

    /**
     * Create new XraySourceScanner with org.w3c.dom.Document.
     */
    public XraySourceScanner(org.w3c.dom.Document document) {
        this.document = document;
    }

    /**
     * Scan through org.w3c.dom.Document document.
     */
    public void visitDocument() {
        org.w3c.dom.Element element = document.getDocumentElement();
        if ((element != null) && element.getTagName().equals("XraySourceParameters")) {
            visitElement_XraySourceParameters(element);
        }
        if ((element != null) && element.getTagName().equals("AnodeElement")) {
            visitElement_AnodeElement(element);
        }
        if ((element != null) && element.getTagName().equals("WindowElement")) {
            visitElement_WindowElement(element);
        }
        if ((element != null) && element.getTagName().equals("FilterElements")) {
            visitElement_FilterElements(element);
        }
        if ((element != null) && element.getTagName().equals("FilterElement")) {
            visitElement_FilterElement(element);
        }
        if ((element != null) && element.getTagName().equals("Algorithm")) {
            visitElement_Algorithm(element);
        }
    }

    /**
     * Scan through org.w3c.dom.Element named XraySourceParameters.
     */
    void visitElement_XraySourceParameters(org.w3c.dom.Element element) {
        // <XraySourceParameters>
        // element.getValue();
        org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals("programVersion")) {
                // <XraySourceParameters programVersion="???">
                // attr.getValue();
            }
            if (attr.getName().equals("inAngle")) {
                // <XraySourceParameters inAngle="???">
                // attr.getValue();
            }
            if (attr.getName().equals("outAngle")) {
                // <XraySourceParameters outAngle="???">
                // attr.getValue();
            }
            if (attr.getName().equals("windowThickness")) {
                // <XraySourceParameters windowThickness="???">
                // attr.getValue();
            }
            if (attr.getName().equals("filterThickness")) {
                // <XraySourceParameters filterThickness="???">
                // attr.getValue();
            }
            if (attr.getName().equals("anodeVoltage")) {
                // <XraySourceParameters anodeVoltage="???">
                // attr.getValue();
            }
            if (attr.getName().equals("intervalSize")) {
                // <XraySourceParameters intervalSize="???">
                // attr.getValue();
            }
            if (attr.getName().equals("maxWavelength")) {
                // <XraySourceParameters maxWavelength="???">
                // attr.getValue();
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
                    if (nodeElement.getTagName().equals("AnodeElement")) {
                        visitElement_AnodeElement(nodeElement);
                    }
                    if (nodeElement.getTagName().equals("WindowElement")) {
                        visitElement_WindowElement(nodeElement);
                    }
                    if (nodeElement.getTagName().equals("FilterElements")) {
                        visitElement_FilterElements(nodeElement);
                    }
                    if (nodeElement.getTagName().equals("Algorithm")) {
                        visitElement_Algorithm(nodeElement);
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
    void visitElement_AnodeElement(org.w3c.dom.Element element) {
        // <AnodeElement>
        // element.getValue();
        org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals("symbol")) {
                // <AnodeElement symbol="???">
                // attr.getValue();
            }
            if (attr.getName().equals("atomicNumber")) {
                // <AnodeElement atomicNumber="???">
                // attr.getValue();
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
    void visitElement_WindowElement(org.w3c.dom.Element element) {
        // <WindowElement>
        // element.getValue();
        org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals("symbol")) {
                // <WindowElement symbol="???">
                // attr.getValue();
            }
            if (attr.getName().equals("atomicNumber")) {
                // <WindowElement atomicNumber="???">
                // attr.getValue();
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
    void visitElement_FilterElements(org.w3c.dom.Element element) {
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
                    if (nodeElement.getTagName().equals("FilterElement")) {
                        visitElement_FilterElement(nodeElement);
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
    void visitElement_FilterElement(org.w3c.dom.Element element) {
        // <FilterElement>
        // element.getValue();
        org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals("symbol")) {
                // <FilterElement symbol="???">
                // attr.getValue();
            }
            if (attr.getName().equals("atomicNumber")) {
                // <FilterElement atomicNumber="???">
                // attr.getValue();
            }
            if (attr.getName().equals("conc")) {
                // <FilterElement conc="???">
                // attr.getValue();
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
    void visitElement_Algorithm(org.w3c.dom.Element element) {
        // <Algorithm>
        // element.getValue();
        org.w3c.dom.NamedNodeMap attrs = element.getAttributes();
        for (int i = 0; i < attrs.getLength(); i++) {
            org.w3c.dom.Attr attr = (org.w3c.dom.Attr) attrs.item(i);
            if (attr.getName().equals("index")) {
                // <Algorithm index="???">
                // attr.getValue();
            }
            if (attr.getName().equals("description")) {
                // <Algorithm description="???">
                // attr.getValue();
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
    
}
