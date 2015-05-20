package CTPV;

import java.awt.Component;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class HiloEscucha implements Runnable {

    private Socket cliente;
    private CTPV gui;
    private int cont;
    private ObjectInputStream entrada;
    private boolean seguirEnBloque;
    private HiloPrincipal hp;
    private BufferedWriter writer;

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
                    //System.out.println(aux.getClass());
                    String cadena = (String) aux;

                    //Si se le pasa salir => se cierra el TPV
                    if (cadena.equalsIgnoreCase("salir")) {
                        interna.setLblClienteServido("CLIENTE SERVIDO");

                        Component[] ventanas = gui.getPanel().getComponents();

                        gui.getPanel().remove(interna);
                        gui.getPanel().repaint();

                        seguirEnBloque = false;

                        //Inteneto modificar el contador
                        hp.restarCont();

                        hp.setHaySitio(true);
                        //System.out.println("Modifico haysitio a true: " + hp.getHaySitio());

                        //Escribir en fichero ventas.dat
                        File fichero = new File("ventas.dat");
                        if (!fichero.exists()) {
                            fichero.createNewFile();
                        }
                        FileWriter fw = new FileWriter(fichero.getAbsoluteFile(),true);
                        writer = new BufferedWriter(fw);

                        //System.out.println(interna.getModeloTabla().getDataVector().toString());
                        
                        //Obtengo el modelo de la tabla para sacar sus filas y columnas y asi luego recorrer
                        //con facilidad el modelo
                        DefaultTableModel modeloTabla = interna.getModeloTabla();
                        int filas = modeloTabla.getRowCount();
                        int columnas = modeloTabla.getColumnCount();
                        
                        //3 variables que seran las que estan dentro de cada linea del modelo
                        String producto = "";
                        int cantidad = 0;
                        float precioTotal = 0;
                        
                        //Creo un arrayList donde guardo los productos comprados
                        ArrayList<ProductoAuxiliar> array = new ArrayList<>();
                        //Recorro filas y columnas y dependiendo de que lea le asigno una variable u otra
                        for (int i = 0; i < filas; i++) {
                            for (int j = 0; j < columnas; j++) {
                                if (j == 0) {
                                    producto = (String) modeloTabla.getValueAt(i, j);
                                }
                                if (j == 1) {
                                    cantidad = Integer.parseInt((String) modeloTabla.getValueAt(i, j));
                                }
                                if (j == 2) {
                                    precioTotal = Float.parseFloat((String) modeloTabla.getValueAt(i, j));
                                }
                            }
                            array.add(new ProductoAuxiliar(producto, cantidad, precioTotal));
                        }
                        //Obtengo la fecha
                        Calendar cal = Calendar.getInstance();
                        Fecha fecha = new Fecha(cal.get(cal.HOUR_OF_DAY), cal.get(cal.MINUTE),
                                cal.get(cal.DATE), cal.get(cal.MONTH), cal.get(cal.YEAR));

                        //System.out.println(fecha.toString());
                        String escribir;
                        //Recorro el arraylist y escribo en el fichero
                        Iterator it = array.iterator();
                        while (it.hasNext()) {
                            ProductoAuxiliar pa = (ProductoAuxiliar) it.next();
                            //System.out.println(pa.toString());

                            escribir = pa.toString() + " Fecha: " + fecha.toString();

                            writer.write(escribir, 0, escribir.length());
                            writer.newLine();

                        }
                        writer.close();
                    }
                } else /*if (aux instanceof java.util.Vector) */ {

                    //Dos vectores auxiliares para recojer los datos enviados desde el TPV
                    //1º Los productos seleccionados
                    //2º Los encabezados
                    Vector auxVector, auxColumnas;

                    Vector[] arrayRecibido = (Vector[]) aux;

                    auxVector = arrayRecibido[0];
                    auxColumnas = arrayRecibido[1];

                    //System.out.println(auxVector.toString());
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
