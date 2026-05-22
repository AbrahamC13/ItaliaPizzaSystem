package italiapizzasystem.persistencia.pojo;

/**
 *
 * @author acrca
 */
public class PedidoCliente {

    private int idPedido;
    private String nombreCliente;
    private String direccion;
    private String fechaPedido;
    private String status;

    public PedidoCliente() {

    }

    public PedidoCliente(int idPedido, String nombreCliente,
            String direccion, String fechaPedido,
            String status) {

        this.idPedido = idPedido;
        this.nombreCliente = nombreCliente;
        this.direccion = direccion;
        this.fechaPedido = fechaPedido;
        this.status = status;
    }

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(String fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}