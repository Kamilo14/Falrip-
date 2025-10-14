   
package bd;
import java.sql.Connection;
import java.sql.DriverManager;

/**
 *
 * @author USRVI-LC4
 */
public class Conexion {
    
    private final String URL_CONEXION = 
        "jdbc:oracle:thin:@";
    
    private final String USUARIO = ""; 
    
    private final String CLAVE = ".";
    
    // Clase del Driver JDBC (necesaria para versiones anteriores de Java, pero buena práctica)
    private final String DRIVER_CLASS = "oracle.jdbc.OracleDriver";
    
    public Connection obtenerConexion() {
        
        Connection conexion = null;
        
        try {
            Class.forName(DRIVER_CLASS);//oracle.jdbc.driver.OracleDriver
            conexion=DriverManager.getConnection(URL_CONEXION, USUARIO, CLAVE);
            
            
            System.out.println("Conexion exitosa");
        } catch (Exception e) {
            System.out.println("No se logro conectar a la BD");
        }
        
        return conexion;
        
    }
    
    
    
}

    

