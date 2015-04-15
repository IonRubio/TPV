/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CTPV;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usuario
 */
public class HiloEscucha implements Runnable {

    private Socket cliente;
    private CTPV gui;
    private int cont;
    private BufferedReader flujoEntrada;

    public HiloEscucha(Socket cliente, CTPV gui, int cont) {
        this.cliente = cliente;
        this.cont = cont;
        this.gui = gui;
    }

    @Override
    public void run() {
        try {
            //Crear objeto ventana interna
            //Poner titulo a la ventana
            //Añadir al jpanel la ventanainterna
            //Hacer la ventana visible
            
            VentanaInterna interna = new VentanaInterna();
            interna.setTitle("Terminal TPV " + cont);
            gui.getjPanel3().add(interna);
            interna.setVisible(true);
            
            //FLUJO DE ENTRADA
            flujoEntrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            String cadena = flujoEntrada.readLine();
            System.out.println("Recibiendo:" + cadena);
            
            if(cadena.equals("Salir")){
                interna.setLblClienteServido("CLIENTE SERVIDO");
            }
            
        } catch (IOException ex) {
            Logger.getLogger(HiloEscucha.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
