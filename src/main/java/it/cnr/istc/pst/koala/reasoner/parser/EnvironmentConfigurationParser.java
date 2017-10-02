package it.cnr.istc.pst.koala.reasoner.parser;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import it.cnr.istc.pst.koala.lang.Sensor;


/**
 * 
 * @author alessandro
 *
 */
public class EnvironmentConfigurationParser 
{
	private static final String CONFIG_FILE = "etc/environment/house_config.xml";
	private Document document;
	
	/**
	 * 
	 */
	protected EnvironmentConfigurationParser() {
		try
		{
			// parse the document file
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			this.document = builder.parse(new FileInputStream(CONFIG_FILE));
		}
		catch (Exception ex) {
			throw new RuntimeException(ex.getMessage());
		}
	}
	
	/**
	 * @return
	 * @throws Exception
	 */
	public List<Sensor> getListOfSensors() 
			throws Exception
	{
		// list of parsed sensors
		List<Sensor> list = new ArrayList<Sensor>();
		try
		{
		
			// prepare XPath expression
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();
			
			// create XPATH expression
			XPathExpression expression = xp.compile("//sensor");
			NodeList nodes = (NodeList) expression.evaluate(this.document, XPathConstants.NODESET);
			// iterate over results
			for (int index = 0; index < nodes.getLength(); index++) {
				// get node
				Node node = nodes.item(index);
				// get id attribute
				Attr attrId = (Attr) node.getAttributes().getNamedItem("id");
				Attr attrType = (Attr) node.getAttributes().getNamedItem("type");
				// create sensor element
				Sensor sensor = new Sensor(attrId.getValue(), attrType.getValue());
				list.add(sensor);
			}
		}
		catch (XPathExpressionException ex) {
			throw new Exception(ex.getMessage());
		}
		
		// get the list
		return list;
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		try
		{
			EnvironmentConfigurationParser parser = new EnvironmentConfigurationParser();
			List<Sensor> sensors = parser.getListOfSensors();
			for (Sensor sensor : sensors) {
				System.out.println(sensor);
			}
		 
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		
		
	}
}
