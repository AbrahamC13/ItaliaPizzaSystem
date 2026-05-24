package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.pojo.DescripcionPedido;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Gerardo
 */
public class DescripcionPedidoDAO {
    public static ArrayList<DescripcionPedido> obtenerDescripcionPedido(int idPedido) throws SQLException{
        ArrayList<DescripcionPedido>  pedidos = new ArrayList<DescripcionPedido>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD!= null){
            String consulta = "SELECT * FROM descripcionPedido where idPedido=? ";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idPedido);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                pedidos.add(serializarDescripcionPedido(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexión a la BD");
        }
        return pedidos;
    }
    
    private static DescripcionPedido serializarDescripcionPedido(ResultSet resultado) throws SQLException{
        DescripcionPedido descripcionPedido = new DescripcionPedido();
        descripcionPedido.setCantidad(resultado.getInt("cantidad"));
        descripcionPedido.setIdPedido(resultado.getInt("Pedido_idPedido"));
        descripcionPedido.setIdProducto(resultado.getInt("Producto_idProducto"));
        descripcionPedido.setTotal(resultado.getDouble("total"));
        return descripcionPedido;
    }
}
