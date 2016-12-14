/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package styloconnecte;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JFrame;
import static styloconnecte.StyloConnecte.saveImageWords;

/**
 *
 * @author Guillemfranck
 */
public class ButtonsWindows extends JFrame implements ActionListener {

    private JButton bouton = new JButton("Sauvegarder et Quitter");

    public ButtonsWindows() {

        this.setTitle("Aide");
        this.setBounds(10, 10, 200, 150);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        //Nous ajoutons notre fenêtre à la liste des auditeurs de notre bouton
        bouton.addActionListener(this);
        //On ajoute le bouton au content pane de la JFrame
        this.getContentPane().add(bouton);
        //this.getContentPane().add(la);
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        try {
            saveImageWords(StyloConnecte.setXFrame, StyloConnecte.setYFrame,
                    StyloConnecte.setWidthFrame, StyloConnecte.setHeightFrame);
        } catch (Exception c) {
            throw new RuntimeException(c);
        }
        this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
    }
}
