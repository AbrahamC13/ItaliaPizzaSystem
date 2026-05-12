
package italiapizzasystem.controlador;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Gerardo
 */
public class FXMLRegistrarProductoController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextArea taDescripcion;
    @FXML
    private TextArea taRestricciones;
    @FXML
    private TextField tfPrecio;
    @FXML
    private TextField tfCantidad;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void btnClicCancelarRegistro(ActionEvent event) {
    }

    @FXML
    private void btnClicSubirArchivo(ActionEvent event) {
    }

    @FXML
    private void btnClicRegistrarProducto(ActionEvent event) {
    }
    
}
