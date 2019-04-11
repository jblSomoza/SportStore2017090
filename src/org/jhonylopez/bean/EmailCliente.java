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
public class EmailCliente {
    
    private int codigoEmailCliente;
    private int codigoCliente;
    private String email;
    private String descripcion;

    public EmailCliente(int codigoEmailCliente, int codigoCliente, String email, String descripcion) {
        this.codigoEmailCliente = codigoEmailCliente;
        this.codigoCliente = codigoCliente;
        this.email = email;
        this.descripcion = descripcion;
    }

    public EmailCliente() {
    }

    public int getCodigoEmailCliente() {
        return codigoEmailCliente;
    }

    public void setCodigoEmailCliente(int codigoEmailCliente) {
        this.codigoEmailCliente = codigoEmailCliente;
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    
}
