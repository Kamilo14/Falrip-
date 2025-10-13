/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

/**
 *
 * @author CAMILO
 */
public class Comuna {
    private int codComuna;
    private String nombreComuna;

    public Comuna() {
    }

    public Comuna(int codComuna, String nombreComuna) {
        this.codComuna = codComuna;
        this.nombreComuna = nombreComuna;
    }

    public int getCodComuna() {
        return codComuna;
    }

    public void setCodComuna(int codComuna) {
        this.codComuna = codComuna;
    }

    public String getNombreComuna() {
        return nombreComuna;
    }

    public void setNombreComuna(String nombreComuna) {
        this.nombreComuna = nombreComuna;
    }
    
    
}
