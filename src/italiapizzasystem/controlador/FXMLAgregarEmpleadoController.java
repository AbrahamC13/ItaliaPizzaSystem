
package italiapizzasystem.controlador;

import italiapizzasystem.ItaliaPizzaSystem;
import italiapizzasystem.persistencia.dao.EmpleadoDAO;
import italiapizzasystem.persistencia.pojo.Empleado;
import italiapizzasystem.utilidad.EfectoBotones;
import italiapizzasystem.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gerardo
 */
public class FXMLAgregarEmpleadoController implements Initializable {

    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfCiudad;
    @FXML
    private TextField tfCodigoPostal;
    @FXML
    private TextField tfDireccion;
    @FXML
    private TextField tfEmail;
    @FXML
    private TextField tfTelefono;
    @FXML
    private TextField tfRol;
    @FXML
    private TextField tfUsuario;
    @FXML
    private TextField tfContrasenia;
    private static final Logger LOGGER = Logger.getLogger(FXMLAgregarEmpleadoController.class.getName());
    // Restricciones de longitud
    public static final int NOMBRE_MAX = 45;
    public static final int APATERNO_MAX = 45;
    public static final int AMATERNO_MAX = 45;
    public static final int CIUDAD_MAX = 45;
    public static final int DIRECCION_MAX = 500;
    public static final int EMAIL_MAX = 255;
    public static final int CODIGO_POSTAL_MAX = 5;
    public static final int TELEFONO_MAX = 10;
    public static final int ROL_MAX = 45;
    public static final int USUARIO_MAX = 45;
    public static final int CONTRASENIA_MAX = 255;

    // Expresiones regulares para formato
    private static final String NOMBRE_REGEX = "^[a-zA-ZáéíóúüñÁÉÍÓÚÜÑ\\s]+$";
    private static final String TELEFONO_REGEX = "^\\d{10}$";
    private static final String CODIGO_POSTAL_REGEX = "^\\d{5}$";
    private static final String EMAIL_REGEX = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private static final String DIRECCION_REGEX = "^[a-zA-Z0-9áéíóúüñÁÉÍÓÚÜÑ\\s,.#\\-]+$";
    private static final String USUARIO_REGEX = "^[a-zA-Z0-9_]{4,45}$";  // alfanumérico y guión bajo, 4-45 caracteres
    private static final String ROL_REGEX = "^[a-zA-ZáéíóúüñÁÉÍÓÚÜÑ\\s]+$"; // solo letras y espacios
    @FXML
    private Button btn_Cancelar;
    @FXML
    private Button btn_Aceptar;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EfectoBotones.darEfectoBotones(btn_Cancelar, btn_Aceptar);
    }    

    @FXML
    private void btnClicCancelar(ActionEvent event) {
         try {
            Stage escenarioBase = (Stage) tfNombre.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource("vista/FXMLVerEmpleados.fxml"));
            Parent vista = cargador.load();
            FXMLVerEmpleadosController controlador = cargador.getController();
            Scene escenaEmpleados = new Scene(vista);
            escenarioBase.setScene(escenaEmpleados);
            escenarioBase.setTitle("Ver empleados");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al volver a ver a los empleados.", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al cargar FXMLVerEmpleados", ex);
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en btnClicCancelar", ex);
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnClicAceptar(ActionEvent event) {
        if (validarCamposVacios() && validarLongitudCampos() && validarFormatoCampos() && !validarEmpleadoExistente()) {
            registrarEmpleado();
        }
    }
    
     private boolean validarCamposVacios() {
        if (tfNombre.getText().isEmpty() || tfApellidoPaterno.getText().isEmpty() ||
            tfTelefono.getText().isEmpty() || tfEmail.getText().isEmpty() ||
            tfDireccion.getText().isEmpty() || tfCodigoPostal.getText().isEmpty() ||
            tfCiudad.getText().isEmpty() || tfRol.getText().isEmpty() ||
            tfUsuario.getText().isEmpty() || tfContrasenia.getText().isEmpty()) {

            Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Datos incompletos",
                    "Por favor, asegúrate de que todos los campos están llenos.");
            return false;
        }
        return true;
    }

    private boolean validarLongitudCampos() {
        if (tfNombre.getText().length() > NOMBRE_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                    "El nombre debe tener máximo " + NOMBRE_MAX + " caracteres.");
            return false;
        }
        if (tfApellidoPaterno.getText().length() > APATERNO_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                    "El apellido paterno debe tener máximo " + APATERNO_MAX + " caracteres.");
            return false;
        }
        String apMaterno = tfApellidoMaterno.getText();
        if (!apMaterno.isEmpty() && apMaterno.length() > AMATERNO_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                    "El apellido materno debe tener máximo " + AMATERNO_MAX + " caracteres.");
            return false;
        }
        if (tfTelefono.getText().length() != TELEFONO_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                    "El teléfono debe tener exactamente " + TELEFONO_MAX + " dígitos.");
            return false;
        }
        if (!tfTelefono.getText().matches("\\d+")) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                    "El teléfono solo puede contener dígitos.");
            return false;
        }
        if (tfEmail.getText().length() > EMAIL_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                    "El email debe tener máximo " + EMAIL_MAX + " caracteres.");
            return false;
        }
        if (tfDireccion.getText().length() > DIRECCION_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                    "La dirección debe tener máximo " + DIRECCION_MAX + " caracteres.");
            return false;
        }
        if (tfCodigoPostal.getText().length() != CODIGO_POSTAL_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                    "El código postal debe tener exactamente " + CODIGO_POSTAL_MAX + " caracteres.");
            return false;
        }
        if (!tfCodigoPostal.getText().matches("\\d+")) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                    "El código postal solo puede contener dígitos.");
            return false;
        }
        if (tfCiudad.getText().length() > CIUDAD_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                    "La ciudad debe tener máximo " + CIUDAD_MAX + " caracteres.");
            return false;
        }
        if (tfRol.getText().length() > ROL_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                    "El rol debe tener máximo " + ROL_MAX + " caracteres.");
            return false;
        }
        if (tfUsuario.getText().length() > USUARIO_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                    "El usuario debe tener máximo " + USUARIO_MAX + " caracteres.");
            return false;
        }
        if (tfContrasenia.getText().length() > CONTRASENIA_MAX) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                    "La contraseña debe tener máximo " + CONTRASENIA_MAX + " caracteres.");
            return false;
        }
        return true;
    }
    
    private boolean validarFormatoCampos() {
        String nombre = tfNombre.getText().trim();
        if (!nombre.matches(NOMBRE_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "El nombre solo puede contener letras, espacios y acentos.");
            return false;
        }
        String apPaterno = tfApellidoPaterno.getText().trim();
        if (!apPaterno.matches(NOMBRE_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "El apellido paterno solo puede contener letras, espacios y acentos.");
            return false;
        }
        String apMaterno = tfApellidoMaterno.getText().trim();
        if (!apMaterno.isEmpty() && !apMaterno.matches(NOMBRE_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "El apellido materno solo puede contener letras, espacios y acentos.");
            return false;
        }
        String ciudad = tfCiudad.getText().trim();
        if (!ciudad.matches(NOMBRE_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "La ciudad solo puede contener letras, espacios y acentos.");
            return false;
        }
        String telefono = tfTelefono.getText().trim();
        if (!telefono.matches(TELEFONO_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "El teléfono debe contener exactamente 10 dígitos numéricos.");
            return false;
        }
        String email = tfEmail.getText().trim();
        if (!email.matches(EMAIL_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "El email no tiene un formato válido (ej: nombre@dominio.com).");
            return false;
        }
        String direccion = tfDireccion.getText().trim();
        if (!direccion.matches(DIRECCION_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "La dirección contiene caracteres no permitidos.");
            return false;
        }
        String codigoPostal = tfCodigoPostal.getText().trim();
        if (!codigoPostal.matches(CODIGO_POSTAL_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "El código postal debe ser exactamente 5 dígitos numéricos.");
            return false;
        }
        String rol = tfRol.getText().trim();
        if (!rol.matches(ROL_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "El rol solo puede contener letras y espacios.");
            return false;
        }
        String usuario = tfUsuario.getText().trim();
        if (!usuario.matches(USUARIO_REGEX)) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Formato inválido",
                    "El usuario debe tener entre 4 y 45 caracteres alfanuméricos o guión bajo.");
            return false;
        }
        // Contraseña: no se aplica regex complejo aquí, solo longitud 
        return true;
    }

    // Verifica si ya existe un empleado con el mismo nombre de usuario o email
    private boolean validarEmpleadoExistente() {
        try {
            boolean existe = EmpleadoDAO.validarEmpleadoExistente(tfUsuario.getText().trim()) && EmpleadoDAO.validarEmpleadoExistentePorEmail(tfEmail.getText().trim());
            if (existe) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Información existente",
                        "El nombre de usuario ya está registrado. Por favor, elija otro.");
                return true;
            }
        } catch (Exception ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al validar existencia", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error en validarEmpleadoExistente", ex);
        }
        return false;
    }
    
     private void registrarEmpleado() {
        Empleado empleado = new Empleado();
        empleado.setNombre(tfNombre.getText().trim());
        empleado.setAPaterno(tfApellidoPaterno.getText().trim());
        empleado.setAMaterno(tfApellidoMaterno.getText().trim());
        empleado.setCiudad(tfCiudad.getText().trim());
        empleado.setCodigoPostal(tfCodigoPostal.getText().trim());
        empleado.setDireccion(tfDireccion.getText().trim());
        empleado.setEmail(tfEmail.getText().trim());
        empleado.setTelefono(tfTelefono.getText().trim());
        empleado.setRol(tfRol.getText().trim());
        empleado.setUsuario(tfUsuario.getText().trim());
        empleado.setContrasenia(tfContrasenia.getText().trim()); 
        empleado.setStatus(true);

        try {
            boolean resultado = EmpleadoDAO.registrarEmpleado(empleado) ;
            if (resultado) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Éxito",
                        "El empleado ha sido registrado correctamente.");
                limpiarCampos();
                
            } else {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error",
                        "No se pudo registrar el empleado. Intente de nuevo.");
            }
        }catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de base de datos", ex.getMessage());
            LOGGER.log(Level.SEVERE, "SQLException al registrar empleado", ex);
            ex.printStackTrace();
        } catch (Exception ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general en registrarEmpleado", ex);
            ex.printStackTrace();
        }
    }
     
     private void limpiarCampos() {
        tfNombre.clear();
        tfApellidoPaterno.clear();
        tfApellidoMaterno.clear();
        tfCiudad.clear();
        tfCodigoPostal.clear();
        tfDireccion.clear();
        tfEmail.clear();
        tfTelefono.clear();
        tfRol.clear();
        tfUsuario.clear();
        tfContrasenia.clear();
    }
}
