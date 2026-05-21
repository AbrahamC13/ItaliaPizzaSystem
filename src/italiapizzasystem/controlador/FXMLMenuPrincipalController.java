package italiapizzasystem.controlador;

import italiapizzasystem.ItaliaPizzaSystem;
import italiapizzasystem.persistencia.pojo.Empleado;
import italiapizzasystem.utilidad.Navegador;
import italiapizzasystem.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.geometry.Insets;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;

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
    private static final Logger LOGGER = Logger.getLogger(FXMLMenuPrincipalController.class.getName());
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
        try {
            Stage escenarioBase = (Stage) lbEmpleado.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource("vista/FXMLLogin.fxml"));
            Parent vista = cargador.load();
            FXMLLoginController controlador = cargador.getController();
            Scene escenaLogin = new Scene(vista);
            escenarioBase.setScene(escenaLogin);
            escenarioBase.setTitle("Login");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cerrar sesión.", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al cargar FXMLLogin", ex);
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en la función btnClicCerrarSesion", ex);
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnClicAdministracion(ActionEvent event) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Administración");
        dialog.setHeaderText("Seleccione una opción");
        dialog.setContentText("¿Qué desea gestionar?");

        ButtonType empleadosBtn = new ButtonType("Empleados", ButtonBar.ButtonData.LEFT);
        ButtonType clientesBtn = new ButtonType("Clientes", ButtonBar.ButtonData.LEFT);
        ButtonType cancelarBtn = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

        dialog.getDialogPane().getButtonTypes().addAll(empleadosBtn, clientesBtn, cancelarBtn);

        dialog.getDialogPane().setPrefSize(400, 200);
        // Procesamos la respuesta
        dialog.showAndWait().ifPresent(response -> {
            if (response == empleadosBtn) {
                abrirVistaEmpleados();
            } else if (response == clientesBtn) {
                abrirVistaClientes();
            }
            // Si es cancelar, no hace nada
        });
    }
    
    private void abrirVistaEmpleados(){
        try {
            Stage escenarioBase = (Stage) lbEmpleado.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource("vista/FXMLVerEmpleados.fxml"));
            Parent vista = cargador.load();
            FXMLVerEmpleadosController controlador = cargador.getController();
            Scene escenaEmpleados = new Scene(vista);
            escenarioBase.setScene(escenaEmpleados);
            escenarioBase.setTitle("Empleados.");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,"Error", "No se pudo abrir la vista de empleados.");
            LOGGER.log(Level.SEVERE, "Error al cargar FXMLVerEmpleados ", ex);
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,"Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en abrirVistaEmpleados ", ex);
            ex.printStackTrace();
        }
    }
    
    private void abrirVistaClientes(){
        try {
            Stage escenarioBase = (Stage) lbEmpleado.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource("vista/FXMLVerClientes.fxml"));
            Parent vista = cargador.load();
            FXMLVerClientesController controlador = cargador.getController();
            Scene escenaClientes = new Scene(vista);
            escenarioBase.setScene(escenaClientes);
            escenarioBase.setTitle("Clientes.");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,"Error", "No se pudo abrir la vista de clientes.");
            LOGGER.log(Level.SEVERE, "Error al cargar FXMLVerClientes ", ex);
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR,"Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en abrirVistaClientes ", ex);
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnClicAyuda(ActionEvent event) {
        Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "✨ Información del Sistema ✨",
                "╔════════════════════════════════════════╗\n" +
                "║  👥 EQUIPO DE DESARROLLO               ║\n" +
                "╠════════════════════════════════════════╣\n" +
                "║  ♣ Gerardo Abraham Barrón Gómez ♣      ║\n" +
                "║  ♕ Abraham Cano Ramírez ♕            ║\n" +
                "║  ♣ Javier Yajseel Lily Reyes ♣         ║\n" +
                "║  ♣ Leonardo [Apellido] ♣               ║\n" +
                "╠════════════════════════════════════════╣\n" +
                "║         📚 CARRERA EN CURSO            ║\n" +
                "║     Ingeniería de Software             ║\n" +
                "╠════════════════════════════════════════╣\n" +
                "║          🎓 SEMESTRES                  ║\n" +
                "║           4to y 6to                    ║\n" +
                "╠════════════════════════════════════════╣\n" +
                "║    🏫 INSTITUCIÓN EDUCATIVA            ║\n" +
                "║     Universidad Veracruzana            ║\n" +
                "╚════════════════════════════════════════╝");
    }

    @FXML
    private void btnClicInventarios(ActionEvent event) {
        try {
            Stage escenarioBase = (Stage) lbEmpleado.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource("vista/FXMLInventario.fxml"));
            Parent vista = cargador.load();
            FXMLInventarioController controlador = cargador.getController();
            Scene escenaInventario = new Scene(vista);
            escenarioBase.setScene(escenaInventario);
            escenarioBase.setTitle("Inventario.");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al acceder a la página.", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al cargar FXMLInventario ", ex);
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en la función btnClicInventarios", ex);
            ex.printStackTrace();
        }
        
    }

    @FXML
    private void btnClicPedidos(ActionEvent event) {
        Navegador.cambiarVentana(lbEmpleado, "vista/FXMLPedidos.fxml", "Pedidos");
    }
    
}
