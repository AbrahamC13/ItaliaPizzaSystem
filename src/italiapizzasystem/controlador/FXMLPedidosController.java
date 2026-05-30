package italiapizzasystem.controlador;

import italiapizzasystem.ItaliaPizzaSystem;
import italiapizzasystem.excepciones.PedidoEstadoInvalidoException;
import italiapizzasystem.persistencia.dao.PedidoDAO;
import italiapizzasystem.persistencia.pojo.PedidoCliente;
import italiapizzasystem.utilidad.EfectoBotones;
import italiapizzasystem.utilidad.Navegador;
import italiapizzasystem.utilidad.PedidoFila;
import italiapizzasystem.utilidad.Utilidad;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
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
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Abraham
 */
public class FXMLPedidosController implements Initializable {

    private static final Logger LOGGER = Logger.getLogger(FXMLPedidosController.class.getName());
    
    @FXML
    private TableColumn<PedidoFila, Integer> col_IdPedido;
    @FXML
    private TableColumn<PedidoFila, String> col_NombreCliente;
    @FXML
    private TableColumn<PedidoFila, String> col_Direccion;
    @FXML
    private TableColumn<PedidoFila, String> col_Fecha;
    @FXML
    private TableColumn<PedidoFila, String> col_Status;
    @FXML
    private TableColumn<PedidoFila, Void> col_Editar;
    @FXML
    private TextField tf_NombreCliente;
    @FXML
    private TableView<PedidoFila> tbl_Pedidos;
    
    public ObservableList<PedidoFila> listaPedidosFila;
    
    @FXML
    private Button btn_NuevoPedido;
    @FXML
    private Button btn_Exportar;
    @FXML
    private Button btn_Regresar;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarTabla();
        cargarDatosTabla();
        EfectoBotones.darEfectoBotones(btn_NuevoPedido, btn_Exportar, btn_Regresar);
    }    
    
    private void configurarTabla() {
        col_IdPedido.setCellValueFactory(new PropertyValueFactory<>("idPedido"));
        col_NombreCliente.setCellValueFactory(new PropertyValueFactory<>("nombreCliente"));
        col_Direccion.setCellValueFactory(new PropertyValueFactory<>("direccion"));
        col_Fecha.setCellValueFactory(new PropertyValueFactory<>("fechaPedido"));
        col_Status.setCellValueFactory(new PropertyValueFactory<>("status"));
        
        configurarColumnaEstatus();
        configurarColumnaEditar();
        
        alinearColumnasTabla();
    }
    
    private void alinearColumnasTabla() {
        col_Status.setStyle("-fx-alignment: CENTER;");
        col_Editar.setStyle("-fx-alignment: CENTER;");
        col_IdPedido.setStyle("-fx-alignment: CENTER;");
        col_Fecha.setStyle("-fx-alignment: CENTER;");
    }
    
    private void configurarColumnaEstatus() {
        col_Status.setCellFactory(param -> new TableCell<PedidoFila, String>() {
            private final ComboBox<String> comboEstado = new ComboBox<>(
                FXCollections.observableArrayList("En Proceso", "Entregado", "Cancelado")
            );

            {
                comboEstado.setPrefWidth(150.0);
                comboEstado.setCursor(javafx.scene.Cursor.HAND);
                comboEstado.setStyle(
                    "-fx-background-color: #FFFFFF; " +
                    "-fx-border-color: #CCCCCC; " +
                    "-fx-border-radius: 5; " +
                    "-fx-background-radius: 5; " +
                    "-fx-font-size: 13px;"
                );

                comboEstado.valueProperty().addListener((observable, valorAnterior, valorNuevo) -> {
                    if (valorNuevo == null || valorNuevo.equals(valorAnterior) || getTableRow() == null) {
                        return;
                    }

                    if (!comboEstado.isFocused() && !comboEstado.isShowing()) {
                        return;
                    }
                    
                    PedidoFila filaActiva = (PedidoFila) getTableRow().getItem();
                    if (filaActiva != null && filaActiva.getPedidoCliente() != null) {
                        try {
                            if (PedidoDAO.actualizarEstatusPedido(filaActiva.getIdPedido(), valorNuevo)) {
                                filaActiva.getPedidoCliente().setStatus(valorNuevo);
                                
                                Utilidad.mostrarAlertaSimple(
                                    Alert.AlertType.INFORMATION, 
                                    "Estatus actualizado", 
                                    "El estado del pedido #" + filaActiva.getIdPedido() + " se actualizó a: " + valorNuevo
                                );
                                getTableView().refresh();
                            }
                        } catch (SQLException ex) {
                            javafx.application.Platform.runLater(() -> comboEstado.setValue(valorAnterior));
                            LOGGER.log(Level.SEVERE, "Error al actualizar el estatus del pedido #" + filaActiva.getIdPedido(), ex);
                            Utilidad.mostrarAlertaSimple(
                                Alert.AlertType.ERROR, 
                                "Error de Conexión", 
                                "No se pudo guardar el cambio de estado en la base de datos."
                            );
                        }
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    PedidoFila fila = (PedidoFila) getTableRow().getItem();
                    if (fila != null && fila.getPedidoCliente() != null) {
                        comboEstado.setValue(fila.getPedidoCliente().getStatus());
                        setGraphic(comboEstado);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void configurarColumnaEditar() {
        col_Editar.setCellFactory(param -> new TableCell<PedidoFila, Void>() {
            private final Button btnEditar = new Button("Editar");
            
            private final String estiloBase = 
                "-fx-background-color: #007BFF; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 5 15 5 15; -fx-font-size: 12px;";
                
            private final String estiloHover = 
                "-fx-background-color: #0056b3; -fx-text-fill: white; -fx-font-weight: bold; " +
                "-fx-border-radius: 5; -fx-background-radius: 5; -fx-padding: 5 15 5 15; -fx-font-size: 12px;";

            {
                btnEditar.setPrefWidth(90.0);
                btnEditar.setCursor(javafx.scene.Cursor.HAND);
                btnEditar.setStyle(estiloBase);
                btnEditar.setOnMouseEntered(e -> btnEditar.setStyle(estiloHover));
                btnEditar.setOnMouseExited(e -> btnEditar.setStyle(estiloBase));
                
                btnEditar.setOnAction(event -> {
                    PedidoFila filaActiva = (PedidoFila) getTableRow().getItem();
                    if (filaActiva != null && filaActiva.getPedidoCliente() != null) {
                        abrirPantallaEdicion(filaActiva.getPedidoCliente());
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableRow() == null || getTableRow().getItem() == null) {
                    setGraphic(null);
                } else {
                    PedidoFila fila = (PedidoFila) getTableRow().getItem();
                    if (fila != null && fila.getPedidoCliente() != null) {
                        String estatusActual = fila.getPedidoCliente().getStatus();
                        if ("Entregado".equalsIgnoreCase(estatusActual) || "Cancelado".equalsIgnoreCase(estatusActual)) {
                            btnEditar.setDisable(true);
                        } else {
                            btnEditar.setDisable(false);
                        }
                        setGraphic(btnEditar);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }
    
    private void cargarDatosTabla() {
        listaPedidosFila = FXCollections.observableArrayList();
        try {
            List<PedidoCliente> datosBD = PedidoDAO.obtenerPedidosConCliente();
            for (PedidoCliente pedidoBD : datosBD) {
                listaPedidosFila.add(new PedidoFila(pedidoBD));
            }
            tbl_Pedidos.setItems(listaPedidosFila);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error de Conexión", "No se pudieron obtener los pedidos.");
            LOGGER.log(Level.SEVERE, "Error al cargar pedidos en la tabla", ex);
        } catch (Exception ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general al cargar datos de la tabla", ex);
            ex.printStackTrace();
        }
    }
    
    private void abrirPantallaEdicion(PedidoCliente pedidoSeleccionado) {
        try {
            validarEstadoPermitidoParaEdicion(pedidoSeleccionado.getStatus());
            FXMLLoader cargador = Navegador.cambiarVentanaConControlador(
                tbl_Pedidos, 
                "vista/FXMLEditarPedido.fxml", 
                "Editar Pedido"
            );
            if (cargador != null) {
                FXMLEditarPedidoController controladorEdicion = cargador.getController();
                controladorEdicion.inicializarDatos(pedidoSeleccionado);
            }
        } catch (PedidoEstadoInvalidoException ex) {
            LOGGER.log(Level.WARNING, "Intento de edición no permitido: {0}", ex.getMessage());
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Operación No Permitida", ex.getMessage());
        }
    }
    
    private void validarEstadoPermitidoParaEdicion(String estatus) throws PedidoEstadoInvalidoException {
        if ("Cancelado".equalsIgnoreCase(estatus) || "Entregado".equalsIgnoreCase(estatus)) {
            throw new PedidoEstadoInvalidoException("No es posible editar un pedido con estatus: " + estatus + ".");
        }
    }
    
    @FXML
    public void btn_ClicRegresar(ActionEvent event) {
        Navegador.cambiarVentana(tbl_Pedidos, "vista/FXMLMenuPrincipal.fxml", "Menu principal");
    }

    @FXML
    private void btn_ClicNuevoPedido(ActionEvent event) {
        Navegador.cambiarVentana(tbl_Pedidos, "vista/FXMLPedidosBuscarCliente.fxml", "Nuevo pedido");
    }

    @FXML
    private void btn_ClicExportarInformacionPedido(ActionEvent event) {
        try {
            if (listaPedidosFila == null || listaPedidosFila.isEmpty()) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Sin registros", "No existen pedidos en la tabla para ser exportados.");
                return;
            }
            List<PedidoCliente> listaPuros = new java.util.ArrayList<>();
            for (PedidoFila fila : listaPedidosFila) {
                if (fila.getPedidoCliente() != null) {
                    listaPuros.add(fila.getPedidoCliente());
                }
            }
            Stage escenarioBase = (Stage) tbl_Pedidos.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(ItaliaPizzaSystem.class.getResource("vista/FXMLExportarPedido.fxml"));
            Parent vista = cargador.load();
            
            FXMLExportarPedidoController controladorModal = cargador.getController();
            controladorModal.inicializarDatos(listaPuros);
            
            Stage escenarioModal = new Stage();
            escenarioModal.setScene(new Scene(vista));
            escenarioModal.setTitle("Exportar pedidos");
            escenarioModal.initModality(Modality.WINDOW_MODAL);
            escenarioModal.initOwner(escenarioBase);
            escenarioModal.centerOnScreen();
            escenarioModal.showAndWait();
        } catch (IOException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error", "No se pudo abrir la vista de exportar pedido.");
            LOGGER.log(Level.SEVERE, "Error al cargar FXMLExportarPedido ", ex);
        } catch (Exception ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error general", ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en btn_ClicExportarInformacionPedido ", ex);
            ex.printStackTrace();
        }
    }
}