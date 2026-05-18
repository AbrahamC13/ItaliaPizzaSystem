package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.pojo.Empleado;
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

    @BeforeAll
    public static void setUpClass() throws Exception {
    }

    @AfterAll
    public static void tearDownClass() throws Exception {
    }

    @BeforeEach
    public void setUp() throws Exception {
    }

    @AfterEach
    public void tearDown() throws Exception {
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
