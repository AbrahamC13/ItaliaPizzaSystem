package italiapizzasystem.controlador;

import italiapizzasystem.persistencia.pojo.Empleado;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author Gerardo
 */
public class FXMLMenuPrincipalController implements Initializable {

    /**
     * Initializes the controller class.
     */
    private Empleado empleado;
    @FXML
    private Label lbEmpleado;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(Empleado empleado){
        this.empleado = empleado;
        lbEmpleado.setText("Bienvenido(a) "+empleado.getNombre()+" "+empleado.getaPaterno()+" "+empleado.getaMaterno());
    }

    @FXML
    private void btnClicCerrarSesion(ActionEvent event) {
    }

    @FXML
    private void btnClicAdministracion(ActionEvent event) {
    }

    @FXML
    private void btnClicAyuda(ActionEvent event) {
    }

    @FXML
    private void btnClicInventarios(ActionEvent event) {
    }

    @FXML
    private void btnClicPedidos(ActionEvent event) {
    }
    
}
