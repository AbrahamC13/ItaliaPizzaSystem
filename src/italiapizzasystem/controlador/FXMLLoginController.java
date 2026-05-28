package italiapizzasystem.controlador;
import italiapizzasystem.ItaliaPizzaSystem;
import italiapizzasystem.excepciones.ValidarCredencialesException;
import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.dao.EmpleadoDAO;
import italiapizzasystem.persistencia.pojo.Empleado;
import italiapizzasystem.utilidad.Navegador;
import italiapizzasystem.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
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
    private Hyperlink hp_RegistrarNuevoEmpleado;

    @FXML
    private PasswordField pfContraseniaUsuario;
    //Restricción de credenciales
    private static final int LONGITUD_MINIMA_CREDENCIALES = 5;
    private static final int LONGITUD_MAXIMA_CREDENCIALES = 45;
    private static final Logger LOGGER = Logger.getLogger(FXMLLoginController.class.getName());
    //Prevención de inyecciones sql
    // Regex para username: letras, números, punto, guión bajo. Nada más.
    private static final String USUARIO_REGEX = "^[a-zA-Z0-9._]{3,45}$";
    // Regex para contraseña fuerte: Al menos 5 caracteres, al menos una mayúscula, Al menos una minúscula, Al menos un dígito
    private static final String CONTRASENIA_STRONG_REGEX =  "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).{5,45}$";
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
         Platform.runLater(() -> {
        tfNombreUsuario.requestFocus();
        });
        // TODO
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD==null){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error", "Error al conectar a la BD");
            
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
        int longitudUsuario = usuario.trim().length();  
        int longitudContrasenia = contrasenia.length();     
        
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
                        empleadoAutenticado.getNombre()+" "+empleadoAutenticado.getAPaterno()+" "+empleadoAutenticado.getAMaterno());
                italiapizzasystem.persistencia.pojo.UserSession.getInstancia().setEmpleadoConectado(empleadoAutenticado);
                irPantallaPrincipal(empleadoAutenticado);
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION,"Credenciales incorrectas", "Porfavor, verifique la información ingresada.");
            }
        }catch(ValidarCredencialesException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al validar credenciales", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al ingresar, el usuario usó claves incorrectas", ex);
        }
        catch(SQLException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al conectarse a la base de datos", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al cargar la bd en el método validarUsuario", ex);
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en validarUsuario", ex);
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
            LOGGER.log(Level.SEVERE, "Error al cargar FXMLMenuPrincipal", ex);
            ex.printStackTrace();
        }catch(IllegalStateException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al redirigirse al menú", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al cargar FXMLMenuPrincipal", ex);
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error no gestionado", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en irPantallaPrincipal", ex);
            ex.printStackTrace();
        }
    }

    @FXML
    private void hpClicContraseñaOlvidada(ActionEvent event) {
        TextInputDialog dialogo = new TextInputDialog();
        dialogo.setTitle("Recuperar Contraseña");
        dialogo.setHeaderText("Restablecer credenciales de acceso");
        dialogo.setContentText("Ingresa tu nombre de usuario o correo electrónico:");

        // Obtener el Stage actual para centrar el diálogo
        Stage stage = (Stage) tfNombreUsuario.getScene().getWindow();
        dialogo.initOwner(stage);

        Optional<String> resultado = dialogo.showAndWait();

        // Si el usuario presionó "Aceptar"
        resultado.ifPresent(new java.util.function.Consumer<String>() {
            @Override
            public void accept(String identificador) {
                String input = identificador.trim();
                if (input.isEmpty()) {
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Campo vacío", "Debe ingresar un usuario o correo.");
                    return;
                }

                String contrasenia = null;
                boolean ocurrioError = false; // Bandera para controlar el flujo si falla la BD

                try {
                    contrasenia = EmpleadoDAO.recuperarYEnviarcontrasenia(input);
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "Error en la base de datos al recuperar clave", ex);
                    Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo conectar a la base de datos.");
                    ocurrioError = true; // Marcamos que hubo un error
                } 

                // Solo evaluamos el resultado si la consulta a la BD no falló
                if (!ocurrioError) {
                    if (contrasenia != null) {
                        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Información",
                                "Contraseña registrada: " + contrasenia);
                    } else {
                        Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "No encontrado", 
                                "No se encontró ningún usuario o correo registrado con esos datos.");
                    }   
                }
            }
        });      
    }
        
    @FXML
    private void hp_ClicRegistrarNuevoEmpleado(ActionEvent event) {
        Navegador.cambiarVentana(tfNombreUsuario, "vista/FXMLAgregarEmpleado.fxml", "Registrar nuevo empleado");
    }
}
    
    

