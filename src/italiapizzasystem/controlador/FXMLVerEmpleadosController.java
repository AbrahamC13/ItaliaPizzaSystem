
package italiapizzasystem.controlador;

import italiapizzasystem.ItaliaPizzaSystem;
import italiapizzasystem.persistencia.dao.EmpleadoDAO;
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
public class FXMLVerEmpleadosController implements Initializable {

    @FXML
    private TextField tfNombre;
    private static final Logger LOGGER = Logger.getLogger(FXMLVerEmpleadosController.class.getName());
    @FXML
    private TableView<Empleado> tvEmpleados;
    @FXML
    private TableColumn<Empleado, String> tcNombre;
    @FXML
    private TableColumn<Empleado, String> tcPaterno;
    @FXML
    private TableColumn<Empleado, String> tcMaterno;
    @FXML
    private TableColumn<Empleado, String> tcTelefono;
    @FXML
    private TableColumn<Empleado, String> tcCorreo;
    @FXML
    private TableColumn<Empleado, String> tcDireccion;
    @FXML
    private TableColumn<Empleado, String> tcCodigoPostal;
    @FXML
    private TableColumn<Empleado, String> tcStatus;
    private ObservableList<Empleado> listaEmpleados = FXCollections.observableArrayList();
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
        tcStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        // Ejecutamos la carga de datos desde el DAO
        cargarDatosEmpleados();
    }    
    
    private void cargarDatosEmpleados(){
        listaEmpleados.clear(); 

        try {
            ArrayList<Empleado> empleadosBD = EmpleadoDAO.obtenerEmpleados();
            listaEmpleados.addAll(empleadosBD);
            // Al inicio, el predicado es 'p -> true' para que muestre todos los registros
            FilteredList<Empleado> listaFiltrada = new FilteredList<>(listaEmpleados, p -> true);

            // 3. Agregar un "Listener" al texto del TextField
            // Esto se ejecutará automáticamente cada que el usuario escriba o borre una letra
            tfNombre.textProperty().addListener((observable, oldValue, newValue) -> {
                listaFiltrada.setPredicate(empleado -> {
                    // Si el buscador está vacío, muestra todos los empleados
                    if (newValue == null || newValue.trim().isEmpty()) {
                        return true;
                    }
                    // Convertimos el texto a minúsculas para que la búsqueda no sea estricta (case-insensitive)
                    String textoBusqueda = newValue.toLowerCase().trim();
                    // CONDICIONES DE BÚSQUEDA: Aquí defines por qué campos se puede buscar
                    if (empleado.getNombre().toLowerCase().contains(textoBusqueda)) {
                        return true; // Coincide con el nombre
                    } else if (empleado.getAPaterno().toLowerCase().contains(textoBusqueda)) {
                        return true; // Coincide con el apellido paterno
                    } else if (empleado.getAMaterno() != null && empleado.getAMaterno().toLowerCase().contains(textoBusqueda)) {
                        return true; // Coincide con el apellido materno (validando que no sea nulo)
                    }
                    return false; // No hubo coincidencia en ningún campo
                });
            });

            // 4. Envolver el filtro en una SortedList para que el usuario pueda seguir ordenando las columnas dando clic en ellas
            SortedList<Empleado> listaOrdenada = new SortedList<>(listaFiltrada);
            listaOrdenada.comparatorProperty().bind(tvEmpleados.comparatorProperty());

            // 5. IMPORTANTE: Ahora le pasamos la lista ordenada/filtrada a la tabla en lugar de la original
            tvEmpleados.setItems(listaOrdenada);
            
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al conectar a la bd.", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al cargar los empleados de la base de datos", ex);
            ex.printStackTrace();
        }catch (Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en cargarDatosEmpleados", ex);
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
    private void btnClicAgregarEmpleado(ActionEvent event) {
        try {
            Stage escenarioBase = (Stage) tfNombre.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource("vista/FXMLAgregarEmpleado.fxml"));
            Parent vista = cargador.load();
            FXMLAgregarEmpleadoController controlador = cargador.getController();
            Scene escenaEmpleado = new Scene(vista);
            escenarioBase.setScene(escenaEmpleado);
            escenarioBase.setTitle("Agregar Empleado");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al agregar el empleado.", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al cargar FXMLAgregarEmpleado", ex);
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en btnClicAgregarEmpleado", ex);
            ex.printStackTrace();
        }
    }
    
}
