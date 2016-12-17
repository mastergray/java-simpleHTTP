/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplehttp;

import java.io.*;

/**
 *
 * @author jgray
 */
public class terminate implements Runnable {
    
    static BufferedReader in ;  
      
    @Override
    public void run(){  
        
        String msg = null;  
        
        while(true){  
            
            try{
                           
                   msg=in.readLine();  
            
            }catch(Exception e){
             
                System.out.println("Server has unexpectedly quit because of a problem with termination.");
                System.exit(0);
                    
            }  
              
            if(msg.equals("")) {
                
                System.out.println("Server has been terminated.");
                System.exit(0);
            }  
        }
        
    }
    
}
