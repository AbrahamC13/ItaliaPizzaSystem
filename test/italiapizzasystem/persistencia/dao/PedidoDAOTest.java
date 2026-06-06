package italiapizzasystem.persistencia.dao;

import italiapizzasystem.excepciones.RegistrarPedidoException;
import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.pojo.OrdenFila;
import italiapizzasystem.persistencia.pojo.Pedido;
import italiapizzasystem.persistencia.pojo.PedidoCliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Gerardo
 */
public class PedidoDAOTest {
  private Connection conexion;

    // === CONFIGURACIÓN Y AISLAMIENTO DE LA BASE DE DATOS ===
    public void setUp() throws SQLException {
        conexion = ConexionBD.abrirConexion();
        if (conexion == null) {
            throw new RuntimeException("Error: La conexión a la BD de pruebas es nula.");
        }
        
        try (PreparedStatement disableKeys = conexion.prepareStatement("SET FOREIGN_KEY_CHECKS = 0");
             PreparedStatement limpiarDescripcion = conexion.prepareStatement("DELETE FROM descripcionPedido");
             PreparedStatement limpiarPedido = conexion.prepareStatement("DELETE FROM pedido");
             PreparedStatement limpiarCliente = conexion.prepareStatement("DELETE FROM cliente");
             PreparedStatement limpiarProducto = conexion.prepareStatement("DELETE FROM producto");
             PreparedStatement limpiarEmpleado = conexion.prepareStatement("DELETE FROM empleado");
             // Reiniciar el contador AUTO_INCREMENT de empleado para evitar desfases secuenciales
             PreparedStatement reiniciarAiEmpleado = conexion.prepareStatement("ALTER TABLE empleado AUTO_INCREMENT = 1");
             PreparedStatement enableKeys = conexion.prepareStatement("SET FOREIGN_KEY_CHECKS = 1")) {
            
            disableKeys.executeUpdate();
            limpiarDescripcion.executeUpdate();
            limpiarPedido.executeUpdate();
            limpiarCliente.executeUpdate();
            limpiarProducto.executeUpdate();
            limpiarEmpleado.executeUpdate();
            reiniciarAiEmpleado.executeUpdate();
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

    // === INSERCIONES AUXILIARES SOLUCIONANDO FOREIGN KEYS ===
    
    private void insertarEmpleadoPruebaReal(int idEmpleado, String nombre, String usuario) throws SQLException {
        String sql = "INSERT INTO empleado (idEmpleado, nombre, aPaterno, aMaterno, ciudad, codigoPostal, direccion, email, telefono, usuario, contrasenia, status, rol) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.abrirConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idEmpleado);
            stmt.setString(2, nombre);
            stmt.setString(3, "Soto");
            stmt.setString(4, "Mora"); // Mapeado el campo opcional aMaterno
            stmt.setString(5, "Xalapa");
            stmt.setString(6, "91000");
            stmt.setString(7, "Av. Avila Camacho");
            stmt.setString(8, usuario + "@italiapizza.com"); 
            stmt.setString(9, "2288112233");
            stmt.setString(10, usuario);
            stmt.setString(11, "password123");
            stmt.setInt(12, 1); // 1 = Activo (tinyint)
            stmt.setString(13, "Mostrador");
            stmt.executeUpdate();
        }
    }

    private void insertarClientePruebaReal(int idCliente, String nombre, String aPaterno, String direccion) throws SQLException {
        String sql = "INSERT INTO cliente (idCliente, nombre, aPaterno, ciudad, codigoPostal, direccion, telefono, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.abrirConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idCliente);
            stmt.setString(2, nombre);
            stmt.setString(3, aPaterno);
            stmt.setString(4, "Xalapa"); 
            stmt.setString(5, "91000"); 
            stmt.setString(6, direccion);
            stmt.setString(7, "2281002030"); 
            stmt.setInt(8, 1); 
            stmt.executeUpdate();
        }
    }

    private void insertarProductoPruebaReal(int idProducto, String nombre, double precio) throws SQLException {
        String sql = "INSERT INTO producto (idProducto, codigoProducto, nombre, precio, cantidad) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = ConexionBD.abrirConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idProducto);
            stmt.setString(2, "PROD-" + idProducto); 
            stmt.setString(3, nombre);
            stmt.setDouble(4, precio);
            stmt.setInt(5, 10); 
            stmt.executeUpdate();
        }
    }

    private int insertarPedidoPruebaDirecto(String estatus, int idCliente, int idEmpleado) throws SQLException {
        String sql = "INSERT INTO pedido (fechaPedido, estatus, Cliente_idCliente, Empleado_idEmpleado) VALUES (NOW(), ?, ?, ?)";
        try (Connection conn = ConexionBD.abrirConexion();
             PreparedStatement stmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, estatus);
            stmt.setInt(2, idCliente);
            stmt.setInt(3, idEmpleado);
            stmt.executeUpdate();
            
            try (ResultSet keys = stmt.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        return -1;
    }

    private void insertarDescripcionPruebaDirecto(int cantidad, double total, int idProducto, int idPedido) throws SQLException {
        String sql = "INSERT INTO descripcionPedido (cantidad, total, Producto_idProducto, Pedido_idPedido) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConexionBD.abrirConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, cantidad);
            stmt.setDouble(2, total);
            stmt.setInt(3, idProducto);
            stmt.setInt(4, idPedido);
            stmt.executeUpdate();
        }
    }

    // === PRUEBAS UNITARIAS ===

    public void testObtenerPedidos_ConDatos() {
        System.out.println("Prueba 1: obtenerPedidos - Recuperar pedidos registrados");
        try {
            insertarEmpleadoPruebaReal(1, "Emp Uno", "emp1");
            insertarEmpleadoPruebaReal(2, "Emp Dos", "emp2");
            
            insertarClientePruebaReal(1, "Carlos", "Sánchez", "Calle 1");
            insertarClientePruebaReal(2, "Ana", "Gomez", "Calle 2");
            
            insertarPedidoPruebaDirecto("Pendiente", 1, 1);
            insertarPedidoPruebaDirecto("Entregado", 2, 2);

            ArrayList<Pedido> result = PedidoDAO.obtenerPedidos();
            assertNotNull(result, "La lista de pedidos no debería ser nula.");
            assertEquals(2, result.size(), "Deberían haberse recuperado exactamente 2 pedidos.");
            
            System.out.println("   -> Listado de pedidos validado.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    public void testObtenerPedidosConCliente_InnerJoin() {
        System.out.println("Prueba 2: obtenerPedidosConCliente - Validación del INNER JOIN");
        try {
            insertarEmpleadoPruebaReal(1, "Empleado Aux", "empAux");
            insertarClientePruebaReal(5, "Juan", "Perez", "Av. Central 123");
            insertarPedidoPruebaDirecto("Preparación", 5, 1);

            ArrayList<PedidoCliente> result = PedidoDAO.obtenerPedidosConCliente();
            assertNotNull(result, "La lista no debe ser nula.");
            assertEquals(1, result.size(), "Debería retornar el pedido asociado al cliente.");
            
            PedidoCliente pCliente = result.get(0);
            assertEquals("Juan Perez", pCliente.getNombreCliente(), "El CONCAT del nombre completo falló.");
            
            System.out.println("   -> Join de Pedido-Cliente e información concatenada correctos.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    public void testRegistrarPedido_TransaccionExitosa() {
        System.out.println("Prueba 3: registrarPedido - Transacción Completa (Cabecera + Detalles)");
        try {
            insertarEmpleadoPruebaReal(1, "Vendedor", "vendedor1");
            insertarClientePruebaReal(1, "Luis", "Robles", "Centro");
            insertarProductoPruebaReal(101, "Pizza Pepperoni", 135.00);
            insertarProductoPruebaReal(102, "Refresco Familiar", 45.00);

            Pedido nuevoPedido = new Pedido();
            nuevoPedido.setStatus("Registrado");
            nuevoPedido.setIdCliente(1); 
            nuevoPedido.setIdEmpleado(1); 

            ArrayList<OrdenFila> productos = new ArrayList<>();
            productos.add(new OrdenFila(101, "Pizza Pepperoni", 2, 135.00, 270.00, null, null)); 
            productos.add(new OrdenFila(102, "Refresco Familiar", 1, 45.00, 45.00, null, null));

            boolean exito = PedidoDAO.registrarPedido(nuevoPedido, productos);
            assertTrue(exito, "El método registrarPedido debería retornar verdadero al completarse con éxito.");
            System.out.println("   -> Transacción e inserciones en lote (Batch) ejecutadas correctamente.");
        } catch (RegistrarPedidoException | SQLException e) {
            fail("Excepción inesperada al registrar: " + e.getMessage());
        }
    }

    public void testModificarPedido_Exito() {
        System.out.println("Prueba 4: modificarPedido - Actualizar cabecera y reemplazar detalles");
        try {
            insertarEmpleadoPruebaReal(1, "Cajero Antiguo", "cajero1");
            insertarEmpleadoPruebaReal(2, "Cajero Nuevo", "cajero2");
            insertarClientePruebaReal(1, "María", "López", "Xalapa");
            insertarProductoPruebaReal(101, "Pizza Original", 100.00);
            insertarProductoPruebaReal(201, "Pizza Especial", 180.00);

            int idPedido = insertarPedidoPruebaDirecto("Pendiente", 1, 1);
            insertarDescripcionPruebaDirecto(1, 100.00, 101, idPedido);

            Pedido pedidoModificado = new Pedido();
            pedidoModificado.setIdPedido(idPedido);
            pedidoModificado.setStatus("Modificado");
            pedidoModificado.setIdEmpleado(2); 
            pedidoModificado.setIdCliente(1);

            ArrayList<OrdenFila> nuevosProductos = new ArrayList<>();
            nuevosProductos.add(new OrdenFila(201, "Pizza Especial", 1, 180.00, 180.00, null, null));

            boolean exito = PedidoDAO.modificarPedido(pedidoModificado, nuevosProductos);
            assertTrue(exito, "modificarPedido debió devolver true.");

            double nuevoTotal = PedidoDAO.obtenerTotalPedidoPorId(idPedido);
            assertEquals(180.00, nuevoTotal, "El detalle anterior no se reemplazó correctamente.");
            
            System.out.println("   -> Modificación transaccional (Update + Delete + Insert) exitosa.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    public void testActualizarEstatusPedido_Exito() {
        System.out.println("Prueba 5: actualizarEstatusPedido - Modificación de estatus");
        try {
            insertarEmpleadoPruebaReal(1, "Empleado Sistema", "sys");
            insertarClientePruebaReal(1, "Clara", "Farrera", "Norte");
            int idGenerado = insertarPedidoPruebaDirecto("Solicitado", 1, 1);
            
            boolean actualizado = PedidoDAO.actualizarEstatusPedido(idGenerado, "En Camino");
            assertTrue(actualizado, "El update debió retornar true.");
            
            System.out.println("   -> Estatus actualizado exitosamente.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    public void testCancelarPedido_CambioEstatus() {
        System.out.println("Prueba 6: cancelarPedido - Forzar estatus 'Cancelado'");
        try {
            insertarEmpleadoPruebaReal(1, "Empleado Sistema", "sys2");
            insertarClientePruebaReal(1, "Pedro", "Páramo", "Sur");
            int idGenerado = insertarPedidoPruebaDirecto("Pendiente", 1, 1);
            
            boolean cancelado = PedidoDAO.cancelarPedido(idGenerado);
            assertTrue(cancelado, "El método cancelarPedido debió retornar true.");
            
            System.out.println("   -> Pedido cancelado correctamente.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    public void testObtenerTotalPedidoPorId_SumaCorrecta() {
        System.out.println("Prueba 7: obtenerTotalPedidoPorId - Suma acumulada de detalles");
        try {
            insertarEmpleadoPruebaReal(1, "Encargado", "boss");
            insertarProductoPruebaReal(1, "Producto A", 75.25);
            insertarProductoPruebaReal(2, "Producto B", 50.25);

            insertarClientePruebaReal(1, "Cliente Dummy", "Prueba", "Domicilio");
            int idPedido = insertarPedidoPruebaDirecto("Pendiente", 1, 1);

            insertarDescripcionPruebaDirecto(2, 150.50, 1, idPedido);
            insertarDescripcionPruebaDirecto(1, 50.25, 2, idPedido);

            double total = PedidoDAO.obtenerTotalPedidoPorId(idPedido);
            assertEquals(200.75, total, "La suma total del pedido no es correcta.");
            
            System.out.println("   -> SUM e IFNULL de la consulta validados.");
        } catch (SQLException e) {
            fail("Error SQL inesperado: " + e.getMessage());
        }
    }

    // === EJECUTOR PRINCIPAL MANUAL (MAIN) ===
    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBAS MANUALES DE PEDIDODAO ===");
        int pruebasPasadas = 0;
        int totalPruebas = 7;

        PedidoDAOTest ejecutor = new PedidoDAOTest();

        for (int i = 1; i <= totalPruebas; i++) {
            try {
                ejecutor.setUp();
                switch(i) {
                    case 1: ejecutor.testObtenerPedidos_ConDatos(); break;
                    case 2: ejecutor.testObtenerPedidosConCliente_InnerJoin(); break;
                    case 3: ejecutor.testRegistrarPedido_TransaccionExitosa(); break;
                    case 4: ejecutor.testModificarPedido_Exito(); break;
                    case 5: ejecutor.testActualizarEstatusPedido_Exito(); break;
                    case 6: ejecutor.testCancelarPedido_CambioEstatus(); break;
                    case 7: ejecutor.testObtenerTotalPedidoPorId_SumaCorrecta(); break;
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
