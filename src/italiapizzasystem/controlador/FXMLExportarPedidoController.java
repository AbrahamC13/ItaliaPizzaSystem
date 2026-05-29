/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package italiapizzasystem.controlador;

import italiapizzasystem.utilidad.EfectoBotones;
import italiapizzasystem.utilidad.Navegador;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        EfectoBotones.darEfectoBotones(btn_Cancelar, btn_ExportarAPDF, btn_ExportarACSV);
    }    

    @FXML
    private void clic_btnExportarAPDF(ActionEvent event) {
    }

    @FXML
    private void clic_btnExportarACSV(ActionEvent event) {
    }
    
    @FXML
    private void btn_ClicCancelar(ActionEvent event) {
        Navegador.cambiarVentana(btn_Cancelar, "vista/FXMLPedidos.fxml", "Pedidos");
    }
}
