package Controlador;


import Modelo.TarjetaCliente;
import Modelo.TipoTransaccionTarjeta;
import Modelo.TransaccionTarjetaCliente;
import bd.Conexion;
import java.sql.Connection;
import java.sql.CallableStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RegistroTrans {

    
public List<TransaccionTarjetaCliente> listarTodasLasTransacciones() {
    List<TransaccionTarjetaCliente> listaTransacciones = new ArrayList<>();
    
    // 1. CAMBIO: El SQL ahora es una llamada al procedimiento del paquete
    String sql = "{CALL PKG_TRANSACCIONES.SP_LISTAR_TODAS(?)}"; 

    // 2. CAMBIO: Se usa CallableStatement en lugar de PreparedStatement
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
                // Crear objetos anidados
                TarjetaCliente tarjeta = new TarjetaCliente();
                tarjeta.setNroTarjeta(rs.getString("nro_tarjeta"));

                TipoTransaccionTarjeta tipoTx = new TipoTransaccionTarjeta();
                tipoTx.setCodTptranTarjeta(rs.getInt("cod_tptran_tarjeta"));
                tipoTx.setNombreTptranTarjeta(rs.getString("nombre_tptran_tarjeta"));

                // Crear objeto principal Transaccion
                TransaccionTarjetaCliente tx = new TransaccionTarjetaCliente();
                tx.setNroTransaccion(rs.getInt("nro_transaccion"));
                tx.setFechaTransaccion(rs.getDate("fecha_transaccion"));
                tx.setMontoTransaccion(rs.getDouble("monto_transaccion"));
                tx.setTotalCuotasTransaccion(rs.getInt("total_cuotas_transaccion"));
                tx.setMontoTotalTransaccion(rs.getDouble("monto_total_transaccion"));

                // Asignar objetos anidados
                tx.setTarjeta(tarjeta);
                tx.setTipoTransaccion(tipoTx);

                listaTransacciones.add(tx);
            }
        } // 7. CAMBIO: El 'rs' se cierra solo aquí

    } catch (SQLException e) {
        // 8. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar SP_LISTAR_TODAS: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Error desconocido al ejecutar SP_LISTAR_TODAS: " + e.getMessage());
        e.printStackTrace();
    }
    
    return listaTransacciones;
}
    /**
     * Lista todas las transacciones de un cliente específico, buscándolas por su RUN.
     * @param runCliente El RUN del cliente del cual se quieren obtener las transacciones.
     * @return Una lista de objetos Transaccion.
     */
    public List<TransaccionTarjetaCliente> listarTransaccionesPorCliente(int runCliente) {
    List<TransaccionTarjetaCliente> listaTransacciones = new ArrayList<>();
    
    // 1. CAMBIO: El SQL ahora es una llamada al procedimiento del paquete
    String sql = "{CALL PKG_TRANSACCIONES.SP_LISTAR_POR_CLIENTE(?, ?)}"; 

    // 2. CAMBIO: Se usa CallableStatement
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. NUEVO: Asignamos el parámetro de ENTRADA (el run)
        cstmt.setInt(1, runCliente); 

        // 4. NUEVO: Registramos el parámetro de SALIDA (el cursor)
        cstmt.registerOutParameter(2, java.sql.Types.REF_CURSOR); 

        // 5. NUEVO: Ejecutamos el procedimiento
        cstmt.execute();

        // 6. NUEVO: Obtenemos el cursor como un ResultSet (del parámetro 2)
        try (ResultSet rs = (ResultSet) cstmt.getObject(2)) {
            
            // 7. SIN CAMBIOS: Este bucle 'while' es idéntico al que tenías
            while (rs.next()) {
                // 1. Crear los objetos anidados
                TarjetaCliente tarjeta = new TarjetaCliente();
                tarjeta.setNroTarjeta(rs.getString("nro_tarjeta"));
                
                TipoTransaccionTarjeta tipoTx = new TipoTransaccionTarjeta();
                tipoTx.setCodTptranTarjeta(rs.getInt("cod_tptran_tarjeta"));
                tipoTx.setNombreTptranTarjeta(rs.getString("nombre_tptran_tarjeta"));
                
                // 2. Crear el objeto principal Transaccion
                TransaccionTarjetaCliente tx = new TransaccionTarjetaCliente();
                tx.setNroTransaccion(rs.getInt("nro_transaccion"));
                tx.setFechaTransaccion(rs.getDate("fecha_transaccion"));
                tx.setMontoTransaccion(rs.getDouble("monto_transaccion"));
                tx.setTotalCuotasTransaccion(rs.getInt("total_cuotas_transaccion"));
                tx.setMontoTotalTransaccion(rs.getDouble("monto_total_transaccion"));
                
                // 3. Asignar los objetos anidados
                tx.setTarjeta(tarjeta);
                tx.setTipoTransaccion(tipoTx);
                
                listaTransacciones.add(tx);
            }
        } // 8. CAMBIO: El 'rs' se cierra solo aquí

    } catch (SQLException e) {
        // 9. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar SP_LISTAR_POR_CLIENTE: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Error desconocido al ejecutar SP_LISTAR_POR_CLIENTE: " + e.getMessage());
        e.printStackTrace();
    }
    
    return listaTransacciones;
}
    
    /**
 * Modifica los datos editables de una transacción existente en la BD.
 * Usa la clave primaria compuesta (Transacción + Tarjeta).
 *
 * @param nroTransaccion La PK de la transacción.
 * @param nroTarjeta La PK de la tarjeta.
 * @param nuevoMonto El nuevo monto (columna 2).
 * @param nuevasCuotas Las nuevas cuotas (columna 3).
 * @param nuevoMontoTotal El nuevo monto total (columna 4).
 * @return true si la actualización fue exitosa, false si falló.
 */
public boolean modificar(int nroTransaccion, long nroTarjeta, double nuevoMonto, int nuevasCuotas, double nuevoMontoTotal) {
    
    // 1. CAMBIO: El SQL ahora es una llamada al procedimiento del paquete
    String sql = "{CALL PKG_TRANSACCIONES.SP_MODIFICAR(?, ?, ?, ?, ?)}";

    // 2. CAMBIO: Se usa try-with-resources con CallableStatement
    //    Esto elimina la necesidad de un bloque 'finally' y de cerrar 'cnx' y 'stmt' manualmente.
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. CAMBIO: Asignamos los 5 parámetros en el orden del paquete
        cstmt.setInt(1, nroTransaccion);
        cstmt.setLong(2, nroTarjeta);
        cstmt.setDouble(3, nuevoMonto);
        cstmt.setInt(4, nuevasCuotas);
        cstmt.setDouble(5, nuevoMontoTotal);

        // 4. CAMBIO: Se usa execute() para llamar al procedimiento
        cstmt.execute();
        
        // 5. CAMBIO: Se elimina todo el manejo de transacciones (commit/rollback)
        //    El paquete PL/SQL lo hace automáticamente.
        
        return true; // Si no hay excepción, fue un éxito

    } catch (SQLException ex) {
        // 6. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar SP_MODIFICAR: " + ex.getMessage());
        ex.printStackTrace();
        return false;
    } catch (Exception e) {
        System.err.println("Error desconocido al ejecutar SP_MODIFICAR: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
    // 7. CAMBIO: El bloque 'finally' se elimina por completo.
}

    /**
 * Elimina una transacción y todas sus cuotas asociadas.
 * Maneja la operación como una transacción (o todo o nada).
 * @param nroTransaccion El ID de la transacción a eliminar.
 * @param nroTarjeta El ID de la tarjeta asociada a la transacción.
 * @return true si la eliminación fue exitosa, false si falló.
 */
public boolean eliminar(int nroTransaccion, Long nroTarjeta) {
    
    // 1. CAMBIO: El SQL ahora es una llamada al procedimiento del paquete
    String sql = "{CALL PKG_TRANSACCIONES.SP_ELIMINAR(?, ?)}";

    // 2. CAMBIO: Se usa try-with-resources con CallableStatement
    //    Esto reemplaza todo el bloque try-catch-finally manual
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. CAMBIO: Asignamos los 2 parámetros en el orden del paquete
        cstmt.setInt(1, nroTransaccion);
        cstmt.setLong(2, nroTarjeta);

        // 4. CAMBIO: Se usa execute() para llamar al procedimiento
        cstmt.execute();
        
        // 5. CAMBIO: Se elimina todo el manejo de transacciones (borrar hijos, padre, commit, rollback)
        //    El paquete PL/SQL lo hace automáticamente.
        
        return true; // Si no hay excepción, fue un éxito

    } catch (SQLException ex) {
        // 6. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar SP_ELIMINAR: " + ex.getMessage());
        ex.printStackTrace();
        return false;
    } catch (Exception e) {
        System.err.println("Error desconocido al ejecutar SP_ELIMINAR: " + e.getMessage());
        e.printStackTrace();
        return false;
    }
    // 7. CAMBIO: El bloque 'finally' y el 'try-catch' de rollback se eliminan.
}

    /**
     * Busca y devuelve una única transacción por su número.
     * @param nroTransaccion El número de la transacción a buscar.
     * @return El objeto TransaccionTarjetaCliente si se encuentra, o null si no existe.
     */
    public TransaccionTarjetaCliente buscarPorNroTransaccion(int nroTransaccion) {
    TransaccionTarjetaCliente tx = null;
    
    // 1. CAMBIO: El SQL ahora es una llamada al procedimiento del paquete
    String sql = "{CALL PKG_TRANSACCIONES.SP_BUSCAR_POR_NRO(?, ?)}"; 

    // 2. CAMBIO: Se usa CallableStatement
    try (Connection cnx = new Conexion().obtenerConexion();
         CallableStatement cstmt = cnx.prepareCall(sql)) {

        // 3. NUEVO: Asignamos el parámetro de ENTRADA (el nroTransaccion)
        cstmt.setInt(1, nroTransaccion); 

        // 4. NUEVO: Registramos el parámetro de SALIDA (el cursor)
        cstmt.registerOutParameter(2, java.sql.Types.REF_CURSOR); 

        // 5. NUEVO: Ejecutamos el procedimiento
        cstmt.execute();

        // 6. NUEVO: Obtenemos el cursor como un ResultSet (del parámetro 2)
        try (ResultSet rs = (ResultSet) cstmt.getObject(2)) {
            
            // 7. CAMBIO: Completamos la lógica que faltaba en tu comentario
            if (rs.next()) {
                tx = new TransaccionTarjetaCliente();
                
                // Crear objetos anidados
                TarjetaCliente tarjeta = new TarjetaCliente();
                tarjeta.setNroTarjeta(rs.getString("nro_tarjeta"));

                TipoTransaccionTarjeta tipoTx = new TipoTransaccionTarjeta();
                tipoTx.setCodTptranTarjeta(rs.getInt("cod_tptran_tarjeta"));
                tipoTx.setNombreTptranTarjeta(rs.getString("nombre_tptran_tarjeta"));

                // Poblar objeto principal
                tx.setNroTransaccion(rs.getInt("nro_transaccion"));
                tx.setFechaTransaccion(rs.getDate("fecha_transaccion"));
                tx.setMontoTransaccion(rs.getDouble("monto_transaccion"));
                tx.setTotalCuotasTransaccion(rs.getInt("total_cuotas_transaccion"));
                tx.setMontoTotalTransaccion(rs.getDouble("monto_total_transaccion"));

                // Asignar objetos anidados
                tx.setTarjeta(tarjeta);
                tx.setTipoTransaccion(tipoTx);
            }
        } // 8. CAMBIO: El 'rs' se cierra solo aquí

    } catch (SQLException e) {
        // 9. CAMBIO: Mensaje de error actualizado
        System.err.println("Error SQL al ejecutar SP_BUSCAR_POR_NRO: " + e.getMessage());
        e.printStackTrace();
    } catch (Exception e) {
        System.err.println("Error desconocido al ejecutar SP_BUSCAR_POR_NRO: " + e.getMessage());
        e.printStackTrace();
    }
    
    return tx; // Devuelve el objeto (o null si no se encontró)
}

}