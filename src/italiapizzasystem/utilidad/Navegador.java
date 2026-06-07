
package italiapizzasystem.utilidad;

import italiapizzasystem.ItaliaPizzaSystem;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 *
 * @author acrca
 */
public class Navegador {
    private static final Logger LOGGER = Logger.getLogger(Navegador.class.getName());

    public static void cambiarVentana(Control nodoAcceso, String rutaFXML, String titulo) {
        try {
            Stage escenarioBase = (Stage) nodoAcceso.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource(rutaFXML));
            Parent vista = cargador.load();
            
            Scene escena = new Scene(vista);
            escenarioBase.setScene(escena);
            escenarioBase.setTitle(titulo);
            escenarioBase.centerOnScreen();
            escenarioBase.show();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de navegación", "No se pudo cargar la vista: " + titulo);
            LOGGER.log(Level.SEVERE, "Error al cargar FXML en Navegador: " + rutaFXML, ex);
        }
    }

    public static FXMLLoader cambiarVentanaConControlador(Control nodoAcceso, String rutaFXML, String titulo) {
        try {
            Stage escenarioBase = (Stage) nodoAcceso.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource(rutaFXML));
            Parent vista = cargador.load();
            
            Scene escena = new Scene(vista);
            escenarioBase.setScene(escena);
            escenarioBase.setTitle(titulo);
            escenarioBase.centerOnScreen();
            escenarioBase.show();
            
            return cargador;
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de navegación", "No se pudo abrir la ventana: " + titulo);
            LOGGER.log(Level.SEVERE, "Error al cargar FXML con controlador en Navegador: " + rutaFXML, ex);
            return null;
        }
    }
}
