package tpv;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.border.LineBorder;

/**
 * Esta clase modela una familia de productos, conteniendo todos pos productos pertenecientes
 * a la familia en un ArrayList de productos
 * @author IVAN
 */
@SuppressWarnings("serial")
public class Familia extends JButton {
    
    //----------CAMPOS
    String nombre;                          //El nombre de la familia
    private ArrayList<Producto> productos;  // La lista con todos los productos
    private TPVJFrame tpv;                  // El TPW al que pertenece la familia

    //----------CONSTRUCTOR
    /**
     * Constructor de una familia de productos, a su vez crea todos los productos
     * dependientes de la familia
     * @param nombre    El nombre de la familia
     * @param productos Los productos que pertenecen a esta familia
     * @param tpv       El TPW al que pertenece la familia a crear
     */
    public Familia(String nombre, ArrayList<Producto> productos, TPVJFrame tpv) {
        super(nombre);
        setForeground(Color.WHITE);
        this.nombre = nombre;
        this.tpv = tpv;
        setToolTipText(nombre);
        setBorder(new LineBorder(Color.WHITE));
        setContentAreaFilled(false);
        crearListaProductos(productos, tpv);
    }

    //----------METODOS
    /**
     * Este metodo crea todos los productos que se incluyen en la familia
     * @param listaProductos    La lista de los productos que forman parte de la familia
     * @param tpw               El TPW donde estaran los productos
     */
    private void crearListaProductos(ArrayList<Producto> listaProductos, TPVJFrame tpv) {
        productos = new ArrayList<>();
        for (Producto producto : listaProductos) {
            final Producto nuevoProducto = producto;
            nuevoProducto.setTpv(tpv);
            nuevoProducto.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nuevoProducto.añadirAFactura(nuevoProducto);
                }
            });
            productos.add(nuevoProducto);
        }
    }

    /**
     * Envia los productos de esta familia al TPW cuando este los solicita, para 
     * que puedan ser mostrados
     */
    public void mostrarProductos() {
        tpv.mostrarPanel(productos);
    }
}