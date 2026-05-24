/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package italiapizzasystem.persistencia.pojo;

import javafx.scene.control.Button;

/**
 *
 * @author acrca
 */
public class OrdenFila {
    private int idProducto;
    private String nombreProducto;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private Button btnQuitar;
    
    private Producto productoBase;

    public OrdenFila(Producto producto) {
        this.productoBase = producto;
        this.idProducto = producto.getIdProducto();
        this.nombreProducto = producto.getNombre();
        this.precioUnitario = producto.getPrecio();
        this.cantidad = 1; 
        this.subtotal = producto.getPrecio();
        
        this.btnQuitar = new Button("-");
        
        this.btnQuitar.setStyle(
            "-fx-background-color: #FF3B30; " +
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-background-radius: 15; " +
            "-fx-cursor: hand;"
        );
        this.btnQuitar.setPrefWidth(50.0);
    }

    public void actualizarCantidad(int nuevaCantidad) {
        this.cantidad = nuevaCantidad;
        this.subtotal = this.precioUnitario * nuevaCantidad;
    }

    public int getIdProducto() { return idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public int getCantidad() { return cantidad; }
    public double getSubtotal() { return subtotal; }
    public Button getBtnQuitar() { return btnQuitar; }
    public Producto getProductoBase() { return productoBase; }
}
