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
public class Categoria {
    private int codigoCategoria;
    private String descripcion;       

    public Categoria(){}
    
    public Categoria(int codigoCategoria, String descripcion) 
    {
        this.codigoCategoria = codigoCategoria;
        this.descripcion = descripcion;
    }

    public void setCodigoCategoria(int codigoCategoria) 
    {
        this.codigoCategoria = codigoCategoria;
    }

    public void setDescripcion(String descripcion) 
    {
        this.descripcion = descripcion;
    }
    
    public int getCodigoCategoria() 
    {
        return codigoCategoria;
    }

    public String getDescripcion() 
    {
        return descripcion;
    }  
    
   
    @Override
    public String toString()
    {
        return getCodigoCategoria()+ ")  " + getDescripcion();
    }
}
