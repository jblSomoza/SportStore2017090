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
public class Cliente {
    private int codigoCliente;
    private String nombre;
    private String nit;
    private String direccion;

    public Cliente(int codigoCliente, String nombre, String nit, String direccion) {
        this.codigoCliente = codigoCliente;
        this.nombre = nombre;
        this.nit = nit;
        this.direccion = direccion;
    }

    public Cliente() {
    }

    public int getCodigoCliente() {
        return codigoCliente;
    }

    public void setCodigoCliente(int codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public String toString() {
        return getCodigoCliente() + ")  " + getNombre();
    }
    
    

    
    
}
