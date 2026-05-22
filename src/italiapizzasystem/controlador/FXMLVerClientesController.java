
package italiapizzasystem.controlador;

import italiapizzasystem.ItaliaPizzaSystem;
import italiapizzasystem.persistencia.dao.ClienteDAO;
import italiapizzasystem.persistencia.dao.EmpleadoDAO;
import italiapizzasystem.persistencia.pojo.Cliente;
import italiapizzasystem.persistencia.pojo.Empleado;
import italiapizzasystem.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gerardo
 */
public class FXMLVerClientesController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TableView<Cliente> tvClientes;
    private static final Logger LOGGER = Logger.getLogger(FXMLVerClientesController.class.getName());
    @FXML
    private TableColumn<Cliente, String> tcNombre;
    @FXML
    private TableColumn<Cliente, String> tcPaterno;
    @FXML
    private TableColumn<Cliente, String> tcMaterno;
    @FXML
    private TableColumn<Cliente, String> tcTelefono;
    @FXML
    private TableColumn<Cliente, String> tcCorreo;
    @FXML
    private TableColumn<Cliente, String> tcCodigoPostal;
    @FXML
    private TableColumn<Cliente, String> tcDireccion;
    private ObservableList<Cliente> listaClientes = FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Vinculación exacta usando las propiedades del POJO 'Empleado'
        tcNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        tcPaterno.setCellValueFactory(new PropertyValueFactory<>("aPaterno")); 
        tcMaterno.setCellValueFactory(new PropertyValueFactory<>("aMaterno"));
        tcTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        tcCorreo.setCellValueFactory(new PropertyValueFactory<>("email")); 
        tcDireccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        tcCodigoPostal.setCellValueFactory(new PropertyValueFactory<>("codigoPostal")); 
        // Ejecutamos la carga de datos desde el DAO
        cargarDatosClientes();
    }    
    
    private void cargarDatosClientes(){
        listaClientes.clear(); 

        try {
            ArrayList<Cliente> clientesBD = ClienteDAO.obtenerClientes();
            listaClientes.addAll(clientesBD);
            // Al inicio, el predicado es 'p -> true' para que muestre todos los registros
            FilteredList<Cliente> listaFiltrada = new FilteredList<>(listaClientes, p -> true);

            // 3. Agregar un "Listener" al texto del TextField
            // Esto se ejecutará automáticamente cada que el usuario escriba o borre una letra
            tfNombre.textProperty().addListener((observable, oldValue, newValue) -> {
                listaFiltrada.setPredicate(cliente -> {
                    // Si el buscador está vacío, muestra todos los empleados
                    if (newValue == null || newValue.trim().isEmpty()) {
                        return true;
                    }
                    // Convertimos el texto a minúsculas para que la búsqueda no sea estricta (case-insensitive)
                    String textoBusqueda = newValue.toLowerCase().trim();
                    // CONDICIONES DE BÚSQUEDA: Aquí defines por qué campos se puede buscar
                    if (cliente.getNombre().toLowerCase().contains(textoBusqueda)) {
                        return true; // Coincide con el nombre
                    } else if (cliente.getAPaterno().toLowerCase().contains(textoBusqueda)) {
                        return true; // Coincide con el apellido paterno
                    } else if (cliente.getAMaterno() != null && cliente.getAMaterno().toLowerCase().contains(textoBusqueda)) {
                        return true; // Coincide con el apellido materno (validando que no sea nulo)
                    }
                    return false; // No hubo coincidencia en ningún campo
                });
            });

            // 4. Envolver el filtro en una SortedList para que el usuario pueda seguir ordenando las columnas dando clic en ellas
            SortedList<Cliente> listaOrdenada = new SortedList<>(listaFiltrada);
            listaOrdenada.comparatorProperty().bind(tvClientes.comparatorProperty());

            // 5. IMPORTANTE: Ahora le pasamos la lista ordenada/filtrada a la tabla en lugar de la original
            tvClientes.setItems(listaOrdenada);
            
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al conectar a la bd.", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al cargar los clientes de la base de datos", ex);
            ex.printStackTrace();
        }catch (Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en cargarDatosClientes", ex);
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = (Stage) tfNombre.getScene().getWindow();
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
    private void btnAgregarCliente(ActionEvent event) {
        try {
            Stage escenarioBase = (Stage) tfNombre.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource("vista/FXMLAgregarCliente.fxml"));
            Parent vista = cargador.load();
            FXMLAgregarClienteController controlador = cargador.getController();
            Scene escenaMenu = new Scene(vista);
            escenarioBase.setScene(escenaMenu);
            escenarioBase.setTitle("Agregar Cliente");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al acceder a la ventana.", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al cargar FXMLAgregarCliente", ex);
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en btnAgregarCliente", ex);
            ex.printStackTrace();
        }
    }
    
}
