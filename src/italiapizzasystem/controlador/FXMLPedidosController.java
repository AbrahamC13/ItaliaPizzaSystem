package italiapizzasystem.controlador;

import italiapizzasystem.ItaliaPizzaSystem;
import italiapizzasystem.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
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
public class FXMLPedidosController implements Initializable {

    @FXML
    private TextField tfNombreCliente;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnClicRegresar(ActionEvent event) {
        try {
            Stage escenarioBase = (Stage) tfNombreCliente.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource("vista/FXMLMenuPrincipal.fxml"));
            Parent vista = cargador.load();
            FXMLMenuPrincipalController controlador = cargador.getController();
            Scene escenaMenu = new Scene(vista);
            escenarioBase.setScene(escenaMenu);
            escenarioBase.setTitle("Menu Principal");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al volver al menú principal.", ex.getMessage());
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnClicNuevoPedido(ActionEvent event) {
    }


    @FXML
    private void btnClicEditarPedido(ActionEvent event) {
    }

    @FXML
    private void btnClicExportarInformacionPedido(ActionEvent event) {
    }
    
}
