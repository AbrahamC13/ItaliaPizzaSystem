package italiapizzasystem.controlador;
import italiapizzasystem.ItaliaPizzaSystem;
import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.dao.EmpleadoDAO;
import italiapizzasystem.persistencia.pojo.Empleado;
import italiapizzasystem.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

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
         Platform.runLater(() -> {
        tfNombreUsuario.requestFocus();
        });
        // TODO
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD==null){
            Alert alerta;
            alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Error");
            alerta.setHeaderText("Error al conectar a la BD");
            alerta.show();
        }
    }  

    @FXML
    void btnEntrar(ActionEvent event) {
        lbCampoUsuario.setText("");
        lbCampoContrasenia.setText("");
        if(validarCamposLlenos()){     
            validarUsuario(tfNombreUsuario.getText(), pfContraseniaUsuario.getText());   
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
    
    private void validarUsuario(String username, String password) {
        try{
            Empleado empleadoAutenticado = EmpleadoDAO.validarCredenciales(username, password);
            if(empleadoAutenticado!= null){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Credenciales correctas", "Bienvenido(a): "+empleadoAutenticado.getNombre());
                irPantallaPrincipal(empleadoAutenticado);
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION,"Credenciales incorrectas", "Porfavor, verifique la información ingresada.");
            }
        }catch(SQLException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al conectarse a la base de datos", ex.getMessage());
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            ex.printStackTrace();
        }
        
    }
    
    private void irPantallaPrincipal(Empleado empleado){
        try{
        //casteo: solicitud explicita a Java para cambiar un tipo de dato a otro
        Stage escenarioBase = (Stage) tfNombreUsuario.getScene().getWindow(); //A partir de mi componente tfUsuario puedo obtenr la escena en donde está
        //FXMLLoader cargador = new FXMLLoader(italiapizzasystem.class.getResource("vista/FXMLMenuPrincipal.fxml"));//Lo que está adentro del paréntesis funsiona como url
        FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource("vista/FXMLMenuPrincipal.fxml"));
        //La variable FXMLLoader me permite manipular el controlador 
        Parent vista = cargador.load();//Aquí innicio la vista 
        FXMLMenuPrincipalController controlador = cargador.getController();
        controlador.inicializarInformacion(empleado);
        Scene escenaPrincipal = new Scene(vista);
        escenarioBase.setScene(escenaPrincipal);
        escenarioBase.setTitle("Menú Principal.");
        escenarioBase.show();
        }catch (IOException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al redirigirse al menú. ", ex.getMessage());
            ex.printStackTrace();
        }catch(IllegalStateException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al redirigirse al menú", ex.getMessage());
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error no gestionado", ex.getMessage());
            ex.printStackTrace();
        }
    }
    
}
