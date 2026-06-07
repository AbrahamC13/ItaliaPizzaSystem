
package italiapizzasystem.excepciones;

/**
 *
 * @author acrca
 */
public class RegistrarPedidoException extends Exception{
    
    public RegistrarPedidoException(String mensaje) {
        super(mensaje);
    }

    public RegistrarPedidoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
}
