package italiapizzasystem.persistencia;

import italiapizzasystem.controlador.FXMLLoginController;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gerardo
 */
public class ConexionBD {
    private static final String ARCHIVO_CONFIG = "config.properties";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver" ;
     private static final Logger LOGGER = Logger.getLogger(ConexionBD.class.getName());
    
    public static Connection abrirConexion(){
        Connection conexionBD = null; 
        Properties propiedades = new Properties();
        try(InputStream entrada = ConexionBD.class.getClassLoader().getResourceAsStream("config.properties")){ 
            propiedades.load(entrada);
            String urlConexion = String.format("jdbc:mysql://%s:%s/%s?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC",
                    propiedades.getProperty("db.ip"),
                    propiedades.getProperty("db.puerto"),
                    propiedades.getProperty("db.nombre"));
           Class.forName(DRIVER);
            conexionBD = DriverManager.getConnection(urlConexion, propiedades.getProperty("db.usuario"), 
                                                    propiedades.getProperty("db.contrasenia"));
        }catch (IOException ex) {
            System.err.println("Error al leer el archivo de configuración: " + ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al leer el archivo de configuración en ConexionBD", ex);
        }catch(ClassNotFoundException ex){
            System.err.printf("Error, Clase no encontrada"+ ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error al no encontrar la clase para la conexión en ConexionBD", ex);
        }catch(SQLException ex){    
            System.err.printf("Error en la conexión: " + ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error SQL en la clase ConexionBD", ex);
        }catch(Exception ex){
            System.err.print("Error general:"+ex.getMessage());
            LOGGER.log(Level.SEVERE, "Error general capturado en ConexionBD", ex);
        }
        return conexionBD; 
    }
}
