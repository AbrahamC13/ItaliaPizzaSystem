
package italiapizzasystem.controlador;

import italiapizzasystem.excepciones.ImagenInvalidaException;
import italiapizzasystem.persistencia.pojo.Producto;
import italiapizzasystem.utilidad.EfectoBotones;
import italiapizzasystem.utilidad.GestorImagen;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author acrca
 */
public class FXMLItemProductoController implements Initializable {

    @FXML
    private ImageView img_ImagenProducto;
    @FXML
    private AnchorPane ap_DescripcionCompletaProducto;
    @FXML
    private Label lb_NombreProducto;
    @FXML
    private Label lb_CodigoProducto;
    @FXML
    private Label lb_DescripcionProducto;
    @FXML
    private Label lb_RestriccionesProducto;
    @FXML
    private Label lb_PrecioProducto;
    @FXML
    private Button btn_Agregar;
    
    private Producto productoBase;
    private FXMLRealizarPedidoController controladorPrincipal;
    private FXMLEditarPedidoController contenedorEdicion;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void inicializarTarjeta(Producto producto, FXMLRealizarPedidoController principal){
        this.productoBase = producto;
        this.controladorPrincipal = principal;
        
        lb_NombreProducto.setText(producto.getNombre());
        lb_CodigoProducto.setText("#" + producto.getCodigoProducto());
        lb_DescripcionProducto.setText(producto.getDescripcion() != null ? producto.getDescripcion(): "Sin descripcion.");
        lb_RestriccionesProducto.setText(producto.getRestricciones() != null ? producto.getRestricciones(): "Ninguna");
        lb_PrecioProducto.setText(String.format("$%.2f", producto.getPrecio()));
        try {
            Image imagenDelProducto = GestorImagen.convertirBytesAImagen(producto.getFoto());

            if (imagenDelProducto != null) {
                img_ImagenProducto.setImage(imagenDelProducto);
            } else {
                GestorImagen.cargarImagenDefault(img_ImagenProducto);
            }
        } catch (ImagenInvalidaException ex) {
            System.err.println("Alerta Visual: " + ex.getMessage());
            GestorImagen.cargarImagenDefault(img_ImagenProducto);
        }
        EfectoBotones.darEfectoBotones(btn_Agregar);
    } 
    
    public void inicializarTarjeta(Producto producto, FXMLEditarPedidoController edicion){
        this.productoBase = producto;
        this.contenedorEdicion = edicion;

        lb_NombreProducto.setText(producto.getNombre());
        lb_CodigoProducto.setText("#" + producto.getCodigoProducto());
        lb_DescripcionProducto.setText(producto.getDescripcion() != null ? producto.getDescripcion(): "Sin descripcion.");
        lb_RestriccionesProducto.setText(producto.getRestricciones() != null ? producto.getRestricciones(): "Ninguna");
        lb_PrecioProducto.setText(String.format("$%.2f", producto.getPrecio()));
        try {
            Image imagenDelProducto = GestorImagen.convertirBytesAImagen(producto.getFoto());

            if (imagenDelProducto != null) {
                img_ImagenProducto.setImage(imagenDelProducto);
            } else {
                GestorImagen.cargarImagenDefault(img_ImagenProducto);
            }
        } catch (ImagenInvalidaException ex) {
            System.err.println("Alerta Visual: " + ex.getMessage());
            GestorImagen.cargarImagenDefault(img_ImagenProducto);
        }
        EfectoBotones.darEfectoBotones(btn_Agregar);
    }

    @FXML
    private void btn_clicAgregar(ActionEvent event) {
        if (this.controladorPrincipal != null) {
            this.controladorPrincipal.agregarProductoAOrden(this.productoBase);
        } else if (this.contenedorEdicion != null) {
            this.contenedorEdicion.agregarProductoAOrden(this.productoBase);
        }
    }
    
}
