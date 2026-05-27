/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package italiapizzasystem.controlador;

import italiapizzasystem.excepciones.PedidoVacioException;
import italiapizzasystem.persistencia.dao.DescripcionPedidoDAO;
import italiapizzasystem.persistencia.dao.PedidoDAO;
import italiapizzasystem.persistencia.dao.ProductoDAO;
import italiapizzasystem.persistencia.pojo.DescripcionPedido;
import italiapizzasystem.persistencia.pojo.OrdenFila;
import italiapizzasystem.persistencia.pojo.Pedido;
import italiapizzasystem.persistencia.pojo.PedidoCliente;
import italiapizzasystem.persistencia.pojo.Producto;
import italiapizzasystem.persistencia.pojo.UserSession;
import italiapizzasystem.utilidad.EfectoBotones;
import italiapizzasystem.utilidad.Navegador;
import italiapizzasystem.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

/**
 * FXML Controller class
 *
 * @author acrca
 */
public class FXMLEditarPedidoController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(FXMLEditarPedidoController.class.getName());
    
    private PedidoCliente pedidoOriginal;
    
    @FXML
    private AnchorPane ap_ContenedorProductos;
    @FXML
    private FlowPane fp_productos;
    @FXML
    private Label lb_Fecha;
    @FXML
    private Label lb_IdPedido;
    @FXML
    private TableView<OrdenFila> tbl_Orden;
    @FXML
    private TableColumn<OrdenFila, String> col_Producto;
    @FXML
    private TableColumn<OrdenFila, Integer> col_Cantidad;
    @FXML
    private TableColumn<OrdenFila, Double> col_Subtotal;
    @FXML
    private TableColumn<OrdenFila, Button> col_Quitar;
    @FXML
    private Button btn_Cancelar;
    @FXML
    private Button btn_GuardarCambios;
    @FXML
    private Label lb_Total;
    @FXML
    private Label lb_NombreCliente;
    
    private ObservableList<OrdenFila> listaOrden;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        lb_Total.setText("$0.00");
        cargarCatalogoProductos();
        EfectoBotones.darEfectoBotones(btn_Cancelar, btn_GuardarCambios);
    }    
    
    public void inicializarDatos(PedidoCliente pedidoSeleccionado) {
        if (pedidoSeleccionado != null) {
            this.pedidoOriginal = pedidoSeleccionado;
            this.lb_IdPedido.setText(String.valueOf(pedidoSeleccionado.getIdPedido()));
            this.lb_NombreCliente.setText(pedidoSeleccionado.getNombreCliente());
            this.lb_Fecha.setText(pedidoSeleccionado.getFechaPedido());
            
            cargarDetallePedidoOriginal(pedidoSeleccionado.getIdPedido());
        }
    }
    
    private void configurarTabla() {
        col_Producto.setCellValueFactory(new PropertyValueFactory<>("nombreProducto"));
        col_Cantidad.setCellValueFactory(new PropertyValueFactory<>("cantidad"));
        col_Subtotal.setCellValueFactory(new PropertyValueFactory<>("subtotal"));
        col_Quitar.setCellValueFactory(new PropertyValueFactory<>("btnQuitar"));
        col_Quitar.setStyle("-fx-alignment: CENTER;");
        col_Cantidad.setStyle("-fx-alignment: CENTER;");
        
        listaOrden = FXCollections.observableArrayList();
        tbl_Orden.setItems(listaOrden);
    }
    
    private void cargarDetallePedidoOriginal(int idPedido) {
        try {
            ArrayList<Producto> catalogo = ProductoDAO.obtenerProductos();
            ArrayList<DescripcionPedido> detallesBD = DescripcionPedidoDAO.obtenerDescripcionPedido(idPedido);
            
            for (DescripcionPedido detalle : detallesBD) {
                Producto productoAsociado = null;
                for (Producto prod : catalogo) {
                    if (prod.getIdProducto() == detalle.getIdProducto()) {
                        productoAsociado = prod;
                        break;
                    }
                }
                
                if (productoAsociado != null) {
                    OrdenFila fila = new OrdenFila(productoAsociado);
                    fila.actualizarCantidad(detalle.getCantidad());
                    
                    fila.getBtnQuitar().setOnAction(event -> {
                        evaluarDisminucionProducto(fila);
                    });
                    listaOrden.add(fila);
                }
            }
            actualizarTotalOrden();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Fallo al recuperar el detalle del pedido", ex);
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error BD", "No se pudieron cargar los productos originales del pedido.");
        }
    }
    
    public void agregarProductoAOrden(Producto producto) {
        OrdenFila filaExistente = null;
        for (OrdenFila fila : listaOrden) {
            if (fila.getIdProducto() == producto.getIdProducto()) {
                filaExistente = fila;
                break;
            }
        }

        if (filaExistente != null) {
            filaExistente.actualizarCantidad(filaExistente.getCantidad() + 1);
            tbl_Orden.refresh(); 
        } else {
            OrdenFila nuevaFila = new OrdenFila(producto);
            nuevaFila.getBtnQuitar().setOnAction(event -> {
                evaluarDisminucionProducto(nuevaFila);
            });
            listaOrden.add(nuevaFila);
        }
        actualizarTotalOrden();
    }
    
    private void evaluarDisminucionProducto(OrdenFila fila) {
        if (fila.getCantidad() > 1) {
            fila.actualizarCantidad(fila.getCantidad() - 1);
            tbl_Orden.refresh();
        } else {
            listaOrden.remove(fila);
        }
        actualizarTotalOrden();
    }
    
    private void actualizarTotalOrden() {
        double total = 0.0;
        for (OrdenFila fila : listaOrden) {
            total += fila.getSubtotal();
        }
        lb_Total.setText(String.format("$%.2f", total));
    }
    
    private void cargarCatalogoProductos() {
        try {
            ArrayList<Producto> productos = ProductoDAO.obtenerProductos();
            fp_productos.getChildren().clear();
            inyectarProductosEnFlowPane(productos);
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Fallo al consultar productos", ex);
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error BD", "No se pudo cargar el catálogo.");
        }
    }
    
    private void inyectarProductosEnFlowPane(ArrayList<Producto> productos) {
        for (Producto prod : productos) {
            Parent tarjetaVisual = generarTarjetaIndividual(prod);
            if (tarjetaVisual != null) {
                fp_productos.getChildren().add(tarjetaVisual);
            }
        }
    }
    
    private Parent generarTarjetaIndividual(Producto producto) {
        try {
            FXMLLoader cargador = new FXMLLoader(
                getClass().getResource("/italiapizzasystem/vista/FXMLItemProducto.fxml")
            );
            Parent nodoVisual = cargador.load();
            FXMLItemProductoController ctrlTarjeta = cargador.getController();
            
            ctrlTarjeta.inicializarTarjeta(producto, this);
            return nodoVisual;
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, "Error al llenar FXML del producto", ex);
            return null; 
        }
    }
    
    @FXML
    private void btn_clicGuardarCambios(ActionEvent event) {
        try {
            validarPedidoNoVacio();

            Pedido pedidoModificado = construirPedido();
            ArrayList<OrdenFila> productosOrden = new ArrayList<>(listaOrden);

            boolean resultadoExitoso = PedidoDAO.modificarPedido(pedidoModificado, productosOrden);

            if (!resultadoExitoso) {
                Utilidad.mostrarAlertaSimple(
                    Alert.AlertType.ERROR, 
                    "Error de guardado", 
                    "No se pudo actualizar el pedido. Intente más tarde."
                );
                return;
            }

            Utilidad.mostrarAlertaSimple(
                Alert.AlertType.INFORMATION, 
                "Pedido actualizado", 
                "El pedido se ha modificado exitosamente en el sistema."
            );

            listaOrden.clear();
            Navegador.cambiarVentana(btn_GuardarCambios, "vista/FXMLPedidos.fxml", "Pedidos");

        } catch (PedidoVacioException ex) {
            LOGGER.log(Level.WARNING, ex.getMessage());
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Pedido vacío", ex.getMessage());
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error al procesar la actualización del pedido en el controlador", ex);
            Utilidad.mostrarAlertaSimple(
                Alert.AlertType.ERROR, 
                "Error de actualización", 
                "Hubo un error al guardar los cambios en la BD."
            );
        }
    }
    
    private Pedido construirPedido() {
        Pedido pedido = new Pedido();
        int idEmpleadoSesion = UserSession.getInstancia().getEmpleadoConectado().getIdEmpleado();
        
        pedido.setIdPedido(pedidoOriginal.getIdPedido());
        pedido.setFechaPedido(pedidoOriginal.getFechaPedido()); 
        pedido.setStatus(pedidoOriginal.getStatus());
        pedido.setIdEmpleado(idEmpleadoSesion); 

        return pedido;
    }
    
    private void validarPedidoNoVacio() throws PedidoVacioException {
        if (listaOrden == null || listaOrden.isEmpty()) {
            throw new PedidoVacioException("No puedes guardar un pedido sin productos. "
                    + "Si deseas eliminar el pedido, utiliza la opción de 'Cancelar Pedido'.");
        }
    }
    
    @FXML
    private void btn_clicCancelar(ActionEvent event) {
        Navegador.cambiarVentana(btn_Cancelar, "vista/FXMLPedidos.fxml", "Pedidos");
    }
    
}
