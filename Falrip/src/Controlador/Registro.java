/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controlador;

import Modelo.Cliente;
import Modelo.Comuna;
import Modelo.ProfesionOficio;
import Modelo.Provincia;
import Modelo.Region;
import Modelo.TipoCliente;
import bd.Conexion;
import java.beans.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.sql.ResultSet;

/**
 *
 * @author CAMILO
 */
public class Registro {
    
     public boolean agregar(Cliente cli) {
        
      
        String query = "INSERT INTO cliente (numrun, dvrun, pnombre, snombre, appaterno, apmaterno, fecha_nacimiento, fecha_inscripcion,correo,fonocontancto, direccion,id_region,id_provincia, id_comuna, id_profesion_oficio, id_tipo_cliente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?)";

        try (
                Connection cnx = new Conexion().obtenerConexion();
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
            stmt.setInt(12, cli.getReg().getCodRegion());
            stmt.setInt(13, cli.getProv().getCodProvincia()); 
            stmt.setInt(14, cli.getCom().getCodComuna());   
            stmt.setInt(15, cli.getProf().getCodProfOfic());
            stmt.setInt(16, cli.getTipocl().getCodTipoCliente());

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
     
     
   public List<Cliente>listarCliente()
   {
       List<Cliente>listaCliente= new ArrayList<>();
       
       try {
           Conexion conexion= new Conexion();
           Connection cnx = conexion.obtenerConexion();
           
           
           //query
           String query= "SELECT * FROM CLIENTE";
           PreparedStatement stmt =cnx.prepareStatement(query);
           
           ResultSet rs = stmt.executeQuery();
           while (rs.next()){
               Cliente cli = new Cliente();

            // --- POBLANDO EL OBJETO CLIENTE ORDENADAMENTE ---

            // 1. Datos directos del cliente
            cli.setRun(rs.getInt("numrun"));
            cli.setDvrun(rs.getString("dvrun"));
            cli.setPnombre(rs.getString("pnombre"));
            cli.setSnombre(rs.getString("snombre"));
            cli.setAppaterno(rs.getString("appaterno"));
            cli.setApmaterno(rs.getString("apmaterno"));

            // 2. Fechas
            cli.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
            cli.setFechaInscripcion(rs.getDate("fecha_inscripcion"));

            // 3. Datos de contacto y dirección
            cli.setCorreo(rs.getString("correo"));
            cli.setFonoContacto(rs.getInt("fonocontacto"));
            cli.setDireccion(rs.getString("direccion"));

            // 4. Objetos compuestos (Claves Foráneas)
            
            Region region = new Region();
            region.setCodRegion(rs.getInt("id_region"));
            region.setNombreRegion(rs.getString("nom_region"));

            Provincia provincia = new Provincia();
            provincia.setCodProvincia(rs.getInt("id_provincia"));
            provincia.setNombreProvincia(rs.getString("nom_provincia"));
            
            Comuna comuna = new Comuna();
            comuna.setCodComuna(rs.getInt("id_comuna"));
            comuna.setNombreComuna(rs.getString("nom_comuna"));

            ProfesionOficio profesion = new ProfesionOficio();
            profesion.setCodProfOfic(rs.getInt("id_profesion_oficio"));
            profesion.setNombreProfesion(rs.getString("desc_profesion_oficio"));

            TipoCliente tipoCliente = new TipoCliente();
            tipoCliente.setCodTipoCliente(rs.getInt("id_tipo_cliente"));
            tipoCliente.setNombreTipoCliente(rs.getString("desc_tipo_cliente"));

            //asignamos al cliente principal.
            cli.setReg(region);
            cli.setProv(provincia);
            cli.setCom(comuna);
            cli.setProf(profesion);
            cli.setTipocl(tipoCliente);
            
            
            listaCliente.add(cli);
               
            
              
              
               
               
           }
       } catch (Exception e) {
           System.out.println("Error SQL al listar clientes");
       }
       return listaCliente;
       
   }
   
   public boolean actualizar(Cliente cliente){
       
       try {
           Conexion conexion= new Conexion();
           Connection cnx = conexion.obtenerConexion();
           
           
           //query
           String query= "UPDATE cliente SET pnombre = ?, snombre = ?, appaterno = ?, apmaterno = ?, \" +\n" +
"                       \"fecha_nacimiento = ?, correo = ?, fonocontacto = ?, direccion = ?, \" +\n" +
"                       \"id_region = ?, id_provincia = ?, id_comuna = ?, id_profesion_oficio = ?, id_tipo_cliente = ? \" +\n" +
"                       \"WHERE numrun = ?";
           PreparedStatement stmt =cnx.prepareStatement(query);
           
       } catch (Exception e) {
       }
   }
     
          
}
