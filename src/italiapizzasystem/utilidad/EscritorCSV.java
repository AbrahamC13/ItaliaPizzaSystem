
package italiapizzasystem.utilidad;

import italiapizzasystem.excepciones.ExportarDocumentoException;
import italiapizzasystem.persistencia.dao.PedidoDAO;
import italiapizzasystem.persistencia.pojo.DescripcionPedido;
import italiapizzasystem.persistencia.pojo.PedidoCliente;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author acrca
 */
public class EscritorCSV {
    
    private static final Logger LOGGER = Logger.getLogger(EscritorCSV.class.getName());
    private static final String SEPARADOR = ",";
    private static final String ENCABEZADO = "ID Pedido,Cliente,Dirección,Fecha,Total ($),Estatus";

    public static void exportarPedidos(List<PedidoCliente> listaPedidos, File archivoDestino) 
            throws ExportarDocumentoException {
        
        try (PrintWriter escritor = new PrintWriter(archivoDestino)){
            escritor.println(ENCABEZADO);
            escribirLineas(listaPedidos, escritor);
        } catch (FileNotFoundException ex) {
            LOGGER.log(Level.SEVERE, "Error de acceso al crear el archivo CSV: " + archivoDestino.getAbsolutePath(), ex);
            throw new ExportarDocumentoException("El archivo no pudo ser creado o está abierto en otro lado.", ex);
        } catch (ExportarDocumentoException ex) {
            throw ex;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error general e inesperado durante la exportación a CSV", ex);
            throw new ExportarDocumentoException("Ocurrió un error inesperado al generar el reporte en formato CSV: " + ex.getMessage(), ex);
        }
    }

    private static void escribirLineas(List<PedidoCliente> listaPedidos, PrintWriter escritor) 
            throws ExportarDocumentoException {
        for (PedidoCliente pedido : listaPedidos) {
            String lineaFormateada = formatearPedidoACSV(pedido);
            escritor.println(lineaFormateada);
        }
    }

    private static String formatearPedidoACSV(PedidoCliente pedido) throws ExportarDocumentoException {
        StringBuilder constructorLinea = new StringBuilder();
        double totalPedido = 0.0;

        try {
            totalPedido = PedidoDAO.obtenerTotalPedidoPorId(pedido.getIdPedido());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al obtener el monto del pedido " + pedido.getIdPedido() + " desde la BD para el CSV", ex);
            throw new ExportarDocumentoException("No se pudieron recuperar los totales de los pedidos desde la base de datos.", ex);
        }

        constructorLinea.append(pedido.getIdPedido()).append(SEPARADOR);
        constructorLinea.append(escaparCamposTexto(pedido.getNombreCliente())).append(SEPARADOR);
        constructorLinea.append(escaparCamposTexto(pedido.getDireccion())).append(SEPARADOR);
        constructorLinea.append(pedido.getFechaPedido()).append(SEPARADOR);
        constructorLinea.append(String.format("%.2f", totalPedido)).append(SEPARADOR); 
        constructorLinea.append(escaparCamposTexto(pedido.getStatus()));
        
        return constructorLinea.toString();
    }

    private static String escaparCamposTexto(String texto){
        if (texto == null) {
            return "";
        }
        if (texto.contains(SEPARADOR)) {
            return "\"" + texto.replace("\"", "\"\"") + "\"";
        }
        return texto;
    }
    
}
