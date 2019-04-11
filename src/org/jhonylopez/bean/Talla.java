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
public class Talla {
    private int codigoTalla;
    private String descripcion;
    
    public Talla(){}
    
    public Talla(int codigoTalla, String descripcion)
    {
        this.codigoTalla = codigoTalla;
        this.descripcion = descripcion;
    }
    
    public void setCodigoTalla(int codigoTalla)
    {
        this.codigoTalla = codigoTalla;
    }
    
    public void setDescripcion(String descripcion)
    {
        this.descripcion = descripcion;
    }
    
    public int getCodigoTalla()
    {
        return codigoTalla;
    }

    public String getDescripcion() 
    {
        return descripcion;
    }
    
    @Override
    public String toString()
    {
        return getCodigoTalla()+ ")  " + getDescripcion();
    }
}
