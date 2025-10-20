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
import java.sql.CallableStatement;
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
    
    // El SQL ahora es una llamada a un Procedimiento Almacenado
    String sql = "{CALL PKG_CLIENTES.SP_AGREGAR(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    // Se usa try-with-resources y un CallableStatement
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // Se asignan los 16 parámetros de ENTRADA
        cstmt.setInt(1, cli.getRun());
        cstmt.setString(2, cli.getDvrun());
        cstmt.setString(3, cli.getPnombre());

        // Manejo de Snombre (parámetro 4)
        if (cli.getSnombre() == null || cli.getSnombre().trim().isEmpty()) {
            cstmt.setNull(4, java.sql.Types.VARCHAR);
        } else {
            cstmt.setString(4, cli.getSnombre());
        }

        cstmt.setString(5, cli.getAppaterno());

        // Manejo de Apmaterno (parámetro 6)
        if (cli.getApmaterno() == null || cli.getApmaterno().trim().isEmpty()) {
            cstmt.setNull(6, java.sql.Types.VARCHAR);
        } else {
            cstmt.setString(6, cli.getApmaterno());
        }
        
        cstmt.setDate(7, new java.sql.Date(cli.getFechaNacimiento().getTime()));
        cstmt.setDate(8, new java.sql.Date(cli.getFechaInscripcion().getTime()));
        
        if (cli.getCorreo() == null || cli.getCorreo().trim().isEmpty()) {
            cstmt.setNull(9, java.sql.Types.VARCHAR);
        } else {
            cstmt.setString(9, cli.getCorreo());
        }

        cstmt.setLong(10, cli.getFonoContacto());
        cstmt.setString(11, cli.getDireccion());
        
        cstmt.setInt(12, cli.getReg().getCodRegion());
        cstmt.setInt(13, cli.getProv().getCodProvincia()); 
        cstmt.setInt(14, cli.getCom().getCodComuna());    
        cstmt.setInt(15, cli.getProf().getCodProfOfic());
        cstmt.setInt(16, cli.getTipocl().getCodTipoCliente());
        
        // La lógica de "Bronce" (parámetro 17) ya no existe aquí
        // Se maneja DENTRO del paquete PL/SQL

        // Se ejecuta el procedimiento
        cstmt.execute();
        
        return true; 

    } catch (SQLException ex) {
        System.err.println("Error en SQL al ejecutar SP_AGREGAR: " + ex.getMessage());
        ex.printStackTrace();
        return false;
    } catch (Exception e) {
        System.err.println("Error desconocido al ejecutar SP_AGREGAR: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
}
     
     
   public List<Cliente> listarCliente() {
    List<Cliente> listaCliente = new ArrayList<>();
    
    // 1. CAMBIO: Esta es la nueva llamada al paquete
    String sql = "{CALL PKG_CLIENTES.SP_LISTAR_TODOS(?)}"; 

    // 2. CAMBIO: Usamos try-with-resources con CallableStatement
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. NUEVO: Registramos el parámetro de SALIDA (el cursor)
        cstmt.registerOutParameter(1, java.sql.Types.REF_CURSOR); 

        // 4. NUEVO: Ejecutamos el procedimiento
        cstmt.execute();

        // 5. NUEVO: Obtenemos el cursor como un ResultSet
        try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
            
            // 6. SIN CAMBIOS: Este bucle 'while' es idéntico al que tenías
            while (rs.next()) {
                Cliente cli = new Cliente();
                
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

                cli.setReg(region);
                cli.setProv(provincia);
                cli.setCom(comuna);
                cli.setProf(profesion);
                cli.setTipocl(tipoCliente);
                cli.setCategoria(rs.getString("categoria_cliente"));
                
                listaCliente.add(cli);
            }
        } // 7. CAMBIO: El 'rs' se cierra solo aquí
    } catch (SQLException e) {
        // 8. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al llamar SP_LISTAR_TODOS: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println(" Error desconocido al llamar SP_LISTAR_TODOS: " + e.getMessage());
        e.printStackTrace();
    }
    // 9. CAMBIO: Los rs.close(), stmt.close() y cnx.close() manuales se eliminan
    
    return listaCliente;
}

   
   public boolean modificarCliente(Cliente cli) {
    
    // 1. CAMBIO: El SQL ahora es una llamada al paquete (16 parámetros IN)
    String sql = "{CALL PKG_CLIENTES.SP_MODIFICAR(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

    // 2. CAMBIO: Se usa CallableStatement
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. CAMBIO: Asignamos los 16 parámetros en el orden del paquete
        
        // p_numrun (parámetro 1)
        cstmt.setInt(1, cli.getRun());
        
        // p_pnombre (parámetro 2)
        cstmt.setString(2, cli.getPnombre());

        // p_snombre (parámetro 3)
        if (cli.getSnombre() == null || cli.getSnombre().trim().isEmpty()) {
            cstmt.setNull(3, Types.VARCHAR);
        } else {
            cstmt.setString(3, cli.getSnombre());
        }
        
        // p_appaterno (parámetro 4)
        cstmt.setString(4, cli.getAppaterno());

        // p_apmaterno (parámetro 5)
        if (cli.getApmaterno() == null || cli.getApmaterno().trim().isEmpty()) {
            cstmt.setNull(5, Types.VARCHAR);
        } else {
            cstmt.setString(5, cli.getApmaterno());
        }

        // p_fecha_nac (parámetro 6)
        cstmt.setDate(6, new java.sql.Date(cli.getFechaNacimiento().getTime()));
        // p_fecha_insc (parámetro 7)
        cstmt.setDate(7, new java.sql.Date(cli.getFechaInscripcion().getTime()));

        // p_correo (parámetro 8)
        if (cli.getCorreo() == null || cli.getCorreo().trim().isEmpty()) {
            cstmt.setNull(8, Types.VARCHAR);
        } else {
            cstmt.setString(8, cli.getCorreo());
        }

        // p_fono (parámetro 9)
        cstmt.setLong(9, cli.getFonoContacto());
        // p_direccion (parámetro 10)
        cstmt.setString(10, cli.getDireccion());
        // p_cod_region (parámetro 11)
        cstmt.setInt(11, cli.getReg().getCodRegion());
        // p_cod_provincia (parámetro 12)
        cstmt.setInt(12, cli.getProv().getCodProvincia());
        // p_cod_comuna (parámetro 13)
        cstmt.setInt(13, cli.getCom().getCodComuna());
        // p_cod_prof (parámetro 14)
        cstmt.setInt(14, cli.getProf().getCodProfOfic());
        // p_cod_tipo_cli (parámetro 15)
        cstmt.setInt(15, cli.getTipocl().getCodTipoCliente());

        // p_categoria (parámetro 16)
        if (cli.getCategoria() == null || cli.getCategoria().trim().isEmpty()) {
            cstmt.setString(16, "Bronce");
        } else {
            cstmt.setString(16, cli.getCategoria());
        }

        // 4. CAMBIO: Se usa execute() en lugar de executeUpdate()
        cstmt.execute();
        
        // 5. CAMBIO: El procedure maneja el COMMIT, solo retornamos true
        return true; 

    } catch (SQLException ex) {
        // 6. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar SP_MODIFICAR: " + ex.getMessage());
        ex.printStackTrace();
    } catch (Exception e) {
        System.err.println("Error inesperado al ejecutar SP_MODIFICAR: " + e.getMessage());
        e.printStackTrace();
    }

    // 7. CAMBIO: Se retorna false si algo falla
    return false;
}
  
   
   public boolean eliminar(int run) {
    
    // 1. CAMBIO: El SQL ahora es una llamada al procedimiento del paquete
    String sql = "{CALL PKG_CLIENTES.SP_ELIMINAR(?)}";

    // 2. CAMBIO: Se usa try-with-resources para CallableStatement
    //    Esto cierra cnx y cstmt automáticamente, incluso si hay un error.
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. SIN CAMBIOS: Asignamos el parámetro
        cstmt.setInt(1, run);

        // 4. CAMBIO: Se usa execute() para llamar al procedimiento
        cstmt.execute();
        
        // El COMMIT/ROLLBACK se maneja dentro del paquete PL/SQL
        return true;

    } catch (SQLException ex) {
        // 5. CAMBIO: Mensaje de error actualizado
        System.err.println("Error en SQL al ejecutar SP_ELIMINAR: " + ex.getMessage());
        return false;
    } catch (Exception e) {
        // 6. CAMBIO: Mensaje de error actualizado
        System.err.println("Error inesperado al ejecutar SP_ELIMINAR: " + e.getMessage());
        return false;
    }
    // 7. CAMBIO: Los .close() manuales se eliminan
}
   
   
public Cliente buscarPorRun(long run) {
    Cliente cli = null;
    
    // 1. CAMBIO: El SQL ahora es una llamada al procedimiento del paquete
    String sql = "{CALL PKG_CLIENTES.SP_BUSCAR_POR_RUN(?, ?)}"; 

    // 2. CAMBIO: Usamos try-with-resources con CallableStatement
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. NUEVO: Asignamos el parámetro de ENTRADA (el run)
        cstmt.setLong(1, run); 

        // 4. NUEVO: Registramos el parámetro de SALIDA (el cursor)
        cstmt.registerOutParameter(2, java.sql.Types.REF_CURSOR); 

        // 5. NUEVO: Ejecutamos el procedimiento
        cstmt.execute();

        // 6. NUEVO: Obtenemos el cursor como un ResultSet
        try (ResultSet rs = (ResultSet) cstmt.getObject(2)) {
            
            // 7. SIN CAMBIOS: Este bloque 'if' es idéntico al que tenías
            if (rs.next()) {
                cli = new Cliente();
                Region reg = new Region();
                Provincia prov = new Provincia();
                Comuna com = new Comuna();
                ProfesionOficio prof = new ProfesionOficio();
                TipoCliente tipo = new TipoCliente();

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

                cli.setReg(reg);
                cli.setProv(prov);
                cli.setCom(com);
                cli.setProf(prof);
                cli.setTipocl(tipo);
            }
        } // 8. CAMBIO: El 'rs' se cierra solo aquí
    } catch (Exception e) {
        // 9. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar SP_BUSCAR_POR_RUN: " + e.getMessage());
        e.printStackTrace(); 
    }
    return cli; 
}

// 1. Método para obtener TODAS las regiones (usando Paquete)
public List<Region> obtenerRegiones() {
    List<Region> lista = new ArrayList<>();
    
    // 1. CAMBIO: El SQL ahora es una llamada al procedimiento del paquete
    String sql = "{CALL PKG_CLIENTES.SP_OBTENER_REGIONES(?)}";

    // 2. CAMBIO: Se usa try-with-resources con CallableStatement
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. NUEVO: Registramos el parámetro de SALIDA (el cursor)
        cstmt.registerOutParameter(1, java.sql.Types.REF_CURSOR); 

        // 4. NUEVO: Ejecutamos el procedimiento
        cstmt.execute();

        // 5. NUEVO: Obtenemos el cursor como un ResultSet
        try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
            
            // 6. SIN CAMBIOS: Este bucle 'while' es idéntico al que tenías
            while (rs.next()) {
                Region reg = new Region();
                reg.setCodRegion(rs.getInt("COD_REGION"));
                reg.setNombreRegion(rs.getString("NOMBRE_REGION"));
                lista.add(reg);
            }
        } // 7. CAMBIO: El 'rs' se cierra solo aquí (por el try-with-resources)

    } catch (SQLException e) {
        // 8. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar SP_OBTENER_REGIONES: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Error desconocido al ejecutar SP_OBTENER_REGIONES: " + e.getMessage());
        e.printStackTrace();
    }
    
    return lista;
}

// 2. Método para obtener provincias DE UNA REGIÓN específica
// 2. Método para obtener provincias DE UNA REGIÓN específica (usando Paquete)
public List<Provincia> obtenerProvinciasPorRegion(int idRegion) {
    List<Provincia> lista = new ArrayList<>();
    
    // 1. CAMBIO: El SQL ahora es una llamada al procedimiento del paquete
    String sql = "{CALL PKG_CLIENTES.SP_OBTENER_PROVINCIAS(?, ?)}"; 

    // 2. CAMBIO: Se usa try-with-resources con CallableStatement
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. NUEVO: Asignamos el parámetro de ENTRADA (el idRegion)
        cstmt.setInt(1, idRegion); 

        // 4. NUEVO: Registramos el parámetro de SALIDA (el cursor)
        cstmt.registerOutParameter(2, java.sql.Types.REF_CURSOR); 

        // 5. NUEVO: Ejecutamos el procedimiento
        cstmt.execute();

        // 6. NUEVO: Obtenemos el cursor como un ResultSet (del parámetro 2)
        try (ResultSet rs = (ResultSet) cstmt.getObject(2)) {
            
            // 7. SIN CAMBIOS: Este bucle 'while' es idéntico
            while (rs.next()) {
                Provincia prov = new Provincia();
                prov.setCodProvincia(rs.getInt("COD_PROVINCIA"));
                prov.setNombreProvincia(rs.getString("NOMBRE_PROVINCIA"));
                lista.add(prov);
            }
        } // 8. CAMBIO: El 'rs' se cierra solo aquí

    } catch (SQLException e) { 
        // 9. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar SP_OBTENER_PROVINCIAS: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Error desconocido al ejecutar SP_OBTENER_PROVINCIAS: " + e.getMessage());
        e.printStackTrace();
    }
    
    return lista;
}

public List<Comuna> obtenerComunasPorRegionYProvincia(int idRegion, int idProvincia) {
    List<Comuna> listaComunas = new ArrayList<>();

    // 1. CAMBIO: El SQL ahora es una llamada al procedimiento
    String sql = "{CALL PKG_CLIENTES.SP_OBTENER_COMUNAS(?, ?, ?)}"; 

    // 2. CAMBIO: Se usa CallableStatement
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. NUEVO: Asignamos los parámetros de ENTRADA
        cstmt.setInt(1, idRegion);    // p_cod_region
        cstmt.setInt(2, idProvincia); // p_cod_provincia

        // 4. NUEVO: Registramos el parámetro de SALIDA (el cursor)
        cstmt.registerOutParameter(3, java.sql.Types.REF_CURSOR); 

        // 5. NUEVO: Ejecutamos el procedimiento
        cstmt.execute();

        // 6. NUEVO: Obtenemos el cursor como un ResultSet (del parámetro 3)
        try (ResultSet rs = (ResultSet) cstmt.getObject(3)) {
            
            // 7. SIN CAMBIOS: Este bucle 'while' es idéntico
            while (rs.next()) {
                Comuna com = new Comuna();
                com.setCodComuna(rs.getInt("cod_comuna"));
                com.setNombreComuna(rs.getString("nombre_comuna"));
                listaComunas.add(com);
            }
        } // 8. CAMBIO: El 'rs' se cierra solo

    } catch (SQLException e) {
        // 9. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar SP_OBTENER_COMUNAS: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Error desconocido al ejecutar SP_OBTENER_COMUNAS: " + e.getMessage());
        e.printStackTrace();
    }
    
    return listaComunas;
}


public List<ProfesionOficio> obtenerProfesiones() {
    List<ProfesionOficio> lista = new ArrayList<>();
    
    // 1. CAMBIO: El SQL ahora es una llamada al procedimiento del paquete
    String sql = "{CALL PKG_CLIENTES.SP_OBTENER_PROFESIONES(?)}";

    // 2. CAMBIO: Se usa try-with-resources con CallableStatement
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. NUEVO: Registramos el parámetro de SALIDA (el cursor)
        cstmt.registerOutParameter(1, java.sql.Types.REF_CURSOR); 

        // 4. NUEVO: Ejecutamos el procedimiento
        cstmt.execute();

        // 5. NUEVO: Obtenemos el cursor como un ResultSet
        try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
            
            // 6. SIN CAMBIOS: Este bucle 'while' es idéntico al que tenías
            while (rs.next()) {
                ProfesionOficio prof = new ProfesionOficio();
                prof.setCodProfOfic(rs.getInt("COD_PROF_OFIC"));
                prof.setNombreProfesion(rs.getString("NOMBRE_PROF_OFIC"));
                lista.add(prof);
            }
        } // 7. CAMBIO: El 'rs' se cierra solo

    } catch (SQLException e) {
        // 8. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar SP_OBTENER_PROFESIONES: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Error desconocido al ejecutar SP_OBTENER_PROFESIONES: " + e.getMessage());
        e.printStackTrace();
    }
    
    return lista;
}

public List<TipoCliente> obtenerTiposCliente() {
    List<TipoCliente> lista = new ArrayList<>();

    // 1. CAMBIO: El SQL ahora es una llamada al procedimiento del paquete
    String sql = "{CALL PKG_CLIENTES.SP_OBTENER_TIPOS_CLIENTE(?)}";

    // 2. CAMBIO: Se usa try-with-resources con CallableStatement
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. NUEVO: Registramos el parámetro de SALIDA (el cursor)
        cstmt.registerOutParameter(1, java.sql.Types.REF_CURSOR); 

        // 4. NUEVO: Ejecutamos el procedimiento
        cstmt.execute();

        // 5. NUEVO: Obtenemos el cursor como un ResultSet
        try (ResultSet rs = (ResultSet) cstmt.getObject(1)) {
            
            // 6. SIN CAMBIOS: Este bucle 'while' es idéntico al que tenías
            while (rs.next()) {
                TipoCliente tipo = new TipoCliente();
                tipo.setCodTipoCliente(rs.getInt("COD_TIPO_CLIENTE"));
                tipo.setNombreTipoCliente(rs.getString("NOMBRE_TIPO_CLIENTE"));
                lista.add(tipo);
            }
        } // 7. CAMBIO: El 'rs' se cierra solo

    } catch (SQLException e) {
        // 8. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar SP_OBTENER_TIPOS_CLIENTE: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Error desconocido al ejecutar SP_OBTENER_TIPOS_CLIENTE: " + e.getMessage());
        e.printStackTrace();
    }
    
    return lista;
}
 
public boolean ejecutarCategorizacion() throws SQLException { // Lanza SQLException para que la Vista maneje el mensaje
        String sql = "{CALL pkg_categorizacion.sp_actualizar_categorias()}";

        // Usamos try-with-resources para asegurar el cierre
        try (Connection cnx = new Conexion().obtenerConexion();
             CallableStatement cstmt = cnx.prepareCall(sql)) {

            System.out.println("Ejecutando sp_actualizar_categorias desde Registro.java...");
            cstmt.execute();
            System.out.println("sp_actualizar_categorias ejecutado con éxito.");
            return true; // Éxito si no hubo SQLException

        } catch (SQLException ex) {
             System.err.println("Error SQL al ejecutar sp_actualizar_categorias: " + ex.getMessage());
             throw ex; // Relanzamos la excepción para que la Vista la capture y muestre el mensaje
        } catch (Exception e){
             // Captura otros posibles errores (ej: ClassNotFoundException si falta el driver)
             System.err.println("Error inesperado al ejecutar sp_actualizar_categorias: " + e.getMessage());
             // Podrías envolverlo en una SQLException o lanzar una excepción personalizada
             throw new SQLException("Error inesperado: " + e.getMessage(), e);
        }
    }



}
