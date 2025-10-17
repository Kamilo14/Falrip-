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
        
      
        String query = "INSERT INTO cliente (numrun, dvrun, pnombre, snombre, appaterno, apmaterno, fecha_nacimiento, fecha_inscripcion,correo,fono_contancto, direccion,cod_region,cod_provincia, cod_comuna, cod_profesion_oficio, cod_tipo_cliente,categoria_cliente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

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
     
     
   public List<Cliente> listarCliente() {
    List<Cliente> listaCliente = new ArrayList<>();
    
    try {
        Conexion conexion = new Conexion();
        Connection cnx = conexion.obtenerConexion();
        
        // Query CORREGIDA 
        String query = "SELECT NUMRUN, DVRUN, PNOMBRE, SNOMBRE, APPATERNO, APMATERNO, " +
               "NUMRUN||'-'||DVRUN AS RUT, " +                    
               "PNOMBRE||' '||SNOMBRE||' '||APPATERNO||' '||APMATERNO AS NOMBRES, " +
               "FECHA_NACIMIENTO, FECHA_INSCRIPCION, NVL(CORREO,'NO TIENE CORREO')AS CORREO,FONO_CONTACTO, DIRECCION, " +
               "COD_REGION, COD_PROVINCIA, COD_COMUNA, COD_PROF_OFIC, COD_TIPO_CLIENTE, " +
               "CATEGORIA_CLIENTE " +
               "FROM cliente ORDER BY numrun";
        
        PreparedStatement stmt = cnx.prepareStatement(query);
        ResultSet rs = stmt.executeQuery();
        
        
        while (rs.next()) {
            
            Cliente cli = new Cliente();

            // Mapear todas las columnas del select respectivo en la query creada es decir tiene que ser el mismo nombre de la consulta para evitar errores
            cli.setRun(rs.getInt("numrun"));
            cli.setDvrun(rs.getString("dvrun"));
            cli.setPnombre(rs.getString("pnombre"));
            cli.setSnombre(rs.getString("snombre"));
            cli.setAppaterno(rs.getString("appaterno"));
            cli.setApmaterno(rs.getString("apmaterno"));
            cli.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
            cli.setFechaInscripcion(rs.getDate("fecha_inscripcion"));
            cli.setCorreo(rs.getString("correo"));
            cli.setFonoContacto(rs.getInt("fono_contacto"));
            cli.setDireccion(rs.getString("direccion"));
            

            // Objetos compuestos 
            Region region = new Region();
            region.setCodRegion(rs.getInt("cod_region"));

            Provincia provincia = new Provincia();
            provincia.setCodProvincia(rs.getInt("cod_provincia"));
            
            Comuna comuna = new Comuna();
            comuna.setCodComuna(rs.getInt("cod_comuna"));

            ProfesionOficio profesion = new ProfesionOficio();
            profesion.setCodProfOfic(rs.getInt("cod_prof_ofic"));

            TipoCliente tipoCliente = new TipoCliente();
            tipoCliente.setCodTipoCliente(rs.getInt("cod_tipo_cliente"));

            // Asignar al cliente
            cli.setReg(region);
            cli.setProv(provincia);
            cli.setCom(comuna);
            cli.setProf(profesion);
            cli.setTipocl(tipoCliente);
            cli.setCategoria(rs.getString("categoria_cliente"));
            
            listaCliente.add(cli);
        }
        
        
        
        rs.close();
        stmt.close();
        cnx.close();
        
    } catch (SQLException e) {
        System.err.println("Error SQL al listar clientes: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println(" Error desconocido al listar clientes: " + e.getMessage());
        e.printStackTrace();
    }
    
    return listaCliente;
}

   
   public boolean actualizar(Cliente cli){
       
       try {
           Conexion conexion= new Conexion();
           Connection cnx = conexion.obtenerConexion();
           
           
           //query
           String query= "UPDATE cliente SET pnombre = ?, snombre = ?, appaterno = ?, apmaterno = ?, \" +\n" +
"                       \"fecha_nacimiento = ?, correo = ?, fono_contacto = ?, direccion = ?, \" +\n" +
"                       \"cod_region = ?, cod_provincia = ?, cod_comuna = ?, cod_prof_ofic = ?, cod_tipo_cliente = ? \" +\n" +
"                       \"WHERE numrun = ?";
           
           PreparedStatement stmt =cnx.prepareStatement(query);
           
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
            
            stmt.executeUpdate();
            stmt.close();
            cnx.close();
            
            return true;
           
       } catch (SQLException ex) {
            //Logger
            System.out.println("Error en SQL al actualizar cliente " + ex.getMessage());
            return false;
        }
        catch(Exception e){
            System.out.println("Error en el método actualizar cliente " + e.getMessage());
            return false;
        }
    }
     
   
   public boolean eliminar(int run){
       try {
           Conexion conexion = new Conexion();
           Connection cnx = conexion.obtenerConexion();
           
           //query
           String query = "DELETE FROM CLIENTE WHERE run = ?";
           PreparedStatement stmt = cnx.prepareStatement(query);
           
           stmt.setInt(1,run);
           
           stmt.executeUpdate();
           stmt.close();
           cnx.close();
           
           return true;
           
         } catch (SQLException ex) {
            //Logger
            System.out.println("Error en SQL al eliminar cliente " + ex.getMessage());
            return false;
        }
        catch(Exception e){
            System.out.println("Error en el método eliminar cliente " + e.getMessage());
            return false;
        }
          
}
public Cliente buscarPorRun(int run) {
    Cliente cli = null; // Empezamos con el cliente como nulo.

    try {
        Conexion conexion = new Conexion();
        Connection cnx = conexion.obtenerConexion();

       
        String query = "SELECT * FROM CLIENTE WHERE numrun = ?"; 
        PreparedStatement stmt = cnx.prepareStatement(query);

        // Asignamos el valor del 'run' al primer parámetro (?) de la consulta.
        stmt.setInt(1, run);

        ResultSet rs = stmt.executeQuery();

        // Usamos if en lugar de while, porque esperamos un solo resultado.
        if (rs.next()) {
            cli = new Cliente(); // Solo creamos el objeto si encontramos un resultado.
            Region reg = new Region();
            Provincia prov = new Provincia();
            Comuna com = new Comuna();
             ProfesionOficio profesion = new ProfesionOficio();
             TipoCliente tipoCliente = new TipoCliente();
            

            // --- POBLANDO EL OBJETO CLIENTE (es el mismo código de tu método listar) ---
            cli.setRun(rs.getInt("numrun"));
            cli.setDvrun(rs.getString("dvrun"));
            cli.setPnombre(rs.getString("pnombre"));
            cli.setAppaterno(rs.getString("appaterno"));
            cli.setApmaterno(rs.getString("apmaterno"));
            cli.setFechaNacimiento(rs.getDate("fecha_nacimiento"));
            cli.setFechaInscripcion(rs.getDate("fecha_inscripcion"));
            cli.setCorreo(rs.getString("correo"));
            cli.setFonoContacto(rs.getInt("fono_contacto"));
            cli.setDireccion(rs.getString("direccion"));
            //Foraneas 
            reg.setCodRegion(rs.getInt("cod_region"));
            prov.setCodProvincia(rs.getInt("cod_provincia")); 
            com.setCodComuna(rs.getInt("cod_comuna")); 
            
            profesion.setCodProfOfic(rs.getInt("cod_prof_ofic")) ;
            tipoCliente.setCodTipoCliente(rs.getInt("cod_tipo_cliente"));
            cli.setCategoria(rs.getString("categoria_cliente"));
            
            cli.setReg(reg);
            cli.setProv(prov);
            cli.setCom(com);
            cli.setProf(profesion);
            cli.setTipocl(tipoCliente);

            
            
            
          
            
            
            
            
            
            
          
        }

        rs.close();
        stmt.close();
        cnx.close();

    } catch (Exception e) {
        System.out.println("Error SQL al buscar cliente por RUN: " + e.getMessage());
    }
    
    return cli; // Devuelve el cliente encontrado o 'null' si no se encontró.
}

}
