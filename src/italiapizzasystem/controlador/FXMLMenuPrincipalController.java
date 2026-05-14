package italiapizzasystem.controlador;

import italiapizzasystem.ItaliaPizzaSystem;
import italiapizzasystem.persistencia.pojo.Empleado;
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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;

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
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnClicAdministracion(ActionEvent event) {
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
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            ex.printStackTrace();
        }
        
    }

    @FXML
    private void btnClicPedidos(ActionEvent event) {
        try {
            Stage escenarioBase = (Stage) lbEmpleado.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource("vista/FXMLPedidos.fxml"));
            Parent vista = cargador.load();
            FXMLPedidosController controlador = cargador.getController();
            Scene escenaPedidos = new Scene(vista);
            escenarioBase.setScene(escenaPedidos);
            escenarioBase.setTitle("Pedidos.");
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al acceder a la página.", ex.getMessage());
            ex.printStackTrace();
        }catch(Exception ex){
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            ex.printStackTrace();
        }
    }
    
}
