/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Date;

/**
 *
 * @author CAMILO
 */
public class TransaccionTarjetaCliente {
    private int nroTransaccion;
    private Date fechaTransaccion;
    private double montoTransaccion;
    private int totalCuotasTransaccion;
    private double montoTotalTransaccion; // El monto * el interés, si aplica
    private TarjetaCliente tarjeta;
    private TipoTransaccionTarjeta tipoTransaccion;

    public TransaccionTarjetaCliente() {
    }

    public TransaccionTarjetaCliente(int nroTransaccion, Date fechaTransaccion, double montoTransaccion, int totalCuotasTransaccion, double montoTotalTransaccion, TarjetaCliente tarjeta, TipoTransaccionTarjeta tipoTransaccion) {
        this.nroTransaccion = nroTransaccion;
        this.fechaTransaccion = fechaTransaccion;
        this.montoTransaccion = montoTransaccion;
        this.totalCuotasTransaccion = totalCuotasTransaccion;
        this.montoTotalTransaccion = montoTotalTransaccion;
        this.tarjeta = tarjeta;
        this.tipoTransaccion = tipoTransaccion;
    }

    public int getNroTransaccion() {
        return nroTransaccion;
    }

    public void setNroTransaccion(int nroTransaccion) {
        this.nroTransaccion = nroTransaccion;
    }

    public Date getFechaTransaccion() {
        return fechaTransaccion;
    }

    public void setFechaTransaccion(Date fechaTransaccion) {
        this.fechaTransaccion = fechaTransaccion;
    }

    public double getMontoTransaccion() {
        return montoTransaccion;
    }

    public void setMontoTransaccion(double montoTransaccion) {
        this.montoTransaccion = montoTransaccion;
    }

    public int getTotalCuotasTransaccion() {
        return totalCuotasTransaccion;
    }

    public void setTotalCuotasTransaccion(int totalCuotasTransaccion) {
        this.totalCuotasTransaccion = totalCuotasTransaccion;
    }

    public double getMontoTotalTransaccion() {
        return montoTotalTransaccion;
    }

    public void setMontoTotalTransaccion(double montoTotalTransaccion) {
        this.montoTotalTransaccion = montoTotalTransaccion;
    }

    public TarjetaCliente getTarjeta() {
        return tarjeta;
    }

    public void setTarjeta(TarjetaCliente tarjeta) {
        this.tarjeta = tarjeta;
    }

    public TipoTransaccionTarjeta getTipoTransaccion() {
        return tipoTransaccion;
    }

    public void setTipoTransaccion(TipoTransaccionTarjeta tipoTransaccion) {
        this.tipoTransaccion = tipoTransaccion;
    }
    
    
    
    
    
    
}
