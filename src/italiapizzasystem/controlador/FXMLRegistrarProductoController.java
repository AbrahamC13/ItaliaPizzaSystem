
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
public class FXMLRegistrarProductoController implements Initializable {

    @FXML
    private TextField tf_Nombre;
    @FXML
    private TextArea ta_Descripcion;
    @FXML
    private TextArea ta_Restricciones;
    @FXML
    private TextField tf_Precio;
    @FXML
    private TextField tf_Cantidad;
    @FXML
    private Label lb_Campos;
    @FXML
    private Label lb_Nombre;
    @FXML
    private Label lb_Descripcion;
    @FXML
    private Label lb_Restriccion;
    @FXML
    private Label lb_Precio;
    @FXML
    private Label lb_Cantidad;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void btn_CancelarRegistro(ActionEvent event) {
        
    }

    @FXML
    private void btn_SubirArchivo(ActionEvent event) {
        
    }

    @FXML
    private void btn_RegistrarProducto(ActionEvent event) {
        
    }
    
    
}
