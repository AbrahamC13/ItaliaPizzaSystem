
package italiapizzasystem.excepciones;

/**
 *
 * @author acrca
 */
public class ExportarDocumentoException extends Exception{
    public ExportarDocumentoException(String mensaje) {
        super(mensaje);
    }
    
    public ExportarDocumentoException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
