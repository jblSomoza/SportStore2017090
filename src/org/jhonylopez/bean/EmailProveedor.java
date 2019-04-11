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
public class EmailProveedor {
    private int codigoEmailProveedor;
    private String email;
    private String descripcion;
    private int codigoProveedor;

    public EmailProveedor(int codigoEmailProveedor, String email, String descripcion, int codigoProveedor) {
        this.codigoEmailProveedor = codigoEmailProveedor;
        this.email = email;
        this.descripcion = descripcion;
        this.codigoProveedor = codigoProveedor;
    }

    public EmailProveedor() {
    }

    public int getCodigoEmailProveedor() {
        return codigoEmailProveedor;
    }

    public void setCodigoEmailProveedor(int codigoEmailProveedor) {
        this.codigoEmailProveedor = codigoEmailProveedor;
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

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }
    
    public String toString()
    {
        return getCodigoEmailProveedor()+ "";
    }
}
