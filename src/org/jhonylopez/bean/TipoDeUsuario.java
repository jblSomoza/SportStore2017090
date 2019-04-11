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
public class TipoDeUsuario {
    
    private String descripcion;
    private int codigoTipoDeUsuario;

    public TipoDeUsuario(String descripcion, int codigoTipoDeUsuario) {
        this.descripcion = descripcion;
        this.codigoTipoDeUsuario = codigoTipoDeUsuario;
    }

    public TipoDeUsuario() {
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCodigoTipoDeUsuario() {
        return codigoTipoDeUsuario;
    }

    public void setCodigoTipoDeUsuario(int codigoTipoDeUsuario) {
        this.codigoTipoDeUsuario = codigoTipoDeUsuario;
    }

    @Override
    public String toString() {
        return getDescripcion();
    }

   
    
    
}
