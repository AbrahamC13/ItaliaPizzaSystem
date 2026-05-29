package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.pojo.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test; 
import org.junit.jupiter.api.BeforeEach;
/**
 *
 * @author Gerardo
 */

public class EmpleadoDAOTest {

    private Connection conexion;
    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBAS MANUALES ===");
        EmpleadoDAOTest ejecutor = new EmpleadoDAOTest();
        
        int pruebasEjecutadas = 0;
        int pruebasExitosas = 0;
        //--- PRUEBA 1 ---
        try {
            pruebasEjecutadas++;
            System.out.println("\n[Test " + pruebasEjecutadas + "] Ejecutando: testRegistrarEmpleado_Exitoso...");
            ejecutor.setUp();
            ejecutor.testRegistrarEmpleado_Exitoso();
            System.out.println("✅ Pasó Exitosamente");
            pruebasExitosas++;
        } catch (Throwable e) {
            System.err.println("\n❌ LA PRUEBA FALLÓ:");
            e.printStackTrace();
        } finally {
            try {
                ejecutor.tearDown();
            } catch (Exception ex) {
                System.err.println("Error al cerrar conexión en tearDown.");
            }
        }
        
        // --- PRUEBA 2 ---
        try {
            pruebasEjecutadas++;
            System.out.println("\n[Test " + pruebasEjecutadas + "] Ejecutando: testObtenerEmpleados...");
            ejecutor.setUp();
            ejecutor.testObtenerEmpleados();
            System.out.println("✅ Pasó Exitosamente");
            pruebasExitosas++;
        } catch (Throwable e) { System.err.println("❌ Falló: " + e.getMessage()); e.printStackTrace(); }
        finally { try { ejecutor.tearDown(); } catch(Exception e){} }
        
        // --- PRUEBA 3 ---
        try {
            pruebasEjecutadas++;
            System.out.println("\n[Test " + pruebasEjecutadas + "] Ejecutando: testObtenerEmpleadoExistente...");
            ejecutor.setUp();
            ejecutor.testObtenerEmpleadoExistente();
            System.out.println("✅ Pasó Exitosamente");
            pruebasExitosas++;
        } catch (Throwable e) { System.err.println("❌ Falló: " + e.getMessage()); e.printStackTrace(); }
        finally { try { ejecutor.tearDown(); } catch(Exception e){} }
        
        // --- PRUEBA 4 ---
        try {
            pruebasEjecutadas++;
            System.out.println("\n[Test " + pruebasEjecutadas + "] Ejecutando: testObtenerEmpleadoInexistente...");
            ejecutor.setUp();
            ejecutor.testObtenerEmpleadoInexistente();
            System.out.println("✅ Pasó Exitosamente");
            pruebasExitosas++;
        } catch (Throwable e) { System.err.println("❌ Falló: " + e.getMessage()); e.printStackTrace(); }
        finally { try { ejecutor.tearDown(); } catch(Exception e){} }
        
        // --- PRUEBA 5 ---
        try {
            pruebasEjecutadas++;
            System.out.println("\n[Test " + pruebasEjecutadas + "] Ejecutando: testValidarCredenciales...");
            ejecutor.setUp();
            ejecutor.testValidarCredenciales();
            System.out.println("✅ Pasó Exitosamente");
            pruebasExitosas++;
        } catch (Throwable e) { System.err.println("❌ Falló: " + e.getMessage()); e.printStackTrace(); }
        finally { try { ejecutor.tearDown(); } catch(Exception e){} }
        
        // --- PRUEBA 6 ---
        try {
            pruebasEjecutadas++;
            System.out.println("\n[Test " + pruebasEjecutadas + "] Ejecutando: testRecuperarYEnviarcontrasenia...");
            ejecutor.setUp();
            ejecutor.testRecuperarYEnviarcontrasenia();
            System.out.println("✅ Pasó Exitosamente");
            pruebasExitosas++;
        } catch (Throwable e) { System.err.println("❌ Falló: " + e.getMessage()); e.printStackTrace(); }
        finally { try { ejecutor.tearDown(); } catch(Exception e){} }

        // RESUMEN FINAL
        System.out.println("\n=============================================");
        System.out.println("  RESUMEN: " + pruebasExitosas + " / " + pruebasEjecutadas + " PRUEBAS PASARON CON ÉXITO.");
        System.out.println("=============================================");
    }

    @BeforeEach
    public void setUp() throws SQLException {
        conexion =  ConexionBD.abrirConexion();
        if (conexion == null) {
            conexion = ConexionBD.abrirConexion();
        }
        if (conexion == null) {
            throw new RuntimeException("Error: La conexión a la BD de pruebas es nula.");
        }
        
        // Limpiamos la tabla para iniciar cada prueba con un escenario limpio
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

       if (!resultado) {
            throw new RuntimeException("testRegistrarEmpleado_Exitoso: El empleado no se pudo registrar.");
        }
    }
    
    @Test
    public void testObtenerEmpleados() throws Exception {
        System.out.println("Prueba: obtenerEmpleados");
        // Primero registramos un empleado para asegurarnos de que la consulta devuelva algo
        Empleado nuevo = crearEmpleadoMock("juanp", "juan.perez@mail.com", "secure123");
        EmpleadoDAO.registrarEmpleado(nuevo);
        ArrayList<Empleado> result = EmpleadoDAO.obtenerEmpleados();
        if (result == null) {
            throw new RuntimeException("testObtenerEmpleados: La lista retornada es nula.");
        }
        if (result.isEmpty()) {
            throw new RuntimeException("testObtenerEmpleados: La lista de empleados está vacía tras registrar uno.");
        }
    }
    @Test
    public void testObtenerEmpleadoExistente() throws Exception {
        System.out.println("Prueba: obtenerEmpleado con ID existente");
        Empleado nuevo = crearEmpleadoMock("testuser", "test@mail.com", "pass123");
        EmpleadoDAO.registrarEmpleado(nuevo);
        
        // Obtenemos toda la lista para recuperar el ID asignado por la BD de forma dinámica
        ArrayList<Empleado> lista = EmpleadoDAO.obtenerEmpleados();
        int idEmpleado = lista.get(0).getIdEmpleado();
        Empleado result = EmpleadoDAO.obtenerEmpleado(idEmpleado);
        if (result == null) {
            throw new RuntimeException("testObtenerEmpleadoExistente: Retornó un empleado nulo.");
        }
        if (result.getIdEmpleado() != idEmpleado) {
            throw new RuntimeException("testObtenerEmpleadoExistente: El ID recuperado no coincide con el buscado.");
        }
    }
    @Test
    public void testObtenerEmpleadoInexistente() throws Exception {
        System.out.println("Prueba: obtenerEmpleado con ID que no existe");
        int idEmpleado = -1;
        Empleado result = EmpleadoDAO.obtenerEmpleado(idEmpleado);
        if (result != null && result.getNombre() != null) {
            throw new RuntimeException("testObtenerEmpleadoInexistente: Se encontró un nombre para un ID que no debería existir.");
        }
    }

    /**
     * Test of validarCredenciales method, of class EmpleadoDAO.
     */
    @Test
    public void testValidarCredenciales() throws Exception {
        System.out.println("validarCredenciales");
        Empleado nuevo = crearEmpleadoMock("loginUser", "login@mail.com", "claveSecreta");
        EmpleadoDAO.registrarEmpleado(nuevo);
       // 1. Caso Exitoso
        Empleado correcto = EmpleadoDAO.validarCredenciales("loginUser", "claveSecreta");
        if (correcto == null || correcto.getUsuario() == null) {
            throw new RuntimeException("testValidarCredenciales: Falló la validación con credenciales correctas.");
        }
        
        // 2. Caso Erróneo
        Empleado incorrecto = EmpleadoDAO.validarCredenciales("loginUser", "claveEquivocada");
        if (incorrecto != null && incorrecto.getUsuario() != null) {
            throw new RuntimeException("testValidarCredenciales: Validó con éxito una contraseña que era incorrecta.");
        }
    }

    /**
     * Test of recuperarYEnviarcontrasenia method, of class EmpleadoDAO.
     */
    @Test
    public void testRecuperarYEnviarcontrasenia() throws Exception {
        System.out.println("recuperarYEnviarcontrasenia");
        String usuario = "recuperarUser";
        String contraseniaEsperada = "Segura123";
        Empleado nuevo = crearEmpleadoMock(usuario, "recupera@mail.com", contraseniaEsperada);
        EmpleadoDAO.registrarEmpleado(nuevo);
        String result = EmpleadoDAO.recuperarYEnviarcontrasenia(usuario);
        if (result == null) {
            throw new RuntimeException("testRecuperarYEnviarcontrasenia: La contraseña recuperada es nula.");
        }
        if (!result.equals(contraseniaEsperada)) {
            throw new RuntimeException("testRecuperarYEnviarcontrasenia: La contraseña obtenida ('" + result + "') no coincide con la esperada.");
        }
    }
    
    // 🛠️ Función auxiliar para construir objetos Empleado rápidamente reduciendo código repetido
    private Empleado crearEmpleadoMock(String usuario, String email, String contrasenia) {
        Empleado emp = new Empleado();
        emp.setNombre("TestNombre");
        emp.setAPaterno("Paterno");
        emp.setAMaterno("Materno");
        emp.setCiudad("Xalapa");
        emp.setCodigoPostal("91000");
        emp.setDireccion("Direccion Fija 123");
        emp.setEmail(email);
        emp.setTelefono("2281112233");
        emp.setRol("Administrador");
        emp.setUsuario(usuario);
        emp.setContrasenia(contrasenia);
        emp.setStatus(true);
        return emp;
    }
}
