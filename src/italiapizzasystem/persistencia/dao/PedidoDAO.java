
package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.pojo.Pedido;
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
            String consulta = "SELECT idPedido, fechaPedido, status, idCliente, idEmpleado FROM pedido";
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
    
    private static Pedido serializarPedido(ResultSet resultado) throws SQLException{
        Pedido pedido = new Pedido();
        pedido.setIdPedido(resultado.getInt("idPedido"));
        pedido.setStatus(resultado.getString("status"));
        pedido.setIdEmpleado(resultado.getInt("idEmpleado"));
        pedido.setIdCliente(resultado.getInt("idCliente"));
        pedido.setFechaPedido(resultado.getString("fechaPedido"));
        return pedido;
    }
}
