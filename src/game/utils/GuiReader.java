package game.utils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.badlogic.gdx.files.FileHandle;

import game.gui.Component;
import game.gui.Document;

public class GuiReader {

	
	public static Document readDocument(FileHandle handle) {
		try {

			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			org.w3c.dom.Document doc = dBuilder.parse(handle.read());
			Document document = new Document();

			// optional, but recommended
			// read this -
			// http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
			doc.getDocumentElement().normalize();

			System.out.println("Root element :" + doc.getDocumentElement().getNodeName());

			NodeList nList = doc.getElementsByTagName("document");

			
			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);
				
				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;
					if(eElement.getNodeName().equals("document"))
					{
						NamedNodeMap documentMap = nNode.getAttributes();
						NodeList nodes = eElement.getChildNodes();
						setLocationDataFor(document, documentMap);
						
						for(int k = 0; k < nodes.getLength(); k++)
						{
							Node node = nodes.item(k);
							NamedNodeMap map = node.getAttributes();
							Component component = (Component) Class.forName(node.getNodeName()).newInstance();
							setLocationDataFor(component, map);
							//TODO Add specific function in Component class to specifiy what to do with additinoal attributes.
							//This is definently required for radio button groups and much more.
							document.add(component);
						}
					}
				}
			}
			return document;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static void setLocationDataFor(Component component, NamedNodeMap map)
	{
		for(int i = 0; i < map.getLength(); i++)
		{
			Node attribute = map.item(i);
			String attributeName = attribute.getNodeName();
			if(attributeName.equals("x"))
			{
				component.setX(Float.parseFloat(attribute.getNodeValue()));
			}
			else if(attributeName.equals("y"))
			{
				component.setY(Float.parseFloat(attribute.getNodeValue()));
			}
			else if(attributeName.equals("width"))
			{
				component.setWidth(Float.parseFloat(attribute.getNodeValue()));
			}
			else if(attributeName.equals("height"))
			{
				component.setHeight(Float.parseFloat(attribute.getNodeValue()));
			}
		}
	}
	

}
