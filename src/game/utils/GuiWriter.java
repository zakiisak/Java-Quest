package game.utils;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import game.gui.Component;
import game.gui.Document;
import game.gui.Document.LocalComponent;

public class GuiWriter {

	public static void writeDocument(Document document, String path) {
		try {
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

			// root elements
			org.w3c.dom.Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement("document");
			{
				Attr attr = doc.createAttribute("x");
				attr.setValue("" + document.getX());
				rootElement.setAttributeNode(attr);
			}
			{
				Attr attr = doc.createAttribute("y");
				attr.setValue("" + document.getY());
				rootElement.setAttributeNode(attr);
			}
			{
				Attr attr = doc.createAttribute("width");
				attr.setValue("" + document.getWidth());
				rootElement.setAttributeNode(attr);
			}
			{
				Attr attr = doc.createAttribute("height");
				attr.setValue("" + document.getHeight());
				rootElement.setAttributeNode(attr);
			}
			
			doc.appendChild(rootElement);
			
			for(LocalComponent localComponent : document.getComponents())
			{
				Component component = localComponent.component;
				Element element = doc.createElement(component.getClass().getName());
				rootElement.appendChild(element);

				// set attribute to staff element
				{
					Attr attr = doc.createAttribute("x");
					attr.setValue("" + localComponent.x);
					element.setAttributeNode(attr);
				}
				{
					Attr attr = doc.createAttribute("y");
					attr.setValue("" + localComponent.y);
					element.setAttributeNode(attr);
				}
				{
					Attr attr = doc.createAttribute("width");
					attr.setValue("" + component.getWidth());
					element.setAttributeNode(attr);
				}
				{
					Attr attr = doc.createAttribute("height");
					attr.setValue("" + component.getHeight());
					element.setAttributeNode(attr);
				}
			}

			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(path));

			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);

			transformer.transform(source, result);

			System.out.println("File saved!");

		} catch (ParserConfigurationException pce) {
			pce.printStackTrace();
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
		}
	}

}
