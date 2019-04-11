/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhonylopez.bean;

/**
 *
 * @author bryan
 */
public class TelefonoCliente {
    
    private int codigoTelefonoCliente;
    private int codigoCliente;
    private String numero;
    private String descripcion;

    public TelefonoCliente(int codigoTelefonoCliente, int codigoCliente, String numero, String descripcion) {
        this.codigoTelefonoCliente = codigoTelefonoCliente;
        this.codigoCliente = codigoCliente;
        this.numero = numero;
        this.descripcion = descripcion;
    }

    public TelefonoCliente() {
    }

    public int getCodigoTelefonoCliente() {
        return codigoTelefonoCliente;
    }

    public void setCodigoTelefonoCliente(int codigoTelefonoCliente) {
        this.codigoTelefonoCliente = codigoTelefonoCliente;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return getCodigoTelefonoCliente() + "";
    }
}   
