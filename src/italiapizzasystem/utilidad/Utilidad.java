package italiapizzasystem.utilidad;

import java.text.NumberFormat;
import java.util.Optional;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;

/**
 *
 * @author Gerardo
 */
public class Utilidad {

    public static void mostrarAlertaSimple(
            Alert.AlertType tipo,
            String titulo, String contenido) {

        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(contenido);
        alerta.getDialogPane().setStyle(
                "-fx-font-family: 'Consolas'; -fx-font-size: 13px;"
        );
        
        alerta.showAndWait();
    }

    public static boolean mostrarAlertaConfirmacion(
            String titulo, String contenido) {

        Alert alertaConfirmacion =
                new Alert(Alert.AlertType.CONFIRMATION);
        
        alertaConfirmacion.setTitle(titulo);
        alertaConfirmacion.setHeaderText(null);
        alertaConfirmacion.setContentText(contenido);
        Optional<ButtonType> seleccion =
                alertaConfirmacion.showAndWait();

        // Evita errores si la ventana se cierra con la X
        return seleccion.isPresent()
                && seleccion.get() == ButtonType.OK;
    }

    // Evita procesar cadenas vacías o con espacios
    // que podrían generar validaciones incorrectas
    public static boolean campoVacio(String texto) {
        return texto == null
                || texto.trim().isEmpty();
    }

    // Verifica que la cadena contenga únicamente
    // caracteres numéricos válidos
    public static boolean esNumero(String texto) {
        return texto.matches("\\d+");
    }

    // Permite validar el formato básico de un correo
    // antes de almacenarlo en el sistema
    public static boolean validarCorreo(String correo) {
        return correo.matches(
                "^[A-Za-z0-9+_.-]+@(.+)$"
        );
    }

    // Reduce código repetido al limpiar múltiples
    // campos de texto en formularios JavaFX
    public static void limpiarCampos(TextField... campos) {
        for (TextField campo : campos) {
            campo.clear();
        }
    }

    // Mantiene un formato uniforme de moneda
    // en distintas ventanas del sistema
    public static String formatearMoneda(double cantidad) {
        NumberFormat formato =
                NumberFormat.getCurrencyInstance();
        return formato.format(cantidad);
    }

    // Controla errores de conversión para evitar
    // excepciones por entradas inválidas
    public static int convertirEntero(String texto) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    // Evita fallos al convertir valores decimales
    // ingresados incorrectamente por el usuario
    public static double convertirDouble(String texto) {
        try {
            return Double.parseDouble(texto);
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}