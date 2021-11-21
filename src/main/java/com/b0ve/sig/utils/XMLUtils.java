package com.b0ve.sig.utils;

import com.b0ve.sig.flow.Message;
import com.b0ve.sig.utils.exceptions.SIGException;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
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
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class XMLUtils {

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

    /**
     * Creates a W3C Document from ResultSet given by SQL DB.
     *
     * @param rs
     * @return
     * @throws ParserConfigurationException
     * @throws SQLException
     */
    public static Document rs2doc(ResultSet rs) throws ParserConfigurationException, SQLException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.newDocument();

        Element results = doc.createElement("Results");
        doc.appendChild(results);

        ResultSetMetaData rsmd = rs.getMetaData();
        int colCount = rsmd.getColumnCount();

        while (rs.next()) {
            Element row = doc.createElement("Row");
            results.appendChild(row);

            for (int i = 1; i <= colCount; i++) {
                String columnName = rsmd.getColumnName(i);
                Object value = rs.getObject(i);

                Element node = doc.createElement(columnName);
                node.appendChild(doc.createTextNode(value.toString()));
                row.appendChild(node);
            }
        }
        return doc;
    }

    /**
     * Converts an xml document to JSON string
     *
     * @param doc W3C Document
     * @return JSON
     */
    public static String doc2json(Document doc) {
        return doc2json(XMLUtils.serialize(doc));
    }

    /**
     * Converts an xml string to JSON string
     *
     * @param xml W3C Document
     * @return JSON
     */
    public static String doc2json(String xml) {
        JSONObject xmlJSONObj = XML.toJSONObject(xml);
        return xmlJSONObj.toString(4);
    }

    /**
     * Converts a JSON string to XML Document
     *
     * @param json JSON String
     * @return W3C Document
     * @throws SIGException
     */
    public static Document json2doc(String json) throws SIGException {
        JSONObject jsonObject = new JSONObject(json);
        String responseXML = XML.toString(jsonObject);
        try {
            return XMLUtils.parse(responseXML);
        } catch (Exception e) {
            return XMLUtils.parse("<root>" + responseXML + "</root>");
        }
    }

    /**
     * Splits a message using a precompiled XPath expression
     *
     * @param m
     * @param xpath Precompiled XPath expression
     * @return
     * @throws SIGException
     */
    public static Document[] split(Message m, XPathExpression xpath) throws SIGException {
        NodeList lista = m.eval(xpath);
        Document[] partes = new Document[lista.getLength()];
        for (int i = 0; i < lista.getLength(); i++) {
            partes[i] = XMLUtils.node2document(lista.item(i));
        }
        return partes;
    }

    /**
     * Joins messages on a single root or tags in cascade
     *
     * @param messages Messages to be joint
     * @param rootName String or Array of String
     * @return Document containing messages on root
     * @throws SIGException
     */
    public static Document join(Message[] messages, Object rootName) throws SIGException {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = dbf.newDocumentBuilder();
            Document doc = builder.newDocument();
            Element appendPoint = null;
            if (rootName instanceof String) {
                appendPoint = doc.createElement((String) rootName);
                doc.appendChild(appendPoint);
            } else if (rootName instanceof String[]) {
                String[] rootNames = (String[]) rootName;
                for (String name : rootNames) {
                    Element newPoint = doc.createElement(name);
                    if (appendPoint == null) {
                        doc.appendChild(newPoint);
                    } else {
                        appendPoint.appendChild(newPoint);
                    }
                    appendPoint = newPoint;
                }
            } else {
                appendPoint = doc.createElement("list");
                doc.appendChild(appendPoint);
            }
            for (Message message : messages) {
                Node newChild = XMLUtils.document2node(message.getBody());
                Node imported = doc.importNode(newChild, true);
                appendPoint.appendChild(imported);
            }
            return doc;
        } catch (ParserConfigurationException ex) {
            throw new SIGException("Messages could not be combined", messages, ex);
        }
    }
}
