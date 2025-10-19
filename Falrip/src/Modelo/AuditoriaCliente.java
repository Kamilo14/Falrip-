/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Modelo;

import java.util.Date;
import java.sql.Timestamp;
/**
 *
 * @author Basaes
 */
public class AuditoriaCliente {
    
    private int idAuditoria;
    private String usuarioApp;
    private Timestamp fechaEvento;
    private String tipoOperacion;
    private String descripcion;

    public AuditoriaCliente() {
    }

    public AuditoriaCliente(int idAuditoria, String usuarioApp, Timestamp fechaEvento, String tipoOperacion, String descripcion) {
        this.idAuditoria = idAuditoria;
        this.usuarioApp = usuarioApp;
        this.fechaEvento = fechaEvento;
        this.tipoOperacion = tipoOperacion;
        this.descripcion = descripcion;
    }

    public int getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(int idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public String getUsuarioApp() {
        return usuarioApp;
    }

    public void setUsuarioApp(String usuarioApp) {
        this.usuarioApp = usuarioApp;
    }

    public Timestamp getFechaEvento() {
        return fechaEvento;
    }

    public void setFechaEvento(Timestamp fechaEvento) {
        this.fechaEvento = fechaEvento;
    }

    public String getTipoOperacion() {
        return tipoOperacion;
    }

    public void setTipoOperacion(String tipoOperacion) {
        this.tipoOperacion = tipoOperacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
    
    
    
}
