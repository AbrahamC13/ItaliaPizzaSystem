    
package italiapizzasystem.controlador;

import italiapizzasystem.ItaliaPizzaSystem;
import italiapizzasystem.utilidad.Utilidad;
import italiapizzasystem.persistencia.pojo.Producto;
import italiapizzasystem.persistencia.dao.ProductoDAO;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gerardo
 */
public class FXMLRegistrarProductoController implements Initializable {

    @FXML
    private TextField tf_Nombre;
    @FXML
    private TextArea ta_Descripcion;
    @FXML
    private TextArea ta_Restricciones;
    @FXML
    private TextField tf_Precio;
    @FXML
    private TextField tf_Cantidad;
    @FXML
    private Label lb_Campos;
    @FXML
    private Label lb_Nombre;
    @FXML
    private Label lb_Descripcion;
    @FXML
    private Label lb_Restriccion;
    @FXML
    private Label lb_Precio;
    @FXML
    private Label lb_Cantidad;
    @FXML
    private Button btnCancelarRegistro;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void btnClicCancelarRegistro(ActionEvent event) {
        try {

        Stage escenarioBase =
                (Stage) btnCancelarRegistro
                .getScene()
                .getWindow();

        FXMLLoader cargador =
                new FXMLLoader(
                        ItaliaPizzaSystem.class.getResource(
                                "vista/FXMLInventario.fxml"
                        )
                );

        Parent vista =
                cargador.load();

        Scene escena =
                new Scene(vista);

        escenarioBase.setScene(
                escena
        );

        escenarioBase.show();

        } catch (IOException ex) {

           ex.printStackTrace();
        }
    }

    @FXML
    private void btnClicSubirArchivo(ActionEvent event) {
        System.out.println("Entró a subir archivo");

        FileChooser selector = new FileChooser();

        File archivo =
            selector.showOpenDialog(null);

        if (archivo != null) {

            System.out.println(
                archivo.getAbsolutePath()
            );
        }
    }

    @FXML
    private void btnClicRegistrarProducto(ActionEvent event) {

    try {

        if (tf_Nombre.getText().trim().isEmpty()
                || tf_Precio.getText().trim().isEmpty()) {

            Utilidad.mostrarAlertaSimple(
                    Alert.AlertType.WARNING,
                    "Campos vacíos",
                    "Ingrese nombre y precio"
            );

            return;
        }

        Producto producto = new Producto();

        producto.setCodigoProducto(
                "P" + System.currentTimeMillis()
        );

        producto.setNombre(
                tf_Nombre.getText().trim()
        );

        producto.setDescripcion(
                ta_Descripcion.getText().trim()
        );

        producto.setRestricciones(
                ta_Restricciones.getText().trim()
        );

        producto.setPrecio(
                Double.parseDouble(
                        tf_Precio.getText().trim()
                )
        );
        
        producto.setCantidad(
                Integer.parseInt(
                        tf_Cantidad.getText()
                )
        );

        boolean registrado =
                ProductoDAO.registrarProducto(
                        producto
                );

        if (registrado) {

        Utilidad.mostrarAlertaSimple(
            Alert.AlertType.INFORMATION,
            "Éxito",
            "Producto registrado correctamente"
        );

        Stage escenarioBase =
            (Stage) tf_Nombre
            .getScene()
            .getWindow();

        FXMLLoader cargador =
            new FXMLLoader(
                    ItaliaPizzaSystem.class.getResource(
                            "vista/FXMLInventario.fxml"
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
            "Inventario"
        );

        escenarioBase.show();

        } else {

            Utilidad.mostrarAlertaSimple(
                    Alert.AlertType.ERROR,
                    "Error",
                    "No se pudo registrar el producto"
            );
        }

    } catch (NumberFormatException ex) {

        Utilidad.mostrarAlertaSimple(
                Alert.AlertType.WARNING,
                "Precio inválido",
                "Ingrese un número válido en el precio"
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
} 

