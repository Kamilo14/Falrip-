/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author CAMILO
 */
public class Provincia {
    private int codProvincia;
    private String nombreProvincia;

    public Provincia() {
    }

    public Provincia(int codProvincia, String nombreProvincia) {
        this.codProvincia = codProvincia;
        this.nombreProvincia = nombreProvincia;
    }

    public int getCodProvincia() {
        return codProvincia;
    }

    public void setCodProvincia(int codProvincia) {
        this.codProvincia = codProvincia;
    }

    public String getNombreProvincia() {
        return nombreProvincia;
    }

    public void setNombreProvincia(String nombreProvincia) {
        this.nombreProvincia = nombreProvincia;
    }
    
    
}
