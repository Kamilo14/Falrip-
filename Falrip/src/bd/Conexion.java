package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import Vista.Jfrm_Menu;
import java.sql.CallableStatement; 
import java.sql.SQLException;      

/**
 *
 * @author USRVI-LC4
 */
public class Conexion {

    private final String URL_CONEXION = 
        "jdbc:oracle:thin:@basededatoaplicada2_high?TNS_ADMIN=/Users/Basaes/Downloads/Wallet_BASEDEDATOAPLICADA2";

    private final String USUARIO = "FALRIP"; 
    private final String CLAVE = "DuocEmpresa01.";
    private final String DRIVER_CLASS = "oracle.jdbc.OracleDriver";

    public Connection obtenerConexion() {
        
        Connection conexion = null;
        
        try {
            Class.forName(DRIVER_CLASS);
            conexion = DriverManager.getConnection(URL_CONEXION, USUARIO, CLAVE);
            System.out.println("Conexion exitosa");

            // --- INICIO DE CÓDIGO DE AUDITORÍA ---
            
            // ¡¡ESTA ES LA LÍNEA QUE CAMBIÓ!!
            // Ahora llama al procedimiento de Oracle que tu trigger entiende.
            try (CallableStatement cstmt = conexion.prepareCall("{CALL DBMS_SESSION.SET_IDENTIFIER(?)}")) {
                
                // Leemos la variable estática que guardamos en Jfrm_Menu
                cstmt.setString(1, Jfrm_Menu.usuarioLogueado);
                cstmt.execute();
                
            } catch (SQLException eAudit) {
                System.err.println("Error al setear CLIENT_IDENTIFIER de auditoría: " + eAudit.getMessage());
            }
            // --- FIN DE CÓDIGO DE AUDITORÍA ---

        } catch (Exception e) {
            System.out.println("No se logro conectar a la BD");
            e.printStackTrace();
        }
        
        return conexion;
    }
}