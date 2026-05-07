package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.pojo.DescripcionPedido;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Gerardo
 */
public class DescripcionPedidoDAOTest {
    @Test
    public void testObtenerDescripcionPedidoExistente() throws Exception {
        System.out.println("obtenerDescripcionPedido - ID Existente");
        int idPedido = 1; 
        ArrayList<DescripcionPedido> result = DescripcionPedidoDAO.obtenerDescripcionPedido(idPedido);
        assertNotNull(result, "La lista no debe ser nula.");
        assertFalse(result.isEmpty(), "El pedido debería tener al menos un producto en su descripción.");   
        System.out.println("Productos encontrados para el pedido " + idPedido + ": " + result.size());
    }

    @Test
    public void testObtenerDescripcionPedidoInexistente() throws Exception {
        System.out.println("obtenerDescripcionPedido - ID Inexistente");
        int idPedido = -1; 
        ArrayList<DescripcionPedido> result = DescripcionPedidoDAO.obtenerDescripcionPedido(idPedido);
        assertNotNull(result);
        assertTrue(result.isEmpty(), "La lista debería estar vacía para un ID inexistente.");
    }
    
}
