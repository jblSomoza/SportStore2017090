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
public class Marca {
    private int codigoMarca; //Propiedades
    private String descripcion;
    
    public Marca(){}
    
    public Marca(int codigoMarca, String descripcion)
    {
        this.codigoMarca = codigoMarca;
        this.descripcion = descripcion;
    }
    
    public void setCodigoMarca(int codigoMarca)
    {
        this.codigoMarca = codigoMarca;
    }
    
    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }
    
    public int getCodigoMarca()
    {
        return codigoMarca;
    }
    
    public String getDescripcion()
    {
        return descripcion;
    }
    
    @Override
    public String toString()
    {
        return getCodigoMarca() + ")  " + getDescripcion();
    }
}
