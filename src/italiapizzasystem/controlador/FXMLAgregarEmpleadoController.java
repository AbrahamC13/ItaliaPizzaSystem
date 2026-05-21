
package italiapizzasystem.controlador;

import italiapizzasystem.ItaliaPizzaSystem;
import italiapizzasystem.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gerardo
 */
public class FXMLAgregarEmpleadoController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfCiudad;
    @FXML
    private TextField tfCodigoPostal;
    @FXML
    private TextField tfDireccion;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfRol;
    @FXML
    private TextField tfUsuario;
    @FXML
    private TextField tfContrasenia;
     private static final Logger LOGGER = Logger.getLogger(FXMLAgregarEmpleadoController.class.getName());
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnClicCancelar(ActionEvent event) {
         try {
            Stage escenarioBase = (Stage) tfNombre.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource("vista/FXMLVerEmpleados.fxml"));
            Parent vista = cargador.load();
            FXMLVerEmpleadosController controlador = cargador.getController();
            Scene escenaEmpleados = new Scene(vista);
            escenarioBase.setScene(escenaEmpleados);
            escenarioBase.setTitle("Ver empleados");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al volver a ver a los empleados.", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al cargar FXMLVerEmpleados", ex);
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en btnClicCancelar", ex);
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
    }
    
}
