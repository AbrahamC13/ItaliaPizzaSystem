package italiapizzasystem.controlador;

import italiapizzasystem.excepciones.RegistrarPedidoException;
import italiapizzasystem.persistencia.dao.PedidoDAO;
import italiapizzasystem.persistencia.dao.ProductoDAO;
import italiapizzasystem.persistencia.pojo.Cliente;
import italiapizzasystem.persistencia.pojo.OrdenFila;
import italiapizzasystem.persistencia.pojo.Pedido;
import italiapizzasystem.persistencia.pojo.Producto;
import italiapizzasystem.persistencia.pojo.UserSession;
import italiapizzasystem.utilidad.EfectoBotones;
import italiapizzasystem.utilidad.Navegador;
import italiapizzasystem.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;

/**
 * FXML Controller class
 *
 * @author Gerardo
 */
public class FXMLRealizarPedidoController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(FXMLRealizarPedidoController.class.getName());
    
    private Cliente clienteElegido;
    @FXML
    private Label lb_Fecha;
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
    private Button btn_RealizarPedido;
    @FXML
    private Label lb_Total;
    @FXML
    private Label lb_NombreCliente;
    @FXML
    private AnchorPane ap_ContenedorProductos;
    @FXML
    private FlowPane fp_productos;
    
    private ObservableList<OrdenFila> listaOrden;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        lb_Total.setText("$0.00");
        cargarFechaActual();
        cargarCatalogoProductos();
        EfectoBotones.darEfectoBotones(btn_Cancelar, btn_RealizarPedido);
    }    
    
    public void inicializarCliente(Cliente cliente) {
        if (cliente != null) {
            this.clienteElegido = cliente;
            this.lb_NombreCliente.setText(cliente.getNombre() + " " + cliente.getAPaterno() + " " + cliente.getAMaterno());
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

    private void actualizarTotalOrden() {
        double total = 0.0;
        for (OrdenFila fila : listaOrden){
            total += fila.getSubtotal();
        }
        lb_Total.setText(String.format("$%.2f", total));
    }
    
    public void agregarProductoAOrden(Producto producto){
        OrdenFila filaExistente = null;
        for (OrdenFila fila : listaOrden){
            if (fila.getIdProducto() == producto.getIdProducto()){
                filaExistente = fila;
                break;
            }
        }

        if (filaExistente != null){
            filaExistente.actualizarCantidad(filaExistente.getCantidad() + 1);
            tbl_Orden.refresh(); 
        } else{
            OrdenFila nuevaFila = new OrdenFila(producto);
            
            nuevaFila.getBtnQuitar().setOnAction(event -> {
                if (nuevaFila.getCantidad() > 1) {
                    nuevaFila.actualizarCantidad(nuevaFila.getCantidad() - 1);
                    tbl_Orden.refresh();
                } else {
                    listaOrden.remove(nuevaFila);
                }
                actualizarTotalOrden();
            });
            
            listaOrden.add(nuevaFila);
        }

        actualizarTotalOrden();
    }
    
    private void cargarCatalogoProductos() {
        try {
            java.util.ArrayList<Producto> productos = ProductoDAO.obtenerProductos();
            
            fp_productos.getChildren().clear();
            inyectarProductosEnFlowPane(productos);
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Fallo al consultar productos", ex);
            Utilidad.mostrarAlertaSimple(javafx.scene.control.Alert.AlertType.ERROR, "Error BD", "No se pudo cargar el catálogo.");
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

    private Parent generarTarjetaIndividual(Producto producto){
        try {
            FXMLLoader cargador = new javafx.fxml.FXMLLoader(
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
    
    private void cargarFechaActual(){
        LocalDate fechaHoy = LocalDate.now();
        DateTimeFormatter formateador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        lb_Fecha.setText(fechaHoy.format(formateador));
    }

    @FXML
    private void btn_clicRealizarPedido(ActionEvent event) {
        if (!validarFormulario()) {
            return;
        }

        try {
            Pedido pedidoNuevo = construirPedido();
            ArrayList<OrdenFila> productosOrden = new ArrayList<>(listaOrden);

            boolean resultadoExitoso = PedidoDAO.registrarPedido(pedidoNuevo, productosOrden);

            if (!resultadoExitoso) {
                Utilidad.mostrarAlertaSimple(
                    javafx.scene.control.Alert.AlertType.ERROR, 
                    "Error de guardado", 
                    "No se pudo registrar el pedido. Intente más tarde."
                );
                return;
            }

            Utilidad.mostrarAlertaSimple(
                javafx.scene.control.Alert.AlertType.INFORMATION, 
                "Pedido registrado", 
                "El pedido se ha guardado exitosamente en el sistema."
            );

            listaOrden.clear();
            Navegador.cambiarVentana(btn_RealizarPedido, "vista/FXMLPedidos.fxml", "Pedidos");

        } catch (RegistrarPedidoException ex) {
            LOGGER.log(Level.SEVERE, "Error al procesar el registro de pedido en el controlador", ex);

            Utilidad.mostrarAlertaSimple(
                javafx.scene.control.Alert.AlertType.ERROR, 
                "Error de registro", 
                "Hubo un error al guarda el pedido en la BD"
            );
        }
    }

    private boolean validarFormulario() {
        if (listaOrden == null || listaOrden.isEmpty()) {
            Utilidad.mostrarAlertaSimple(
                javafx.scene.control.Alert.AlertType.WARNING, 
                "Campos vacíos", 
                "No se puede realizar un pedido si no se han agregado productos a la orden."
            );
            return false;
        }

        if (clienteElegido == null) {
            Utilidad.mostrarAlertaSimple(
                javafx.scene.control.Alert.AlertType.WARNING, 
                "Falta Cliente", 
                "Es necesario tener un cliente asignado para registrar el pedido."
            );
            return false;
        }

        return true;
    }

    private Pedido construirPedido() {
        Pedido pedido = new Pedido();

        LocalDate fechaHoy = LocalDate.now();
        DateTimeFormatter formateadorBD = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        int idEmpleadoSesion = UserSession.getInstancia().getEmpleadoConectado().getIdEmpleado();
        
        pedido.setFechaPedido(fechaHoy.format(formateadorBD));
        pedido.setStatus("Registrado");
        pedido.setIdCliente(clienteElegido.getIdCliente());
        pedido.setIdEmpleado(idEmpleadoSesion); 

        return pedido;
    }

    @FXML
    private void btn_clicCancelar(ActionEvent event){
        if (listaOrden != null){
            listaOrden.clear();
        }
        
        Navegador.cambiarVentana(btn_Cancelar, "vista/FXMLPedidos.fxml", "Pedidos"
        );
    }
}
