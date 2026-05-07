
package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.pojo.Cliente;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Gerardo
 */
public class ClienteDAOTest {

    /**
     * Test of obtenerClientes method, of class ClienteDAO.
     */
    @Test
    public void testObtenerClientesExito() {
       System.out.println("obtenerClientes - Caso Exito");
        try {
            ArrayList<Cliente> result = ClienteDAO.obtenerClientes();
            assertNotNull(result, "La lista de clientes no debería ser nula.");
            if (!result.isEmpty()) {
                Cliente primerCliente = result.get(0);
                assertNotNull(primerCliente.getNombre(), "El nombre del cliente no debe ser nulo.");
                assertTrue(primerCliente.getIdCliente() > 0, "El ID del cliente debe ser positivo.");
            }
            System.out.println("Clientes recuperados: " + result.size());
        } catch (SQLException e) {
            fail("No se pudo conectar a la base de datos o hubo un error SQL: " + e.getMessage());
        }
    }
    
    
}
