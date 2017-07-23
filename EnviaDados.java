/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sliclickserver;

import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPort;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.comm.SerialPortEvent;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class EnviaDados {
    private OutputStream serialOut;
    private int taxa;
    private String portaCOM;
    byte[] readBuffer;
    InputStream inputStream;

    public EnviaDados(String portaCOM, int taxa) {
        this.portaCOM = portaCOM;
        this.taxa = taxa;
        this.initialize();
    }

    private void initialize() {
        try {
          //Define uma variável portId do tipo CommPortIdentifier para realizar a comunicação serial
          CommPortIdentifier portId = null;
          try {
            //Tenta verificar se a porta COM informada existe
            portId = CommPortIdentifier.getPortIdentifier(this.portaCOM);
          }catch (NoSuchPortException npe) {
            //Caso a porta COM não exista será exibido um erro 
            JOptionPane.showMessageDialog(null, "Porta COM não encontrada.",
                      "Porta COM", JOptionPane.PLAIN_MESSAGE);
          }
          //Abre a porta COM 
          SerialPort port = (SerialPort) portId.open("Comunicação serial", this.taxa);
          serialOut = port.getOutputStream();
          port.setSerialPortParams(this.taxa, //taxa de transferência da porta serial 
                                   SerialPort.DATABITS_8, //taxa de 10 bits 8 (envio)
                                   SerialPort.STOPBITS_1, //taxa de 10 bits 1 (recebimento)
                                   SerialPort.PARITY_NONE); //receber e enviar dados
        }catch (Exception e) {
          e.printStackTrace();
        }
    }

    public void close() {
        try {
            System.out.println("tentando fechar..");
            serialOut.close();
            System.out.println("fechou");
        }catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Não foi possível fechar porta COM.","Fechar porta COM", JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    public void enviaDados(int opcao){
        try {
            serialOut.write(opcao);//escreve o valor na porta serial para ser enviado
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Não foi possível enviar o dado. ","Enviar dados", JOptionPane.PLAIN_MESSAGE);
        }
    }

    public void serialEvent(SerialPortEvent event) {
        switch (event.getEventType()) {
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

    }

}
