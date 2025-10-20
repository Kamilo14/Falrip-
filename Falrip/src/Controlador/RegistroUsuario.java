package Controlador;

import bd.Conexion;
import java.sql.Connection;
import java.sql.CallableStatement;

import java.sql.SQLException;
import java.sql.Types;

public class RegistroUsuario {

    /**
     * Valida si un usuario y clave existen en la tabla USUARIOS_APP.
     * @param username El nombre de usuario (de la columna NOMBRE_USUARIO).
     * @param clave La contraseña (de la columna CLAVE).
     * @return true si el login es correcto, false si no.
     */
    public boolean validarUsuario(String username, String clave) {
    
    // 1. CAMBIO: El SQL ahora es una llamada a una FUNCIÓN del paquete
    // {? = CALL ...} El '?' del inicio es para el valor de RETORNO
    String sql = "{? = CALL PKG_USUARIOS.F_VALIDAR_USUARIO(?, ?)}";
    boolean isValid = false;
    
    // 2. CAMBIO: Se usa CallableStatement
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {
         
        // 3. NUEVO: Registramos el parámetro de SALIDA (el retorno de la función)
        cstmt.registerOutParameter(1, Types.INTEGER); // La función devuelve un NUMBER (1 o 0)
        
        // 4. NUEVO: Asignamos los parámetros de ENTRADA
        cstmt.setString(2, username); // p_username
        cstmt.setString(3, clave);    // p_clave
        
        // 5. NUEVO: Ejecutamos la función
        cstmt.execute();
        
        // 6. NUEVO: Obtenemos el valor de retorno (del parámetro 1)
        int result = cstmt.getInt(1);
        
        // 7. CAMBIO: Comparamos el resultado (1 = válido, 0 = inválido)
        if (result == 1) {
            isValid = true;
        }
        
    } catch (SQLException ex) {
        // 8. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar F_VALIDAR_USUARIO: " + ex.getMessage());
        ex.printStackTrace();
    } catch (Exception e) {
        System.err.println("Error desconocido al ejecutar F_VALIDAR_USUARIO: " + e.getMessage());
        e.printStackTrace();
    }
    
    return isValid; // Devuelve true si result fue 1, false en cualquier otro caso
}
}