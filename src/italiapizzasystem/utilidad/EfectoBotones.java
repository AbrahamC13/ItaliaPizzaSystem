
package italiapizzasystem.utilidad;

import javafx.scene.Cursor;
import javafx.scene.control.Button;

/**
 *
 * @author acrca
 */
public class EfectoBotones {
    public static void darEfectoBotones(Button... botones) {
        for (Button boton : botones) {
            boton.setOnMouseEntered(event -> {
                boton.setCursor(Cursor.HAND); 
                boton.setOpacity(0.8);        
                boton.setScaleX(1.02);        
                boton.setScaleY(1.02);
            });

            boton.setOnMouseExited(event -> {
                boton.setCursor(Cursor.DEFAULT);
                boton.setOpacity(1.0);        
                boton.setScaleX(1.0);         
                boton.setScaleY(1.0);
            });

            boton.setOnMousePressed(event -> {
                boton.setScaleX(0.95);
                boton.setScaleY(0.95);
            });

            boton.setOnMouseReleased(event -> {
                boton.setScaleX(1.02);
                boton.setScaleY(1.02);
            });
        }
    }
}
