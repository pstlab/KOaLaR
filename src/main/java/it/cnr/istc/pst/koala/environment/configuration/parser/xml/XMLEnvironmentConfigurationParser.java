package it.cnr.istc.pst.koala.environment.configuration.parser.xml;

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

import it.cnr.istc.pst.koala.environment.configuration.parser.EnvironmentConfigurationParser;
import it.cnr.istc.pst.koala.environment.configuration.parser.lang.Room;
import it.cnr.istc.pst.koala.environment.configuration.parser.lang.RoomObject;
import it.cnr.istc.pst.koala.environment.configuration.parser.lang.Sensor;
import it.cnr.istc.pst.koala.environment.configuration.parser.lang.SensorState;


/**
 * 
 * @author alessandro
 *
 */
public class XMLEnvironmentConfigurationParser implements EnvironmentConfigurationParser
{
	private static XMLEnvironmentConfigurationParser INSTANCE = null;
	private static final String CONFIG_FILE = "etc/environment/house_config.xml";
	private Document document;
	
	/**
	 * 
	 */
	protected XMLEnvironmentConfigurationParser() 
	{
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
	 * 
	 * @return
	 */
	public static XMLEnvironmentConfigurationParser getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new XMLEnvironmentConfigurationParser();
		}
		
		// get instance
		return INSTANCE;
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
			for (int index = 0; index < nodes.getLength(); index++) 
			{
				// get node
				Node node = nodes.item(index);
				// get id attribute
				Attr attrId = (Attr) node.getAttributes().getNamedItem("id");
				Attr attrType = (Attr) node.getAttributes().getNamedItem("type");
				Attr attrState = (Attr) node.getAttributes().getNamedItem("state");
				// check sensor state
				SensorState state = attrState.getValue().equals("0") ? SensorState.ON : SensorState.OFF;
				// create sensor element
				Sensor sensor = new Sensor(attrId.getValue(), attrType.getValue(), state);
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
	 * @return
	 * @throws Exception
	 */
	public List<Room> getListOfRooms() 
			throws Exception
	{
		// list of parsed rooms
		List<Room> list = new ArrayList<Room>();
		try
		{
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();
			
			// create XPATH expression
			XPathExpression expression = xp.compile("//room");
			NodeList nodes = (NodeList) expression.evaluate(this.document, XPathConstants.NODESET);
			// iterate over results
			for (int index = 0; index < nodes.getLength(); index++)
			{
				// get node
				Node node = nodes.item(index);
				// get id attribute
				Attr attrId = (Attr) node.getAttributes().getNamedItem("id");
				Attr attrType = (Attr) node.getAttributes().getNamedItem("type");
				// create sensor element
				Room room = new Room(attrId.getValue(), attrType.getValue());
				list.add(room);
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
	 * @param room
	 * @return
	 * @throws Exception
	 */
	public List<RoomObject> getListOfObjectsByRoom(Room room) 
			throws Exception
	{
		// list of parsed objects
		List<RoomObject> list = new ArrayList<RoomObject>();
		try
		{
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();
			
			// create XPATH expression
			XPathExpression expression = xp.compile("//room[@id= " + room.getId() + "]/object");
			NodeList nodes = (NodeList) expression.evaluate(this.document, XPathConstants.NODESET);
			// iterate over results
			for (int index = 0; index < nodes.getLength(); index++)
			{
				// get node
				Node node = nodes.item(index);
				// get id attribute
				Attr attrId = (Attr) node.getAttributes().getNamedItem("id");
				Attr attrType = (Attr) node.getAttributes().getNamedItem("type");
				// create sensor element
				RoomObject object = new RoomObject(attrId.getValue(), attrType.getValue());
				list.add(object);
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
	 * @param room
	 * @return
	 * @throws Exception
	 */
	public List<Sensor> getListOfSensorsByRoom(Room room) 
			throws Exception
	{
		// list of parsed objects
		List<Sensor> list = new ArrayList<Sensor>();
		try
		{
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();
			
			// create XPATH expression
			XPathExpression expression = xp.compile("//room[@id= " + room.getId() + "]/sensor");
			NodeList nodes = (NodeList) expression.evaluate(this.document, XPathConstants.NODESET);
			// iterate over results
			for (int index = 0; index < nodes.getLength(); index++)
			{
				// get node
				Node node = nodes.item(index);
				// get id attribute
				Attr attrId = (Attr) node.getAttributes().getNamedItem("id");
				Attr attrType = (Attr) node.getAttributes().getNamedItem("type");
				Attr attrState = (Attr) node.getAttributes().getNamedItem("state");
				// check sensor state
				SensorState state = attrState.getValue().equals("0") ? SensorState.ON : SensorState.OFF;
				// create sensor element
				Sensor sensor = new Sensor(attrId.getValue(), attrType.getValue(), state);
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
	 * @param room
	 * @return
	 * @throws Exception
	 */
	public List<Sensor> getListOfSensorsByObject(RoomObject object) 
			throws Exception
	{
		// list of parsed objects
		List<Sensor> list = new ArrayList<Sensor>();
		try
		{
			XPathFactory xpf = XPathFactory.newInstance();
			XPath xp = xpf.newXPath();
			
			// create XPATH expression
			XPathExpression expression = xp.compile("//object[@id= " + object.getId() + "]/sensor");
			NodeList nodes = (NodeList) expression.evaluate(this.document, XPathConstants.NODESET);
			// iterate over results
			for (int index = 0; index < nodes.getLength(); index++)
			{
				// get node
				Node node = nodes.item(index);
				// get id attribute
				Attr attrId = (Attr) node.getAttributes().getNamedItem("id");
				Attr attrType = (Attr) node.getAttributes().getNamedItem("type");
				Attr attrState = (Attr) node.getAttributes().getNamedItem("state");
				// check sensor state
				SensorState state = attrState.getValue().equals("0") ? SensorState.ON : SensorState.OFF;
				// create sensor element
				Sensor sensor = new Sensor(attrId.getValue(), attrType.getValue(), state);
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
			XMLEnvironmentConfigurationParser parser = new XMLEnvironmentConfigurationParser();
			List<Sensor> sensors = parser.getListOfSensors();
			for (Sensor sensor : sensors) {
				System.out.println(sensor);
			}
			
			List<Room> rooms = parser.getListOfRooms();
			for (Room room : rooms) {
				System.out.println(room);
				// get room objects
				List<RoomObject> objects = parser.getListOfObjectsByRoom(room);
				for (RoomObject obj : objects) {
					System.out.println("\t" + obj);
					// get list of sensors
					List<Sensor> objSensors = parser.getListOfSensorsByObject(obj);
					for (Sensor sensor : objSensors) {
						System.out.println("\t\t" + sensor);
					}
				}
				
				// get list of room sensors
				List<Sensor> roomSensors = parser.getListOfSensorsByRoom(room);
				for(Sensor sensor : roomSensors) {
					System.out.println("\t" + sensor);
				}
			}			
		}
		catch (Exception ex) {
			System.err.println(ex.getMessage());
		}
		
		
	}
}
