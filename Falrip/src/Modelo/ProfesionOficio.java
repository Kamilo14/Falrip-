/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author CAMILO
 */
public class ProfesionOficio {
    private int codProfOfic;
    private String nombreProfesion;

    public ProfesionOficio() {
    }

    public ProfesionOficio(int codProfOfic, String nombreProfesion) {
        this.codProfOfic = codProfOfic;
        this.nombreProfesion = nombreProfesion;
    }

    public int getCodProfOfic() {
        return codProfOfic;
    }

    public void setCodProfOfic(int codProfOfic) {
        this.codProfOfic = codProfOfic;
    }

    public String getNombreProfesion() {
        return nombreProfesion;
    }

    public void setNombreProfesion(String nombreProfesion) {
        this.nombreProfesion = nombreProfesion;
    }
    
    
@Override
    public String toString() {
    if (this.getNombreProfesion()== null) {
        return "Seleccione Profesion";
    }
    return this.getNombreProfesion();
}
    
    
}
