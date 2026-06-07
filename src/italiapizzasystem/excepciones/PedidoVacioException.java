
package italiapizzasystem.excepciones;

/**
 *
 * @author acrca
 */
public class PedidoVacioException extends Exception{

    public PedidoVacioException(String mensaje) {
        super(mensaje);
    }
    
    public PedidoVacioException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
    
}
