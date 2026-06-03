
package italiapizzasystem.controlador;

import javafx.fxml.FXMLLoader;
import italiapizzasystem.ItaliaPizzaSystem;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import italiapizzasystem.persistencia.pojo.Producto;
import italiapizzasystem.persistencia.dao.ProductoDAO;
import italiapizzasystem.utilidad.Utilidad;
import javafx.scene.control.Alert;

import java.io.File;
import java.nio.file.Files;
import javafx.stage.FileChooser;


/**
 * FXML Controller class
 *
 * @author Gerardo
 */
public class FXMLActualizarProductoController implements Initializable {

    @FXML
    private TextField tf_Nombre;
    @FXML
    private TextArea ta_Descripcion;
    @FXML
    private TextArea ta_Restricciones;
    @FXML
    private Label lb_CamposInvalidos;
    @FXML
    private Label lb_NombreInvalido;
    @FXML
    private Label lb_DescripcionInvalido;
    @FXML
    private Label lb_RestriccionInvalida;
    @FXML
    private TextField tf_Precio;
    @FXML
    private TextField tf_Cantidad;
    @FXML
    private Label lb_PrecioInvalido;
    @FXML
    private Label lb_CantidadInvalida;
    
    private Producto productoEditar;
    
    private File archivoImagen;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarDatos(Producto producto) {
    
    productoEditar = producto;

    tf_Nombre.setText(
            producto.getNombre()
    );

    ta_Descripcion.setText(
            producto.getDescripcion()
    );

    ta_Restricciones.setText(
            producto.getRestricciones()
    );

    tf_Precio.setText(
            String.valueOf(
                    producto.getPrecio()
            )
    );

    tf_Cantidad.setText(
            String.valueOf(
                    producto.getCantidad()
            )
    );
    }

   @FXML
   private void btn_ClicCancelar(ActionEvent event) {

    try {

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

        escenarioBase.show();

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
    private void btn_ClicSubirArchivo(ActionEvent event) {
       
    FileChooser selector = new FileChooser();

    File archivo =
        selector.showOpenDialog(
                tf_Nombre
                .getScene()
                .getWindow()
        );

    if (archivo != null) {

    archivoImagen = archivo;

    Utilidad.mostrarAlertaSimple(
            Alert.AlertType.INFORMATION,
            "Imagen seleccionada",
            archivo.getName()
        );
      }
    }

    @FXML
    private void btn_ClicActualizarDatos(ActionEvent event) {

    try {

        productoEditar.setNombre(
                tf_Nombre.getText()
        );

        productoEditar.setDescripcion(
                ta_Descripcion.getText()
        );

        productoEditar.setRestricciones(
                ta_Restricciones.getText()
        );

        productoEditar.setPrecio(
                Double.parseDouble(
                        tf_Precio.getText()
                )
        );

        productoEditar.setCantidad(
                Integer.parseInt(
                        tf_Cantidad.getText()
                )
        );
        
        if (archivoImagen != null) {

        productoEditar.setFoto(
            Files.readAllBytes(
                    archivoImagen.toPath()
            )
          );
        }

        boolean actualizado =
                ProductoDAO.actualizarProducto(
                        productoEditar
                );
        
        if (actualizado) {

            Utilidad.mostrarAlertaSimple(
                    Alert.AlertType.INFORMATION,
                    "Éxito",
                    "Producto actualizado correctamente"
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

            escenarioBase.show();

        }

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
    private void btn_ClicEliminarProducto(ActionEvent event) {

    try {

        boolean eliminado =
                ProductoDAO.eliminarProducto(
                        productoEditar.getIdProducto()
                );

        if (eliminado) {

        Utilidad.mostrarAlertaSimple(
            Alert.AlertType.INFORMATION,
            "Éxito",
            "Producto eliminado correctamente"
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

        escenarioBase.show();
        }

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
