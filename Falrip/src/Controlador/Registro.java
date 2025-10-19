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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.ResultSet;
import java.sql.Types;

/**
 *
 * @author CAMILO
 */
public class Registro {
    
     public boolean agregarCliente(Cliente cli) {
        
      
        String query = "INSERT INTO cliente (numrun, dvrun, pnombre, snombre, appaterno, apmaterno, fecha_nacimiento, fecha_inscripcion,correo,fono_contacto, direccion,cod_region,cod_provincia, cod_comuna, cod_prof_ofic, cod_tipo_cliente,categoria_cliente) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?)";

        try (
                Connection cnx = new Conexion().obtenerConexion();
             PreparedStatement stmt = cnx.prepareStatement(query)) {

         
            stmt.setInt(1, cli.getRun());
            stmt.setString(2, cli.getDvrun());
            stmt.setString(3, cli.getPnombre());
            if (cli.getSnombre() == null || cli.getSnombre().trim().isEmpty()) {
            stmt.setNull(4, java.sql.Types.VARCHAR);
        } else {
            stmt.setString(4, cli.getSnombre());
        }

        stmt.setString(5, cli.getAppaterno());

        // *** CORREGIDO: Manejo de NULL para APMATERNO ***
        if (cli.getApmaterno() == null || cli.getApmaterno().trim().isEmpty()) {
            stmt.setNull(6, java.sql.Types.VARCHAR);
        } else {
            stmt.setString(6, cli.getApmaterno());
        }
            
            
            // --- CORRECCIÓN 2: Manejo correcto de Fechas ---
            // Se convierte la fecha de java.util.Date a java.sql.Date
            stmt.setDate(7, new java.sql.Date(cli.getFechaNacimiento().getTime()));
            stmt.setDate(8, new java.sql.Date(cli.getFechaInscripcion().getTime()));
            stmt.setString(9, cli.getCorreo());
            stmt.setLong(10, cli.getFonoContacto());
            stmt.setString(11, cli.getDireccion());
            stmt.setInt(12, cli.getReg().getCodRegion());
            stmt.setInt(13, cli.getProv().getCodProvincia()); 
            stmt.setInt(14, cli.getCom().getCodComuna());   
            stmt.setInt(15, cli.getProf().getCodProfOfic());
            stmt.setInt(16, cli.getTipocl().getCodTipoCliente());
            stmt.setString(17, "Bronce");

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
        
        
        String query = "SELECT NUMRUN, DVRUN, PNOMBRE, SNOMBRE, APPATERNO, APMATERNO, " +
               "NUMRUN||'-'||DVRUN AS RUT, " +                    
               "PNOMBRE||' '||SNOMBRE||' '||APPATERNO||' '||APMATERNO AS NOMBRES, " +
               "FECHA_NACIMIENTO, FECHA_INSCRIPCION, NVL(CORREO,'NO TIENE CORREO')AS CORREO,FONO_CONTACTO, DIRECCION, " +
               "COD_REGION, COD_PROVINCIA, COD_COMUNA, COD_PROF_OFIC, COD_TIPO_CLIENTE, " +
               "CATEGORIA_CLIENTE " +
               "FROM cliente ORDER BY numrun"
                ;
        
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

   
   public boolean modificarCliente(Cliente cli){
       
       
           
           //query
          String query = "UPDATE cliente SET " +
                   "  pnombre = ?, snombre = ?, appaterno = ?, apmaterno = ?, " +             
                   "  fecha_nacimiento = ?, fecha_inscripcion = ?, correo = ?, " +         
                   "  fono_contacto = ?, direccion = ?, cod_region = ?, cod_provincia = ?, " + 
                   "  cod_comuna = ?, cod_prof_ofic = ?, cod_tipo_cliente = ?, " +         
                   "  categoria_cliente = ? " +                                          
                   "WHERE numrun = ?"
                  
                  ;                                                     

    int filasAfectadas = 0;

    // Usar try-with-resources para cerrar todo automáticamente
    try (Connection cnx = new Conexion().obtenerConexion();
         PreparedStatement stmt = cnx.prepareStatement(query)) {

       
        stmt.setString(1, cli.getPnombre()); // Índice 1 = pnombre

        
        if (cli.getSnombre() == null || cli.getSnombre().trim().isEmpty()) {
            stmt.setNull(2, Types.VARCHAR);
        } else {
            stmt.setString(2, cli.getSnombre());
        }
        stmt.setString(3, cli.getAppaterno()); 

        
        if (cli.getApmaterno() == null || cli.getApmaterno().trim().isEmpty()) {
            stmt.setNull(4, Types.VARCHAR);
        } else {
            stmt.setString(4, cli.getApmaterno());
        }

        
        stmt.setDate(5, new java.sql.Date(cli.getFechaNacimiento().getTime()));
        stmt.setDate(6, new java.sql.Date(cli.getFechaInscripcion().getTime()));

        
        if (cli.getCorreo() == null || cli.getCorreo().trim().isEmpty()) {
            stmt.setNull(7, Types.VARCHAR);
        } else {
            stmt.setString(7, cli.getCorreo());
        }

        stmt.setLong(8, cli.getFonoContacto()); 
        stmt.setString(9, cli.getDireccion());  
        stmt.setInt(10, cli.getReg().getCodRegion()); 
        stmt.setInt(11, cli.getProv().getCodProvincia());
        stmt.setInt(11, cli.getProv().getCodProvincia());
        stmt.setInt(12, cli.getCom().getCodComuna());   
        stmt.setInt(13, cli.getProf().getCodProfOfic()); 
        stmt.setInt(14, cli.getTipocl().getCodTipoCliente());

        
         if (cli.getCategoria() == null || cli.getCategoria().trim().isEmpty()) {
             stmt.setString(15, "Bronce"); 
         } else {
             stmt.setString(15, cli.getCategoria());
         }

        
        stmt.setLong(16, cli.getRun()); 

        // --- Fin de asignación de parámetros ---

        filasAfectadas = stmt.executeUpdate();

    } catch (SQLException ex) {
        System.err.println("Error SQL al actualizar cliente: " + ex.getMessage() + " (Código: " + ex.getErrorCode() + ")");
    } catch (Exception e) {
        System.err.println("Error inesperado al actualizar cliente: " + e.getMessage());
        e.printStackTrace();
    }

   
    return filasAfectadas == 1;
}
  
   
   public boolean eliminar(int run){
       try {
           Conexion conexion = new Conexion();
           Connection cnx = conexion.obtenerConexion();
           
           //query
           String query = "DELETE FROM CLIENTE WHERE numrun = ?";
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
public Cliente buscarPorRun(long run) {
  
  Cliente cli = null;
    
    String query = "SELECT " +
                   "    c.NUMRUN, c.DVRUN, c.PNOMBRE, c.SNOMBRE, c.APPATERNO, c.APMATERNO, " +
                   "    c.FECHA_NACIMIENTO, c.FECHA_INSCRIPCION, NVL(c.CORREO, 'S/C') AS CORREO, " +
                   "    c.FONO_CONTACTO, c.DIRECCION, c.CATEGORIA_CLIENTE, " +
                   "    r.nombre_region, " +
                   "    p.nombre_provincia, " +
                   "    co.nombre_comuna, " +
                   "    pro.nombre_prof_ofic, " +
                   "    tc.nombre_tipo_cliente " +
                   "FROM " +
                   "    CLIENTE c " +
                   "LEFT JOIN REGION r ON c.cod_region = r.cod_region " + 
                   "LEFT JOIN PROVINCIA p ON c.cod_region = p.cod_region AND c.cod_provincia = p.cod_provincia " +
                   "LEFT JOIN COMUNA co ON c.cod_region = co.cod_region AND c.cod_provincia = co.cod_provincia AND c.cod_comuna = co.cod_comuna " +
                   "LEFT JOIN PROFESION_OFICIO pro ON c.cod_prof_ofic = pro.cod_prof_ofic " +
                   "LEFT JOIN TIPO_CLIENTE tc ON c.cod_tipo_cliente = tc.cod_tipo_cliente " +
                   "WHERE c.numrun = ?";

    try (Connection cnx = new Conexion().obtenerConexion();
         PreparedStatement stmt = cnx.prepareStatement(query)) {

        stmt.setLong(1, run); 

        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                cli = new Cliente();
                Region reg = new Region();
                Provincia prov = new Provincia();
                Comuna com = new Comuna();
                ProfesionOficio prof = new ProfesionOficio();
                TipoCliente tipo = new TipoCliente();

                // Poblar cliente (usando long)
                cli.setRun((int) rs.getLong("NUMRUN"));
                cli.setDvrun(rs.getString("DVRUN"));
                cli.setPnombre(rs.getString("PNOMBRE"));
                cli.setSnombre(rs.getString("SNOMBRE")); 
                cli.setAppaterno(rs.getString("APPATERNO"));
                cli.setApmaterno(rs.getString("APMATERNO")); 
                cli.setFechaNacimiento(rs.getDate("FECHA_NACIMIENTO"));
                cli.setFechaInscripcion(rs.getDate("FECHA_INSCRIPCION"));
                cli.setCorreo(rs.getString("CORREO"));
                cli.setFonoContacto((int) rs.getLong("FONO_CONTACTO")); 
                cli.setDireccion(rs.getString("DIRECCION"));
                cli.setCategoria(rs.getString("CATEGORIA_CLIENTE"));

                
                reg.setNombreRegion(rs.getString("nombre_region"));
                prov.setNombreProvincia(rs.getString("nombre_provincia"));
                com.setNombreComuna(rs.getString("nombre_comuna"));
                prof.setNombreProfesion(rs.getString("nombre_prof_ofic"));
                tipo.setNombreTipoCliente(rs.getString("nombre_tipo_cliente"));

                // Asignar objetos
                cli.setReg(reg);
                cli.setProv(prov);
                cli.setCom(com);
                cli.setProf(prof);
                cli.setTipocl(tipo);
            }
        }
    } catch (Exception e) {
        System.err.println("Error SQL al buscar cliente por RUN: " + e.getMessage());
        e.printStackTrace(); // Imprime el stack trace para depuración
    }
    return cli; // Devuelve el cliente encontrado o null
}

// 1. Método para obtener TODAS las regiones
public List<Region> obtenerRegiones() {
    List<Region> lista = new ArrayList<>();
    String query = "SELECT COD_REGION, NOMBRE_REGION FROM REGION ORDER BY NOMBRE_REGION";
    
    try (Connection cnx = new Conexion().obtenerConexion();
         PreparedStatement stmt = cnx.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Region reg = new Region();
            reg.setCodRegion(rs.getInt("COD_REGION"));
            reg.setNombreRegion(rs.getString("NOMBRE_REGION"));
            lista.add(reg);
        }
    } catch (Exception e) {
        System.err.println("Error al obtener regiones: " + e.getMessage());
    }
    return lista;
}

// 2. Método para obtener provincias DE UNA REGIÓN específica
public List<Provincia> obtenerProvinciasPorRegion(int idRegion) {
    List<Provincia> lista = new ArrayList<>();
    String query = "SELECT COD_PROVINCIA, NOMBRE_PROVINCIA FROM PROVINCIA WHERE COD_REGION = ? ORDER BY NOMBRE_PROVINCIA";
    
    try (Connection cnx = new Conexion().obtenerConexion();
         PreparedStatement stmt = cnx.prepareStatement(query)) {
        
        stmt.setInt(1, idRegion);
        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Provincia prov = new Provincia();
                prov.setCodProvincia(rs.getInt("COD_PROVINCIA"));
                prov.setNombreProvincia(rs.getString("NOMBRE_PROVINCIA"));
                lista.add(prov);
            }
        }
    } catch (Exception e) {
        System.err.println("Error al obtener provincias: " + e.getMessage());
    }
    return lista;
}


public List<Comuna> obtenerComunasPorRegionYProvincia(int idRegion, int idProvincia) {
   List<Comuna> listaComunas = new ArrayList<>();

    
    String query = "SELECT * FROM comuna WHERE cod_region = ? AND cod_provincia = ? ORDER BY nombre_comuna";

    try (Connection cnx = new Conexion().obtenerConexion();
         PreparedStatement stmt = cnx.prepareStatement(query)) {

        // Asignar ambos parámetros
        stmt.setInt(1, idRegion);      // <-- Parámetro 1 para cod_region
        stmt.setInt(2, idProvincia);   // <-- Parámetro 2 para cod_provincia

        try (ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Comuna com = new Comuna();
                com.setCodComuna(rs.getInt("cod_comuna"));
                com.setNombreComuna(rs.getString("nombre_comuna"));
                // Opcional: guardar también los IDs de región/prov
                // com.setCodRegion(rs.getInt("cod_region")); 
                // com.setCodProvincia(rs.getInt("cod_provincia"));
                listaComunas.add(com);
            }
        }
    } catch (Exception e) {
        System.err.println("Error SQL al obtener comunas por región y provincia: " + e.getMessage());
        e.printStackTrace();
    }
    return listaComunas;
}
public List<ProfesionOficio> obtenerProfesiones() {
    List<ProfesionOficio> lista = new ArrayList<>();
    // Consulto la tabla PROFESION_OFICIO de tu script
    String query = "SELECT COD_PROF_OFIC, NOMBRE_PROF_OFIC FROM PROFESION_OFICIO ORDER BY NOMBRE_PROF_OFIC";
    
    try (Connection cnx = new Conexion().obtenerConexion();
         PreparedStatement stmt = cnx.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            ProfesionOficio prof = new ProfesionOficio();
            prof.setCodProfOfic(rs.getInt("COD_PROF_OFIC"));
            prof.setNombreProfesion(rs.getString("NOMBRE_PROF_OFIC"));
            lista.add(prof);
        }
    } catch (Exception e) {
        System.err.println("Error al obtener profesiones: " + e.getMessage());
    }
    return lista;
}

public List<TipoCliente> obtenerTiposCliente() {
    List<TipoCliente> lista = new ArrayList<>();

    String query = "SELECT COD_TIPO_CLIENTE, NOMBRE_TIPO_CLIENTE FROM TIPO_CLIENTE ORDER BY NOMBRE_TIPO_CLIENTE";
    
    try (Connection cnx = new Conexion().obtenerConexion();
         PreparedStatement stmt = cnx.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            TipoCliente tipo = new TipoCliente();
            tipo.setCodTipoCliente(rs.getInt("COD_TIPO_CLIENTE"));
            tipo.setNombreTipoCliente(rs.getString("NOMBRE_TIPO_CLIENTE"));
            lista.add(tipo);
        }
    } catch (Exception e) {
        System.err.println("Error al obtener tipos de cliente: " + e.getMessage());
    }
    return lista;
}

 

}
