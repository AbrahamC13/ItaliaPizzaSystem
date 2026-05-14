
package italiapizzasystem;

import italiapizzasystem.utilidad.Utilidad;
import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

/**
 *
 * @author Gerardo
 */
public class ItaliaPizzaSystem extends Application {
    private static final Logger LOGGER = Logger.getLogger(ItaliaPizzaSystem.class.getName());
    
    @Override
    public void start(Stage stage) {
        try {
            //Creamos directorio de logs si no existe
            String logPath = System.getProperty("user.home") + "/italiapizza_logs/";
            File logDir = new File(logPath);
            if (!logDir.exists()) {
                logDir.mkdirs();
            }
            
            //Configuramos FileHandler
            FileHandler fileHandler = new FileHandler(logPath + "italiapizza.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            fileHandler.setLevel(Level.ALL);
            
            //Evitamos duplicados: remover FileHandler existente del rootLogger
            Logger rootLogger = Logger.getLogger("");
            Handler[] handlers = rootLogger.getHandlers();
            for (Handler handler : handlers) {
                if (handler instanceof FileHandler) {
                    rootLogger.removeHandler(handler);
                    handler.close();
                }
            }
            rootLogger.addHandler(fileHandler);
            rootLogger.setLevel(Level.INFO);
            
            //Cargamos la interfaz
            Parent root = FXMLLoader.load(getClass().getResource("vista/FXMLLogin.fxml"));
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al iniciar la aplicación", ex);
            // Opcional: mostrar un diálogo de error al usuario   
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "No se pudo iniciar la aplicación.Revide los logs.", ex.getMessage());
          
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}