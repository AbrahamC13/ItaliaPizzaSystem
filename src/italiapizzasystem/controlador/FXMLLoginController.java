package italiapizzasystem.controlador;
import italiapizzasystem.ItaliaPizzaSystem;
import italiapizzasystem.excepciones.ValidarCredencialesException;
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
    //Restricción de credenciales
    private static final int LONGITUD_MINIMA_CREDENCIALES = 5;
    private static final int LONGITUD_MAXIMA_CREDENCIALES = 45;
    //Prevención de inyecciones sql
    // Regex para username: letras, números, punto, guión bajo. Nada más.
    private static final String USUARIO_REGEX = "^[a-zA-Z0-9._]{3,45}$";
    // Regex para contraseña fuerte:
    // - Al menos 5 caracteres
    // - Al menos una mayúscula
    // - Al menos una minúscula
    // - Al menos un dígito
    private static final String CONTRASENIA_STRONG_REGEX = 
        "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{5,45}$";
    
    
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
        if(validarCamposLlenos() && validarLongitudCampos(tfNombreUsuario.getText(), pfContraseniaUsuario.getText())
                && validarFormatoCampos(tfNombreUsuario.getText(), pfContraseniaUsuario.getText())){     
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
    
    private boolean validarLongitudCampos(String usuario, String contrasenia){
        boolean resultado = true;
        int longitudUsuario = usuario.trim().length();  // trim() evita contar solo espacios
        int longitudContrasenia = contrasenia.length();     // La contraseña suele conservar espacios
        
        // Usuario: al menos 1 caracter, máximo 45
        // Contraseña: permitir 0? Normalmente no, pero ajusta según tu caso
        if (longitudUsuario < LONGITUD_MINIMA_CREDENCIALES || longitudUsuario > LONGITUD_MAXIMA_CREDENCIALES) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,"Error", "El usuario debe tener entre 5 y 45 caracteres.");
            resultado= false;
        }
    
        if (longitudContrasenia < LONGITUD_MINIMA_CREDENCIALES || longitudContrasenia > LONGITUD_MAXIMA_CREDENCIALES ) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "La contraseña debe tener entre 5 y 45 caracteres.");
            resultado= false;
        }
        return resultado;
    }
    
    private boolean validarFormatoCampos(String usuario, String contrasenia){
        boolean resultado = true;
        if (!usuario.matches(USUARIO_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido", 
                "El usuario solo puede contener letras, números, puntos y guiones bajos. Sin espacios ni caracteres especiales.");
            resultado = false;
        }
        if (!contrasenia.matches(CONTRASENIA_STRONG_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido", 
                "La contraseña debe tener al menos una mayúscula, una minúscula y un número.");
            resultado = false;
        }
        return resultado;
    }
    
    private void validarUsuario(String username, String password) {
        try{
            Empleado empleadoAutenticado = EmpleadoDAO.validarCredenciales(username, password);
            if(empleadoAutenticado!= null){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Credenciales correctas", "Bienvenido(a): "+
                        empleadoAutenticado.getNombre()+" "+empleadoAutenticado.getaPaterno()+" "+empleadoAutenticado.getaMaterno());
                irPantallaPrincipal(empleadoAutenticado);
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION,"Credenciales incorrectas", "Porfavor, verifique la información ingresada.");
            }
        }catch(ValidarCredencialesException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al validar credenciales", ex.getMessage());
        }
        catch(SQLException ex){
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

    @FXML
    private void hpClicContraseñaOlvidada(ActionEvent event) {
        
    }
    
}
