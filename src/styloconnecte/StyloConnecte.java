/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package styloconnecte;

import java.awt.AWTException;
import java.awt.Robot;
import java.util.*;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import javax.imageio.ImageIO;

/**
 *
 * @author Guillemfranck
 */
public class StyloConnecte {

    public static Robot robot = null;
    public static List<Integer> posClickX = new ArrayList<Integer>();
    public static List<Integer> posClickY = new ArrayList<Integer>();
    public static int setXFrame, setYFrame, setWidthFrame, setHeightFrame;
    public static String theFolderName;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here          
        System.out.println("En attente du mot de passe de synchronisation ");
        java.util.Scanner sc = new java.util.Scanner(System.in);
        if (sc.nextInt() == 123) {
            /* --- Initializing --- */
            try {
                //Using robot class for simulate clicks
                robot = new Robot();
            } catch (AWTException e) {
                e.printStackTrace();
            }

            System.out.println("Synchronisation avec le stylo en cours ..");
            robot.delay(1000);
            drawRegister();

        } else {
            System.out.println("Synchronisation annulée ..");
        }
    }

    public static void drawRegister() {
        /*Si notre arduino a detecte une main on écrit sinon non*/
        boolean stateArduino = ArduinoCommu.ArduinoCom();

        if (stateArduino == false) {

            System.out.println("Aucune main détectée");
            System.out.println("Synchronisation annulée");
        } else {
            System.out.println("Main de l'utilisateur détectée");
            robot.delay(1000);
            System.out.println("Synchronisation OK");
            robot.delay(1000);
            System.out.println("Vous pouvez écrire");
            theFolderName = createPrincipalFolder();

            DrawWindows dwin = new DrawWindows();
            setXFrame = dwin.getX();
            setYFrame = dwin.getY();
            setWidthFrame = dwin.getWidth();
            setHeightFrame = dwin.getHeight();
            ButtonsWindows bwin = new ButtonsWindows();
            bwin.setLocation(1150, 350);
        }
    }

    public static int setXFrame() {
        return setXFrame();
    }

    public static int setYFrame() {
        return setYFrame();
    }

    public static int setWidthFrame() {
        return setWidthFrame();
    }

    public static int setHeightFrame() {
        return setHeightFrame();
    }

    public static void saveImageWords(int x, int y, int w, int h) throws AWTException, IOException {

        String dirNameFile = createImagesFolder();
        //Rectangle screenRect = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
        Rectangle screenRect = new Rectangle(x, y + 20, w, h - 20);
        BufferedImage screencapture = new Robot().createScreenCapture(screenRect);
        ImageIO.write(screencapture, "jpg", new File(dirNameFile));

    }

    public static String createPrincipalFolder() {
        String User = System.getProperty("user.name");
        String FileDir = "/Users/" + User + "/Documents/";
        boolean folderExist = false;
        File file = new File(FileDir);
        String[] names = file.list();
        for (String name : names) {
            if (new File(FileDir + name).isDirectory()) {
                if (name == "MyWords_StyloConnecte") {
                    folderExist = true;
                }
            }
        }
        if (folderExist == false) {
            File MyWords_StyloConnecte = new File(FileDir + "MyWords_StyloConnecte");
            MyWords_StyloConnecte.mkdir();
        }
        Date date = new Date();
        File dir = new File(FileDir + "MyWords_StyloConnecte/" + date.toString());
        dir.mkdir();
        FileDir = FileDir + "MyWords_StyloConnecte/" + date.toString() + "/";

        return FileDir;
    }

    public static String createImagesFolder() {

        int fileExist = 1;
        File file = new File(theFolderName);
        String[] names = file.list();
        for (String name : names) {
            if (new File(theFolderName + name).isFile()) {
                if (name.contains("word")) {
                    fileExist++;
                }
            }
        }
        String screenName = "word" + Integer.toString(fileExist) + ".jpg";
        String dirNameFile = theFolderName + screenName;
        return dirNameFile;
    }

}
