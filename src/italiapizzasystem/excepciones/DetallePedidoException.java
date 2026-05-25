/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
