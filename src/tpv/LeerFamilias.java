package tpv;

import java.io.File;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.xml.parsers.*;
import org.w3c.dom.*;

/**
 * Esta clase permite leer los archivos XML donde se encuentran los productos de
 * cada familia.
 * @author IVAN
 */
public abstract  class LeerFamilias {
    //--------METODOS
    /**
     * Transforma los archivos XML en objetos Familia con su lista de productos
     * @param tpv El TPV donde añadiremos las familias
     * @return Un ArrayList con las familias que tendra el TPV
     */
    public static ArrayList<Familia> leerFamilias(TPVJFrame tpv){
        String sDirectorio = "..\\TPV\\src\\familias";          // Directorio de los archivos XML
        File f = new File(sDirectorio);                         // Creamos un File con el directorio para poder recorrerlo
        ArrayList<Familia> listaFamilias = new ArrayList<>();   // Lista con todas las familias que se encuentren
        
        //Si existe el directorio procedemos a leer los archivos
        if (f.exists()) {
            //Array con los ficheros XML
            File[] ficheros = f.listFiles();
            //Recorremos los distintos ficheros
            for (int x = 0; x < ficheros.length; x++) {
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                
                //Lista para echar los productos de la familia
                ArrayList<Producto> arrayProductos = new ArrayList<>();
                String nombreFamilia="";

                try {
                    DocumentBuilder builder = factory.newDocumentBuilder();
                    Document documento = builder.parse(ficheros[x]);
                    documento.getDocumentElement().normalize();

                    Element raiz = documento.getDocumentElement();
                    String etiqueta = raiz.getFirstChild().getNodeName();
                    NodeList listaProductos = documento.getElementsByTagName(etiqueta);
                    //Leemos los nodos del XML
                    for (int i = 0; i < listaProductos.getLength(); i++) {
                        //Cogemos un producto(nodo)
                        Node producto = listaProductos.item(i);
                        //Si el nodo es un elemento, sacamos sus valores
                        if (producto.getNodeType() == Node.ELEMENT_NODE) {
                            Element elemento = (Element) producto;
                            NodeList listaDatos = elemento.getChildNodes();
                            String nombre;
                            //El primer elemento es el que da nombre a la familia
                            if (i == 0) {
                                nombreFamilia = listaDatos.item(0).getTextContent();
                            } 
                            //El resto de elementos son los productos de la familia
                            else {
                                nombre = listaDatos.item(0).getTextContent();
                                String precio = listaDatos.item(1).getTextContent();
                                String img = listaDatos.item(2).getTextContent();
                                Producto jProducto = new Producto(nombre, Float.parseFloat(precio), new ImageIcon(img));
                                arrayProductos.add(jProducto);
                            }
                        }
                    }
                } 
                // En caso de error en la lectura se lo notificamos al usuario
                catch (Exception e) {
                    JOptionPane.showMessageDialog(null,"Error al leer los ficheros de configuración",
                            "Lectura erronea",JOptionPane.ERROR_MESSAGE);
                }
                // Creamaos la familia y la añadimos a la lista
                Familia familia = new Familia(nombreFamilia, arrayProductos, tpv);
                listaFamilias.add(familia);
            }
        }
        // Si el directorio no existe se lo notificamos al usuario
        else{
            JOptionPane.showMessageDialog(null,"No se encontro el directorio con los archivos de configuracion",
                    "Faltan archivos",JOptionPane.ERROR_MESSAGE);
        }
        return listaFamilias;
    }
}