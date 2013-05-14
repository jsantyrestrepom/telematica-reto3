package mom;


import java.util.HashMap;
import java.util.Set;
import javax.jms.DeliveryMode;
//import javax.jms.Connection;
import javax.jms.Destination;
import javax.jms.ExceptionListener;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.advisory.DestinationSource;
import org.apache.activemq.command.ActiveMQTopic;
/**
 *
 * @author SANTY
 */
public class AdCenter implements ExceptionListener {
    private ActiveMQConnection conn;
    private Session session;
    private Channel topic;
    private HashMap subscribeTopics;
    
    
    
        /**** SECTION ****/
    public String channelList() {
        try {
            DestinationSource ds = conn.getDestinationSource();
            Set<ActiveMQTopic> topics = ds.getTopics();
            String str = "| ";
            for (ActiveMQTopic t : topics) {
                str += t.getTopicName() +" | ";
            }
            return str;
            //return topic.toString();
        } catch (JMSException e) {
            return "error";
        }
    }
    
//    public Boolean channelExist(String channelName) {
//        return true;
//    }
    
        /**** PRODUCER SECTION ****/
    public Boolean conectAsProducer(String host) {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(host);
            conn = (ActiveMQConnection) connectionFactory.createConnection();
            return true;
        } catch(JMSException e) {
            return false;
        }
        
    }
    
    public Boolean sendToChannel(String channel, String msj) {
        Destination destination = topic.locateChannel(channel);
        if (destination == null) {
            return false;
        } else {
            try {
                MessageProducer producer = session.createProducer(destination);
                producer.setDeliveryMode(DeliveryMode.PERSISTENT);
                TextMessage message = session.createTextMessage(msj);          
		producer.send(message);                
                return true;
            } catch (JMSException e) {
                return false;
            }
        }
        
    }
    
    public Boolean createChannel(String name) {
        try {
            topic.addChannel(session.createTopic(name));
            return true;
        } catch(JMSException e) {
            return false;
        }
    }
    
    
        /**** CONSUMER SECTION ****/
    public Boolean conectAsConsumer(String host) {
        try {
            ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory(host);
            conn = (ActiveMQConnection) connectionFactory.createConnection();
            conn.setExceptionListener(this);
            subscribeTopics = new HashMap<String, Thread>();
            return true;
        } catch(JMSException e) {
            return false;
        }
        
    }
    
    public Boolean subscribe(String channel) {
        try {
            Destination destination = session.createQueue(channel);
            MessageConsumer consumer = session.createConsumer(destination);
            MessageListener listener = new MessageListener() {
                @Override
                public void onMessage(Message msg) {
                    if (msg instanceof TextMessage) {
                        TextMessage textMessage = (TextMessage) msg;
                        String text = null;
                        try {
                            text = textMessage.getText();
                        } catch (JMSException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                        System.out.println("Received: " + text);
                    } else {
                        System.out.println("Received: " + msg);
                    }
                }
            };
            consumer.setMessageListener(listener);
            subscribeTopics.put(channel, new Thread());
            return true;
        } catch (JMSException ex) {
            return false;
        }
    }
    
    public Boolean pull(String channel) {
        return true;
    }
    
    @Override
    public void onException(JMSException jmse) {
        System.out.println("JMS Exception occured.  Shutting down client.");
    }
        
    
        /**** GENERAL SECTION ****/
    public Boolean logIn() {
        try {
            session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);//createSession(true, -1); // 
            conn.start();
            topic = new Channel();
            return true;
        } catch(JMSException e) {
            return false;
        }
    }
    
    public Boolean logOut() {
        try {
            session.close();
            return true;
        } catch(JMSException e) {
            return false;
        }
    }
    
    public Boolean disconnect() {
        try {
            conn.close();
            return true;
        } catch(JMSException e) {
            return false;
        }
    }
    
}
