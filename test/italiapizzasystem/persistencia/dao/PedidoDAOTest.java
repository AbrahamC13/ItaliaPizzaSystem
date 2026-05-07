package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.pojo.Pedido;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Gerardo
 */
public class PedidoDAOTest {
    @Test
    public void testObtenerPedidos() {
        System.out.println("Ejecutando: obtenerPedidos");
        
        try {
            ArrayList<Pedido> result = PedidoDAO.obtenerPedidos();
            assertNotNull(result, "La lista de pedidos no debería ser nula.");  
            // Verificamos consistencia de datos si la lista tiene elementos
            if (!result.isEmpty()) {
                Pedido primerPedido = result.get(0);
                assertTrue(primerPedido.getIdPedido() > 0, "El ID del pedido debe ser un entero positivo.");
                assertNotNull(primerPedido.getStatus(), "El status del pedido no debe ser nulo.");
                System.out.println("Se recuperaron " + result.size() + " pedidos exitosamente.");
            } else {
                System.out.println("La base de datos no tiene pedidos registrados, pero la conexión fue exitosa.");
            }
            
        } catch (SQLException e) {
            fail("Error de SQL durante la prueba: " + e.getMessage());
        }
    }
    
}
