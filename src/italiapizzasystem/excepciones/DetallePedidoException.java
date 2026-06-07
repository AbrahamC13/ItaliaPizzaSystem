
package italiapizzasystem.excepciones;

/**
 *
 * @author acrca
 */
public class DetallePedidoException extends Exception{
    
    public DetallePedidoException(String mensaje) {
        super(mensaje);
    }

    public DetallePedidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
}
