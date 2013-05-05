/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package adfuente;


import java.util.Scanner;
import mom.AdCenter;
/**
 *
 * @author SANTY
 */
public class AdFuente {
    private static AdCenter adCenter;
    private static Scanner scan;
    private static String canalActive;

    
    private static void establishConnection(String host) {
        try {
            System.out.println("=> start connection to "+ host +" ....");
            adCenter.conectAsProducer(host);
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

    private static void createChannel() {
        System.out.print("Name for the new channel: ");
        String input = scan.nextLine();
        adCenter.createChannel(input);
        System.out.println("=> channel created !");
        canalActive = input;
    }
    
    private static void changeChannel() {
        System.out.print("Name of the channel to active: ");
        String input = scan.nextLine();
        if (adCenter.createChannel(input)) {
            canalActive = input;
            System.out.println("=> "+ input +" is now the active channel !");
        } else {
            System.out.println("=> "+ input +" is not a current channel.");
        }
    }
    
    private static void sendAd() {
        System.out.print("Channel: "+ canalActive +" (active channel)\n"
                + "Message: ");
        String message = scan.nextLine();
        adCenter.sendToChannel(canalActive, message);
        System.out.println("=> advertisement sent: " + message);
    }
    
    public static void main(String[] args) throws Exception {
        scan = new Scanner(System.in);
        String host, input;
        int optionInput;
        
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
                    + "\n2.Create a channel"
                    + "\n3.Change active channel"
                    + "\n4.Send an advertisement"
                    + "\n5.Delete a channel"
                    + "\nX.Log out");
            input = scan.nextLine();
            try {
                optionInput = Integer.parseInt(input);
                switch (optionInput) {
                    case 1:
                        System.out.println("current channels: \n"+ adCenter.channelList());
                        break;
                    case 2:
                        createChannel();
                        break;
                    case 3:
                        changeChannel();
                    case 4:
                        sendAd();
                        break;
                    case 8:
                        input = "halt";
                }
            } catch (Exception e) {}
        }


        endingConnection();
    }
}
