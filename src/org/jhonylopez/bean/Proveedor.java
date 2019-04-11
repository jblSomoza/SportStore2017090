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
public class Proveedor {
    
    private int codigoProveedor;
    private String nit;
    private String razonSocial;
    private String contactoPrincipal;
    private String direccion;
    private String paginaWeb;

    public Proveedor(int codigoProveedor, String nit, String razonSocial, String contactoPrincipal, String direccion, String paginaWeb) 
    {
        this.codigoProveedor = codigoProveedor;
        this.nit = nit;
        this.razonSocial = razonSocial;
        this.contactoPrincipal = contactoPrincipal;
        this.direccion = direccion;
        this.paginaWeb = paginaWeb;
    }

    public Proveedor() {
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public void setContactoPrincipal(String contactoPrincipal) {
        this.contactoPrincipal = contactoPrincipal;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public String getNit() {
        return nit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public String getContactoPrincipal() {
        return contactoPrincipal;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }
    
    public String toString()
    {
        return getCodigoProveedor()+ ")  " + getRazonSocial();
    }
    
    
}
