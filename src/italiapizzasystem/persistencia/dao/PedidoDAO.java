
package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.pojo.Pedido;
import italiapizzasystem.persistencia.pojo.PedidoCliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Gerardo
 */
public class PedidoDAO {
    public static ArrayList<Pedido> obtenerPedidos() throws SQLException{
        ArrayList<Pedido> pedidos = new ArrayList<Pedido>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if( conexionBD != null){
            String consulta = "SELECT idPedido, fechaPedido, estatus, idCliente, idEmpleado FROM pedido";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                pedidos.add(serializarPedido(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Error al conectar a la BD.");
        }
        return pedidos;
    }
    
    public static ArrayList<PedidoCliente> obtenerPedidosConCliente() throws SQLException {
        ArrayList<PedidoCliente> listaPedidos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        
        if(conexionBD != null){
            String consulta = "SELECT p.idPedido, " +
                          "CONCAT(c.nombre, ' ', c.aPaterno, ' ', IFNULL(c.aMaterno, '')) AS nombreCompleto, " +
                          "c.direccion, p.fechaPedido, p.estatus " +
                          "FROM pedido p " +
                          "INNER JOIN cliente c ON p.Cliente_idCliente = c.idCliente";
            
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                listaPedidos.add(serializarPedidoCliente(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }  else {
            throw new SQLException("Error al conectar a la BD.");
        }
        return listaPedidos;
    }
    
    private static Pedido serializarPedido(ResultSet resultado) throws SQLException{
        Pedido pedido = new Pedido();
        pedido.setIdPedido(resultado.getInt("idPedido"));
        pedido.setStatus(resultado.getString("estatus"));
        pedido.setIdEmpleado(resultado.getInt("idEmpleado"));
        pedido.setIdCliente(resultado.getInt("idCliente"));
        pedido.setFechaPedido(resultado.getString("fechaPedido"));
        return pedido;
    }
    
    private static PedidoCliente serializarPedidoCliente(ResultSet resultado) throws SQLException {
        PedidoCliente pedido = new PedidoCliente();
        pedido.setIdPedido(resultado.getInt("idPedido"));
        pedido.setNombreCliente(resultado.getString("nombreCompleto").trim());
        pedido.setDireccion(resultado.getString("direccion"));
        pedido.setFechaPedido(resultado.getString("fechaPedido"));
        pedido.setStatus(resultado.getString("estatus"));
        return pedido;
    }
}
