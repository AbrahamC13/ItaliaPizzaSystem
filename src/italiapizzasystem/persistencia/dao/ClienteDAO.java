package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.pojo.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Gerardo
 */
public class ClienteDAO {
    public static ArrayList<Cliente> obtenerClientes() throws SQLException{
        ArrayList<Cliente> clientes = new ArrayList<Cliente>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD!=null){
            String consulta = "SELECT idCliente, nombre, aPaterno,aMaterno, ciudad, codigopostal, "
                    + "direccion,email,telefono,status FROM cliente ";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                clientes.add(convertirRegistroCliente(resultado));
            }
            sentencia.close();
            resultado.close();  
            conexionBD.close();
        }else{
            throw new SQLException("Error al conectar a la BD");
        }
        return clientes;
        
    }
    
    private static Cliente convertirRegistroCliente(ResultSet resultado)throws SQLException{
        Cliente cliente = new Cliente();
        cliente.setIdCliente(resultado.getInt("idCliente"));
        cliente.setNombre(resultado.getString("nombre"));
        cliente.setAPaterno(resultado.getString("aPaterno"));
        String aMaterno = resultado.getString("aMaterno");
        cliente.setAMaterno(resultado.wasNull() ? "" : aMaterno);
        cliente.setAMaterno(resultado.getString("amaterno"));
        cliente.setTelefono(resultado.getString("telefono"));
        cliente.setStatus(resultado.getBoolean("status"));
        cliente.setCiudad(resultado.getString("ciudad"));
        cliente.setCodigoPostal(resultado.getString("codigopostal"));
        cliente.setDireccion(resultado.getString("direccion"));
        cliente.setEmail(resultado.getString("email"));
        return cliente;
    }
    //Regla: No puede haber más de un cliente con el mismo correo electrónico, puede haber clientes con el mismo nombre y apellido
    public static boolean validarClienteExistente(String correo) throws SQLException{
        if(correo==null || correo.trim().isEmpty()){
            throw new IllegalArgumentException("El campo correo está vacío");
        }
        boolean clienteExiste = false;
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD!=null){
            String consulta = "SELECT 1 FROM cliente WHERE email=?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, correo);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                clienteExiste = true;
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Error al conectar a la BD");
        }
        return clienteExiste;
    }
    
    public static boolean registrarCliente(Cliente cliente) throws SQLException{
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD!=null){
            String consulta = "INSERT INTO cliente (nombre, aPaterno, aMaterno, ciudad, codigoPostal, direccion, email, telefono, status) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, cliente.getNombre());
            sentencia.setString(2, cliente.getAPaterno());
            sentencia.setString(3, cliente.getAMaterno()); // puede ser null
            sentencia.setString(4, cliente.getCiudad());
            sentencia.setString(5, cliente.getCodigoPostal());
            sentencia.setString(6, cliente.getDireccion());
            sentencia.setString(7, cliente.getEmail()); // puede ser null
            sentencia.setString(8, cliente.getTelefono());
            sentencia.setByte(9, cliente.getStatus() ? (byte) 1 : (byte) 0);
            int filasAfectadas = sentencia.executeUpdate();
            return filasAfectadas > 0;
        }else{
            throw new SQLException("Error al conectar a la BD");
        }
        
    }
}
