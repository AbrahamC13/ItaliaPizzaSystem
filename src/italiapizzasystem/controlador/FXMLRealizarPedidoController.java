package italiapizzasystem.controlador;

import italiapizzasystem.persistencia.pojo.Cliente;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Gerardo
 */
public class FXMLRealizarPedidoController implements Initializable {

    private Cliente clienteElegido;
    
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
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
    }
    
}
