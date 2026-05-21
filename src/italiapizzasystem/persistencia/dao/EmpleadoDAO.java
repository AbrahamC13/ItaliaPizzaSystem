package italiapizzasystem.persistencia.dao;

import italiapizzasystem.excepciones.ValidarCredencialesException;
import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.pojo.Empleado;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


/**
 *
 * @author Gerardo
 */
public class EmpleadoDAO {
    public static ArrayList<Empleado> obtenerEmpleados() throws SQLException{
        ArrayList<Empleado> empleados = new ArrayList<Empleado>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT idempleado, nombre, apaterno, amaterno, ciudad, codigopostal,direccion, email, telefono,"
                + "usuario,contrasenia, status,rol FROM empleado";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                empleados.add(serializarEmpleado(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Error al conectar a la BD.");
        }
        return empleados;
    }
    
    public static Empleado obtenerEmpleado(int idEmpleado) throws SQLException{
        Empleado empleado = new Empleado();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT idempleado, nombre, apaterno, amaterno, telefono, ciudad, codigopostal, direccion, email, rol, "
                + "status, usuario, contrasenia FROM empleado WHERE idempleado = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idEmpleado);
            ResultSet resultado = sentencia.executeQuery();
            if(resultado != null){
                empleado = serializarEmpleado(resultado);
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Error al conectar a la BD.");
        }
        return empleado;
    }
    
    private static Empleado serializarEmpleado(ResultSet resultado)throws SQLException{
        Empleado empleado = new Empleado();
        empleado.setIdEmpleado(resultado.getInt("idEmpleado"));
        empleado.setNombre(resultado.getString("nombre"));
        empleado.setAPaterno(resultado.getString("aPaterno"));
        String aMaterno = resultado.getString("aMaterno");
        empleado.setAMaterno(resultado.wasNull() ? "" : aMaterno);
        empleado.setTelefono(resultado.getString("telefono"));
        empleado.setCiudad(resultado.getString("ciudad"));
        empleado.setCodigoPostal(resultado.getString("codigoPostal"));
        empleado.setDireccion(resultado.getString("direccion"));
        empleado.setEmail(resultado.getString("email"));
        empleado.setRol(resultado.getString("rol"));
        empleado.setStatus(resultado.getBoolean("status"));
        empleado.setUsuario(resultado.getString("usuario"));
        empleado.setContrasenia(resultado.getString("contrasenia"));
        return empleado;
    }
    
    public static Empleado validarCredenciales(String usuario, String contrasenia) throws SQLException, ValidarCredencialesException{
        Connection conexionBD= ConexionBD.abrirConexion();
        Empleado empleado = null;
        if(conexionBD != null){
            String consulta = "SELECT * FROM empleado WHERE usuario = ? AND contrasenia = ? AND status=1";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1,usuario);
            sentencia.setString(2,contrasenia);
            ResultSet resultado = sentencia.executeQuery();
            if(resultado.next()){
                empleado = serializarEmpleado(resultado);
            }else{
                throw new ValidarCredencialesException("Error al validar credenciales");
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
        }else{
            throw new SQLException("Error al conectarse a la base de datos.");
        }
        return empleado;
    }
    
    public static String recuperarYEnviarcontrasenia(String identificador) throws SQLException{
        String contraseniaEncontrada = "";
        String queryBusqueda = "SELECT contrasenia FROM empleado WHERE usuario  = ? OR email = ?";
        
        Connection conexion = ConexionBD.abrirConexion();
        if (conexion == null) {
            throw new SQLException("No se pudo establecer conexión con la base de datos.");
        }
        
        try (PreparedStatement stmtBusqueda = conexion.prepareStatement(queryBusqueda)) {
            stmtBusqueda.setString(1, identificador);
            stmtBusqueda.setString(2, identificador);
            
            try (ResultSet rs = stmtBusqueda.executeQuery()) {
                if (rs.next()) {
                    contraseniaEncontrada = rs.getString("contrasenia");
                }
            }
        } finally {
            conexion.close(); 
        }
        return contraseniaEncontrada;
    
    }
    
    public static boolean registrarEmpleado(Empleado empleado) throws SQLException{
        boolean resultado = false;
        String insert = "INSERT INTO empleados (nombre, apellido_paterno, apellido_materno, ciudad, codigo_postal, "
                   + "direccion, email, telefono, rol, usuario, contrasenia, status) "
                   + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            PreparedStatement pstmt = conexionBD.prepareStatement(insert); 
            pstmt.setString(1, empleado.getNombre());
            pstmt.setString(2, empleado.getAPaterno());
            pstmt.setString(3, empleado.getAMaterno());
            pstmt.setString(4, empleado.getCiudad());
            pstmt.setString(5, empleado.getCodigoPostal());
            pstmt.setString(6, empleado.getDireccion());
            pstmt.setString(7, empleado.getEmail());
            pstmt.setString(8, empleado.getTelefono());
            pstmt.setString(9, empleado.getRol());
            pstmt.setString(10, empleado.getUsuario());
            pstmt.setString(11, empleado.getContrasenia());
            pstmt.setBoolean(12, empleado.isStatus());
            int filasAfectadas = pstmt.executeUpdate();
            resultado = filasAfectadas > 0;
        }else{
            throw new SQLException("Error al conectar a la BD");
        }
            
       return resultado;
    }
    
    //Restricción: No puede haber dos empleados con el mismo usuario
     public static boolean validarEmpleadoExistente(String usuario) throws SQLException {
         boolean resultado = false;
        String consulta = "SELECT COUNT(*) FROM empleados WHERE usuario = ?";
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            PreparedStatement pstmt = conexionBD.prepareStatement(consulta);
            pstmt.setString(1, usuario);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                int count = rs.getInt(1);
                resultado = count > 0;
            }
        }else{
            throw new SQLException("Error al conectar a la BD");
        }
       
        return resultado;
    }
    //Restricción: No puede haber dos empleados con el mismo correo electrónico 
    public static boolean validarEmpleadoExistentePorEmail(String email) throws SQLException {
        boolean resultado = false;      
        String consulta = "SELECT COUNT(*) FROM empleados WHERE email = ?";
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            PreparedStatement pstmt = conexionBD.prepareStatement(consulta);
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                resultado = rs.getInt(1) > 0;
            }
        }else{
            throw new SQLException("Error al conectar a la BD.");
        }
       
        return  resultado;
    }
}
