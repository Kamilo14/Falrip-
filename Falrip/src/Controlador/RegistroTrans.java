package Controlador;


import Modelo.TarjetaCliente;
import Modelo.TipoTransaccionTarjeta;
import Modelo.TransaccionTarjetaCliente;
import bd.Conexion;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class RegistroTrans {

    /**
     * Agrega una nueva transacción a la base de datos.
     * @param tx El objeto Transaccion con todos los datos a insertar.
     * @return true si la inserción fue exitosa, false en caso contrario.
     */
    public boolean agregar(TransaccionTarjetaCliente tx) {
        // La secuencia SEQ_NRO_TRANSACCION generará el ID único para la transacción.
        String query = "INSERT INTO transaccion_tarjeta_cliente " +
                       "(nro_transaccion, nro_tarjeta, fecha_transaccion, monto_transaccion, " +
                       "total_cuotas_transaccion, monto_total_transaccion, cod_tptran_tarjeta) " +
                       "VALUES (SEQ_NRO_TRANSACCION.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (Connection cnx = new Conexion().obtenerConexion();
             PreparedStatement stmt = cnx.prepareStatement(query)) {

            // Asignamos los valores a los parámetros de la consulta
            stmt.setString(1, tx.getTarjeta().getNroTarjeta());
            stmt.setDate(2, new java.sql.Date(tx.getFechaTransaccion().getTime()));
            stmt.setDouble(3, tx.getMontoTransaccion());
            stmt.setInt(4, tx.getTotalCuotasTransaccion());
            stmt.setDouble(5, tx.getMontoTotalTransaccion());
            stmt.setInt(6, tx.getTipoTransaccion().getCodTptranTarjeta());

            stmt.executeUpdate();
            return true;

        } catch (SQLException ex) {
            System.err.println("Error en SQL al agregar la transacción: " + ex.getMessage());
            return false;
        } catch (Exception e) {
            System.err.println("Error desconocido al agregar la transacción: " + e.getMessage());
            return false;
        }
    }
    
    
    public List<TransaccionTarjetaCliente> listarTodasLasTransacciones() {
    List<TransaccionTarjetaCliente> listaTransacciones = new ArrayList<>();
    // Query similar a la de buscar por cliente, pero sin el WHERE por numrun
    String query = "SELECT " +
                   "t.nro_transaccion, t.fecha_transaccion, t.monto_transaccion, " +
                   "t.total_cuotas_transaccion, t.monto_total_transaccion, " +
                   "tc.nro_tarjeta, " + // Incluimos nro_tarjeta
                   "ttp.cod_tptran_tarjeta, ttp.nombre_tptran_tarjeta " +
                   "FROM transaccion_tarjeta_cliente t " +
                   "LEFT JOIN tarjeta_cliente tc ON t.nro_tarjeta = tc.nro_tarjeta " + // LEFT JOIN por si alguna transacción no tiene tarjeta? (poco probable)
                   "LEFT JOIN tipo_transaccion_tarjeta ttp ON t.cod_tptran_tarjeta = ttp.cod_tptran_tarjeta " +
                   "ORDER BY t.fecha_transaccion DESC"; // Ordenar por fecha es útil

    try (Connection cnx = new Conexion().obtenerConexion();
         PreparedStatement stmt = cnx.prepareStatement(query);
         ResultSet rs = stmt.executeQuery()) {

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
    } catch (SQLException e) {
        System.err.println("Error SQL al listar todas las transacciones: " + e.getMessage());
        // Considera lanzar una excepción personalizada o devolver una lista vacía controlada
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
        
        // Esta query une la tabla de transacciones con la de tarjetas para poder filtrar por el RUN del cliente.
        String query = "SELECT " +
                       "t.nro_transaccion, t.fecha_transaccion, t.monto_transaccion, " +
                       "t.total_cuotas_transaccion, t.monto_total_transaccion, " +
                       "tc.nro_tarjeta, " +
                       "ttp.cod_tptran_tarjeta, ttp.nombre_tptran_tarjeta " +
                       "FROM transaccion_tarjeta_cliente t " +
                       "JOIN tarjeta_cliente tc ON t.nro_tarjeta = tc.nro_tarjeta " +
                       "JOIN tipo_transaccion_tarjeta ttp ON t.cod_tptran_tarjeta = ttp.cod_tptran_tarjeta " +
                       "WHERE tc.numrun = ? " +
                       "ORDER BY t.fecha_transaccion DESC";

        try (Connection cnx = new Conexion().obtenerConexion();
             PreparedStatement stmt = cnx.prepareStatement(query)) {

            stmt.setInt(1, runCliente);
            try (ResultSet rs = stmt.executeQuery()) {
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
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al listar las transacciones del cliente: " + e.getMessage());
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
    
    String sql = "UPDATE TRANSACCION_TARJETA_CLIENTE " +
                 "SET MONTO_TRANSACCION = ?, TOTAL_CUOTAS_TRANSACCION = ?, MONTO_TOTAL_TRANSACCION = ? " +
                 "WHERE NRO_TRANSACCION = ? AND NRO_TARJETA = ?";

    Connection cnx = null; // ¡No usamos try-with-resources para la conexión!
    PreparedStatement stmt = null;

    try {
        cnx = new Conexion().obtenerConexion();
        
        // 1. Desactivamos el AutoCommit (por si acaso)
        cnx.setAutoCommit(false); 

        // 2. Preparamos y ejecutamos el UPDATE
        stmt = cnx.prepareStatement(sql);
        stmt.setDouble(1, nuevoMonto);
        stmt.setInt(2, nuevasCuotas);
        stmt.setDouble(3, nuevoMontoTotal);
        stmt.setInt(4, nroTransaccion);
        stmt.setLong(5, nroTarjeta);

        int filasAfectadas = stmt.executeUpdate();
        
        // 3. ¡¡EL PASO CLAVE!! Guardamos los cambios permanentemente
        cnx.commit(); 
        
        System.out.println("Conexión exitosa. Filas actualizadas: " + filasAfectadas);
        return filasAfectadas > 0;

    } catch (SQLException ex) {
        System.err.println("Error en SQL al modificar, revirtiendo: " + ex.getMessage());
        try {
            // 4. Si algo falla, deshacemos
            if (cnx != null) {
                cnx.rollback();
            }
        } catch (SQLException eRollback) {
            System.err.println("Error al hacer rollback: " + eRollback.getMessage());
        }
        ex.printStackTrace();
        return false;
        
    } finally {
        // 5. Limpiamos y restauramos todo
        try {
            if (stmt != null) stmt.close();
            if (cnx != null) {
                cnx.setAutoCommit(true); // Restaurar estado original
                cnx.close(); // Cerrar manualmente
            }
        } catch (SQLException eFinal) {
            System.err.println("Error al cerrar recursos: " + eFinal.getMessage());
        }
    }
}

    /**
 * Elimina una transacción y todas sus cuotas asociadas.
 * Maneja la operación como una transacción (o todo o nada).
 * @param nroTransaccion El ID de la transacción a eliminar.
 * @param nroTarjeta El ID de la tarjeta asociada a la transacción.
 * @return true si la eliminación fue exitosa, false si falló.
 */
public boolean eliminar(int nroTransaccion, Long nroTarjeta) {
    
    // 1. Definir las sentencias SQL
    String sqlHijos = "DELETE FROM CUOTA_TRANSAC_TARJETA_CLIENTE " + 
                      "WHERE NRO_TRANSACCION = ? AND NRO_TARJETA = ?";
                  
    String sqlPadre = "DELETE FROM TRANSACCION_TARJETA_CLIENTE " + 
                      "WHERE NRO_TRANSACCION = ? AND NRO_TARJETA = ?";

    Connection cnx = null; // No se puede usar try-with-resources para cnx aquí
    PreparedStatement stmtHijos = null;
    PreparedStatement stmtPadre = null;

    try {
        cnx = new Conexion().obtenerConexion();
        
        // 2. Iniciar la transacción: Desactivar AutoCommit
        cnx.setAutoCommit(false); 

        // 3. Borrar Hijos (Cuotas)
        stmtHijos = cnx.prepareStatement(sqlHijos);
        stmtHijos.setInt(1, nroTransaccion);
        stmtHijos.setLong(2, nroTarjeta); // Asumiendo que nroTarjeta es int
        stmtHijos.executeUpdate();

        // 4. Borrar Padre (Transacción)
        stmtPadre = cnx.prepareStatement(sqlPadre);
        stmtPadre.setInt(1, nroTransaccion);
        stmtPadre.setLong(2, nroTarjeta);
        int filasPadreAfectadas = stmtPadre.executeUpdate();

        // 5. Si todo salió bien, confirmar los cambios
        cnx.commit(); 
        
        return filasPadreAfectadas > 0; // Devuelve true si el padre se borró

    } catch (SQLException ex) {
        System.err.println("Error en SQL al eliminar, revirtiendo: " + ex.getMessage());
        try {
            // 6. REVERTIR EN CASO DE ERROR (Rollback)
            if (cnx != null) {
                cnx.rollback();
            }
        } catch (SQLException eRollback) {
            System.err.println("Error al hacer rollback: " + eRollback.getMessage());
        }
        return false;
        
    } finally {
        // 7. Limpiar recursos y restaurar el autoCommit
        try {
            if (stmtHijos != null) stmtHijos.close();
            if (stmtPadre != null) stmtPadre.close();
            if (cnx != null) {
                cnx.setAutoCommit(true); // Restaurar estado
                cnx.close(); // Cerrar conexión manualmente
            }
        } catch (SQLException eFinal) {
            System.err.println("Error al cerrar recursos: " + eFinal.getMessage());
        }
    }
}

    /**
     * Busca y devuelve una única transacción por su número.
     * @param nroTransaccion El número de la transacción a buscar.
     * @return El objeto TransaccionTarjetaCliente si se encuentra, o null si no existe.
     */
    public TransaccionTarjetaCliente buscarPorNroTransaccion(int nroTransaccion) {
        TransaccionTarjetaCliente tx = null;
        String query = "SELECT * FROM transaccion_tarjeta_cliente WHERE nro_transaccion = ?";
        
        try (Connection cnx = new Conexion().obtenerConexion();
             PreparedStatement stmt = cnx.prepareStatement(query)) {
            
            stmt.setInt(1, nroTransaccion);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    tx = new TransaccionTarjetaCliente();
                    // ... Aquí iría el código para poblar el objeto tx con los datos del ResultSet
                    // (similar a como se hace en el método listar)
                }
            }
        } catch (SQLException e) {
            System.err.println("Error SQL al buscar la transacción: " + e.getMessage());
        }
        return tx;
    }

}