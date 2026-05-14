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
public class FXMLInventarioController implements Initializable {

    @FXML
    private TextField tfNombreProducto;
     private static final Logger LOGGER = Logger.getLogger(FXMLInventarioController.class.getName());
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnClicBuscar(ActionEvent event) {
    }

    @FXML
    private void btnClicGenerarReporte(ActionEvent event) {
    }

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = (Stage) tfNombreProducto.getScene().getWindow();
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
    
}
