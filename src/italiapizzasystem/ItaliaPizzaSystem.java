
package italiapizzasystem;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Gerardo
 */
public class ItaliaPizzaSystem extends Application {
    
    @Override
    public void start(Stage stage) throws IOException {
        
       Parent root = FXMLLoader.load(getClass().getResource("vista/FXMLLogin.fxml"));
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);  
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    
}
