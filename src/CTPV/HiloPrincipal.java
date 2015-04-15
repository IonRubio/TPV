/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CTPV;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JDesktopPane;

/**
 *
 * @author Usuario
 */
public class HiloPrincipal implements Runnable {

    private CTPV gui;
    private int cont;

    public HiloPrincipal(CTPV gui) {
        this.gui = gui;
        cont = 1;
    }

    @Override
    public void run() {
        try {
            ServerSocket servidor = new ServerSocket(6000);
            System.out.println("Servidor iniciado");

            while (cont < 7) {
                //Creo un cliente que se instanciara cuando uno se conecte
                Socket cliente = new Socket();
                cliente = servidor.accept();
                
                //Le paso lo necesario a la clase HiloEscucha para que cree la ventana del cliente
                HiloEscucha escucha=new HiloEscucha(cliente, gui, cont);
                
                Thread th=new Thread(escucha);
                th.start();
                
                cont++;
            }
        } catch (IOException ex) {
            Logger.getLogger(HiloPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
