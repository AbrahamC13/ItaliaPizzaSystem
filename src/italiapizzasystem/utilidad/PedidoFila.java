/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package italiapizzasystem.utilidad;

import italiapizzasystem.persistencia.pojo.PedidoCliente;

/**
 *
 * @author acrca
 */
public class PedidoFila {
    
    private final int idPedido;
    private final String nombreCliente;
    private final String direccion;
    private final String fechaPedido;
    private final String status;
    private final PedidoCliente pedidoCliente; 

    public PedidoFila(PedidoCliente pedidoBase) {
        this.idPedido = pedidoBase.getIdPedido();
        this.nombreCliente = pedidoBase.getNombreCliente();
        this.direccion = pedidoBase.getDireccion();
        this.fechaPedido = pedidoBase.getFechaPedido();
        this.status = pedidoBase.getStatus();
        this.pedidoCliente = pedidoBase; 
    }

    public int getIdPedido() { return idPedido; }
    public String getNombreCliente() { return nombreCliente; }
    public String getDireccion() { return direccion; }
    public String getFechaPedido() { return fechaPedido; }
    public String getStatus() { return status; }
    public PedidoCliente getPedidoCliente() { return pedidoCliente; }
}
