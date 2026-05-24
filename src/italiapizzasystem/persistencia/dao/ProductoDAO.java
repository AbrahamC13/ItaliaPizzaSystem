package italiapizzasystem.persistencia.dao;

import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.pojo.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 *
 * @author Gerardo
 */
public class ProductoDAO {
    public static ArrayList<Producto> obtenerProductos() throws SQLException{
        ArrayList<Producto> productos = new ArrayList<Producto>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT idProducto, codigoProducto, descripcion, nombre, restricciones, precio, foto FROM producto"; 
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                productos.add(serializarProducto(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Error al conectar a la BD.");
        }
        return productos;
    }
    
    private static Producto serializarProducto(ResultSet resultado) throws SQLException{
        Producto producto = new Producto();
        producto.setIdProducto(resultado.getInt("idProducto"));
        producto.setNombre(resultado.getString("nombre"));
        producto.setDescripcion(resultado.getString("descripcion"));
        producto.setCodigoProducto(resultado.getString("codigoProducto"));
        producto.setRestricciones(resultado.getString("restricciones"));
        producto.setPrecio(resultado.getDouble("precio"));
        producto.setFoto(resultado.getBytes("foto"));
        return producto;
    }
}
