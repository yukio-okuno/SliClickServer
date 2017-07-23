/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sliclickserver;

import java.awt.AWTException;
import java.io.*;
import java.util.*; //import gnu.io.*;
import javax.comm.*;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class SimpleRead implements Runnable, SerialPortEventListener {
    static CommPortIdentifier portId;
    static Enumeration portList;

InputStream inputStream;
SerialPort serialPort;
Thread readThread;
byte[] readBuffer;

public static void main(String[] args) {
    portList = CommPortIdentifier.getPortIdentifiers();
    System.out.println("portList... " + portList);
    while (portList.hasMoreElements()) {
        portId = (CommPortIdentifier) portList.nextElement();
        if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
            System.out.println("port identified is Serial.. "
                    + portId.getPortType());
            if (portId.getName().equals("COM4")) {
                System.out.println("port identified is COM4.. "
                        + portId.getName());
                // if (portId.getName().equals("/dev/term/a")) {
                SimpleRead reader = new SimpleRead();
            } else {
                System.out.println("unable to open port");
            }
        }
    }
}

public SimpleRead() {
    try {
        System.out.println("In SimpleRead() contructor");
        serialPort = (SerialPort) portId.open("SimpleReadApp1111",500);
        System.out.println(" Serial Port.. " + serialPort);
    } catch (PortInUseException e) {
        System.out.println("Port in use Exception");
    }
    try {
        inputStream = serialPort.getInputStream();
        System.out.println(" Input Stream... " + inputStream);
    } catch (IOException e) {
        System.out.println("IO Exception");
    }
    try {
        serialPort.addEventListener(this);

    } catch (TooManyListenersException e) {
        System.out.println("Tooo many Listener exception");
    }
    serialPort.notifyOnDataAvailable(true);
    try {

        serialPort.setSerialPortParams(9600, SerialPort.DATABITS_8,
                SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);

        // no handshaking or other flow control
        serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);

        // timer on any read of the serial port
        serialPort.enableReceiveTimeout(500);

        System.out.println("................");

    } catch (UnsupportedCommOperationException e) {
        System.out.println("UnSupported comm operation");
    }
    readThread = new Thread(this);
    readThread.start();
}

public void run() {
    try {
        System.out.println("In run() function ");
        Thread.sleep(2000);
        // System.out.println();
    } catch (InterruptedException e) {
        System.out.println("Interrupted Exception in run() method");
    }
}

public void serialEvent(SerialPortEvent event) {

    // System.out.println("In Serial Event function().. " + event +
    // event.getEventType());
    switch (event.getEventType()) {
    /*
     * case SerialPortEvent.BI: case SerialPortEvent.OE: case
     * SerialPortEvent.FE: case SerialPortEvent.PE: case SerialPortEvent.CD:
     * case SerialPortEvent.CTS: case SerialPortEvent.DSR: case
     * SerialPortEvent.RI: case SerialPortEvent.OUTPUT_BUFFER_EMPTY: break;
     */
    case SerialPortEvent.DATA_AVAILABLE:
        readBuffer = new byte[8];

        try {

            while (inputStream.available()>0) {

                int numBytes = inputStream.read(readBuffer);
                String s = new String(readBuffer);
                System.out.print(s);

                if (s.trim().equals("a0")){
                    try {
                        //System.out.print("avanca");
                        Robot robo = new Robot();
                        
                        //robo.mousePress(InputEvent.BUTTON1_MASK);
                        //robo.mouseRelease(InputEvent.BUTTON1_MASK);
                        
                        robo.keyPress(KeyEvent.VK_RIGHT);
                        
                    } catch (AWTException ex) {
                        Logger.getLogger(SimpleRead.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }

                
                if (s.trim().equals("v0")){
                    try {
                        //System.out.print("volta");
                        Robot robo = new Robot();
                        
                        //robo.mousePress(InputEvent.BUTTON1_MASK);
                        //robo.mouseRelease(InputEvent.BUTTON1_MASK);
                        
                        robo.keyPress(KeyEvent.VK_LEFT);
                        
                    } catch (AWTException ex) {
                        Logger.getLogger(SimpleRead.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
            }

            
        } catch (IOException e) {
            System.out.println("IO Exception in SerialEvent()");
        }
        break;
    }
    // System.out.println();
/*  String one = new String(readBuffer);
    char two = one.charAt(0);
    System.out.println("Character at three: " + two);*/
}

}
