package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.pojo.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 *
 * @author Gerardo
 */
public class EmpleadoDAOTest {

    private Connection conexion;

    @BeforeEach
    public void setUp() throws SQLException {
        // Asegúrate de que ConexionBD apunte a tu entorno de PRUEBAS, no a producción
        conexion = ConexionBD.abrirConexion();
        assertNotNull(conexion, "La conexión a la BD de pruebas no debería ser nula.");
        
        // Limpiamos la tabla antes de cada prueba para garantizar idoneidad
        try (PreparedStatement stmt = conexion.prepareStatement("DELETE FROM empleado")) {
            stmt.executeUpdate();
        }
    }
    @AfterEach
    public void tearDown() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
        }
    }
    
    @Test
    public void testRegistrarEmpleado_Exitoso() throws SQLException {
        // 1. Preparar los datos
        Empleado nuevo = new Empleado();
        nuevo.setNombre("Juan");
        nuevo.setAPaterno("Pérez");
        nuevo.setAMaterno("López");
        nuevo.setCiudad("Xalapa");
        nuevo.setCodigoPostal("91000");
        nuevo.setDireccion("Av. Central 123");
        nuevo.setEmail("juan.perez@mail.com");
        nuevo.setTelefono("2281234567");
        nuevo.setRol("Administrador");
        nuevo.setUsuario("juanp");
        nuevo.setContrasenia("secure123");
        nuevo.setStatus(true);

        // 2. Ejecutar la acción
        boolean resultado = EmpleadoDAO.registrarEmpleado(nuevo);

        // 3. Verificar
        assertTrue(resultado, "El empleado debería registrarse correctamente.");
    }
    
    @Test
    public void testObtenerEmpleados() throws Exception {
        System.out.println("Prueba: obtenerEmpleados");
        ArrayList<Empleado> result = EmpleadoDAO.obtenerEmpleados();
        assertNotNull(result, "La lista de empleados no debe ser nula.");
    }
    @Test
    public void testObtenerEmpleadoExistente() throws Exception {
        System.out.println("Prueba: obtenerEmpleado con ID existente");
        int idEmpleado = 1; 
        Empleado result = EmpleadoDAO.obtenerEmpleado(idEmpleado);
        assertNotNull(result, "El empleado no debería ser nulo.");
        if (result.getNombre() != null) {
            assertEquals(idEmpleado, result.getIdEmpleado());
        }
    }
    @Test
    public void testObtenerEmpleadoInexistente() throws Exception {
        System.out.println("Prueba: obtenerEmpleado con ID que no existe");
        int idEmpleado = -1;
        Empleado result = EmpleadoDAO.obtenerEmpleado(idEmpleado);
        assertNull(result.getNombre(), "El nombre del empleado debería ser nulo para un ID inexistente.");
    }

    /**
     * Test of obtenerEmpleado method, of class EmpleadoDAO.
     */
    @Test
    public void testObtenerEmpleado() throws Exception {
        System.out.println("obtenerEmpleado");
        int idEmpleado = 0;
        Empleado expResult = null;
        Empleado result = EmpleadoDAO.obtenerEmpleado(idEmpleado);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of validarCredenciales method, of class EmpleadoDAO.
     */
    @Test
    public void testValidarCredenciales() throws Exception {
        System.out.println("validarCredenciales");
        String usuario = "";
        String contrasenia = "";
        Empleado expResult = null;
        Empleado result = EmpleadoDAO.validarCredenciales(usuario, contrasenia);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of recuperarYEnviarcontrasenia method, of class EmpleadoDAO.
     */
    @Test
    public void testRecuperarYEnviarcontrasenia() throws Exception {
        System.out.println("recuperarYEnviarcontrasenia");
        String identificador = "juanp";
        String expResult = "Segura123";
        String result = EmpleadoDAO.recuperarYEnviarcontrasenia(identificador);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
