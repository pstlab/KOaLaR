package it.cnr.istc.pst.cognition.koala.reasoner.environment.parser.xml;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.cnr.istc.pst.cognition.koala.reasoner.environment.parser.EnvironmentConfigurationParser;

/**
 * 
 * @author alessandroumbrico
 *
 */
public class XMLEnvironmentConfigurationParser implements EnvironmentConfigurationParser
{
	private Document document;
	private Map<String, Element> eCache;				// element cache
	private Map<String, Sensor> sCache;					// sensor cache
	
	/**
	 * 
	 * @param envConfigFilePath
	 */
	public XMLEnvironmentConfigurationParser(String envConfigFilePath) 
	{
		// setup cache
		this.eCache = new HashMap<String, Element>();
		this.sCache = new HashMap<String, Sensor>();
		
		try
		{
			// parse the document file
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.document = builder.parse(new FileInputStream(envConfigFilePath));
			
			
			// load elements
			this.loadElements();
			
			// set element composition
			this.setElementComposition();
			
			
			// load sensors
			this.loadSensors();
			
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Element> getElements() {
		return new ArrayList<Element>(this.eCache.values());
	}
	
	/**
	 * 
	 * @return
	 */
	public List<Sensor> getSensors() {
		return new ArrayList<Sensor>(this.sCache.values());
	}
	
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private void loadSensors() 
			throws Exception
	{
		// prepare XPath expression
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xp = xpf.newXPath();
		
		// create XPATH expression
		XPathExpression expression = xp.compile("//sensor");
		NodeList nodes = (NodeList) expression.evaluate(this.document, XPathConstants.NODESET);
		// iterate over results
		for (int index = 0; index < nodes.getLength(); index++) 
		{
			// get node
			Node node = nodes.item(index);
			// get id attribute
			Attr attrId = (Attr) node.getAttributes().getNamedItem("id");
			Attr attrName = (Attr) node.getAttributes().getNamedItem("name");
			Attr attrType = (Attr) node.getAttributes().getNamedItem("type");
			Attr attrState = (Attr) node.getAttributes().getNamedItem("state");
			Attr attrPlacement = (Attr) node.getAttributes().getNamedItem("placement");
			Attr attrTarget = (Attr) node.getAttributes().getNamedItem("target");
			
			// check sensor state
			SensorState state = SensorState.getStateByCode(attrState.getValue());
			
			// retrieve owner from cache
			Element owner = this.eCache.get(attrPlacement.getValue());
			// retrieve target from cache
			Element target = this.eCache.get(attrTarget.getValue());
			
			// create sensor element
			Sensor sensor = new Sensor(
					attrId.getValue(),
					attrName.getValue(),
					attrType.getValue(),
					state,
					owner,
					target);
			
			
			// add cache entry
			this.sCache.put(sensor.getName(), sensor);
		}
	}

	
	
	/**
	 * 
	 * @return
	 * @throws Exception
	 */
	private void loadElements() 
			throws Exception
	{
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xp = xpf.newXPath();
		
		// create XPATH expression
		XPathExpression expression = xp.compile("//element");
		
		NodeList nodes = (NodeList) expression.evaluate(this.document, XPathConstants.NODESET);
		// iterate over results
		for (int index = 0; index < nodes.getLength(); index++)
		{
			// get node
			Node node = nodes.item(index);
			// get id attribute
			Attr attrId = (Attr) node.getAttributes().getNamedItem("id");
			Attr attrName = (Attr) node.getAttributes().getNamedItem("name");
			Attr attrType = (Attr) node.getAttributes().getNamedItem("type");
			
			// create element
			Element element = new Element(
					attrId.getValue(), 
					attrName.getValue(), 
					attrType.getValue());
			
			// add cache entry
			this.eCache.put(element.getName(), element);
		}
	}
	
	/**
	 * 
	 * @throws Exception
	 */
	private void setElementComposition() 
			throws Exception
	{
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xp = xpf.newXPath();
		
		// create XPATH expression
		XPathExpression expression = xp.compile("//element");
		
		NodeList nodes = (NodeList) expression.evaluate(this.document, XPathConstants.NODESET);
		// iterate over results
		for (int index = 0; index < nodes.getLength(); index++)
		{
			// get node
			Node node = nodes.item(index);
			// get id attribute
			Attr attrName = (Attr) node.getAttributes().getNamedItem("name");
			Attr partOf = (Attr) node.getAttributes().getNamedItem("partOf");

			// check if part of is set
			if (partOf != null) {
				
				// get element
				Element element = this.eCache.get(attrName.getValue());
				// get "parent" element
				Element parent = this.eCache.get(partOf.getValue());
				
				// set element as part of parent
				element.setPartOf(parent);
				
			}
		}
	}
	
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			XMLEnvironmentConfigurationParser parser = new XMLEnvironmentConfigurationParser("etc/environment/house_config.xml");
			List<Sensor> sensors = parser.getSensors();
			for (Sensor sensor : sensors) {
				System.out.println(sensor);
			}
			
			List<Element> elements = parser.getElements();
			for (Element element : elements) {
				System.out.println(element);
			}			
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
	}
}
