
package italiapizzasystem.persistencia.dao;

import italiapizzasystem.excepciones.RegistrarPedidoException;
import italiapizzasystem.persistencia.ConexionBD;
import italiapizzasystem.persistencia.pojo.OrdenFila;
import italiapizzasystem.persistencia.pojo.Pedido;
import italiapizzasystem.persistencia.pojo.PedidoCliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.logging.Level;

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
    
    public static boolean registrarPedido(Pedido pedido, ArrayList<OrdenFila> productosOrden) throws RegistrarPedidoException {
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD == null) {
            throw new RegistrarPedidoException("Error al conectar a la BD.");
        }

        try {
            conexionBD.setAutoCommit(false);

            int idPedidoGenerado = insertarCabeceraPedido(conexionBD, pedido);
            insertarDetallesPedido(conexionBD, idPedidoGenerado, productosOrden);

            conexionBD.commit();
            return true;

        } catch (SQLException | RegistrarPedidoException ex) {
            deshacerTransaccion(conexionBD); 
            throw new RegistrarPedidoException("Fallo en la transacción de la BD: " + ex.getMessage(), ex);
        } finally {
            try {
                conexionBD.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static int insertarCabeceraPedido(Connection conexion, Pedido pedido) throws SQLException, RegistrarPedidoException {
        String consultaPedido = "INSERT INTO pedido (fechaPedido, estatus, Cliente_idCliente, Empleado_idEmpleado) "
                + "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement sentencia = conexion.prepareStatement(consultaPedido, PreparedStatement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, pedido.getFechaPedido());
            sentencia.setString(2, pedido.getStatus());
            sentencia.setInt(3, pedido.getIdCliente());
            sentencia.setInt(4, pedido.getIdEmpleado());

            if (sentencia.executeUpdate() == 0) {
                throw new RegistrarPedidoException("No se pudieron registrar los datos del pedido.");
            }

            try (ResultSet resultadoLlave = sentencia.getGeneratedKeys()) {
                if (resultadoLlave.next()) {
                    return resultadoLlave.getInt(1);
                }
            }
        }
        throw new RegistrarPedidoException("No se pudo recuperar la clave generada del pedido.");
    }

    private static void insertarDetallesPedido(Connection conexion, int idPedido, ArrayList<OrdenFila> productos) throws SQLException {
        String consultaDetalle = "INSERT INTO descripcionPedido (cantidad, total, Producto_idProducto, Pedido_idPedido) "
                + "VALUES (?, ?, ?, ?)";
        
        try (PreparedStatement sentenciaDetalle = conexion.prepareStatement(consultaDetalle)) {
            for (OrdenFila fila : productos) {
                sentenciaDetalle.setInt(1, fila.getCantidad());
                sentenciaDetalle.setDouble(2, fila.getSubtotal());
                sentenciaDetalle.setInt(3, fila.getIdProducto());
                sentenciaDetalle.setInt(4, idPedido);
                sentenciaDetalle.addBatch();
            }
            sentenciaDetalle.executeBatch();
        }
    }

    private static void deshacerTransaccion(Connection conexion) {
        try {
            if (conexion != null) {
                conexion.rollback();
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
    
    public static boolean actualizarEstatusPedido(int idPedido, String nuevoEstatus) throws SQLException {
        boolean exito = false;
        Connection conexionBD = ConexionBD.abrirConexion();

        if (conexionBD != null) {
            String consulta = "UPDATE pedido SET estatus = ? WHERE idPedido = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);

            sentencia.setString(1, nuevoEstatus);
            sentencia.setInt(2, idPedido);

            int filasAfectadas = sentencia.executeUpdate();
            if (filasAfectadas > 0) {
                exito = true;
            }

            sentencia.close();
            conexionBD.close();
        } else {
            throw new SQLException("Error al conectar a la BD.");
        }

        return exito;
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
