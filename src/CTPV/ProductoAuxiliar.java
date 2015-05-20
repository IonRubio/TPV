package CTPV;

public class ProductoAuxiliar {

    private String nombreProducto;
    private int cantidad;
    private float precioTotal;

    public ProductoAuxiliar(String nombreProducto, int cantidad, float precioTotal) {
        this.nombreProducto = nombreProducto;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public float getPrecioTotal() {
        return precioTotal;
    }

    public void setPrecioTotal(float precioTotal) {
        this.precioTotal = precioTotal;
    }

    @Override
    public String toString() {
        return nombreProducto + " " + cantidad + " " + precioTotal;
    }

}
