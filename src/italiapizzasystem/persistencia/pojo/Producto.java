package italiapizzasystem.persistencia.pojo;

/**
 *
 * @author Gerardo
 */
public class Producto {
    private int idProducto;
    private String codigoProducto;
    private String descripcion;
    private String nombre;
    private double precio;
    
    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public String getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(String codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }
    
    public Producto(){
        
    }

    public Producto(int idProducto, String codigoProducto, String descripcion, String nombre, double precio) {
        this.idProducto = idProducto;
        this.codigoProducto = codigoProducto;
        this.descripcion = descripcion;
        this.nombre = nombre;
        this.precio = precio;
    }
    
}
