package com.b0ve.sig.utils;

import com.b0ve.sig.utils.exceptions.SIGException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import net.sf.saxon.s9api.Processor;
import net.sf.saxon.s9api.SaxonApiException;
import net.sf.saxon.s9api.Serializer;
import net.sf.saxon.s9api.Xslt30Transformer;
import net.sf.saxon.s9api.XsltCompiler;
import net.sf.saxon.s9api.XsltExecutable;
import org.atteo.xmlcombiner.XmlCombiner;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLTools {

    /**
     * XML String to W3C Document
     *
     * @param xml XML String
     * @return W3C Document
     * @throws SIGException
     */
    public static Document parse(String xml) throws SIGException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            return db.parse(new InputSource(new StringReader(xml)));
        } catch (SAXException | ParserConfigurationException | IOException ex) {
            throw new SIGException("Error parsing XML", xml, ex);
        }
    }

    /**
     * W3C Document to XML String
     *
     * @param doc W3C Document
     * @return XML String
     */
    public static String serialize(Document doc) {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer t = tf.newTransformer();
            StringWriter sw = new StringWriter();
            t.transform(new DOMSource(doc), new StreamResult(sw));
            return sw.toString();
        } catch (TransformerException ex) {
            return "Malformed document";
        }
    }

    public static XPathExpression compile(String expresion) throws SIGException {
        try {
            XPathFactory xf = XPathFactory.newInstance();
            XPath x = xf.newXPath();
            return x.compile(expresion);
        } catch (XPathExpressionException ex) {
            throw new SIGException("Error compiling XPath", expresion, ex);
        }
    }

    /**
     * Evaluates an Xpath expression in a Document
     *
     * @param doc W3C Document
     * @param expresion XPath expression
     * @return NodeList
     * @throws SIGException
     */
    public static NodeList eval(Document doc, String expresion) throws SIGException {
        try {
            XPathFactory xf = XPathFactory.newInstance();
            XPath x = xf.newXPath();
            return (NodeList) x.evaluate(expresion, doc, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            throw new SIGException("Error evaluating XPath", new String[]{expresion, serialize(doc)}, ex);
        }
    }

    /**
     * Evaluates a precompiled Xpath expression in a Document
     *
     * @param doc W3C Document
     * @param expresion XPath expression
     * @return NodeList
     * @throws SIGException
     */
    public static NodeList eval(Document doc, XPathExpression expresion) throws SIGException {
        try {
            return (NodeList) expresion.evaluate(doc, XPathConstants.NODESET);
        } catch (XPathExpressionException ex) {
            throw new SIGException("Error evaluating XPath", new String[]{expresion.toString(), serialize(doc)}, ex);
        }
    }

    /**
     * Evaluates an Xpath expression in a Document
     *
     * @param doc W3C Document
     * @param expresion XPath expression
     * @return String
     * @throws SIGException
     */
    public static String evalString(Document doc, String expresion) throws SIGException {
        try {
            XPathFactory xf = XPathFactory.newInstance();
            XPath x = xf.newXPath();
            return (String) x.evaluate(expresion, doc, XPathConstants.STRING);
        } catch (XPathExpressionException ex) {
            throw new SIGException("Error evaluating XPath", new String[]{expresion, serialize(doc)}, ex);
        }
    }

    /**
     * Evaluates a precompiled Xpath expression in a Document
     *
     * @param doc W3C Document
     * @param expresion XPath expression
     * @return String
     * @throws SIGException
     */
    public static String evalString(Document doc, XPathExpression expresion) throws SIGException {
        try {
            return (String) expresion.evaluate(doc, XPathConstants.STRING);
        } catch (XPathExpressionException ex) {
            throw new SIGException("Error evaluating XPath", new String[]{expresion.toString(), serialize(doc)}, ex);
        }
    }

    /**
     * Evaluates an Xpath expression in a Document with default value
     *
     * @param doc W3C Document
     * @param expresion XPath expression
     * @param def Default value
     * @return String
     * @throws SIGException
     */
    public static String evalString(Document doc, String expresion, String def) throws SIGException {
        try {
            XPathFactory xf = XPathFactory.newInstance();
            XPath x = xf.newXPath();
            String val = (String) x.evaluate(expresion, doc, XPathConstants.STRING);
            if (val == null || val.isEmpty()) {
                return def;
            } else {
                return val;
            }
        } catch (XPathExpressionException ex) {
            throw new SIGException("Error evaluating XPath", new String[]{expresion, serialize(doc)}, ex);
        }
    }

    /**
     * Evaluates a precompiled Xpath expression in a Document with default value
     *
     * @param doc W3C Document
     * @param expresion XPath expression
     * @param def Default value
     * @return String
     * @throws SIGException
     */
    public static String evalString(Document doc, XPathExpression expresion, String def) throws SIGException {
        try {
            String val = (String) expresion.evaluate(doc, XPathConstants.STRING);
            if (val == null || val.isEmpty()) {
                return def;
            } else {
                return val;
            }
        } catch (XPathExpressionException ex) {
            throw new SIGException("Error evaluating XPath", new String[]{expresion.toString(), serialize(doc)}, ex);
        }
    }

    /**
     * Merges two W3C Document together
     *
     * @param doc1 W3C Document
     * @param doc2 W3C Document
     * @return W3C Document
     * @throws SIGException
     */
    public static Document merge(Document doc1, Document doc2) throws SIGException {
        try {
            XmlCombiner combiner = new XmlCombiner();
            combiner.combine(doc1);
            combiner.combine(doc2);
            return combiner.buildDocument();
        } catch (ParserConfigurationException ex) {
            throw new SIGException("Error merging documents", new Document[]{doc1, doc2}, ex);
        }
    }

    /**
     * Creates a document where root is the node passed by parameter.References
     * are broken in this process.
     *
     * @param node
     * @return W3C Document
     * @throws SIGException
     */
    public static Document node2document(Node node) throws SIGException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document newDocument = db.newDocument();
            Node importedNode = newDocument.importNode(node, true);
            newDocument.appendChild(importedNode);
            return newDocument;
        } catch (ParserConfigurationException ex) {
            throw new SIGException("Could not create parser", node, ex);
        }
    }

    /**
     * Returns first element of Document as a Node
     *
     * @param doc Node
     * @return
     */
    public static Node document2node(Document doc) {
        return doc.getFirstChild();
    }

    /**
     * Returns an identical document to the one passed by parameter
     *
     * @param doc W3C Document
     * @return W3C Document
     * @throws SIGException
     */
    public static Document clone(Document doc) throws SIGException {
        return node2document(document2node(doc));
    }

    /**
     * Applies a XSLTransformation to a document of the message, returns a new
     * document as a result
     *
     * @param doc W3C Document
     * @param style XSLT
     * @return Document
     * @throws SIGException
     */
    public static Document transform(Document doc, String style) throws SIGException {
        try {
            Source xslt = new StreamSource(new StringReader(style)); //El XLT con el formato
            Source text = new StreamSource(new StringReader(serialize(doc))); //El XML con los datos
            StringWriter outWriter = new StringWriter();

            Processor processor = new Processor(false);
            XsltCompiler compiler = processor.newXsltCompiler();
            XsltExecutable stylesheet = compiler.compile(xslt);
            Serializer out = processor.newSerializer(outWriter);
            out.setOutputProperty(Serializer.Property.METHOD, "xml");
            out.setOutputProperty(Serializer.Property.INDENT, "no");
            Xslt30Transformer transformer = stylesheet.load30();
            transformer.transform(text, out);

            StringBuffer sb = outWriter.getBuffer();
            return parse(sb.toString());
        } catch (SaxonApiException ex) {
            throw new SIGException("Error applying transformation", new String[]{style, serialize(doc)}, ex);
        }
    }
}
