package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.pojo.Producto;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Gerardo
 */
public class ProductoDAOTest {
    @Test
    public void testObtenerProductos() {
        System.out.println("Prueba: obtenerProductos");
        
        try {
            ArrayList<Producto> result = ProductoDAO.obtenerProductos();
            assertNotNull(result, "La lista de productos no debería ser nula.");
            // Si hay productos, validamos que el mapeo sea correcto
            if (!result.isEmpty()) {
                Producto primero = result.get(0);
                assertNotNull(primero.getNombre(), "El nombre del producto no debe ser nulo.");
                assertTrue(primero.getPrecio() >= 0, "El precio no puede ser negativo.");
                assertNotNull(primero.getCodigoProducto(), "El código del producto no debe ser nulo.");
            }
            System.out.println("Productos recuperados: " + result.size());
            
        } catch (SQLException e) {
            fail("Error al consultar la base de datos: " + e.getMessage());
        }
    }
}
