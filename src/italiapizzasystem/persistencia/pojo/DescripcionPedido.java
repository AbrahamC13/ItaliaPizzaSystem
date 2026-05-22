package italiapizzasystem.persistencia.pojo;

/**
 *
 * @author Gerardo
 */
public class DescripcionPedido {

    private int idProducto;
    private int idPedido;
    private int cantidad;
    private double total;

    public DescripcionPedido() {

    }

    public DescripcionPedido(int idProducto,
            int idPedido, int cantidad,
            double total) {

        this.idProducto = idProducto;
        this.idPedido = idPedido;
        this.cantidad = cantidad;
        this.total = total;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }
}