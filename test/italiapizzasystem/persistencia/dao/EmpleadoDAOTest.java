package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.pojo.Empleado;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Gerardo
 */
public class EmpleadoDAOTest {
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
}
