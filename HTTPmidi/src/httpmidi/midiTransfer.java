/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package httpmidi;

import java.io.PrintStream;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Receiver;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;


/**
 *
 * @author Carrie
 */
public class midiTransfer implements Receiver {
    
    private PrintStream	midiOutput;

    public midiTransfer(PrintStream printStream){
	
        midiOutput = printStream;
       
    }
       
    @Override
    public void close() {
    }
     
    @Override
    public void send(MidiMessage message, long lTimeStamp) {
        
        if (message instanceof ShortMessage) {
           
            int[] midiData = getMidiData((ShortMessage) message);

            showMidiData(midiData);
              
	} 
      
    }
    
    //Returns MIDI message data as an array to be read by the relay:
    private int[] getMidiData (ShortMessage message) { 
    
        int[] midiData = new int[4];
    
            midiData[0] = message.getCommand();
            midiData[1] = message.getChannel();
            midiData[2] = message.getData1();
            midiData[3] = message.getData2();
            
        return midiData; 
    }
    
    //  Writes midi data to console:
    private void showMidiData(int[] midiData) {
        
        int command = midiData[0];
        int channel = midiData[1];
        int data1   = midiData[2];
        int data2   = midiData[3];
         
            if (command != 0xF0 || data1 < 0xf0) {
            
                // Currently supports only 'NOTE_ON' commands:
                if (command == 144) {
                    
                     int inputNote      = Integer.valueOf(String.valueOf(data1), 16);
                     //System.out.println("Input Note:  " + inputNote + " on Channel " + channel);
                     
                     HTTPpost.send("note=" + inputNote);
                   
                } 
                
            }
        
    }

}
