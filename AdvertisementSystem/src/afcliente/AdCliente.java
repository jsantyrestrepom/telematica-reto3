/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package afcliente;


import java.util.Scanner;
import javax.jms.Connection;
import javax.jms.Session;
import mom.AdCenter;
/**
 *
 * @author SANTY
 */
public class AdCliente {
    private static AdCenter adCenter;
    private static Scanner scan;
    
    
    private static void establishConnection(String host) {
        try {
            System.out.println("=> start connection to "+ host +" ....");
            adCenter.conectAsConsumer(host);
            adCenter.logIn();
        } catch(Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
        System.out.println("=> connection succesful !");
    }
    
    private static void endingConnection() {
        try {
            //System.out.println("=> ");
            adCenter.logOut();
            adCenter.disconnect();
        } catch(Exception e) {
            System.out.println("Caught: " + e);
            e.printStackTrace();
        }
        System.out.println("=> disconnection succesful !");
    }
    
    private static void makePull() {
        System.out.print("Channel? ");
        String input = scan.nextLine();
        adCenter.pull(input);
    }
    
    private static void activePush() {
        
    }
    
    public static void main(String[] args) throws Exception {
        scan = new Scanner(System.in);
        String host, input;
        int opcion;
        adCenter = new AdCenter();
        
        if (args.length > 1) {
            host = args[0];
        } else {
            host = "tcp://localhost:61616";
        }
        establishConnection(host);
        
        
        System.out.println("current channels: \n"+ adCenter.channelList());
        input = "";
        while (!"halt".equals(input)) {
            System.out.println("\n\n-----------------------------------------");
            System.out.println("\n\n\tOPCIONS"
                    + "\n1.List the current channel"
                    + "\n2.Make PULL to a specific channel"
                    + "\n3.Active PUSH operation"
                    + "\nX.Log out");
            input = scan.nextLine();
            try {
                opcion = Integer.parseInt(input);
                switch (opcion) {
                    case 1:
                        System.out.println("current channels: \n"+ adCenter.channelList());
                        break;
                    case 2:
                        makePull();
                        break;
                    case 3:
                        activePush();
                        break;
                    case 4:
                        //opcion4();
                    case 8:
                        input = "halt";
                }
            } catch (Exception e) {}
        }
        
        endingConnection();
    }

}
