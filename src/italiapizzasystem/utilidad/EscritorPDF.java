/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package italiapizzasystem.utilidad;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import italiapizzasystem.excepciones.ExportarDocumentoException;
import italiapizzasystem.persistencia.dao.PedidoDAO;
import italiapizzasystem.persistencia.pojo.PedidoCliente;
import italiapizzasystem.persistencia.pojo.Producto;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author acrca
 */

public class EscritorPDF {
    
    private static final Logger LOGGER = Logger.getLogger(EscritorPDF.class.getName());
    private static final Font FUENTE_TITULO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, new BaseColor(0, 77, 87));
    private static final Font FUENTE_SUBTITULO = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.GRAY);
    private static final Font FUENTE_ENCABEZADO = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, BaseColor.WHITE);
    private static final Font FUENTE_CELDA = FontFactory.getFont(FontFactory.HELVETICA, 10, BaseColor.BLACK);
    
    private static final BaseColor COLOR_PANEL = new BaseColor(0, 77, 87);

    public static void exportarPedidos(List<PedidoCliente> listaPedidos, File archivoDestino) 
            throws ExportarDocumentoException {
        
        Document documento = new Document();
        
        try {
            PdfWriter.getInstance(documento, new FileOutputStream(archivoDestino));
            documento.open();
            
            añadirMetadatos(documento);
            añadirEncabezadoReporte(documento);
            añadirTablaPedidos(documento, listaPedidos);
            
        } catch (DocumentException | IOException ex) {
            LOGGER.log(Level.SEVERE, "Error de estructura iText o de E/S al crear el PDF: " + archivoDestino.getAbsolutePath(), ex);
            throw new ExportarDocumentoException("No se pudo generar o escribir en el archivo PDF de destino.", ex);
        } catch (ExportarDocumentoException ex) {
            throw ex;
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error general e inesperado durante la exportación a PDF", ex);
            throw new ExportarDocumentoException("Ocurrió un error inesperado al procesar el documento PDF: " + ex.getMessage(), ex);
        } finally {
            if (documento.isOpen()) {
                documento.close();
            }
        }
    }

    private static void añadirMetadatos(Document documento) {
        documento.addTitle("Reporte General de Pedidos");
        documento.addSubject("Sistema Italia Pizza");
        documento.addAuthor("Italia Pizza System");
    }

    private static void añadirEncabezadoReporte(Document documento) throws DocumentException {
        Paragraph titulo = new Paragraph("ITALIA PIZZA SYSTEM", FUENTE_TITULO);
        titulo.setAlignment(Element.ALIGN_CENTER);
        titulo.setSpacingAfter(5);
        documento.add(titulo);

        Paragraph subtitulo = new Paragraph("Reporte General de Información de Pedidos", FUENTE_SUBTITULO);
        subtitulo.setAlignment(Element.ALIGN_CENTER);
        subtitulo.setSpacingAfter(25);
        documento.add(subtitulo);
    }

    private static void añadirTablaPedidos(Document documento, List<PedidoCliente> listaPedidos) throws DocumentException, ExportarDocumentoException {
        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{1.0f, 2.5f, 3.5f, 2.0f, 1.5f, 1.5f});
        
        configurarEncabezadosTabla(tabla);
        poblarCeldasTabla(tabla, listaPedidos);
        
        documento.add(tabla);
    }

    private static void configurarEncabezadosTabla(PdfPTable tabla) {
        String[] titulosColumnas = {"ID", "Cliente", "Dirección", "Fecha", "Total", "Estatus"};
        
        for (String titulo : titulosColumnas) {
            PdfPCell celdaEncabezado = new PdfPCell(new Phrase(titulo, FUENTE_ENCABEZADO));
            celdaEncabezado.setBackgroundColor(COLOR_PANEL);
            celdaEncabezado.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaEncabezado.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celdaEncabezado.setPadding(8);
            tabla.addCell(celdaEncabezado);
        }
    }

    private static void poblarCeldasTabla(PdfPTable tabla, List<PedidoCliente> listaPedidos) 
            throws ExportarDocumentoException {
        for (PedidoCliente pedido : listaPedidos) {
            añadirFilaPedido(tabla, pedido);
        }
    }

    private static void añadirFilaPedido(PdfPTable tabla, PedidoCliente pedido) throws ExportarDocumentoException {
        double totalPedido = 0.0;
        
        try {
            totalPedido = PedidoDAO.obtenerTotalPedidoPorId(pedido.getIdPedido());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al consultar el total del pedido " + pedido.getIdPedido() + " para el PDF.", ex);
            throw new ExportarDocumentoException("No se pudieron recuperar los montos financieros desde la base de datos.", ex);
        }

        tabla.addCell(crearCeldaCentrada(String.valueOf(pedido.getIdPedido())));
        tabla.addCell(crearCeldaIzquierda(pedido.getNombreCliente()));
        tabla.addCell(crearCeldaIzquierda(pedido.getDireccion()));
        tabla.addCell(crearCeldaCentrada(pedido.getFechaPedido()));
        tabla.addCell(crearCeldaCentrada("$" + String.format("%.2f", totalPedido))); 
        tabla.addCell(crearCeldaCentrada(pedido.getStatus()));
    }

    private static PdfPCell crearCeldaCentrada(String texto) {
        String valor = (texto != null) ? texto : "";
        PdfPCell celda = new PdfPCell(new Phrase(valor, FUENTE_CELDA));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(6);
        return celda;
    }

    private static PdfPCell crearCeldaIzquierda(String texto) {
        String valor = (texto != null) ? texto : "";
        PdfPCell celda = new PdfPCell(new Phrase(valor, FUENTE_CELDA));
        celda.setHorizontalAlignment(Element.ALIGN_LEFT);
        celda.setVerticalAlignment(Element.ALIGN_MIDDLE);
        celda.setPadding(6);
        return celda;
    }
    
    public static void exportarInventario(
        List<Producto> listaProductos,
        File archivoDestino
    ) throws ExportarDocumentoException {

    Document documento = new Document();

    try {

        PdfWriter.getInstance(documento, new FileOutputStream(archivoDestino));

        documento.open();

        documento.addTitle(
                "Reporte General de Inventario"
        );

        documento.addSubject(
                "Sistema Italia Pizza"
        );

        documento.addAuthor(
                "Italia Pizza System"
        );

        Paragraph titulo =
                new Paragraph(
                        "ITALIA PIZZA SYSTEM",
                        FUENTE_TITULO
                );

        titulo.setAlignment(
                Element.ALIGN_CENTER
        );

        titulo.setSpacingAfter(5);

        documento.add(titulo);

        Paragraph subtitulo =
                new Paragraph(
                        "Reporte General de Inventario",
                        FUENTE_SUBTITULO
                );

        subtitulo.setAlignment(
                Element.ALIGN_CENTER
        );

        subtitulo.setSpacingAfter(25);

        documento.add(subtitulo);

        PdfPTable tabla = new PdfPTable(4);

        tabla.setWidthPercentage(100);

        tabla.setWidths(
                new float[]{
                    2.0f,
                    4.0f,
                    2.0f,
                    2.0f
                }
        );

        String[] titulos = {
            "Código",
            "Nombre",
            "Precio",
            "Cantidad"
        };

        for (String tituloColumna : titulos) {
                PdfPCell celdaEncabezado =
                    new PdfPCell(
                            new Phrase(
                                    tituloColumna,
                                    FUENTE_ENCABEZADO
                            )
                    );
            celdaEncabezado.setBackgroundColor(COLOR_PANEL);
            celdaEncabezado.setHorizontalAlignment(Element.ALIGN_CENTER);
            celdaEncabezado.setVerticalAlignment(Element.ALIGN_MIDDLE);
            celdaEncabezado.setPadding(8);
            tabla.addCell(celdaEncabezado);
        }

        for (Producto producto : listaProductos) {

            tabla.addCell(crearCeldaCentrada(producto.getCodigoProducto()));
            tabla.addCell(crearCeldaIzquierda(producto.getNombre()));
            tabla.addCell(crearCeldaCentrada("$"+ String.format("%.2f",producto.getPrecio())));
            tabla.addCell(crearCeldaCentrada(String.valueOf(producto.getCantidad())));
        }

        documento.add(tabla);

    } catch (DocumentException | IOException ex) {

        LOGGER.log(Level.SEVERE, "Error al generar PDF de inventario", ex);

        throw new ExportarDocumentoException("No se pudo generar el reporte de inventario.", ex);

    } finally {

        if (documento.isOpen()) {
            documento.close();
        }
      }
    }
}
