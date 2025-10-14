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
public class TarjetaCliente {
    private String nroTarjeta;
    private Cliente cliente; // Para saber a qué cliente pertenece mediante el rut
    private Date fechaCreacion; //Fecha de solicitud
    private int diaPagoMensual;
    private int cupoCompra;
    private int cupoSuperAvance;
    private int cupoDisponibleCompra;
    private int cupoDisponibleSuperAvance;

    public TarjetaCliente() {
    }

    public TarjetaCliente(String nroTarjeta, Cliente cliente, Date fechaCreacion, int diaPagoMensual, int cupoCompra, int cupoSuperAvance, int cupoDisponibleCompra, int cupoDisponibleSuperAvance) {
        this.nroTarjeta = nroTarjeta;
        this.cliente = cliente;
        this.fechaCreacion = fechaCreacion;
        this.diaPagoMensual = diaPagoMensual;
        this.cupoCompra = cupoCompra;
        this.cupoSuperAvance = cupoSuperAvance;
        this.cupoDisponibleCompra = cupoDisponibleCompra;
        this.cupoDisponibleSuperAvance = cupoDisponibleSuperAvance;
    }

    public String getNroTarjeta() {
        return nroTarjeta;
    }

    public void setNroTarjeta(String nroTarjeta) {
        this.nroTarjeta = nroTarjeta;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Date getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public int getDiaPagoMensual() {
        return diaPagoMensual;
    }

    public void setDiaPagoMensual(int diaPagoMensual) {
        this.diaPagoMensual = diaPagoMensual;
    }

    public int getCupoCompra() {
        return cupoCompra;
    }

    public void setCupoCompra(int cupoCompra) {
        this.cupoCompra = cupoCompra;
    }

    public int getCupoSuperAvance() {
        return cupoSuperAvance;
    }

    public void setCupoSuperAvance(int cupoSuperAvance) {
        this.cupoSuperAvance = cupoSuperAvance;
    }

    public int getCupoDisponibleCompra() {
        return cupoDisponibleCompra;
    }

    public void setCupoDisponibleCompra(int cupoDisponibleCompra) {
        this.cupoDisponibleCompra = cupoDisponibleCompra;
    }

    public int getCupoDisponibleSuperAvance() {
        return cupoDisponibleSuperAvance;
    }

    public void setCupoDisponibleSuperAvance(int cupoDisponibleSuperAvance) {
        this.cupoDisponibleSuperAvance = cupoDisponibleSuperAvance;
    }
    
    
    
}
