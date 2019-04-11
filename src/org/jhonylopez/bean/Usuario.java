/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhonylopez.bean;

/**
 *
 * @author informatica
 */
public class Usuario {
    
    private int codigoUsuario;
    private String nombreUsuario;
    private String email;
    private String usuario;
    private String contraseña;
    private String tipoDeUsuario;

    public Usuario(int codigoUsuario, String nombreUsuario, String email, String usuario, String contraseña, String tipoDeUsuario) {
        this.codigoUsuario = codigoUsuario;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.usuario = usuario;
        this.contraseña = contraseña;
        this.tipoDeUsuario = tipoDeUsuario;
    }

    public Usuario() {
    }

    public int getCodigoUsuario() {
        return codigoUsuario;
    }

    public void setCodigoUsuario(int codigoUsuario) {
        this.codigoUsuario = codigoUsuario;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getTipoDeUsuario() {
        return tipoDeUsuario;
    }

    public void setTipoDeUsuario(String tipoDeUsuario) {
        this.tipoDeUsuario = tipoDeUsuario;
    }

    @Override
    public String toString() {
        return getCodigoUsuario() + ")  " + getUsuario();
    }  
}
