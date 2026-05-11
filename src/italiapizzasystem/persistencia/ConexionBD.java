package italiapizzasystem.persistencia;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 *
 * @author Gerardo
 */
public class ConexionBD {
    private static final String ARCHIVO_CONFIG = "config.properties";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver" ;
    
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
        }catch (IOException e) {
            System.err.println("Error al leer el archivo de configuración: " + e.getMessage());
        }catch(ClassNotFoundException e){
            e.printStackTrace();
            System.err.printf("Error, Clase no encontrada");
        }catch(SQLException s){
            s.printStackTrace();
            System.err.printf("Error en la conexión: " + s.getMessage());
        }
        return conexionBD; 
    }
}
