/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
