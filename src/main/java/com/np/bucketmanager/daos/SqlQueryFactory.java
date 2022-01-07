package com.np.bucketmanager.daos;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SqlQueryFactory {

    private static Map<String, Map<String, String>> queryMap = new HashMap<>();

    public static String getQuery() {
        if (queryMap.size() == 0) {
            init();
        }
        return null;
    }

    private static void init() {
        try {
            Path path = Paths.get(ClassLoader.getSystemResource("com/np/bucketmanager/daos/resources").toURI());

            // TODO - directory is not currently expected
            long numberOfDirs = Files.walk(path).filter(Files::isDirectory).count();

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

            Files.walk(path).filter(Files::isRegularFile).forEach(p -> {
                try {
                    Element root = documentBuilder.parse(p.toFile()).getDocumentElement();
                    XPath xPath = XPathFactory.newInstance().newXPath();
                    NodeList nodes = (NodeList) xPath.evaluate("/named-queries/query[@name]", root, XPathConstants.NODESET);

                    for (int i = 0; i < nodes.getLength(); i++) {
                        Element e = (Element) nodes.item(i);
                        String queryName = e.getAttribute("name");
                        String query = e.getTextContent();

                        System.out.println(queryName + ": " + query);
                    }
                } catch (SAXException | IOException | XPathExpressionException e) {
                    e.printStackTrace();
                }
            });

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }
}
