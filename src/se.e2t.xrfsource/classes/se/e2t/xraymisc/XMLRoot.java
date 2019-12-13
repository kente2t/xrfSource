/*
 * XMLRoot.java
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.SAXException;

/**
 * Class is used for initial verification of XML files.
 *
 * @author Kent Ericsson, e2t AB
 */
public class XMLRoot {

    private org.w3c.dom.Element _root;
    private NamedNodeMap _nnm;

    public XMLRoot() {
        _root = null;
        _nnm = null;
    }

    /**
     * Method to input XML file root data
     *
     * @param xmlFile = The XML file
     * @return = 0 if no error occured, else a positive error code is returned.
     */
    public int readFile(File xmlFile) {
        int retval = 0;
        InputStream is = null;
        try {
            is = new FileInputStream(xmlFile);
        } catch (FileNotFoundException ex) {
            retval = 1;
        }
        if (retval == 0) {
            retval = readFile(is);
        }
        return retval;
    }

    /**
     * Method to input XML file root data
     *
     * @param inputStream = Stream of XML data.
     * @return = 0 if no error occured, else a positive error code is returned.
     */
    @SuppressWarnings("null")
    private int readFile(InputStream is) {
        int retval = 0;
        DocumentBuilder db = null;
        Document d = null;
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            db = dbf.newDocumentBuilder();
        } catch (ParserConfigurationException ex) {
            retval = 2;
        }
        if (retval == 0) {
            try {
                d = db.parse(is);
            } catch (SAXException ex) {
                retval = 3;
            } catch (IOException ex) {
                retval = 4;
            }
        }
        if (retval == 0) {
            _root = d.getDocumentElement();
            _nnm = _root.getAttributes();
        }

        return retval;
    }

    /**
     * Method to get the root tag name.
     *
     * @return = root tag name
     */
    public String getRootTagName() {
        if (_root != null) {
            return _root.getNodeName();
        } else {
            return null;
        }
    }

    /**
     * Method to get the value a root tag attribute.
     *
     * @param attributeName = attribute to look for
     * @return = string containing attribute value or null
     */
    public String getAttributeValue(String attributeName) {
        String retval = null;
        if (_nnm != null) {
            for (int i = 0; i < _nnm.getLength() && retval == null; i++) {
                if (attributeName.compareTo(_nnm.item(i).getNodeName()) == 0) {
                    retval = _nnm.item(i).getNodeValue();
                }
            }
        }
        return retval;
    }
}
