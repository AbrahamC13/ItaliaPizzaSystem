
package italiapizzasystem.excepciones;

/**
 *
 * @author acrca
 */
public class ImagenInvalidaException extends Exception{
    
    public ImagenInvalidaException(String mensaje){
        super(mensaje);
    }
    
    public ImagenInvalidaException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
}
