package Controlador;

import Modelo.AuditoriaCliente;
import Modelo.AuditoriaTrans;
import bd.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class RegistroAuditoria {

    /**
     * Esta es la función #1 que pediste:
     * Obtiene todos los registros de la tabla AUDITORIA_CLIENTE.
     * @return una lista de objetos AuditoriaCliente.
     */
    public List<AuditoriaCliente> listarAuditoriaCliente() {
        List<AuditoriaCliente> lista = new ArrayList<>();
        
        String sql = "SELECT ID_AUDITORIA, USUARIO_APP, FECHA_EVENTO, TIPO_OPERACION, DESCRIPCION " +
                     "FROM AUDITORIA_CLIENTE ORDER BY FECHA_EVENTO DESC";

        try (Connection cnx = new Conexion().obtenerConexion();
             PreparedStatement stmt = cnx.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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

        } catch (SQLException e) {
            System.err.println("Error al listar auditoría de AUDITORIA_CLIENTE: " + e.getMessage());
            e.printStackTrace();
        }
        
        return lista; // Devuelve la lista de AuditoriaCliente
    }

    /**
     * Esta es la función #2 que pediste:
     * Obtiene todos los registros de la tabla AUDITORIA_TRANSACCION.
     * @return una lista de objetos AuditoriaTrans.
     */
    public List<AuditoriaTrans> listarAuditoriaTransaccion() {
        List<AuditoriaTrans> lista = new ArrayList<>();
        
        String sql = "SELECT ID_AUDITORIA, USUARIO_APP, FECHA_EVENTO, TIPO_OPERACION, DESCRIPCION " +
                     "FROM AUDITORIA_TRANSACCION ORDER BY FECHA_EVENTO DESC";

        try (Connection cnx = new Conexion().obtenerConexion();
             PreparedStatement stmt = cnx.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

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

        } catch (SQLException e) {
            System.err.println("Error al listar auditoría de AUDITORIA_TRANSACCION: " + e.getMessage());
            e.printStackTrace();
        }
        
        return lista; // Devuelve la lista de AuditoriaTrans
    }
}