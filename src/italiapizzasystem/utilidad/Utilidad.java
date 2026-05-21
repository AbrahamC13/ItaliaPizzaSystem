
package italiapizzasystem.utilidad;

import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Optional;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

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
        alerta.getDialogPane().setStyle("-fx-font-family: 'Consolas'; -fx-font-size: 13px;");
        alerta.showAndWait();
    }
    
    public static boolean mostrarAlertaConfirmacion(String titulo, String contenido){
        Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle(titulo);
        alertaConfirmacion.setHeaderText(null);
        alertaConfirmacion.setContentText(contenido);
        Optional<ButtonType> seleccion = alertaConfirmacion.showAndWait();
        return seleccion.isPresent() && seleccion.get() == ButtonType.OK;// Evita errores si la ventana se cierra con la X 
    }
    
     
    public static Stage getEscenarioComponente(Control componente){//Obtenemos el escenario para cambiarlo o quitarlo o aladir un componente sobre el 
        return (Stage) componente.getScene().getWindow();
    }
    
    public static void cerrarVentana(Control componente) {// Centraliza el cierre de ventanas para evitar repetir código en distintos controladores
        Stage escenario = getEscenarioComponente(componente);
        escenario.close();
    }
    
    // Permite reutilizar la lógica de navegación entre vistas JavaFX desde cualquier controlador
    public static void cambiarEscena(Control componente, String rutaFXML) throws IOException{
        FXMLLoader loader = new FXMLLoader(Utilidad.class.getResource(rutaFXML));
        Parent vista = loader.load();
        Scene escena = new Scene(vista);
        Stage escenario = getEscenarioComponente(componente);
        escenario.setScene(escena);
        escenario.show();
    }

    public static boolean campoVacio(String texto){// Evita procesar cadenas vacías o con espacios que podrían generar validaciones incorrectas 
        return texto == null || texto.trim().isEmpty();
    }

    public static boolean esNumero(String texto){// Verifica que la cadena contenga únicamente caracteres numéricos válidos
        return texto.matches("\\d+");
    }

    public static boolean validarCorreo(String correo){//Permite validar el formato básico de un correo antes de almacenarlo en el sistema
        return correo.matches("^[A-Za-z0-9+_.-]+@(.+)$");
    }

    public static void limpiarCampos(TextField... campos){//Reduce código repetido al limpiar múltiples campos de texto en formularios JavaFX
        for(TextField campo : campos){
            campo.clear();
        }
    }

    public static String formatearMoneda(double cantidad){//Matiene el formato de moneda en distintas ventanas del sistema
        NumberFormat formato = NumberFormat.getCurrencyInstance();
        return formato.format(cantidad);
    }

    public static int convertirEntero(String texto){//Controla errores de conversión para evitar excepciones por entradas inválidas
        try{
            return Integer.parseInt(texto);
        }catch (NumberFormatException e){
            return -1;
        }
    }

    public static double convertirDouble(String texto){// Evita fallos al convertir valores decimales ingresados incorrectamente por el usuario
        try{
            return Double.parseDouble(texto);
        }catch (NumberFormatException e){
            return -1;
        }
    }
}
