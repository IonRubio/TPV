/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CTPV;

import java.awt.Component;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author Usuario
 */
public class HiloEscucha implements Runnable {

    private Socket cliente;
    private CTPV gui;
    private int cont;
    //private BufferedReader flujoEntrada;
    private ObjectInputStream entrada;

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
            gui.getPanel().add(interna);
            interna.setTitle("Terminal TPV " + cont);
            interna.setVisible(true);

            //FLUJO DE ENTRADA
//            flujoEntrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
//            String cadena = flujoEntrada.readLine();
//            System.out.println("Recibiendo:" + cadena);
//            flujoEntrada.close();

            entrada = new ObjectInputStream(cliente.getInputStream());

            Object aux = entrada.readObject();

            if (aux instanceof String) {

                System.out.println("Recojo el objeto y comparo su tipo");
                System.out.println(aux.getClass());

                String cadena = (String) aux;
                if (cadena.equalsIgnoreCase("salir")) {
                    interna.setLblClienteServido("CLIENTE SERVIDO");
                    System.out.println("Cliente servido");

                    Component[] ventanas = gui.getPanel().getComponents();
                    System.out.println("Componentes cargados en ventanas[]");

                    gui.getPanel().remove(interna);
                    System.out.println("Remove hecho");
                    gui.getPanel().repaint();
                    System.out.println("Repaint hecho");
                    //Thread.sleep(500);

                }
            } else /*if (aux instanceof java.util.Vector) */{
                System.out.println("ENTRO!!! Vector recibido");
                
                Vector auxVector,auxColumnas;
                
                Vector[] arrayRecibido= (Vector[])aux;
                
                auxVector=arrayRecibido[0];
                auxColumnas=arrayRecibido[1];
                
                
                JTable tabla=interna.getTable();
                DefaultTableModel modelo=(DefaultTableModel) tabla.getModel();
                
                
                System.out.println(auxVector.toString());
                
                JTable tablaInterna=interna.getTable();
                DefaultTableModel modeloInterno=(DefaultTableModel) tablaInterna.getModel();
                
                                
                modeloInterno.setDataVector(auxVector, auxColumnas);
                
                
                
            }
        } catch (IOException ex) {
            System.err.println("ERROR ENTRADA SALIDA");
            Logger.getLogger(HiloEscucha.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HiloEscucha.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
