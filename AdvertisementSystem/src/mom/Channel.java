/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mom;


import java.util.ArrayList;
import javax.jms.JMSException;
import javax.jms.Topic;
/**
 *
 * @author SANTY
 */
public class Channel {
    private ArrayList<Topic> canales = new ArrayList();
    
    
    
    
    public Boolean addChannel(Topic name) {
        canales.add(name);
        return true;
    }
    
    public Boolean removeChannel(Topic name) {
        canales.remove(name);
        return true;
    }
    
    public Topic locateChannel(String name) {
        try {
            for (Topic t : canales) {
                if (name.equals( t.getTopicName() )) {
                    return t;
                }
            }
            return null;
        } catch (JMSException e) {
            return null;
        }
    }
    
//    @Override
//    public String toString() {
//        String list = "";
//        try {
//            for (Topic t : canales) {
//                list += t.getTopicName() + " | ";
//            }
//            return list;    //.substring(0, list.length() - 3);
//        } catch (JMSException e) {
//            return "";
//        }
//    }
    
}
