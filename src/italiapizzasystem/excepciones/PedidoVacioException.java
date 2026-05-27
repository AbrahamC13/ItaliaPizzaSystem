/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
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
