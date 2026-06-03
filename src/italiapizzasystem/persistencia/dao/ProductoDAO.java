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
            String consulta = "SELECT idProducto, codigoProducto, descripcion, nombre, restricciones, precio, cantidad, foto FROM producto"; 
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
        producto.setCantidad(resultado.getInt("cantidad"));
        producto.setFoto(resultado.getBytes("foto"));
        return producto;
    }
    
    public static boolean registrarProducto(
        Producto producto
    ) throws SQLException {

    Connection conexionBD =
            ConexionBD.abrirConexion();

    if (conexionBD != null) {

        String consulta =
                "INSERT INTO producto "
                + "(codigoProducto, descripcion, "
                + "nombre, restricciones, precio, cantidad, foto) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        PreparedStatement sentencia =
                conexionBD.prepareStatement(
                        consulta
                );

        sentencia.setString(
            1,
            producto.getCodigoProducto()
        );

        sentencia.setString(
            2,
            producto.getDescripcion()
        );

        sentencia.setString(
            3,
            producto.getNombre()
        );

        sentencia.setString(
            4,
            producto.getRestricciones()
        );

        sentencia.setDouble(
            5,
            producto.getPrecio()
        );

        sentencia.setInt(
            6,
            producto.getCantidad()
        ); 

        sentencia.setBytes(
            7,
            producto.getFoto()
        );

        int filasAfectadas =
                sentencia.executeUpdate();

        sentencia.close();
        conexionBD.close();

        return filasAfectadas > 0;

    } else {

        throw new SQLException(
                "Error al conectar a la BD."
        );
        }
    }
    
    public static ArrayList<Producto> buscarProductos(
        String nombre
    ) throws SQLException {

    ArrayList<Producto> productos =
            new ArrayList<>();

    Connection conexionBD =
            ConexionBD.abrirConexion();

    if (conexionBD != null) {

        String consulta =
                "SELECT idProducto, codigoProducto, "
                + "descripcion, nombre, "
                + "restricciones, precio, cantidad, foto "
                + "FROM producto "
                + "WHERE nombre LIKE ?";

        PreparedStatement sentencia =
                conexionBD.prepareStatement(
                        consulta
                );

        sentencia.setString(
                1,
                "%" + nombre + "%"
        );

        ResultSet resultado =
                sentencia.executeQuery();

        while (resultado.next()) {

            productos.add(
                    serializarProducto(
                            resultado
                    )
            );
        }

        resultado.close();
        sentencia.close();
        conexionBD.close();
    }

    return productos;
    }
    
    public static boolean eliminarProducto(
        int idProducto
    ) throws SQLException {

    Connection conexionBD =
            ConexionBD.abrirConexion();

    if (conexionBD != null) {

        String consulta =
                "DELETE FROM producto "
                + "WHERE idProducto = ?";

        PreparedStatement sentencia =
                conexionBD.prepareStatement(
                        consulta
                );

        sentencia.setInt(
                1,
                idProducto
        );

        int filasAfectadas =
                sentencia.executeUpdate();

        sentencia.close();
        conexionBD.close();

        return filasAfectadas > 0;

    } else {

        throw new SQLException(
                "Error al conectar a la BD."
        );
      }
    }
    
    public static boolean actualizarProducto(
        Producto producto
    ) throws SQLException {

    Connection conexionBD =
            ConexionBD.abrirConexion();

    if (conexionBD != null) {

        String consulta =
                "UPDATE producto "
                + "SET nombre = ?, "
                + "descripcion = ?, "
                + "restricciones = ?, "
                + "precio = ?, "
                + "cantidad = ?, "
                + "foto = ? "
                + "WHERE idProducto = ?";

        PreparedStatement sentencia =
                conexionBD.prepareStatement(
                        consulta
                );

        sentencia.setString(
                1,
                producto.getNombre()
        );

        sentencia.setString(
                2,
                producto.getDescripcion()
        );

        sentencia.setString(
                3,
                producto.getRestricciones()
        );

        sentencia.setDouble(
                4,
                producto.getPrecio()
        );

        sentencia.setInt(
                5,
                producto.getCantidad()
        );
        
        sentencia.setBytes(
                6,
                producto.getFoto()
        );

        sentencia.setInt(
                7,
                producto.getIdProducto()
        );

        int filasAfectadas =
                sentencia.executeUpdate();

        sentencia.close();
        conexionBD.close();

        return filasAfectadas > 0;

    } else {

        throw new SQLException(
                "Error al conectar a la BD."
        );
      }
    }
}
