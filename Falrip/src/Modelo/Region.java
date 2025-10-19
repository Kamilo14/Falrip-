/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author CAMILO
 */
public class Region {
    private int codRegion;
    private String nombreRegion;

    public Region() {
    }

    public Region(int codRegion, String nombreRegion) {
        this.codRegion = codRegion;
        this.nombreRegion = nombreRegion;
    }
    
    

    public int getCodRegion() {
        return codRegion;
    }

    public void setCodRegion(int codRegion) {
        this.codRegion = codRegion;
    }

    public String getNombreRegion() {
        return nombreRegion;
    }

    public void setNombreRegion(String nombreRegion) {
        this.nombreRegion = nombreRegion;
    }

    @Override
    public String toString() {
        // Si el nombre es null (como el item "Seleccione..."), muestra un texto
        if (this.getNombreRegion() == null) {
            return "Seleccione Región";
        }
        return this.getNombreRegion(); 
    }
    
    
}
