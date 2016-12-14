/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package styloconnecte;

import com.fazecast.jSerialComm.SerialPort;
import java.util.Scanner;

/**
 *
 * @author Guillemfranck
 */
public class ArduinoCommu {

    public static boolean ArduinoCom() {
        SerialPort[] ports = SerialPort.getCommPorts();
        System.out.println("Selectionnez le port a utiliser :");
        int i = 1;
        for (SerialPort port : ports) {
            System.out.println(i++ + " : " + port.getSystemPortName());
        }
        Scanner s = new Scanner(System.in);
        int chosenPort = s.nextInt();

        SerialPort serialPort = ports[chosenPort - 1];
        if (serialPort.openPort()) {
            System.out.println("Port ouvert avec succès.");
            System.out.println("Detection de la main en cours ..");
            System.out.println("Veuillez patientez ..");
        } else {
            System.out.println("Impossible d'ouvrir port.");
            return false;
        }
        //serialPort.setComPortParameters(9600, 8, 1, SerialPort.NO_PARITY);
        serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_BLOCKING, 0, 0);

        Scanner data = new Scanner(serialPort.getInputStream());
        int value = 0;

        while (data.hasNextLine()) {
            try {
                value = Integer.parseInt(data.nextLine());
            } catch (Exception e) {
            }
            if (value == 1) {
                break;
            }

        }
        return true;
    }
}
