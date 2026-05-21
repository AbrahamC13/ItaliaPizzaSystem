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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gerardo
 */
public class FXMLPedidosController implements Initializable {

    private TextField tfNombreCliente;
    private static final Logger LOGGER = Logger.getLogger(FXMLPedidosController.class.getName());
    @FXML
    private TableColumn<?, ?> col_IdPedido;
    @FXML
    private TableColumn<?, ?> col_NombreCliente;
    @FXML
    private TableColumn<?, ?> col_Direccion;
    @FXML
    private TableColumn<?, ?> col_Fecha;
    @FXML
    private TableColumn<?, ?> col_Status;
    @FXML
    private TableColumn<?, ?> col_Editar;
    @FXML
    private TextField tf_NombreCliente;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

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
            LOGGER.log(Level.SEVERE, "Error al cargar FXMLMenuPrincipal", ex);
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en btnClicRegresar", ex);
            ex.printStackTrace();
        }
    }

    @FXML
    private void click_BtnRegresar(MouseEvent event) {
        
    }


    @FXML
    private void btn_ClicRegresar(ActionEvent event) {
    }

    @FXML
    private void click_BtnNuevoPedido(MouseEvent event) {
    }

    @FXML
    private void btn_ClicNuevoPedido(ActionEvent event) {
    }

    @FXML
    private void click_BtnExportarInformacionPedido(MouseEvent event) {
    }

    @FXML
    private void btn_ClicExportarInformacionPedido(ActionEvent event) {
    }
    
}
