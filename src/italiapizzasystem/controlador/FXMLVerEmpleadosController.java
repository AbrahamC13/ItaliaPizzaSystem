
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
            // Llamamos directamente al método de tu DAO
            ArrayList<Empleado> empleadosBD = EmpleadoDAO.obtenerEmpleados();
            // Pasamos los datos del ArrayList a la lista especial ObservableList de JavaFX
            listaEmpleados.addAll(empleadosBD);
            // Asignamos la lista a la tabla
            tvEmpleados.setItems(listaEmpleados);
            
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
