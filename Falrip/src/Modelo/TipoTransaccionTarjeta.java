/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Basaes
 */
public class TipoTransaccionTarjeta {
    private int codTptranTarjeta;
    private String nombreTptranTarjeta;
    private double tasaIntTptranTarjeta; 
    private int nroMaximoCuotasTran;
    private Producto producto;

    public TipoTransaccionTarjeta() {
    }

    public TipoTransaccionTarjeta(int codTptranTarjeta, String nombreTptranTarjeta, double tasaIntTptranTarjeta, int nroMaximoCuotasTran, Producto producto) {
        this.codTptranTarjeta = codTptranTarjeta;
        this.nombreTptranTarjeta = nombreTptranTarjeta;
        this.tasaIntTptranTarjeta = tasaIntTptranTarjeta;
        this.nroMaximoCuotasTran = nroMaximoCuotasTran;
        this.producto = producto;
    }

    public int getCodTptranTarjeta() {
        return codTptranTarjeta;
    }

    public void setCodTptranTarjeta(int codTptranTarjeta) {
        this.codTptranTarjeta = codTptranTarjeta;
    }

    public String getNombreTptranTarjeta() {
        return nombreTptranTarjeta;
    }

    public void setNombreTptranTarjeta(String nombreTptranTarjeta) {
        this.nombreTptranTarjeta = nombreTptranTarjeta;
    }

    public double getTasaIntTptranTarjeta() {
        return tasaIntTptranTarjeta;
    }

    public void setTasaIntTptranTarjeta(double tasaIntTptranTarjeta) {
        this.tasaIntTptranTarjeta = tasaIntTptranTarjeta;
    }

    public int getNroMaximoCuotasTran() {
        return nroMaximoCuotasTran;
    }

    public void setNroMaximoCuotasTran(int nroMaximoCuotasTran) {
        this.nroMaximoCuotasTran = nroMaximoCuotasTran;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }
    
    
    
    
    
}
