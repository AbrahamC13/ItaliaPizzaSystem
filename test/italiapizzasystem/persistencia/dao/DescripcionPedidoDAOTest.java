package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.pojo.DescripcionPedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author Gerardo
 */
public class DescripcionPedidoDAOTest {
    private Connection conexion;

    // Simulación manual de BeforeEach para aislar las pruebas
    public void setUp() throws SQLException {
        conexion = ConexionBD.abrirConexion();
        if (conexion == null) {
            throw new RuntimeException("Error: La conexión a la BD de pruebas es nula. Verifica el servidor.");
        }
        
        // Desactivamos temporalmente las llaves foráneas para poder limpiar la tabla libremente
        try (PreparedStatement disableKeys = conexion.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
             PreparedStatement stmt = conexion.prepareStatement("DELETE FROM descripcionPedido");
             PreparedStatement enableKeys = conexion.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
            
            disableKeys.executeUpdate();
            stmt.executeUpdate();
            enableKeys.executeUpdate();
            
        } finally {
            if (conexion != null) {
                conexion.close();
            }
        }
    }

    //Métodos de asistencia para sustituir Junit
    private void assertNotNull(Object obj, String mensaje) {
        if (obj == null) {
            throw new AssertionError(mensaje);
        }
    }

    private void assertEquals(int esperado, int actual, String mensaje) {
        if (esperado != actual) {
            throw new AssertionError(mensaje + " (Esperado: " + esperado + ", Actual: " + actual + ")");
        }
    }

    private void assertEquals(double esperado, double actual, String mensaje) {
        if (Double.compare(esperado, actual) != 0) {
            throw new AssertionError(mensaje + " (Esperado: " + esperado + ", Actual: " + actual + ")");
        }
    }

    private void assertTrue(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private void fail(String mensaje) {
        throw new AssertionError(mensaje);
    }

    // Método auxiliar para la inserción de prueba
    // Dado que DescripcionPedidoDAO solo tiene el método de obtener, necesitamos una forma de registrar datos antes de la consulta
    private void insertarDescripcionPrueba(int idProducto, int idPedido, int cantidad, double total) throws SQLException {
       Connection conn = ConexionBD.abrirConexion();
        if (conn != null) {
            // Desactivamos los checks de FK, insertamos el registro aislado, y los volvemos a activar
            String disableSql = "SET FOREIGN_KEY_CHECKS = 0";
            String insertSql = "INSERT INTO descripcionPedido (Producto_idProducto, Pedido_idPedido, cantidad, total) VALUES (?, ?, ?, ?)";
            String enableSql = "SET FOREIGN_KEY_CHECKS = 1";
            
            try {
                try (PreparedStatement stmtDisable = conn.prepareStatement(disableSql)) {
                    stmtDisable.executeUpdate();
                }
                try (PreparedStatement stmtInsert = conn.prepareStatement(insertSql)) {
                    stmtInsert.setInt(1, idProducto);
                    stmtInsert.setInt(2, idPedido);
                    stmtInsert.setInt(3, cantidad);
                    stmtInsert.setDouble(4, total);
                    stmtInsert.executeUpdate();
                }
                try (PreparedStatement stmtEnable = conn.prepareStatement(enableSql)) {
                    stmtEnable.executeUpdate();
                }
            } finally {
                conn.close();
            }
        }
    }

    // Pruebas unitarias

    public void testObtenerDescripcionPedido_Existente() {
        System.out.println("Prueba 1: obtenerDescripcionPedido - Caso ID Existente");
        try {
            int idPedidoPrueba = 99;
            int idProductoPrueba = 5;
            int cantidadPrueba = 3;
            double totalPrueba = 450.50;

            // Insertamos directamente el registro que vamos a intentar recuperar
            insertarDescripcionPrueba(idProductoPrueba, idPedidoPrueba, cantidadPrueba, totalPrueba);

            ArrayList<DescripcionPedido> result = DescripcionPedidoDAO.obtenerDescripcionPedido(idPedidoPrueba);
            
            assertNotNull(result, "La lista de descripciones de pedido no debería ser nula.");
            assertEquals(1, result.size(), "Debería haber exactamente 1 descripción para este pedido.");
            
            DescripcionPedido registro = result.get(0);
            assertEquals(idPedidoPrueba, registro.getIdPedido(), "El ID del pedido no coincide.");
            assertEquals(idProductoPrueba, registro.getIdProducto(), "El ID del producto no coincide.");
            assertEquals(cantidadPrueba, registro.getCantidad(), "La cantidad no coincide.");
            assertEquals(totalPrueba, registro.getTotal(), "El total no coincide.");
            
            System.out.println("   -> Detalles del pedido validados correctamente.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    public void testObtenerDescripcionPedido_Inexistente() {
        System.out.println("Prueba 2: obtenerDescripcionPedido - Caso ID Inexistente");
        try {
            int idPedidoFalso = 9999;
            
            // Consultamos un ID que sabemos que no existe en una tabla recién limpiada
            ArrayList<DescripcionPedido> result = DescripcionPedidoDAO.obtenerDescripcionPedido(idPedidoFalso);
            
            assertNotNull(result, "La lista devuelta no debe ser nula.");
            assertTrue(result.isEmpty(), "La lista debería estar vacía para un ID de pedido inexistente.");
            
            System.out.println("   -> Retornó una lista vacía correctamente.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    public void testObtenerDescripcionPedido_MultiplesProductos() {
        System.out.println("Prueba 3: obtenerDescripcionPedido - Caso Múltiples Productos en el Mismo Pedido");
        try {
            int idPedidoCompartido = 40;
            
            // Insertamos dos productos diferentes bajo el mismo identificador de pedido
            insertarDescripcionPrueba(10, idPedidoCompartido, 1, 120.00);
            insertarDescripcionPrueba(11, idPedidoCompartido, 2, 240.00);

            ArrayList<DescripcionPedido> result = DescripcionPedidoDAO.obtenerDescripcionPedido(idPedidoCompartido);
            
            assertNotNull(result, "La lista no debería ser nula.");
            assertEquals(2, result.size(), "Deberían recuperarse los 2 productos asociados a este pedido.");
            
            System.out.println("   -> Recuperados " + result.size() + " elementos asociados al pedido de manera exitosa.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    //Ejecutamos las pruebas en el main
    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBAS MANUALES DE DESCRIPCIONPEDIDODAO ===");
        int pruebasPasadas = 0;
        int totalPruebas = 3;

        DescripcionPedidoDAOTest ejecutor = new DescripcionPedidoDAOTest();

        // Prueba 1 Caso ID Existente
        try {
            ejecutor.setUp();
            ejecutor.testObtenerDescripcionPedido_Existente();
            System.out.println("\u2705 Pas\u00f3 Exitosamente\n");
            pruebasPasadas++;
        } catch (Exception | AssertionError e) {
            System.out.println("\u274c Fall\u00f3: " + e.getMessage());
            e.printStackTrace();
            System.out.println();
        }

        //Prueba 2 Caso ID Inexistente
        try {
            ejecutor.setUp();
            ejecutor.testObtenerDescripcionPedido_Inexistente();
            System.out.println("\u2705 Pas\u00f3 Exitosamente\n");
            pruebasPasadas++;
        } catch (Exception | AssertionError e) {
            System.out.println("\u274c Fall\u00f3: " + e.getMessage());
            e.printStackTrace();
            System.out.println();
        }

        // Prueba 3 Caso Múltiples Productos
        try {
            ejecutor.setUp();
            ejecutor.testObtenerDescripcionPedido_MultiplesProductos();
            System.out.println("\u2705 Pas\u00f3 Exitosamente\n");
            pruebasPasadas++;
        } catch (Exception | AssertionError e) {
            System.out.println("\u274c Fall\u00f3: " + e.getMessage());
            e.printStackTrace();
            System.out.println();
        }

        System.out.println("=============================================");
        System.out.println("  RESUMEN: " + pruebasPasadas + " / " + totalPruebas + " PRUEBAS PASARON CON \u00c9XITO.");
        System.out.println("=============================================");
    }
    
}
