/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package italiapizzasystem.utilidad;

import italiapizzasystem.excepciones.PedidoEstadoInvalidoException;
import italiapizzasystem.persistencia.dao.PedidoDAO;
import italiapizzasystem.persistencia.pojo.PedidoCliente;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;

/**
 *
 * @author acrca
 */
public class PedidoFila {
    
    private static final Logger LOGGER = Logger.getLogger(PedidoFila.class.getName());
    
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
        
        this.botonEditar = new Button("Editar");
        this.botonEditar.setOnAction(event -> accionEditar(pedidoBase.getStatus()));
        
        verificarEstadoPedido(pedidoBase.getStatus());
        configurarEventosComboBox();
        estilizarComboBox();
        estilizarBotonEditar();
    }
    
    private void configurarEventosComboBox() {
        this.comboEstado.valueProperty().addListener((observable, valorAnterior, valorNuevo) -> {
            if (valorNuevo == null || valorNuevo.equals(valorAnterior)) {
                return;
            }

            try {
                boolean actualizacionExitosa = PedidoDAO.actualizarEstatusPedido(this.idPedido, valorNuevo);

                if (actualizacionExitosa) {
                    Utilidad.mostrarAlertaSimple(
                        Alert.AlertType.INFORMATION, 
                        "Estatus actualizado", 
                        "El estado del pedido #" + this.idPedido + " se actualizó a: " + valorNuevo
                    );
                    verificarEstadoPedido(valorNuevo);
                }

            } catch (SQLException ex) {
                this.comboEstado.setValue(valorAnterior);
                
                LOGGER.log(Level.SEVERE, "Error al actualizar el estatus del pedido #" + this.idPedido, ex);
                Utilidad.mostrarAlertaSimple(
                    Alert.AlertType.ERROR, 
                    "Error de Conexión", 
                    "No se pudo guardar el cambio de estado en la base de datos."
                );
            }
        });
    }
    
    private void verificarEstadoPedido(String estado) {
        if ("Entregado".equalsIgnoreCase(estado) || "Cancelado".equalsIgnoreCase(estado)) {
            this.comboEstado.setDisable(true);
        }
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
    
    private void estilizarComboBox() {
        this.comboEstado.setPrefWidth(150.0);
        this.comboEstado.setCursor(Cursor.HAND);
        this.comboEstado.setStyle(
            "-fx-background-color: #FFFFFF; " +
            "-fx-border-color: #CCCCCC; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-font-size: 13px;"
        );
    }

    private void estilizarBotonEditar() {
        this.botonEditar.setPrefWidth(90.0);
        configurarEfectoHoverBoton();
    }

    private void configurarEfectoHoverBoton() {
        String estiloBase = 
            "-fx-background-color: #007BFF; " + 
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 5 15 5 15; " +
            "-fx-font-size: 12px;";
            
        String estiloHover = 
            "-fx-background-color: #0056b3; " + 
            "-fx-text-fill: white; " +
            "-fx-font-weight: bold; " +
            "-fx-border-radius: 5; " +
            "-fx-background-radius: 5; " +
            "-fx-padding: 5 15 5 15; " +
            "-fx-font-size: 12px;";

        this.botonEditar.setStyle(estiloBase);
        this.botonEditar.setCursor(Cursor.HAND);

        this.botonEditar.setOnMouseEntered(e -> this.botonEditar.setStyle(estiloHover));
        this.botonEditar.setOnMouseExited(e -> this.botonEditar.setStyle(estiloBase));
    }
}
