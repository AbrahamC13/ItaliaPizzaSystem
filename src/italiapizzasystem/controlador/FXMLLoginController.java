package italiapizzasystem.controlador;
import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.dao.EmpleadoDAO;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
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
            try{
                validarUsuario(tfNombreUsuario.getText(), pfContraseniaUsuario.getText());
            }catch(SQLException ex){
                System.console().printf("Error al conectarse a la base de datos", ex);
            }
            catch(Exception ex){
                System.console().printf("Error general. ", ex);
            }
            
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
    
    private void validarUsuario(String username, String password) throws SQLException{
        if(EmpleadoDAO.validarCredenciales(username, password) == 1){
            //Caambiamos la ventana al menú principal 
        }
    }
    
}
