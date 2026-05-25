/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package italiapizzasystem.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

/**
 * FXML Controller class
 *
 * @author acrca
 */
public class FXMLEditarPedidoController implements Initializable {

    @FXML
    private AnchorPane ap_ContenedorProductos;
    @FXML
    private FlowPane fp_productos;
    @FXML
    private Label lb_Fecha;
    @FXML
    private Label lb_IdPedido;
    @FXML
    private TableView<?> tbl_Orden;
    @FXML
    private TableColumn<?, ?> col_Producto;
    @FXML
    private TableColumn<?, ?> col_Cantidad;
    @FXML
    private TableColumn<?, ?> col_Subtotal;
    @FXML
    private TableColumn<?, ?> col_Quitar;
    @FXML
    private Button btn_Cancelar;
    @FXML
    private Button btn_RealizarPedido;
    @FXML
    private Label lb_Total;
    @FXML
    private Label lb_NombreCliente;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btn_clicCancelar(ActionEvent event) {
    }

    @FXML
    private void btn_clicRealizarPedido(ActionEvent event) {
    }
    
}
