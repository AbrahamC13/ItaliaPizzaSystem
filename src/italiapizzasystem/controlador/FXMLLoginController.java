package italiapizzasystem.controlador;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Gerardo
 */
public class FXMLLoginController implements Initializable {
    @FXML
    private Label lbCampoUsuario;

    @FXML
    private Label lbCampoContrasenia;

    @FXML
    private TextField tfNombreUsuario;

    @FXML
    private PasswordField pfContraseniaUsuario;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }  

    @FXML
    void btnEntrar(ActionEvent event) {
        lbCampoUsuario.setText("");
        lbCampoContrasenia.setText("");
        if(validarCamposLlenos()){
            
        }
    }
      
    private boolean validarCamposLlenos(){
        boolean camposLlenos = true;
        if(tfNombreUsuario.getText().isEmpty()){
            lbCampoUsuario.setText("**Campo obligatorio**");
            camposLlenos = false;
        }
        if(pfContraseniaUsuario.getText().isEmpty()){
            lbCampoContrasenia.setText("**Campo obligatorio**");
            camposLlenos = false;
        }
            return camposLlenos;
        
    }
    
}
