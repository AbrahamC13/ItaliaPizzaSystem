package italiapizzasystem.persistencia.dao;

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
            String consulta = "SELECT idempleado, nombre, apaterno, amaterno, ciudad, codigopostal, direccion, email, rol, "
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
        empleado.setIdEmpleado(resultado.getInt("idempleado"));
        empleado.setNombre(resultado.getString("nombre"));
        empleado.setaPaterno(resultado.getString("aPaterno"));
        empleado.setaMaterno(resultado.getString("aMaterno"));
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
    
    public static Empleado validarCredenciales(String username, String password) throws SQLException{
        Connection conexionBD= ConexionBD.abrirConexion();
        Empleado empleado = null;
        if(conexionBD != null){
            String consulta = "SELECT usuario, contrasenia FROM empleado WHERE usuario = ? AND contrasenia = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                empleado = serializarEmpleado(resultado);
            }
            conexionBD.close();
            sentencia.close();
            resultado.close();
        }
        return empleado;
    }
}
