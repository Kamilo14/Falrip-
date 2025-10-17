/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author Basaes
 */
public class Producto {
    private int codProducto;
    private String nombreProducto;
    private String descProducto;
    private double tasaIntProducto; // Usamos double para tasas con decimales
    private int nroMaximoCuotasProd;

    public Producto(int codProducto, String nombreProducto, String descProducto, double tasaIntProducto) {
        this.codProducto = codProducto;
        this.nombreProducto = nombreProducto;
        this.descProducto = descProducto;
        this.tasaIntProducto = tasaIntProducto;
        this.nroMaximoCuotasProd = nroMaximoCuotasProd;
    }

    public Producto() {
    }

    public int getCodProducto() {
        return codProducto;
    }

    public void setCodProducto(int codProducto) {
        this.codProducto = codProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getDescProducto() {
        return descProducto;
    }

    public void setDescProducto(String descProducto) {
        this.descProducto = descProducto;
    }

    public double getTasaIntProducto() {
        return tasaIntProducto;
    }

    public void setTasaIntProducto(double tasaIntProducto) {
        this.tasaIntProducto = tasaIntProducto;
    }

    public int getNroMaximoCuotasProd() {
        return nroMaximoCuotasProd;
    }

    public void setNroMaximoCuotasProd(int nroMaximoCuotasProd) {
        this.nroMaximoCuotasProd = nroMaximoCuotasProd;
    }
    
    
}
