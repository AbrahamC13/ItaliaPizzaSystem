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
            String consulta = "select idCliente, nombre, apaterno,amaterno, ciudad, codigopostal, "
                    + "direccion,email,telefono,status from cliente ";
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
        cliente.setaPaterno(resultado.getString("apaterno"));
        cliente.setaMaterno(resultado.getString("amaterno"));
        cliente.setTelefono(resultado.getString("telefono"));
        cliente.setStatus(resultado.getBoolean("status"));
        cliente.setCiudad(resultado.getString("ciudad"));
        cliente.setCodigoPostal(resultado.getString("codigopostal"));
        cliente.setDireccion(resultado.getString("direccion"));
        cliente.setEmail(resultado.getString("email"));
        return cliente;
    }
    
   
}
