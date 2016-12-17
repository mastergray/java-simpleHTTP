/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package httpmidi;

/**
 *
 * @author jgray
 */
public class HTTPmidi {

    public static String url = "http://192.168.1.105:3000";
    public static int device = 0;
    public static boolean ready = false;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
      
        
        try {
        
            for (int i = 0; i < args.length; i++) {   
                
                //  Command '-show' shows all dectected devices and exits:
                if (args[i].equals("-show")) {

                    System.out.println("\n" + midiManager.showDevices());
                    System.exit(0);
                }

                //  Command '-url' uses next argument to set URL
                if (args[i].equals("-url")) {

                    url = args[i + 1];

                }

                //  Command '-device' uses next argument to set device
                if (args[i].equals("-device")) {

                    device = Integer.parseInt(args[i + 1]);
                    
                }

            } 
        
        } catch (Exception e) {
            
            //  Invalid command exits"
            System.out.println("Invalid command");
            System.exit(0);
            
        }
        
   
            
        
        //  Sets and displays URL for sending MIDI data to:
        HTTPpost.setURL(url);
        System.out.println("Connected: " + url);
        
        //  Sets input device and displays device name:
        midiManager.setInputDevice(device);
        System.out.println(
         "Using Device: " + device + ". " + midiManager.getDeviceName(device) + "\n"
        );

        // Initialize midi out:
        midiTransfer midiOut = new midiTransfer(System.out);

        // Initialize connection between devices:
        midiManager.connect(midiOut);
        
        

    }
}
