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

public class HiloEscucha implements Runnable {

    private Socket cliente;
    private CTPV gui;
    private int cont;
    private ObjectInputStream entrada;
    private boolean seguirEnBloque;
    private HiloPrincipal hp;

    public HiloEscucha(Socket cliente, CTPV gui, int cont, HiloPrincipal hp) {
        this.cliente = cliente;
        this.cont = cont;
        this.gui = gui;
        this.hp = hp;
        seguirEnBloque = true;
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

            entrada = new ObjectInputStream(cliente.getInputStream());

            //Mientras el cliente no cierre su TPV, seguira escuchando
            while (seguirEnBloque) {
                Object aux = entrada.readObject();

                if (aux instanceof String) {

                    //Recojo el objeto y comparo su tipo
                    System.out.println(aux.getClass());

                    String cadena = (String) aux;

                    //Si se le pasa salir => se cierra el TPV
                    if (cadena.equalsIgnoreCase("salir")) {
                        interna.setLblClienteServido("CLIENTE SERVIDO");
                        System.out.println("Cliente servido");

                        Component[] ventanas = gui.getPanel().getComponents();
                        System.out.println("Componentes cargados en ventanas[]");

                        gui.getPanel().remove(interna);
                        System.out.println("Remove hecho");
                        gui.getPanel().repaint();
                        System.out.println("Repaint hecho");

                        seguirEnBloque = false;
                        hp.restarCont();

                    }
                } else /*if (aux instanceof java.util.Vector) */ {
                    System.out.println("ENTRO!!! Vector recibido");

                    //Dos vectores auxiliares para recojer los datos enviados desde el TPV
                    //1º Los productos seleccionados
                    //2º Los encabezados
                    Vector auxVector, auxColumnas;

                    Vector[] arrayRecibido = (Vector[]) aux;

                    auxVector = arrayRecibido[0];
                    auxColumnas = arrayRecibido[1];

                    System.out.println(auxVector.toString());

                    //Variables para tener a mano la tabla de la VentanaInterna y su modelo
                    JTable tablaInterna = interna.getTable();
                    DefaultTableModel modeloInterno = (DefaultTableModel) tablaInterna.getModel();

                    //Se actualiza el modelo de la VentanaInterna con los productos recibidos y con los encabezados
                    modeloInterno.setDataVector(auxVector, auxColumnas);

                }
            }

        } catch (IOException ex) {
            System.err.println("ERROR ENTRADA SALIDA");
            Logger.getLogger(HiloEscucha.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(HiloEscucha.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    
}
