/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Cliente;
import bd.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author CAMILO
 */
public class Registro {
    
     public boolean agregar(Cliente cli) {
        
      
        String query = "INSERT INTO cliente (numrun, dvrun, pnombre, snombre, appaterno, apmaterno, fecha_nacimiento, fecha_inscripcion,correo,fonocontancto direccion,id_region,id_provincia, id_comuna, id_profesion_oficio, id_tipo_cliente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection cnx = new Conexion().obtenerConexion();
             PreparedStatement stmt = cnx.prepareStatement(query)) {

         
            stmt.setInt(1, cli.getRun());
            stmt.setString(2, cli.getDvrun());
            stmt.setString(3, cli.getPnombre());
            stmt.setString(4, cli.getSnombre());
            stmt.setString(5, cli.getAppaterno());
            stmt.setString(6, cli.getApmaterno());
            
            
            // --- CORRECCIÓN 2: Manejo correcto de Fechas ---
            // Se convierte la fecha de java.util.Date a java.sql.Date
            stmt.setDate(7, new java.sql.Date(cli.getFechaNacimiento().getTime()));
            stmt.setDate(8, new java.sql.Date(cli.getFechaInscripcion().getTime()));
            stmt.setString(9, cli.getCorreo());
            stmt.setInt(10, cli.getFonoContacto());
            
            stmt.setString(11, cli.getDireccion());
            
            // --- CORRECCIÓN 3: Manejo de Claves Foráneas (Composición) ---
            
            // Obtenemos el ID del objeto Comuna que está DENTRO del objeto Cliente.
            // Es importante asegurarse en el controlador que estos objetos no sean nulos.
            
            stmt.setInt(12, cli.getReg().getCodRegion());
            stmt.setInt(13, cli.getProf().getCodProfOfic());
            stmt.setInt(14, cli.getTipocl().getCodTipoCliente());

            // Ejecutamos la inserción
            stmt.executeUpdate();
            
            return true; // Si todo salió bien, retornamos true.

        } catch (SQLException ex) {
            // Un mensaje de error más específico ayuda a depurar.
            System.err.println("Error en SQL al agregar cliente: " + ex.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error desconocido al agregar cliente: " + e.getMessage());
            return false;
        }
    }
    
    
}
