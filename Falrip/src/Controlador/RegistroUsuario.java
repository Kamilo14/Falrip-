package Controlador;

import bd.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RegistroUsuario {

    /**
     * Valida si un usuario y clave existen en la tabla USUARIOS_APP.
     * @param username El nombre de usuario (de la columna NOMBRE_USUARIO).
     * @param clave La contraseña (de la columna CLAVE).
     * @return true si el login es correcto, false si no.
     */
    public boolean validarUsuario(String username, String clave) {
        
        // Esta consulta coincide perfectamente con tu tabla
        String sql = "SELECT COUNT(*) AS total FROM USUARIOS_APP " +
                     "WHERE NOMBRE_USUARIO = ? AND CLAVE = ?";
        
        try (Connection cnx = new Conexion().obtenerConexion();
             PreparedStatement stmt = cnx.prepareStatement(sql)) {
            
            stmt.setString(1, username);
            stmt.setString(2, clave); 
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0; // true si encuentra 1
                }
            }
        } catch (SQLException ex) {
            System.err.println("Error al validar usuario: " + ex.getMessage());
            ex.printStackTrace();
        }
        return false; // Falla por defecto
    }
}