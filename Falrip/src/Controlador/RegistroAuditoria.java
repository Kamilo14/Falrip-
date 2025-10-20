package Controlador;

import Modelo.AuditoriaCliente;
import Modelo.AuditoriaTrans;
import bd.Conexion;
import java.sql.Connection;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RegistroAuditoria {

    /**
     * 
     * Obtiene todos los registros de la tabla AUDITORIA_CLIENTE.
     * @return una lista de objetos AuditoriaCliente.
     */
    public List<AuditoriaCliente> listarAuditoriaCliente() {
    List<AuditoriaCliente> lista = new ArrayList<>();
    
    // 1. CAMBIO: El SQL ahora es una llamada al procedimiento del paquete
    String sql = "{CALL PKG_AUDITORIA.SP_LISTAR_AUDITORIA_CLIENTE(?)}"; 

    // 2. CAMBIO: Se usa CallableStatement
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. NUEVO: Registramos el parámetro de SALIDA (el cursor)
        cstmt.registerOutParameter(1, java.sql.Types.REF_CURSOR); 

        // 4. NUEVO: Ejecutamos el procedimiento
        cstmt.execute();

        // 5. NUEVO: Obtenemos el cursor como un ResultSet (del parámetro 1)
        try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
            
            // 6. SIN CAMBIOS: Este bucle 'while' es idéntico al que tenías
            while (rs.next()) {
                // Leemos los datos de la fila
                int id = rs.getInt("ID_AUDITORIA");
                String usuario = rs.getString("USUARIO_APP");
                Timestamp fecha = rs.getTimestamp("FECHA_EVENTO");
                String tipo = rs.getString("TIPO_OPERACION");
                String desc = rs.getString("DESCRIPCION");

                // Creamos un objeto AuditoriaCliente y lo añadimos a la lista
                AuditoriaCliente audit = new AuditoriaCliente(id, usuario, fecha, tipo, desc);
                lista.add(audit);
            }
        } // 7. CAMBIO: El 'rs' se cierra solo aquí

    } catch (SQLException e) {
        // 8. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar SP_LISTAR_AUDITORIA_CLIENTE: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Error desconocido al ejecutar SP_LISTAR_AUDITORIA_CLIENTE: " + e.getMessage());
        e.printStackTrace();
    }
    
    return lista; // Devuelve la lista de AuditoriaCliente
}

    /**
     * Obtiene todos los registros de la tabla AUDITORIA_TRANSACCION.
     * @return una lista de objetos AuditoriaTrans.
     */
    public List<AuditoriaTrans> listarAuditoriaTransaccion() {
    List<AuditoriaTrans> lista = new ArrayList<>();
    
    // 1. CAMBIO: El SQL ahora es una llamada al procedimiento del paquete
    String sql = "{CALL PKG_AUDITORIA.SP_LISTAR_AUDITORIA_TRANS(?)}"; 

    // 2. CAMBIO: Se usa CallableStatement
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. NUEVO: Registramos el parámetro de SALIDA (el cursor)
        cstmt.registerOutParameter(1, java.sql.Types.REF_CURSOR); 

        // 4. NUEVO: Ejecutamos el procedimiento
        cstmt.execute();

        // 5. NUEVO: Obtenemos el cursor como un ResultSet (del parámetro 1)
        try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
            
            // 6. SIN CAMBIOS: Este bucle 'while' es idéntico al que tenías
            while (rs.next()) {
                // Leemos los datos de la fila
                int id = rs.getInt("ID_AUDITORIA");
                String usuario = rs.getString("USUARIO_APP");
                Timestamp fecha = rs.getTimestamp("FECHA_EVENTO");
                String tipo = rs.getString("TIPO_OPERACION");
                String desc = rs.getString("DESCRIPCION");

                // Creamos un objeto AuditoriaTrans y lo añadimos a la lista
                AuditoriaTrans audit = new AuditoriaTrans(id, usuario, fecha, tipo, desc);
                lista.add(audit);
            }
        } // 7. CAMBIO: El 'rs' se cierra solo aquí

    } catch (SQLException e) {
        // 8. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar SP_LISTAR_AUDITORIA_TRANS: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Error desconocido al ejecutar SP_LISTAR_AUDITORIA_TRANS: " + e.getMessage());
        e.printStackTrace();
    }
    
    return lista; // Devuelve la lista de AuditoriaTrans
}
}