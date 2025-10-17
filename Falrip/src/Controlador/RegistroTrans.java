package Controlador;

import Modelo.Cliente;
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
     * Actualiza una transacción existente en la base de datos.
     * La búsqueda se hace por el número de transacción.
     * @param tx El objeto Transaccion con los datos a modificar.
     * @return true si la actualización fue exitosa, false en caso contrario.
     */
    public boolean actualizar(TransaccionTarjetaCliente tx) {
        String query = "UPDATE transaccion_tarjeta_cliente SET " +
                       "fecha_transaccion = ?, " +
                       "monto_transaccion = ?, " +
                       "total_cuotas_transaccion = ?, " +
                       "monto_total_transaccion = ?, " +
                       "cod_tptran_tarjeta = ? " +
                       "WHERE nro_transaccion = ?";

        try (Connection cnx = new Conexion().obtenerConexion();
             PreparedStatement stmt = cnx.prepareStatement(query)) {

            stmt.setDate(1, new java.sql.Date(tx.getFechaTransaccion().getTime()));
            stmt.setDouble(2, tx.getMontoTransaccion());
            stmt.setInt(3, tx.getTotalCuotasTransaccion());
            stmt.setDouble(4, tx.getMontoTotalTransaccion());
            stmt.setInt(5, tx.getTipoTransaccion().getCodTptranTarjeta());
            stmt.setInt(6, tx.getNroTransaccion()); // El ID para el WHERE

            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            System.err.println("Error en SQL al actualizar la transacción: " + ex.getMessage());
            return false;
        }
    }

    /**
     * Elimina una transacción de la base de datos.
     * @param nroTransaccion El número único de la transacción a eliminar.
     * @return true si la eliminación fue exitosa, false en caso contrario.
     */
    public boolean eliminar(int nroTransaccion) {
        String query = "DELETE FROM transaccion_tarjeta_cliente WHERE nro_transaccion = ?";
        try (Connection cnx = new Conexion().obtenerConexion();
             PreparedStatement stmt = cnx.prepareStatement(query)) {

            stmt.setInt(1, nroTransaccion);
            
            int filasAfectadas = stmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException ex) {
            System.err.println("Error en SQL al eliminar la transacción: " + ex.getMessage());
            return false;
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