
package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.pojo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author Gerardo
 */
public class ClienteDAOTest {

    /**
     * Test of obtenerClientes method, of class ClienteDAO.
     */
    private Connection conexion;
    public void setUp() throws SQLException {
        conexion = ConexionBD.abrirConexion();
        if (conexion == null) {
            throw new RuntimeException("Error: La conexión a la BD de pruebas es nula. Verifica el servidor.");
        }
        // Limpieza controlada de la tabla para asegurar un entorno de prueba limpio
        try (PreparedStatement stmt = conexion.prepareStatement("DELETE FROM cliente")) {
            stmt.executeUpdate();
        } finally {
            if (conexion != null) {
                conexion.close();
            }
        }
    }
    
    //Separación de responsabilidades
   private void assertNotNull(Object obj, String mensaje) {
        if (obj == null) {
            throw new AssertionError(mensaje);
        }
    }

    private void assertTrue(boolean condicion, String mensaje) {
        if (!condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private void assertFalse(boolean condicion, String mensaje) {
        if (condicion) {
            throw new AssertionError(mensaje);
        }
    }

    private void fail(String mensaje) {
        throw new AssertionError(mensaje);
    }

    // === PRUEBAS UNITARIAS ===

    public void testObtenerClientesExito() {
        System.out.println("Prueba 1: obtenerClientes - Caso Éxito");
        try {
            // Primero insertamos un cliente base para asegurar que la lista devuelva contenido seguro
            Cliente clientePrueba = new Cliente();
            clientePrueba.setNombre("Gerardo");
            clientePrueba.setAPaterno("Perez");
            clientePrueba.setAMaterno("Lopez");
            clientePrueba.setCiudad("Xalapa");
            clientePrueba.setCodigoPostal("91000");
            clientePrueba.setDireccion("Av. Central 123");
            clientePrueba.setEmail("gerardo@test.com");
            clientePrueba.setTelefono("2281002030");
            clientePrueba.setStatus(true);
            
            ClienteDAO.registrarCliente(clientePrueba);

            ArrayList<Cliente> result = ClienteDAO.obtenerClientes();
            assertNotNull(result, "La lista de clientes no debería ser nula.");
            assertFalse(result.isEmpty(), "La lista de clientes no debería estar vacía.");
            
            Cliente primerCliente = result.get(0);
            assertNotNull(primerCliente.getNombre(), "El nombre del cliente no debe ser nulo.");
            assertTrue(primerCliente.getIdCliente() > 0, "El ID del cliente debe ser positivo.");
            
            System.out.println("   -> Clientes recuperados con éxito: " + result.size());
        } catch (SQLException e) {
            fail("No se pudo conectar a la base de datos o hubo un error SQL: " + e.getMessage());
        }
    }

    public void testRegistrarClienteExitoso() {
        System.out.println("Prueba 2: registrarCliente - Registro Exitoso");
        try {
            Cliente nuevoCliente = new Cliente();
            nuevoCliente.setNombre("Maria");
            nuevoCliente.setAPaterno("Gomez");
            nuevoCliente.setAMaterno("Diaz");
            nuevoCliente.setCiudad("Coatepec");
            nuevoCliente.setCodigoPostal("91500");
            nuevoCliente.setDireccion("Calle Zaragoza 45");
            nuevoCliente.setEmail("maria.gomez@test.com");
            nuevoCliente.setTelefono("2285554433");
            nuevoCliente.setStatus(true);

            boolean registrado = ClienteDAO.registrarCliente(nuevoCliente);
            assertTrue(registrado, "El cliente debería registrarse exitosamente en la BD.");
        } catch (SQLException e) {
            fail("Error al registrar cliente: " + e.getMessage());
        }
    }

    public void testValidarClienteExistente() {
        System.out.println("Prueba 3: validarClienteExistente - Verificación de correo");
        try {
            String correoPrueba = "unico@test.com";
            
            // Caso 1: Validar que no existe antes de registrarse
            boolean existeAntes = ClienteDAO.validarClienteExistente(correoPrueba);
            assertFalse(existeAntes, "El correo no debería existir todavía.");

            // Registrar el cliente con dicho correo
            Cliente cliente = new Cliente();
            cliente.setNombre("Juan");
            cliente.setAPaterno("Rodriguez");
            cliente.setAMaterno("");
            cliente.setCiudad("Xalapa");
            cliente.setCodigoPostal("91020");
            cliente.setDireccion("Conocido");
            cliente.setEmail(correoPrueba);
            cliente.setTelefono("2288112233");
            cliente.setStatus(true);
            ClienteDAO.registrarCliente(cliente);

            // Caso 2: Validar que ahora sí existe
            boolean existeDespues = ClienteDAO.validarClienteExistente(correoPrueba);
            assertTrue(existeDespues, "El correo debería ser detectado como existente.");
        } catch (SQLException e) {
            fail("Error en la validación de cliente existente: " + e.getMessage());
        }
    }

    public void testValidarClienteInexistente() {
        System.out.println("Prueba 4: validarClienteExistente - Correo Inexistente");
        try {
            boolean existe = ClienteDAO.validarClienteExistente("correo.falso.no.existe@test.com");
            assertFalse(existe, "Debería retornar false para un correo que nunca ha sido registrado.");
        } catch (SQLException e) {
            fail("Error al validar correo inexistente: " + e.getMessage());
        }
    }

    //  Ejecutamos las pruebas en el main
    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBAS MANUALES DE CLIENTEDAO ===");
        int pruebasPasadas = 0;
        int totalPruebas = 4;

        ClienteDAOTest ejecutor = new ClienteDAOTest();

        // Prueba 1 obtenerClientes
        try {
            ejecutor.setUp();
            ejecutor.testObtenerClientesExito();
            System.out.println("\u2705 Pas\u00f3 Exitosamente\n");
            pruebasPasadas++;
        } catch (Exception | AssertionError e) {
            System.out.println("\u274c Fall\u00f3: " + e.getMessage());
            e.printStackTrace();
            System.out.println();
        }

        // Prueba 2 registrarCliente
        try {
            ejecutor.setUp();
            ejecutor.testRegistrarClienteExitoso();
            System.out.println("\u2705 Pas\u00f3 Exitosamente\n");
            pruebasPasadas++;
        } catch (Exception | AssertionError e) {
            System.out.println("\u274c Fall\u00f3: " + e.getMessage());
            e.printStackTrace();
            System.out.println();
        }

        // Prueba 3 validarClienteExistente
        try {
            ejecutor.setUp();
            ejecutor.testValidarClienteExistente();
            System.out.println("\u2705 Pas\u00f3 Exitosamente\n");
            pruebasPasadas++;
        } catch (Exception | AssertionError e) {
            System.out.println("\u274c Fall\u00f3: " + e.getMessage());
            e.printStackTrace();
            System.out.println();
        }

        // Prueba 4 validarClienteInexistente
        try {
            ejecutor.setUp();
            ejecutor.testValidarClienteInexistente();
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
