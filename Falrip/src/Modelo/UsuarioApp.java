/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

public class UsuarioApp {
    private int idUsuario;
    private String pNombre;
    private String sNombre;
    private String apPaterno;
    private String apMaterno;
    private int run;
    private String dv;
    private String nombreUsuario;
    private String clave;
    private String rol;
    private boolean activo;

    // Constructor vacío
    public UsuarioApp() {
    }

    // Constructor con todos los campos
    public UsuarioApp(int idUsuario, String pNombre, String sNombre, String apPaterno, String apMaterno, int run, String dv, String nombreUsuario, String clave, String rol, boolean activo) {
        this.idUsuario = idUsuario;
        this.pNombre = pNombre;
        this.sNombre = sNombre;
        this.apPaterno = apPaterno;
        this.apMaterno = apMaterno;
        this.run = run;
        this.dv = dv;
        this.nombreUsuario = nombreUsuario;
        this.clave = clave;
        this.rol = rol;
        this.activo = activo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getPNombre() {
        return pNombre;
    }

    public void setPNombre(String pNombre) {
        this.pNombre = pNombre;
    }

    public String getSNombre() {
        return sNombre;
    }

    public void setSNombre(String sNombre) {
        this.sNombre = sNombre;
    }

    public String getApPaterno() {
        return apPaterno;
    }

    public void setApPaterno(String apPaterno) {
        this.apPaterno = apPaterno;
    }

    public String getApMaterno() {
        return apMaterno;
    }

    public void setApMaterno(String apMaterno) {
        this.apMaterno = apMaterno;
    }

    public int getRun() {
        return run;
    }

    public void setRun(int run) {
        this.run = run;
    }

    public String getDv() {
        return dv;
    }

    public void setDv(String dv) {
        this.dv = dv;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }
    
}