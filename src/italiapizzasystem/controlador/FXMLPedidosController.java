package italiapizzasystem.controlador;

import italiapizzasystem.ItaliaPizzaSystem;
import italiapizzasystem.persistencia.dao.PedidoDAO;
import italiapizzasystem.persistencia.pojo.PedidoCliente;
import italiapizzasystem.utilidad.EfectoBotones;
import italiapizzasystem.utilidad.PedidoFila;
import italiapizzasystem.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Abraham
 */
public class FXMLPedidosController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(FXMLPedidosController.class.getName());
    @FXML
    private TableColumn<PedidoFila, Integer> col_IdPedido;
    @FXML
    private TableColumn<PedidoFila, String> col_NombreCliente;
    @FXML
    private TableColumn<PedidoFila, String> col_Direccion;
    @FXML
    private TableColumn<PedidoFila, String> col_Fecha;
    @FXML
    private TableColumn<PedidoFila, String> col_Status;
    @FXML
    private TableColumn<PedidoFila, Void> col_Editar;
    @FXML
    private TextField tf_NombreCliente;
    @FXML
    private TableView<PedidoFila> tbl_Pedidos;
    
    public ObservableList<PedidoFila> listaPedidosFila;
    
    @FXML
    private Button btn_NuevoPedido;
    @FXML
    private Button btn_Exportar;
    @FXML
    private Button btn_Regresar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatosTabla();
        EfectoBotones.darEfectoBotones(btn_NuevoPedido, btn_Exportar, btn_Regresar);
    }    
    
    private void configurarTabla() {
        col_IdPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        col_NombreCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        col_Direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        col_Fecha.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));
        col_Status.setCellValueFactory(new PropertyValueFactory<>("comboEstado"));
        col_Editar.setCellValueFactory(new PropertyValueFactory<>("botonEditar"));
    }
    
    private void cargarDatosTabla() {
        listaPedidosFila = FXCollections.observableArrayList();
        try {
            List<PedidoCliente> datosBD = PedidoDAO.obtenerPedidosConCliente();
            
            for (PedidoCliente pedidoBD : datosBD) {
                PedidoFila filaInteractiva = new PedidoFila(pedidoBD);
                listaPedidosFila.add(filaInteractiva);
            }
          
            tbl_Pedidos.setItems(listaPedidosFila);
            
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Conexión", "No se pudieron obtener los pedidos.");
            LOGGER.log(Level.SEVERE, "Error al cargar pedidos en la tabla", ex);
        }   catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general al cargar datos de la tabla", ex);
            ex.printStackTrace();
        }
    }
    
    @FXML
    public void btn_ClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = (Stage) tf_NombreCliente.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource("vista/FXMLMenuPrincipal.fxml"));
            Parent vista = cargador.load();
            FXMLMenuPrincipalController controlador = cargador.getController();
            Scene escenaMenu = new Scene(vista);
            escenarioBase.setScene(escenaMenu);
            escenarioBase.setTitle("Menu Principal");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al volver al menú principal.", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al cargar FXMLMenuPrincipal", ex);
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en btnClicRegresar", ex);
            ex.printStackTrace();
        }
    }



    @FXML
    private void btn_ClicNuevoPedido(ActionEvent event) {
        
    }


    @FXML
    private void btn_ClicExportarInformacionPedido(ActionEvent event) {
    }

    
}
