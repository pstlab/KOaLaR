package it.cnr.istc.pst.koala.network.mqtt;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.core.UriBuilder;

import org.apache.activemq.broker.Broker;
import org.apache.activemq.broker.BrokerPlugin;
import org.apache.activemq.broker.BrokerService;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import it.cnr.istc.pst.koala.environment.configuration.reasoner.EnvironmentConfigurationReasoner;
import it.cnr.istc.pst.koala.environment.configuration.reasoner.owl.OWLEnvironmentConfigurationReasoner;
import it.cnr.istc.pst.koala.environment.observation.ObservationProperty;
import it.cnr.istc.pst.koala.environment.observation.owl.OWLObservationReasoner;

/**
 * 
 * @author anacleto
 *
 */
public class MQTTSensorNetworkManager implements MqttCallback 
{
	private Process mqtt = null;
	private String clientId = ""+new Date().getTime();
	private static final String TASKLIST = "tasklist";
    private static final String KILL = "taskkill /F /IM ";
	private final String broker2 = "tcp://192.168.67.182:1883"; //150.146.65.143:1883"; //LUCA "tcp://150.146.65.68:1883";
	
	public static final String TOPIC_ASK_HOUSE_CONFIG = "house-config-file";
    public static final String TOPIC_GET_HOUSE_CONFIG = "get-house-config-file";

    public static final String TOPIC_ASK_SENSOR_VALUE = "house/0x0184e8a0/sensor/";
    public static final String TOPIC_GET_SENSOR_VALUE = "/0x0184e8a0/sensor/";
    public static final String NO_UNIT = "NOUNIT";
    
    private static MQTTSensorNetworkManager _instance = null;
	private MqttClient client;
	private String config = null;
	
	private OWLObservationReasoner reasoner;
	
	/**
	 * 
	 * @return
	 */
	public static MQTTSensorNetworkManager getInstance() {
        if (_instance == null) {
            _instance = new MQTTSensorNetworkManager();
            return _instance;
        } else {
            return _instance;
        }
    }

	/**
	 * 
	 */
    private MQTTSensorNetworkManager() {
        super();
        
        // create env
        EnvironmentConfigurationReasoner environment = new OWLEnvironmentConfigurationReasoner();
        this.reasoner = new OWLObservationReasoner(environment);
    }

    /**
     * 
     * @return
     */
    public String getConfig() {
        return config;
    }

    /**
     * 
     * @param config
     */
    public void setConfig(String config) {
        this.config = config;
    }
	
	/**
	 * 
	 */
	public void connect() 
	{
        try 
        {
            InetAddress localIp = InetAddress.getLocalHost();
            System.out.println("IP of my system is := " + localIp.getHostAddress());

            System.out.println("MQTT connected");
            System.out.println("subscribing all");
            MemoryPersistence persistence = new MemoryPersistence();
            try 
            {
//                client = new MqttClient(broker2, clientId, persistence);
            	client = new MqttClient("tcp://localhost:1883", clientId, persistence);
                MqttConnectOptions connOpts = new MqttConnectOptions();
                connOpts.setCleanSession(false);
//                connOpts.setKeepAliveInterval(Integer.MAX_VALUE);
                
                System.out.println("Connecting to broker: " + broker2);
                client.connect(connOpts);
                System.out.println("Connected");
                client.setCallback(_instance);
            } 
            catch (MqttException me) {
                System.out.println("reason " + me.getReasonCode());
                System.out.println("msg " + me.getMessage());
                System.out.println("loc " + me.getLocalizedMessage());
                System.out.println("cause " + me.getCause());
                System.out.println("excep " + me);
                me.printStackTrace();
            }

        } catch (IOException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
        }
    }

	
//	public void disconnect() {
//        try {
//            if (mqtt != null && isProcessRunning("mosquitto.exe")) {
//                killProcess("mosquitto.exe");
//                System.out.println("MQTT disconnected");
//            }
//        } catch (Exception ex) {
//            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//        }
//    }
//	
//	public static boolean isProcessRunning(String serviceName) throws Exception {
//
//        Process p = Runtime.getRuntime().exec(TASKLIST);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(
//                p.getInputStream()));
//        String line;
//        while ((line = reader.readLine()) != null) {
//
//            System.out.println(line);
//            if (line.contains(serviceName)) {
//                System.out.println("TROVATO !");
//                return true;
//            }
//        }
//
//        return false;
//
//    }
//	
//	/**
//	 * 
//	 * @param serviceName
//	 * @throws Exception
//	 */
//	public static void killProcess(String serviceName) throws Exception {
//        System.out.println("service name : " + serviceName);
//
//        Runtime.getRuntime().exec(KILL + serviceName);
//
//    }
	
	/**
	 * 
	 */
	public void connectionLost(Throwable thrwbl) {
		System.out.println("we have a problem!");
        thrwbl.printStackTrace();
		
	}

	/**
	 * 
	 */
	public void deliveryComplete(IMqttDeliveryToken arg0) {
		// TODO Auto-generated method stub
		
	}

	/**
	 * 
	 */
	public void messageArrived(String topic, MqttMessage mm) 
	{
//		try 
//		{
	        System.out.println("MESSAGE ARRIVED: ");
	        System.out.println("\tTOPIC: " + topic);
	        String message = "";
	        message = new String(mm.getPayload());
	        
	        System.out.println("\tMESSAGE: " + message);
	        
	        if (topic.startsWith("/house/0x0184e8a0/sensorValue")) 
	        {
	            System.out.println("SENSOR REQUEST IS ARRIVED");
	            System.out.println("topic -> " + topic);
	            System.out.println("data -> " + message);

	            String[] split = message.split("/");
	            String sid = split[0];
	            String time = split[1];
	            String value = split[2];
	            
	            // sensor id 
	            String[] sidSplit = sid.split("-");
	            String sensorId = sidSplit[0];
	            // property label
	            String propLabel = sidSplit[1] + "-" + sidSplit[2];
	            
	            // read temperature
	            if (propLabel.equals("49-1")) 
	            {
	            	try
	            	{
	            		double data = Double.parseDouble(value);
	            		
	            		System.out.println("\nADDING OBSERVATION: SENSOR-DI= " + sensorId + ", OBSERVED-VALUE= " + value + ", PROPERTY= " + ObservationProperty.TEMPEREATURE + "\n");
			            // add observation
			            this.reasoner.observation(sensorId, new Long(Math.round(data)).toString(), ObservationProperty.TEMPEREATURE);
	            	}
	            	catch(Exception ex) {
	            		System.err.println(ex.getMessage());
	            	}
	            }
	            
	            /*
	             * TODO 
	             */
	            
	        }
	        
	        
	        
//        } //Sharing sensor informations
//        catch (MqttException ex) {
//            Logger.getLogger(getClass().getName()).log(Level.SEVERE, null, ex);
//        }
	}	
	
	/**
	 * 
	 * @param topic
	 * @param message
	 * @throws MqttException
	 */
	public void publish(String topic, String message) throws MqttException {
        System.out.println("sending message:");
        System.out.println("\tTopic: " + topic);
        System.out.println("\tMessage: " + message);
        System.out.println("sample client is null ? "+(client == null));
        System.out.println("----------------------------------------------");
        client.publish(topic, new MqttMessage(message.getBytes()));
    }
	
	
    public void startBroker() 
    {
        BrokerService broker = new BrokerService();
        try 
        {
            broker.addConnector(UriBuilder.fromUri("mqtt://192.168.67.182:1883").build()); //150.146.65.143:1883").build());//("mqtt://150.146.65.68:1883").build());
            broker.setPlugins(new BrokerPlugin[]{new BrokerPlugin() {
                public Broker installPlugin(Broker broker) throws Exception {
                    return new MyBroker(broker);
                }
            }});
            broker.start();
            System.out.println("broker started [OK]");
        } 
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    
    public void subscribe(String topic) {
        try {
            System.out.println("[MQTT][SUBSCRIBE] topic: " + topic);
            client.subscribe(topic);

        } catch (MqttException ex) {
            Logger.getLogger(getClass()
                    .getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    /**
     * 
     * @param args
     */
    public static void main(String[] args) {
		
		MQTTSensorNetworkManager manager = MQTTSensorNetworkManager.getInstance();
		
		manager.startBroker();
		manager.connect();
		manager.subscribe("/house/0x0184e8a0/sensorValue/#");
		
	}
}
