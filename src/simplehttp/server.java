/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package simplehttp;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;

/**
 *
 * @author jgray
 */
public class server implements Runnable {
    
    private Socket incoming;                       //  Connection from client
    private int counter;                           //  Number of connections
    private InetAddress client;                    //  Client information
    private HashMap<String, String> httpRequest;   //  Stores request 
    
    server(Socket incoming, int counter, InetAddress client) {
    
        this.incoming = incoming;
        this.counter  = counter;
        this.client   = client;
        
    }
    
    @Override
    public void run() {
     
       try {
       
           try {
               
                //   Stream from client:
                BufferedReader input = new BufferedReader(
                    new InputStreamReader(incoming.getInputStream())
                );


                //   Stream to client:
                DataOutputStream output = new DataOutputStream(
                   incoming.getOutputStream()
                );
 
                // Process client request:
                http_request(input, output);
                
                // Shows server activity:
                System.out.println(" | " + counter + " | " 
                      + client.getHostName() 
                      + " | "
                      + httpRequest.get("filename")
                      + " | "
                      +  httpRequest.get("timeStamp")
                      + " | " 
                      +  getResponseMessage(Integer.parseInt(httpRequest.get("responseCode")))
                    );
               
                
            } finally {
           
               incoming.close();      
               
           }
      
       } catch (IOException e) {
         
          System.out.println("Server could not be started: " + e.getMessage());
       
      } catch (Exception e) {
          
          //    TODO: Why does webkit seem to send more than one request?
          SimpleHTTP.counter--;
          
      }
        
    }

/*
 * 
 * Request and Response methods for HTTP:
 *
 */
    
    private void http_request(BufferedReader input, DataOutputStream output) {
        
        int responseCode;
        httpRequest = new HashMap<String, String>();
        
        //  Sets response time:
        httpRequest.put("timeStamp", SimpleHTTP.getTimestamp());
        
        try {
        
            String request  = input.readLine();
            
            httpRequest.put("method", getRequestMethod(request));
            httpRequest.put("filename", getFilename(request));
            httpRequest.put("fileType", getFilename(request));
            
            if (httpRequest.get("method").equals("get")) {
                
                //  Intiates HTTP GET request:
                responseCode = getRequest(
                        output,
                        httpRequest.get("fileType"),
                        httpRequest.get("filename")
                );
                
            } else {

                responseCode = methodNotImplemented(output);

            }
        
        } catch (IOException ioe) {
           
            responseCode = 400;
           
        } catch (Exception e) {
            
            responseCode = 400;
            
        }
        
        //  Sets response code:
        httpRequest.put("responseCode", Integer.toString(responseCode));
        
        
    }
    
    //  Returns HTTP header Response:
    private String http_response(int responseCode, String fileType, int contentLength) {
        
        String header = "HTTP/1.1 " 
               + getResponseMessage(responseCode)
               + "\r\n"
               + "Date: " +  httpRequest.get("timeStamp")
               + "\r\n" 
               + "Connection: close\r\n"
               + "Server: SimpleHTTP\r\n"
               + SimpleHTTP.mimeTypes.get(fileType)
               + "\r\n"
               + "Content-Length: " + contentLength
               + "\r\n\r\n";         
          
        return header;
        
    }
    
    //  Returns HTTP response:
    private String getResponseMessage(int responseCode) {
        
         switch (responseCode) {
          case 200:
            return "200 OK";
          case 400:
            return"400 Bad Request";
          case 403:
            return "403 Forbidden";
          case 404:
            return "404 Not Found";
          case 500:
            return "500 Internal Server Error";
          default:
            return "501 Not Implemented";
        }
        
    }
    
/*
 * 
 * Parse methods For HTTP Request:
 *
 */
       
    private String getFilename(String request) {
        
        String[] filename = request.split(" ");
        return filename[1].substring(1);
    }
    
    private String getFileType(String request) {
        
        String fileType[] = getFilename(request).split("[.]");
        return fileType[1];
        
    }
    
    private String getRequestMethod(String request) {
        
        String[] method = request.toLowerCase().split(" ");
        return method[0];
        
    }
    

    
/*
 * 
 * HTTP request methods:
 *
 */
    
    private int getRequest(DataOutputStream output, String fileType, String filename) {
    
        try {
      
            File file = new File(filename);
            
            //   Open requested file: 
            FileInputStream requestedfile = new FileInputStream(file);

            //    Sets Header:
            output.writeBytes(http_response(200, fileType, requestedfile.available()));

            byte [] buffer = new byte[1024];

                while (true) {
                  //read the file from filestream, and print out through the
                  //client-outputstream on a byte per byte base.
                  int b = requestedfile.read(buffer, 0,1024);
                  if (b == -1) {
                    break; //end of file
                  }
                  output.write(buffer,0,b);
                }

            //    Clean up:
            output.close();
            requestedfile.close();          
            return 200;

        } catch (Exception e) {
             
            return fileNotFound(output);

        }
    }
    
    //  Returns 404 Page if file not found:
    private int fileNotFound(DataOutputStream output) {
        
         try {
      
            File file = new File("404.html");
            
            //   Open requested file: 
            FileInputStream requestedfile = new FileInputStream(file);

            //    Sets Header:
            output.writeBytes(http_response(404, "html", requestedfile.available()));

            byte [] buffer = new byte[1024];

                while (true) {
                  //read the file from filestream, and print out through the
                  //client-outputstream on a byte per byte base.
                  int b = requestedfile.read(buffer, 0,1024);
                  if (b == -1) {
                    break; //end of file
                  }
                  output.write(buffer,0,b);
                }

            //    Clean up:
            output.close();
            requestedfile.close();          
            return 404;

        } catch (Exception e) {   
           
           //   404 page could not be read or found:
           System.out.append(e.getMessage());
           return 500;

        }
        
    }
    
    //  Returns 501 Page if method not implemented:
    private int methodNotImplemented(DataOutputStream output) {
        
        try {
      
            File file = new File("501.html");
            
            //   Open requested file: 
            FileInputStream requestedfile = new FileInputStream(file);

            //    Sets Header:
            output.writeBytes(http_response(501, "html", requestedfile.available()));

            byte [] buffer = new byte[1024];

                while (true) {
                  //read the file from filestream, and print out through the
                  //client-outputstream on a byte per byte base.
                  int b = requestedfile.read(buffer, 0,1024);
                  if (b == -1) {
                    break; //end of file
                  }
                  output.write(buffer,0,b);
                }

            //    Clean up:
            output.close();
            requestedfile.close();          
            return 501;

        } catch (Exception e) {   
           
           //   501 page could not be read or found:
           System.out.append(e.getMessage());
            return 500;

        }
        
    }
    
}
