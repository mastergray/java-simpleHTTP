/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package httpmidi;

import java.io.*;
import java.net.*;

/**
 *
 * @author Carrie
 */
public class HTTPpost {
    
     public static String hostname;
    
    public static void setURL(String url) {
        
        hostname = url;
        
    }
    
    public static void send(String query) {
        
        try {
           
            String request = encode(query);
            
            // Connect to URL
            URL url = new URL(hostname);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("Content-Length",  String.valueOf(request.length()));

            // Write data
            OutputStream os = connection.getOutputStream();
            os.write(request.getBytes());

            // Read response
            StringBuilder responseSB = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ( (line = br.readLine()) != null)
                responseSB.append(line);

            // Close streams
            br.close();
            os.close();

            System.out.println(responseSB.toString());
            
        } catch (IOException e) {
            
            System.out.println(e);
            
        }
    }
    
     public static String encode(String query) throws IOException {
        
      String encoded = "";
      String params[] = query.split("&");
      
        for (int i = 0; i < params.length; i++) {
            
            String values[] = params[i].split("=");
            
            encoded += URLEncoder.encode(values[0], "UTF-8") + "="+ URLEncoder.encode(values[1], "UTF-8");
            
        }
        
       return encoded;
      
        
    }
    
}
