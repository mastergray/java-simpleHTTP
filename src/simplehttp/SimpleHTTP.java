/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplehttp;
import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jgray
 */
public class SimpleHTTP {
    
    static int port;                         //  Server port number
    static int counter = 1;                   //  Number of connections to server
    static HashMap<String, String> mimeTypes; // Associates file type with content header
      
    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {

        //  Intialize port number from command line, otherwise default to 80:
        try {
            
            port = Integer.parseInt(args[0]);
            
        } catch (Exception e) {
            
            port = 80;
            
        }
        
        //  Intialize MIME Types hash map:
        initMimeTypes();
        
        //  Intialize bufferedReader for termination thread:
        terminate.in = new BufferedReader(new InputStreamReader(System.in)); 
        
        
        try {
          
            //  Intializes termination thread:
            Thread terminationThread = new Thread(new terminate());
            terminationThread.start();
            
            
            //  Intializes server with port number:
            ServerSocket serversocket = new ServerSocket(port);
            
            //  Shows server info:
            InetAddress address = InetAddress.getLocalHost();        
            System.out.println("\nHTTP Server: " + address + " | " + port);
            System.out.println("\nWaiting for connections...");
            System.out.println("Press 'Enter' to terminate\n");
                
                while (true) {
                    
                    try {
                        
                        terminationThread.sleep(10);
                       
                    } catch (InterruptedException ex) {
                        
                        System.out.println("Server has unexpectedly quit.");
                        System.exit(0);
                    }
                    
                    
                    //  Establish server socket for incoming connection:
                    Socket incoming = serversocket.accept();
                    
                    //  Get connected client info:
                    InetAddress client = incoming.getInetAddress();

                    //  Intializes thread for new connection:
                    Runnable r = new server(incoming, counter, client);
                    Thread t = new Thread(r);
                    t.start();
                    counter++;
                    
                   
                }

            } catch (IOException e) {

              System.out.println("Server could not be started: " + e.getMessage());
              System.exit(0);
        }
    
    }
   
   //   Builds hash map that returns content types for HTTP responses:
   public static void initMimeTypes() {
       
       mimeTypes = new HashMap<String, String>();
       
        //   Text:
       mimeTypes.put("html", "Content-Type: text/html");
       mimeTypes.put("js", "Content-Type: text/javascript");
       mimeTypes.put("css", "Content-Type: text/css");
       
       //   Images:
       mimeTypes.put("jpeg", "Content-Type: image/jpeg");
       mimeTypes.put("jpg", "Content-Type: image/jpeg");
       mimeTypes.put("png", "Content-Type: image/png");
       
      
       
       
   }
   
   //   Returns current timestamp:
   public static String getTimestamp() {
       
       SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'");
       String date = sdf.format(new Date()); 
       
       return date;
       
   }
   
}
