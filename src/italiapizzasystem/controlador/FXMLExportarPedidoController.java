
package italiapizzasystem.controlador;

import italiapizzasystem.excepciones.ExportarDocumentoException;
import italiapizzasystem.persistencia.pojo.PedidoCliente;
import italiapizzasystem.utilidad.EfectoBotones;
import italiapizzasystem.utilidad.EscritorCSV;
import italiapizzasystem.utilidad.EscritorPDF;
import italiapizzasystem.utilidad.Navegador;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author acrca
 */
public class FXMLExportarPedidoController implements Initializable {

    @FXML
    private Button btn_Cancelar;
    @FXML
    private Button btn_ExportarAPDF;
    @FXML
    private Button btn_ExportarACSV;
    
    private List<PedidoCliente> pedidosParaExportar;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EfectoBotones.darEfectoBotones(btn_Cancelar, btn_ExportarAPDF, btn_ExportarACSV);
    }    
    
    public void inicializarDatos(List<PedidoCliente> listaPedidos) {
        this.pedidosParaExportar = listaPedidos;
    }

    @FXML
    private void clic_btnExportarAPDF(ActionEvent event) {
        if (validarListaPedidos()) {
            FileChooser selectorArchivos = crearConfiguradorSelector("Reporte de Pedidos PDF", "*.pdf");
            Stage escenarioActual = (Stage) btn_ExportarAPDF.getScene().getWindow();
            File archivoSeleccionado = selectorArchivos.showSaveDialog(escenarioActual);

            procesarExportacionPDF(archivoSeleccionado);
        }
    }

    @FXML
    private void clic_btnExportarACSV(ActionEvent event) {
        if (validarListaPedidos()) {
            FileChooser selectorArchivos = crearConfiguradorSelector("Reporte de Pedidos CSV", "*.csv");
            Stage escenarioActual = (Stage) btn_ExportarACSV.getScene().getWindow();
            File archivoSeleccionado = selectorArchivos.showSaveDialog(escenarioActual);

            procesarExportacionCSV(archivoSeleccionado);
        }
    }

    private boolean validarListaPedidos() {
        boolean esValida = (pedidosParaExportar != null && !pedidosParaExportar.isEmpty());
        if (!esValida) {
            mostrarAlerta(Alert.AlertType.WARNING, "Sin datos", "No hay registros de pedidos disponibles para ser exportados en este momento.");
        }
        return esValida;
    }

    private FileChooser crearConfiguradorSelector(String descripcion, String extension) {
        FileChooser selector = new FileChooser();
        selector.setTitle("Guardar Reporte");
        selector.setInitialFileName("Reporte_Pedidos_" + System.currentTimeMillis());
        selector.getExtensionFilters().add(new FileChooser.ExtensionFilter(descripcion, extension));
        return selector;
    }

    private void procesarExportacionPDF(File archivo) {
        if (archivo != null) {
            try {
                EscritorPDF.exportarPedidos(pedidosParaExportar, archivo);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Exportación Exitosa", "El reporte en formato PDF ha sido generado correctamente.");
                cerrarVentana();
            } catch (ExportarDocumentoException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Exportación", ex.getMessage());
            }
        }
    }

    private void procesarExportacionCSV(File archivo) {
        if (archivo != null) {
            try {
                EscritorCSV.exportarPedidos(pedidosParaExportar, archivo);
                mostrarAlerta(Alert.AlertType.INFORMATION, "Exportación Exitosa", "El reporte en formato CSV ha sido generado correctamente.");
                cerrarVentana();
            } catch (ExportarDocumentoException ex) {
                mostrarAlerta(Alert.AlertType.ERROR, "Error de Exportación", ex.getMessage());
            }
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
    
    private void cerrarVentana() {
        Stage escenarioModal = (Stage) btn_Cancelar.getScene().getWindow();
        escenarioModal.close();
    }
    
    @FXML
    private void btn_ClicCancelar(ActionEvent event) {
        Stage escenarioModal = (Stage) btn_Cancelar.getScene().getWindow();
        escenarioModal.close();
    }
}
