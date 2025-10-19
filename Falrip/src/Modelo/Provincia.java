/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author CAMILO y Fabian
 */
public class Provincia {
    private int codProvincia;
    private String nombreProvincia;
    private Region region;

    public Provincia() {
    }

    public Provincia(int codProvincia, String nombreProvincia) {
        this.codProvincia = codProvincia;
        this.nombreProvincia = nombreProvincia;
    }

    public Provincia(int codProvincia, String nombreProvincia, Region region) {
        this.codProvincia = codProvincia;
        this.nombreProvincia = nombreProvincia;
        this.region = region;
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

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    @Override
public String toString() {
    if (this.getNombreProvincia() == null) {
        return "Seleccione Provincia";
    }
    return this.getNombreProvincia();
    
}
    
}
