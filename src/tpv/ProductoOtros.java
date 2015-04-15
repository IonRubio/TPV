/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package tpv;

/**
 *
 * @author Diurno
 */
public class ProductoOtros extends ProductoPedido {
    private float total;

    public ProductoOtros(String nombre, float precio, int cantidad,float total) {
        super(nombre, precio, cantidad);
        this.total = total;
    }
    
    
    @Override
    public float getTotal(){
        return total;
    }
    
    public String[] getProducto(){
        String[] producto = {getNombre(), "" + getCantidad(), redondear(total)};
        return producto;
    }
    
    
}
