
package italiapizzasystem.utilidad;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 *
 * @author Gerardo
 */
public class Utilidad {
    public static void mostrarAlertaSimple(Alert.AlertType tipo, String titulo, String contenido){
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.showAndWait();
    }
    
    public static boolean mostrarAlertaConfirmacion(String titulo, String contenido){
        Alert alertaConfirmacion= new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle(titulo);
        alertaConfirmacion.setHeaderText(null);
        alertaConfirmacion.setContentText(contenido);
        Optional<ButtonType> seleccion = alertaConfirmacion.showAndWait();
        return seleccion.get() == ButtonType.OK;
    }
    
     
    public static Stage getEscenarioComponente(Control componente){//Obtenemos el escenario para cambiarlo o quitarlo o aladir un componente sobre el 
        return (Stage) componente.getScene().getWindow();
    }
}
