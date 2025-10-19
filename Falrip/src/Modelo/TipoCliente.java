/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author CAMILO
 */
public class TipoCliente {
    private int codTipoCliente;
    private String nombreTipoCliente;
    private String promedioRenta;

    public TipoCliente() {
    }

    public TipoCliente(int codTipoCliente, String nombreTipoCliente, String promedioRenta) {
        this.codTipoCliente = codTipoCliente;
        this.nombreTipoCliente = nombreTipoCliente;
        this.promedioRenta = promedioRenta;
    }

    public int getCodTipoCliente() {
        return codTipoCliente;
    }

    public void setCodTipoCliente(int codTipoCliente) {
        this.codTipoCliente = codTipoCliente;
    }

    public String getNombreTipoCliente() {
        return nombreTipoCliente;
    }

    public void setNombreTipoCliente(String nombreTipoCliente) {
        this.nombreTipoCliente = nombreTipoCliente;
    }

    public String getPromedioRenta() {
        return promedioRenta;
    }

    public void setPromedioRenta(String promedioRenta) {
        this.promedioRenta = promedioRenta;
    }
    
    
    @Override
    public String toString() {
    if (this.getNombreTipoCliente()== null) {
        return "Seleccione Tipo Cliente";
    }
    return this.getNombreTipoCliente();
}
    
}
