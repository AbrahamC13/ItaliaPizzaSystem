package italiapizzasystem.persistencia.pojo;

/**
 *
 * @author Gerardo
 */
public class Pedido {
    private int idPedido;
    private String fechaPedido;
    private String status;
    private int idCliente;
    private int idEmpleado;

    public int getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(int idPedido) {
        this.idPedido = idPedido;
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

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public int getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(int idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Pedido() {
    }

    public Pedido(int idPedido, String fechaPedido, String status, int idCliente, int idEmpleado) {
        this.idPedido = idPedido;
        this.fechaPedido = fechaPedido;
        this.status = status;
        this.idCliente = idCliente;
        this.idEmpleado = idEmpleado;
    }
    
}
