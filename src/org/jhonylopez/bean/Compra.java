/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhonylopez.bean;

import java.util.Date;

/**
 *
 * @author bryan
 */
public class Compra {
    
    private int numeroDocumento;
    private String descripcion;
    private Date fecha;
    private double total;
    private int codigoProveedor;
    private String nit;
    private String razonSocial;
    private String contactoPrincipal;
    private String direccion;
    private String paginaWeb;

    public Compra(int numeroDocumento, String descripcion, Date fecha, double total, int codigoProveedor, String nit, String razonSocial, String contactoPrincipal, String direccion, String paginaWeb) {
        this.numeroDocumento = numeroDocumento;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.total = total;
        this.codigoProveedor = codigoProveedor;
        this.nit = nit;
        this.razonSocial = razonSocial;
        this.contactoPrincipal = contactoPrincipal;
        this.direccion = direccion;
        this.paginaWeb = paginaWeb;
    }

    public Compra() {
    }

    public int getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(int numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getCodigoProveedor() {
        return codigoProveedor;
    }

    public void setCodigoProveedor(int codigoProveedor) {
        this.codigoProveedor = codigoProveedor;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public String getContactoPrincipal() {
        return contactoPrincipal;
    }

    public void setContactoPrincipal(String contactoPrincipal) {
        this.contactoPrincipal = contactoPrincipal;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getPaginaWeb() {
        return paginaWeb;
    }

    public void setPaginaWeb(String paginaWeb) {
        this.paginaWeb = paginaWeb;
    }
    
    
    
    
}
