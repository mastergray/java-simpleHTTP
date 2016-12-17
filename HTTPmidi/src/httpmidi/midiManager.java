/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package httpmidi;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Transmitter;

/**
 *
 * @author jgray
 */
public class midiManager {
    
    static MidiDevice           outputDevice = null;
    static MidiDevice           inputDevice  = null;
    // Class Variable for all MIDI devices:
    static MidiDevice.Info[]    aInfos       = MidiSystem.getMidiDeviceInfo();
    static Transmitter          transmitter  = null;
    static String               msgText      =  "";
   
    // Returns device that has been set to send MIDI relays:
    public static MidiDevice getInputDevice () {
        
        return inputDevice;
        
    }
    
    // Returns device that has been set to receive MIDI relays:  
    public static MidiDevice getOutputDevice () {
        
        return outputDevice;
    }
   
    // Sets device that sends MIDI relays: 
    public static void setInputDevice(int deviceID) {
       
         try {
    
           inputDevice  = MidiSystem.getMidiDevice(aInfos[deviceID]);
                
        } catch (MidiUnavailableException e){
           
            inputDevice = null;
            System.out.println("Cannot set input device!");

        }
       
   }
   
    // Sets device that receives MIDI relays: 
    public static void setOutputDevice(int deviceID) {
       
         try {
    
           outputDevice  = MidiSystem.getMidiDevice(aInfos[deviceID]);

        } catch (MidiUnavailableException e){
           
            outputDevice = null;
            System.out.println("Cannot set output device!");

        }
       
   }
     
    // Returns the name of all available MIDI devices:
    static public String showDevices() {
        
        String deviceText = "";
       
            for (int i = 0; i < aInfos.length; i++) {
                
                
                deviceText += "Device ID: " + i + "\nName: " + aInfos[i].getName() + "\n\n";
            
            }
        
            if (aInfos.length == 0) {
                
                return "No devices available";

            }
        
        return deviceText;
    
    }
    
    // Returns name device:
    static public String getDeviceName(int deviceID) {
       
        return aInfos[deviceID].getName();
        
    }
    
    // Displays name and ID of device:
    static public void showDeviceName(int deviceID) {
       
        System.out.println(deviceID + ". " + aInfos[deviceID].getName());
        
    }
     
    // Initializes set devices to use MIDI relays:
    public static void connect(midiTransfer transfer) {
        
         try {
            
             
            // Opens devices:  
            openDevices();
             
            // Sets input device to send relays: 
             transmitter = inputDevice.getTransmitter();
             
           // Sets input device to send relays to output device:      
             
             transmitter.setReceiver(transfer);
             
         } catch (MidiUnavailableException e) {
             
            System.out.println(e);
            
            inputDevice.close();
           // outputDevice.close();
            
         }
        
    }
    
    public static void openDevices() {
        
          try {
            
             inputDevice.open();
           //  outputDevice.open();
   
         } catch (MidiUnavailableException e) {
             
            System.out.println(e);
            
            inputDevice.close();
            //outputDevice.close();
            
         }
        
    }
    
    public static void closeDevices() {
       
             inputDevice.close();
            // outputDevice.close();
  
    }
 
    
}
