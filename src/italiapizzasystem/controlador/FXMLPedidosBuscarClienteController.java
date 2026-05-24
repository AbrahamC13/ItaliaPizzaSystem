/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package italiapizzasystem.controlador;

import italiapizzasystem.persistencia.dao.ClienteDAO;
import italiapizzasystem.persistencia.pojo.Cliente;
import italiapizzasystem.utilidad.EfectoBotones;
import italiapizzasystem.utilidad.Navegador;
import italiapizzasystem.utilidad.Utilidad;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.ResourceBundle;
import java.util.logging.Level;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author acrca
 */
public class FXMLPedidosBuscarClienteController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(FXMLPedidosBuscarClienteController.class.getName());
    
    @FXML
    private Button btn_Regresar;
    @FXML
    private TableView<Cliente> tbl_Clientes;
    @FXML
    private TableColumn<Cliente, String> col_Nombre;
    @FXML
    private TableColumn<Cliente, String> col_ApPaterno;
    @FXML
    private TableColumn<Cliente, String> col_ApMaterno;
    @FXML
    private TableColumn<Cliente, String> col_Telefono;
    @FXML
    private TableColumn<Cliente, String> col_Correo;
    @FXML
    private TableColumn<Cliente, String> col_Direccion;
    @FXML
    private TextField tf_NombreCliente;
    @FXML
    private Button btn_Aceptar;
    @FXML
    private Button btn_AgregarNuevoUsuario;

    private ObservableList<Cliente> listaClientes;
    private FilteredList<Cliente> listaFiltrada;
    
    private static Cliente clienteSeleccionado;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatosClientes();
        inicializarFiltroBusqueda();
        EfectoBotones.darEfectoBotones(btn_Aceptar, btn_AgregarNuevoUsuario, btn_Regresar);
    }    
    
    private void configurarTabla() {
        col_Nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        col_ApPaterno.setCellValueFactory(new PropertyValueFactory<>("aPaterno"));
        col_ApMaterno.setCellValueFactory(new PropertyValueFactory<>("aMaterno"));
        col_Telefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        col_Correo.setCellValueFactory(new PropertyValueFactory<>("email"));
        col_Direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
    }
    
    private void cargarDatosClientes(){
        listaClientes = FXCollections.observableArrayList();
        
        try{
            ArrayList<Cliente> clientesBD = ClienteDAO.obtenerClientes();
            if (clientesBD != null){
                listaClientes.addAll(clientesBD);
            }
        } catch (SQLException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Conexión", "No se pudieron obtenerlos clientes de la base de datos.");
            LOGGER.log(Level.SEVERE, "Error al cargar clientes", ex);
        } catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general al cargar datos de la tabla", ex);
            ex.printStackTrace();
        }
    }
    
    private void inicializarFiltroBusqueda() {
        listaFiltrada = new FilteredList<>(listaClientes, p -> true);

        tf_NombreCliente.textProperty().addListener((observable, oldValue, newValue) -> {
            listaFiltrada.setPredicate(cliente -> cumpleCriterioBusqueda(cliente, newValue));
        });

        SortedList<Cliente> listaOrdenada = new SortedList<>(listaFiltrada);
        listaOrdenada.comparatorProperty().bind(tbl_Clientes.comparatorProperty());

        tbl_Clientes.setItems(listaOrdenada);
    }

    private boolean cumpleCriterioBusqueda(Cliente cliente, String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            return true;
        }

        String filtro = textoBusqueda.toLowerCase().trim();

        boolean coincideNombre = cliente.getNombre() != null && cliente.getNombre().toLowerCase().contains(filtro);

        boolean coincideApellido = cliente.getAPaterno() != null &&  cliente.getAPaterno().toLowerCase().contains(filtro);

        return coincideNombre || coincideApellido;
    }

    @FXML
    private void btn_ClicAceptar(ActionEvent event) {
        Cliente seleccion = tbl_Clientes.getSelectionModel().getSelectedItem();
    
        if (seleccion != null) {
            FXMLLoader cargador = Navegador.cambiarVentanaConControlador(
                tbl_Clientes, 
                "vista/FXMLRealizarPedido.fxml", 
                "Realizar Pedido"
            );

            if (cargador != null) {
                FXMLRealizarPedidoController destino = cargador.getController();
            }
        } else {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Selección requerida", 
                    "Debe seleccionar un cliente de la lista para continuar.");
        }
    }
    
    @FXML
    private void btn_ClicRegresar(ActionEvent event) {
        Navegador.cambiarVentana(tbl_Clientes, "vista/FXMLPedidos.fxml", "Pedidos");
    }

    @FXML
    private void btn_ClicAgregarNuevoUsuario(ActionEvent event) {
        Navegador.cambiarVentana(tbl_Clientes, "vista/FXMLAgregarCliente.fxml", "Agregar Cliente");
    }
    
}
