
package italiapizzasystem.controlador;
import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.dao.ClienteDAO;
import italiapizzasystem.persistencia.pojo.Cliente;
import italiapizzasystem.utilidad.Utilidad;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author Gerardo
 */
public class FXMLAgregarClienteController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfDireccion;
    @FXML
    private TextField tfCodigoPostal;
    @FXML
    private TextField tfCiudad;
    private static final Logger LOGGER = Logger.getLogger(FXMLAgregarClienteController.class.getName());
     //Restricción de campos
    public static final int NOMBRE_MAX = 45;
    public static final int APATERNO_MAX = 45;
    public static final int CIUDAD_MAX = 45;
    public static final int DIRECCION_MAX = 500;
    public static final int AMATERNO_MAX = 45;
    public static final int EMAIL_MAX = 255;
    public static final int CODIGO_POSTAL_MAX = 5;  
    public static final int TELEFONO_MAX = 10; 
    // Regex para formato de los campos
    private static final String NOMBRE_REGEX = "^[a-zA-ZáéíóúüñÁÉÍÓÚÜÑ\\s]+$";
    private static final String TELEFONO_REGEX = "^\\d{10}$";
    private static final String CODIGO_POSTAL_REGEX = "^\\d{5}$";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String DIRECCION_REGEX = "^[a-zA-Z0-9áéíóúüñÁÉÍÓÚÜÑ\\s,.#\\-]+$";
   

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
         Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD==null){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Error", "Error al conectar a la BD");
            
        }
    }    

    @FXML
    private void btnClicCancelar(ActionEvent event) {
    }

    @FXML
    private void btnClicGuardar(ActionEvent event) {
        if(validarCamposVacios() && validarLongitudCampos() && validarFormatoCampos() && validarClienteExistente()){
            registrarCliente();
        }else{
            return;
        }
    }
    
    private boolean validarCamposVacios(){
        boolean resultado = true;
        if(tfNombre.getText().isEmpty() || tfApellidoPaterno.getText().isEmpty()  || 
                tfTelefono.getText().isEmpty() || tfEmail.getText().isEmpty() || tfDireccion.getText().isEmpty() || 
                tfCodigoPostal.getText().isEmpty() || tfCiudad.getText().isEmpty()  ){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Datos incompletos", "Porfavor, asegúrate de que todos los campos están llenos");
            resultado = false;
        }
        return resultado;
    }
    
    private boolean validarLongitudCampos() {
        boolean resultado = true;

        int longitudNombre = tfNombre.getText().length();
        if (longitudNombre > NOMBRE_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", 
                "El nombre debe tener máximo " + NOMBRE_MAX + " caracteres.");
            resultado = false;
        }

        int longitudApellidoPaterno = tfApellidoPaterno.getText().length();
        if (longitudApellidoPaterno > APATERNO_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", 
                "El apellido paterno debe tener máximo " + APATERNO_MAX + " caracteres.");
            resultado = false;
        }

        String apellidoMaterno = tfApellidoMaterno.getText();
        if (apellidoMaterno != null && !apellidoMaterno.isEmpty()) {
            if (apellidoMaterno.length() > AMATERNO_MAX) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", 
                    "El apellido materno debe tener máximo " + AMATERNO_MAX + " caracteres.");
                resultado = false;
            }
        }

        String telefono = tfTelefono.getText();
        if (telefono.length() != TELEFONO_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", 
                "El teléfono debe tener exactamente " + TELEFONO_MAX + " dígitos.");
            resultado = false;
        } else if (!telefono.matches("\\d+")) {
            // Opcional: validar que solo contenga números
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", 
                "El teléfono solo puede contener dígitos.");
            resultado = false;
        }

        String email = tfEmail.getText();
        if (email.length() > EMAIL_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", 
                "El email debe tener máximo " + EMAIL_MAX + " caracteres.");
            resultado = false;
        }

        int longitudDireccion = tfDireccion.getText().length();
        if (longitudDireccion > DIRECCION_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", 
                "La dirección debe tener máximo " + DIRECCION_MAX + " caracteres.");
            resultado = false;
        }

        String codigoPostal = tfCodigoPostal.getText();
        if (codigoPostal.length() != CODIGO_POSTAL_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", 
                "El código postal debe tener exactamente " + CODIGO_POSTAL_MAX + " caracteres.");
            resultado = false;
        } else if (!codigoPostal.matches("\\d+")) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", 
                "El código postal solo puede contener dígitos.");
            resultado = false;
        }
        
        int longitudCiudad = tfCiudad.getText().length();
        if (longitudCiudad > CIUDAD_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", 
                "La ciudad debe tener máximo " + CIUDAD_MAX + " caracteres.");
            resultado = false;
        }

        return resultado;
    }
    
    private boolean validarFormatoCampos() {
        boolean resultado = true;
        String nombre = tfNombre.getText().trim();
        
        if (!nombre.matches(NOMBRE_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "El nombre solo puede contener letras, espacios y acentos.");
            resultado = false;
        }
        
        String apellidoPaterno = tfApellidoPaterno.getText().trim();
        if (!apellidoPaterno.matches(NOMBRE_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "El apellido paterno solo puede contener letras, espacios y acentos.");
            resultado = false;
        }
        
        String apellidoMaterno = tfApellidoMaterno.getText().trim();
        if (!apellidoMaterno.isEmpty() && !apellidoMaterno.matches(NOMBRE_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "El apellido materno solo puede contener letras, espacios y acentos.");
            resultado = false;
        }
        
        String ciudad = tfCiudad.getText().trim();
        if (!ciudad.matches(NOMBRE_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "La ciudad solo puede contener letras, espacios y acentos.");
            resultado = false;
        }
        
        String telefono = tfTelefono.getText().trim();
        if (!telefono.matches(TELEFONO_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "El teléfono debe contener exactamente 10 dígitos numéricos.");
            resultado = false;
        }

        String email = tfEmail.getText().trim();
        if (!email.matches(EMAIL_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "El email no tiene un formato válido (ej: nombre@dominio.com).");
            resultado = false;
        }

        String direccion = tfDireccion.getText().trim();
        if (!direccion.matches(DIRECCION_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "La dirección contiene caracteres no permitidos. Use letras, números, espacios, comas, puntos, # o -.");
            resultado = false;
        }

        String codigoPostal = tfCodigoPostal.getText().trim();
        if (!codigoPostal.matches(CODIGO_POSTAL_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "El código postal debe ser exactamente 5 dígitos numéricos.");
            resultado = false;
        }

        return resultado;
    }
    
    private boolean validarClienteExistente(){
        boolean resultado = false;
        try{
            resultado = ClienteDAO.validarClienteExistente(tfEmail.getText());//Devuelve true si encuentra un usuario registrado
            if(resultado){
              Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Información existente", "El correo ingresado ya "
                      + "está registrado, por favor intente con otro");
            } 
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en validarClienteExistente", ex);
            ex.printStackTrace();
        }
        return resultado;
    }
    
    private void registrarCliente(){
        boolean resultado = false;
        Cliente cliente = new Cliente();
        cliente.setNombre(tfNombre.getText());
        cliente.setaPaterno(tfApellidoPaterno.getText());
        cliente.setaMaterno(tfApellidoMaterno.getText());
        cliente.setTelefono(tfTelefono.getText());
        cliente.setEmail(tfEmail.getText());
        cliente.setDireccion(tfDireccion.getText());
        cliente.setCodigoPostal(tfCodigoPostal.getText());
        cliente.setCiudad(tfCiudad.getText());
        cliente.setStatus(true);
        try{
            resultado = ClienteDAO.registrarCliente(cliente);
            if(resultado){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito", "El usuario ha sido registrado correctamente.");
            }
        }catch(SQLException ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al conectarse a la base de datos", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al cargar la bd en el método registrarCliente", ex);
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en registrarCliente", ex);
            ex.printStackTrace();
        }
    }
    
}
