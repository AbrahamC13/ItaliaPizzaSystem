package italiapizzasystem.controlador;

import italiapizzasystem.ItaliaPizzaSystem;
import italiapizzasystem.persistencia.pojo.Producto;
import italiapizzasystem.utilidad.Utilidad;

import italiapizzasystem.persistencia.dao.ProductoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

/**
 *
 * @author Gerardo
 */
public class FXMLInventarioController
        implements Initializable {

    private static final Logger LOGGER =
            Logger.getLogger(
                    FXMLInventarioController.class.getName()
            );

    @FXML
    private TextField tf_Nombre;

    @FXML
    private Button btn_Buscar;

    @FXML
    private Button btn_Regresar;

    @FXML
    private Button btn_RegistrarProducto;

    @FXML
    private Button btn_GenerarReporte;

    @FXML
    private TableView<Producto> tv_Productos;

    @FXML
    private TableColumn<Producto, String>
            tc_CodigoProducto;

    @FXML
    private TableColumn<Producto, String>
            tc_NombreProducto;
    
    @FXML
    private Button btn_ActualizarProducto;

    @Override
    public void initialize(
            URL url,
            ResourceBundle rb
    ) {
        cargarProductos();
    }

    @FXML
    private void btnClicBuscar(
            ActionEvent event
    ) {

        try {

        ObservableList<Producto> productos =
                FXCollections.observableArrayList(
                        ProductoDAO.buscarProductos(
                                tf_Nombre.getText()
                        )
                );

        tv_Productos.setItems(
                productos
        );

        } catch (Exception ex) {

        ex.printStackTrace();

        Utilidad.mostrarAlertaSimple(
                Alert.AlertType.ERROR,
                "Error",
                ex.getMessage()
        );
        }
    }

    @FXML
    private void btnClicRegresar(
            ActionEvent event
    ) {

        try {

            Stage escenarioBase =
                    (Stage) btn_Regresar
                    .getScene()
                    .getWindow();

            FXMLLoader cargador =
                    new FXMLLoader(
                            ItaliaPizzaSystem.class.getResource(
                                    "vista/FXMLMenuPrincipal.fxml"
                            )
                    );

            Parent vista =
                    cargador.load();

            Scene escenaMenu =
                    new Scene(vista);

            escenarioBase.setScene(
                    escenaMenu
            );

            escenarioBase.setTitle(
                    "Menu Principal"
            );

            escenarioBase.show();

        } catch (IOException ex) {

            Utilidad.mostrarAlertaSimple(
                    Alert.AlertType.ERROR,
                    "Error",
                    ex.getMessage()
            );

            LOGGER.log(
                    Level.SEVERE,
                    "Error al cargar menú principal",
                    ex
            );
        }
    }

    @FXML
    private void btnClicRegistrarProducto(
            ActionEvent event
    ) {

        try {

            Stage escenarioBase =
                    (Stage) btn_RegistrarProducto
                    .getScene()
                    .getWindow();

            FXMLLoader cargador =
                    new FXMLLoader(
                            ItaliaPizzaSystem.class.getResource(
                                    "vista/FXMLRegistrarProducto.fxml"
                            )
                    );

            Parent vista =
                    cargador.load();

            Scene escena =
                    new Scene(vista);

            escenarioBase.setScene(
                    escena
            );

            escenarioBase.setTitle(
                    "Registrar Producto"
            );

            escenarioBase.show();

        } catch (IOException ex) {

            Utilidad.mostrarAlertaSimple(
                    Alert.AlertType.ERROR,
                    "Error",
                    ex.getMessage()
            );

            LOGGER.log(
                    Level.SEVERE,
                    "Error al abrir "
                    + "FXMLRegistrarProducto",
                    ex
            );
        }
    }

    @FXML
    private void btnClicGenerarReporte(
            ActionEvent event
    ) {

        Utilidad.mostrarAlertaSimple(
                Alert.AlertType.INFORMATION,
                "Reporte",
                "Función en desarrollo"
        );
    }
    
    //Metodo
    private void cargarProductos() {

    try {

        ObservableList<Producto> productos =
                FXCollections.observableArrayList(
                        ProductoDAO.obtenerProductos()
                );

        tc_CodigoProducto.setCellValueFactory(
                new PropertyValueFactory<>(
                        "codigoProducto"
                )
        );

        tc_NombreProducto.setCellValueFactory(
                new PropertyValueFactory<>(
                        "nombre"
                )
        );

        tv_Productos.setItems(
                productos
        );

    } catch (Exception ex) {

        ex.printStackTrace();

        Utilidad.mostrarAlertaSimple(
                Alert.AlertType.ERROR,
                "Error",
                ex.getMessage()
        );
       }
    }
    
    @FXML
    private void btnClicActualizarProducto(
        ActionEvent event
    ) {

    try {

        Stage escenarioBase =
                (Stage) btn_ActualizarProducto
                .getScene()
                .getWindow();

        FXMLLoader cargador =
                new FXMLLoader(
                        ItaliaPizzaSystem.class.getResource(
                                "vista/FXMLActualizarProducto.fxml"
                        )
                );

        Parent vista =
                cargador.load();

        Scene escena =
                new Scene(vista);

        escenarioBase.setScene(
                escena
        );

        escenarioBase.setTitle(
                "Actualizar Producto"
        );

        escenarioBase.show();

    } catch (IOException ex) {

        Utilidad.mostrarAlertaSimple(
                Alert.AlertType.ERROR,
                "Error",
                ex.getMessage()
        );

        LOGGER.log(
                Level.SEVERE,
                "Error al abrir FXMLActualizarProducto",
                ex
        );
        }
    }
}

    