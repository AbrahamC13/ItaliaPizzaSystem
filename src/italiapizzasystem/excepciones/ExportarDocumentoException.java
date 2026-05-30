/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
