package italiapizzasystem.controlador;

import italiapizzasystem.persistencia.pojo.Empleado;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

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
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarInformacion(Empleado empleado){
        this.empleado = empleado;
    }
    
}
