/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package italiapizzasystem.utilidad;

import italiapizzasystem.excepciones.ImagenInvalidaException;
import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;
import java.util.logging.Logger;
import java.util.logging.Level;
import javafx.scene.image.ImageView;

/**
 *
 * @author acrca
 */
public class GestorImagen {
    
    private static final Logger LOGGER = Logger.getLogger(GestorImagen.class.getName());
    
    public static Image convertirBytesAImagen(byte[] bytesFoto) throws ImagenInvalidaException{
        if (bytesFoto == null || bytesFoto.length == 0) {
            return null;
        }
        
        try {
            ByteArrayInputStream entradaBytes = new ByteArrayInputStream(bytesFoto);
            return new Image(entradaBytes);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error crítico al procesar el flujo binario de la imagen", ex);
            System.err.println("Error al procesar el flujo de bytes de la imagen: " + ex.getMessage());
            throw new ImagenInvalidaException("Los bytes de la imagen están corruptos o el formato no es compatible.", ex);
        }
    }
    
    public static void cargarImagenDefault(ImageView img_ImagenProducto) {
        try {
            Image imagenComodin = new Image(GestorImagen.class.getResourceAsStream("/italiapizzasystemRecursos/imagenes/imagenDefault.png"));
            img_ImagenProducto.setImage(imagenComodin);
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error crítico al procesar la imagen", ex);
            System.err.println("Error al procesar la carga de imagen default: " + ex.getMessage());
            if (img_ImagenProducto != null) {
                img_ImagenProducto.setImage(null); 
            }
        }
    }
    
}
