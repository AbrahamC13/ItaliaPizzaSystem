package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.pojo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Gerardo
 */
public class ProductoDAOTest {
    private Connection conexion;

    // === CONFIGURACIÓN Y AISLAMIENTO DE LA BASE DE DATOS ===
    public void setUp() throws SQLException {
        conexion = ConexionBD.abrirConexion();
        if (conexion == null) {
            throw new RuntimeException("Error: La conexión a la BD de pruebas es nula.");
        }
        
        try (PreparedStatement disableKeys = conexion.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
             PreparedStatement limpiarDescripcion = conexion.prepareStatement("DELETE FROM descripcionPedido");
             PreparedStatement limpiarProducto = conexion.prepareStatement("DELETE FROM producto");
             PreparedStatement reiniciarAiProducto = conexion.prepareStatement("ALTER TABLE producto AUTO_INCREMENT = 1");
             PreparedStatement enableKeys = conexion.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
            
            disableKeys.executeUpdate();
            limpiarDescripcion.executeUpdate();
            limpiarProducto.executeUpdate();
            reiniciarAiProducto.executeUpdate();
            enableKeys.executeUpdate();
            
        } finally {
            if (conexion != null) {
                conexion.close();
            }
        }
    }

    // === MÉTODOS DE ASISTENCIA (ASSERTIONS) ===
    private void assertNotNull(Object obj, String mensaje) {
        if (obj == null) throw new AssertionError(mensaje);
    }

    private void assertEquals(int esperado, int actual, String mensaje) {
        if (esperado != actual) throw new AssertionError(mensaje + " (Esp: " + esperado + ", Act: " + actual + ")");
    }

    private void assertEquals(double esperado, double actual, String mensaje) {
        if (Double.compare(esperado, actual) != 0) throw new AssertionError(mensaje + " (Esp: " + esperado + ", Act: " + actual + ")");
    }

    private void assertEquals(String esperado, String actual, String mensaje) {
        if (esperado == null && actual != null || esperado != null && !esperado.equals(actual)) {
            throw new AssertionError(mensaje + " (Esp: " + esperado + ", Act: " + actual + ")");
        }
    }

    private void assertTrue(boolean condicion, String mensaje) {
        if (!condicion) throw new AssertionError(mensaje);
    }

    private void fail(String mensaje) {
        throw new AssertionError(mensaje);
    }

    // === INSERCIÓN AUXILIAR DIRECTA EN BD ===
    private void insertarProductoDirecto(String codigo, String desc, String nombre, String restr, double precio, int cant) throws SQLException {
        String sql = "INSERT INTO producto (codigoProducto, descripcion, nombre, restricciones, precio, cantidad, foto) VALUES (?, ?, ?, ?, ?, ?, NULL)";
        try (Connection conn = ConexionBD.abrirConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, codigo);
            stmt.setString(2, desc);
            stmt.setString(3, nombre);
            stmt.setString(4, restr);
            stmt.setDouble(5, precio);
            stmt.setInt(6, cant);
            stmt.executeUpdate();
        }
    }

    // === PRUEBAS UNITARIAS ===

    public void testObtenerProductos_ConDatos() {
        System.out.println("Prueba 1: obtenerProductos - Recuperar todos los productos");
        try {
            insertarProductoDirecto("PROD-01", "Pizza Pepperoni Grande", "Pizza Pepperoni", "Contiene Gluten", 149.00, 15);
            insertarProductoDirecto("PROD-02", "Refresco de Cola 2L", "Refresco Cola", "Ninguna", 45.00, 30);

            ArrayList<Producto> result = ProductoDAO.obtenerProductos();
            assertNotNull(result, "La lista de productos no debería ser nula.");
            assertEquals(2, result.size(), "Deberían haberse recuperado exactamente 2 productos.");
            
            System.out.println("   -> Listado completo verificado.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    public void testRegistrarProducto_Exito() {
        System.out.println("Prueba 2: registrarProducto - Inserción correcta");
        try {
            Producto nuevo = new Producto();
            nuevo.setCodigoProducto("PROD-77");
            nuevo.setDescripcion("Papas Sazonadas");
            nuevo.setNombre("Papas Fritas");
            nuevo.setRestricciones("Ninguna");
            nuevo.setPrecio(60.00);
            nuevo.setCantidad(20);
            nuevo.setFoto(null);

            boolean exito = ProductoDAO.registrarProducto(nuevo);
            assertTrue(exito, "El método registrarProducto debería retornar verdadero.");

            // Validar verificación en la base de datos
            ArrayList<Producto> bdProductos = ProductoDAO.obtenerProductos();
            assertEquals(1, bdProductos.size(), "El producto no se guardó en la base de datos.");
            assertEquals("Papas Fritas", bdProductos.get(0).getNombre(), "El nombre guardado difiere.");
            
            System.out.println("   -> Inserción y persistencia validadas.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    public void testBuscarProductos_PorNombre() {
        System.out.println("Prueba 3: buscarProductos - Coincidencia parcial de Nombre");
        try {
            insertarProductoDirecto("P-01", "Pizza Hawaiana", "Pizza Hawaiana", "Piña", 159.00, 10);
            insertarProductoDirecto("P-02", "Spaghetti Boloñesa", "Pasta Spaghetti", "Gluten", 120.00, 5);

            ArrayList<Producto> result = ProductoDAO.buscarProductos("Hawaiana");
            assertNotNull(result, "El resultado de búsqueda no debe ser nulo.");
            assertEquals(1, result.size(), "Debería haber encontrado 1 producto coincidente.");
            assertEquals("Pizza Hawaiana", result.get(0).getNombre(), "El producto encontrado no es el correcto.");
            
            System.out.println("   -> Búsqueda por filtro LIKE de nombre exitosa.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    public void testBuscarProductos_PorCodigo() {
        System.out.println("Prueba 4: buscarProductos - Coincidencia por Código de Producto");
        try {
            insertarProductoDirecto("CODIGO-ABC", "Agua Ciel 1L", "Agua", "Ninguna", 20.00, 50);

            ArrayList<Producto> result = ProductoDAO.buscarProductos("ABC");
            assertNotNull(result, "El resultado no debe ser nulo.");
            assertEquals(1, result.size(), "Debería coincidir mediante el segundo criterio OR de la consulta.");
            
            System.out.println("   -> Búsqueda por filtro LIKE de código exitosa.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    public void testEliminarProducto_Exito() {
        System.out.println("Prueba 5: eliminarProducto - Remoción física");
        try {
            insertarProductoDirecto("DEL-01", "Producto a borrar", "Borrar Me", "Ninguna", 10.00, 1);
            
            // Obtenemos el ID asignado por el autoincrementable de la BD
            ArrayList<Producto> actuales = ProductoDAO.obtenerProductos();
            int idAsignado = actuales.get(0).getIdProducto();

            boolean eliminado = ProductoDAO.eliminarProducto(idAsignado);
            assertTrue(eliminado, "El método eliminarProducto debió retornar true.");

            // Comprobar que quedó vacía
            ArrayList<Producto> vacio = ProductoDAO.obtenerProductos();
            assertEquals(0, vacio.size(), "El producto sigue apareciendo en la base de datos.");
            
            System.out.println("   -> Eliminación física completada.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    public void testActualizarProducto_Exito() {
        System.out.println("Prueba 6: actualizarProducto - Modificación de datos");
        try {
            insertarProductoDirecto("ACT-01", "Precio Viejo", "Producto Variable", "Ninguna", 50.00, 5);
            
            ArrayList<Producto> actuales = ProductoDAO.obtenerProductos();
            Producto productoExistente = actuales.get(0);

            // Modificar valores del objeto entidad
            productoExistente.setNombre("Producto Actualizado");
            productoExistente.setPrecio(85.50);
            productoExistente.setCantidad(99);

            boolean modificado = ProductoDAO.actualizarProducto(productoExistente);
            assertTrue(modificado, "El método actualizarProducto debió retornar true.");

            // Validar cambios directo de la BD
            ArrayList<Producto> actualizados = ProductoDAO.obtenerProductos();
            assertEquals("Producto Actualizado", actualizados.get(0).getNombre(), "El nombre no cambió.");
            assertEquals(85.50, actualizados.get(0).getPrecio(), "El precio no se actualizó.");
            assertEquals(99, actualizados.get(0).getCantidad(), "La cantidad no cambió.");
            
            System.out.println("   -> Sentencia UPDATE ejecutada y verificada.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    // === EJECUTOR PRINCIPAL MANUAL (MAIN) ===
    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBAS MANUALES DE PRODUCTODAO ===");
        int pruebasPasadas = 0;
        int totalPruebas = 6;

        ProductoDAOTest ejecutor = new ProductoDAOTest();

        for (int i = 1; i <= totalPruebas; i++) {
            try {
                ejecutor.setUp();
                switch(i) {
                    case 1: ejecutor.testObtenerProductos_ConDatos(); break;
                    case 2: ejecutor.testRegistrarProducto_Exito(); break;
                    case 3: ejecutor.testBuscarProductos_PorNombre(); break;
                    case 4: ejecutor.testBuscarProductos_PorCodigo(); break;
                    case 5: ejecutor.testEliminarProducto_Exito(); break;
                    case 6: ejecutor.testActualizarProducto_Exito(); break;
                }
                System.out.println("\u2705 Pasó\n");
                pruebasPasadas++;
            } catch (Exception | AssertionError e) {
                System.out.println("\u274c Falló: " + e.getMessage());
                e.printStackTrace();
                System.out.println();
            }
        }

        System.out.println("=============================================");
        System.out.println("  RESUMEN: " + pruebasPasadas + " / " + totalPruebas + " PRUEBAS PASARON CON ÉXITO.");
        System.out.println("=============================================");
    }
}
