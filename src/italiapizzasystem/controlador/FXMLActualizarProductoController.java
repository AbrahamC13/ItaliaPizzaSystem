
package italiapizzasystem.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Gerardo
 */
public class FXMLActualizarProductoController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private TextArea taRestricciones;
    @FXML
    private Label lbCamposInvalidos;
    @FXML
    private Label lbNombreInvalido;
    @FXML
    private Label lbDescripcionInvalido;
    @FXML
    private Label lbRestriccionInvalida;
    @FXML
    private TextField tfPrecio;
    @FXML
    private TextField tfCantidad;
    @FXML
    private Label lbPrecioInvalido;
    @FXML
    private Label lbCantidadInvalida;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnClicCancelar(ActionEvent event) {
    }

    @FXML
    private void btnClicSubirArchivo(ActionEvent event) {
    }

    @FXML
    private void btnClicActualizarDatos(ActionEvent event) {
    }

    @FXML
    private void btnClicEliminarProducto(ActionEvent event) {
    }
    
}
