/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package italiapizzasystem.utilidad;

import italiapizzasystem.excepciones.PedidoEstadoInvalidoException;
import italiapizzasystem.persistencia.dao.PedidoDAO;
import italiapizzasystem.persistencia.pojo.PedidoCliente;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author acrca
 */
public class PedidoFila {
private int idPedido;
    private String nombreCliente;
    private String direccion;
    private String fechaPedido;
    
    private ComboBox<String> comboEstado;
    private Button botonEditar;

    public PedidoFila(PedidoCliente pedidoBase) {
        this.idPedido = pedidoBase.getIdPedido();
        this.nombreCliente = pedidoBase.getNombreCliente();
        this.direccion = pedidoBase.getDireccion();
        this.fechaPedido = pedidoBase.getFechaPedido();
        this.comboEstado = new ComboBox<>(FXCollections.observableArrayList("En Proceso", "Entregado", "Cancelado"));
        this.comboEstado.setValue(pedidoBase.getStatus());
        
        // Se debe deshabilitar el cambio de estado si ya se encuentra cancelado o entregado
        if ("Entregado".equalsIgnoreCase(pedidoBase.getStatus()) || "Cancelado".equalsIgnoreCase(pedidoBase.getStatus())) {
            this.comboEstado.setDisable(true);
        }
        
        this.botonEditar = new Button("Editar");
        this.botonEditar.setOnAction(event -> accionEditar(pedidoBase.getStatus()));
    }

    private void accionEditar(String estado) {
        try {
            if ("Entregado".equalsIgnoreCase(estado) || "Cancelado".equalsIgnoreCase(estado)) {
                throw new PedidoEstadoInvalidoException("Solo se pueden editar los pedidos mientras estén 'En Proceso'.");
            }
        } catch (PedidoEstadoInvalidoException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Acción no permitida", ex.getMessage());
        }
    }

    public int getIdPedido() { return idPedido; }
    public String getNombreCliente() { return nombreCliente; }
    public String getDireccion() { return direccion; }
    public String getFechaPedido() { return fechaPedido; }
    public ComboBox<String> getComboEstado() { return comboEstado; }
    public Button getBotonEditar() { return botonEditar; }
}
