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
public class Producto {
    
    private int codigoProducto;
    private String descripcion;
    private int existencia;
    private double precioUnitario;
    private double precioPorDocena;
    private double precioPorMayor;
    private String imagen;
    private int codigoCategoria;
    private String categoria;
    private int codigoMarca;
    private String marca;
    private int codigoTalla;
    private String talla;

    public Producto(int codigoProducto, String descripcion, int existencia, double precioUnitario, double precioPorDocena, double precioPorMayor, String imagen, int codigoCategoria, String categoria, int codigoMarca, String marca, int codigoTalla, String talla) {
        this.codigoProducto = codigoProducto;
        this.descripcion = descripcion;
        this.existencia = existencia;
        this.precioUnitario = precioUnitario;
        this.precioPorDocena = precioPorDocena;
        this.precioPorMayor = precioPorMayor;
        this.imagen = imagen;
        this.codigoCategoria = codigoCategoria;
        this.categoria = categoria;
        this.codigoMarca = codigoMarca;
        this.marca = marca;
        this.codigoTalla = codigoTalla;
        this.talla = talla;
    }

    public Producto() {
    }

    public void setCodigoProducto(int codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public void setExistencia(int existencia) {
        this.existencia = existencia;
    }

    public void setPrecioUnitario(double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public void setPrecioPorDocena(double precioPorDocena) {
        this.precioPorDocena = precioPorDocena;
    }

    public void setPrecioPorMayor(double precioPorMayor) {
        this.precioPorMayor = precioPorMayor;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setCodigoCategoria(int codigoCategoria) {
        this.codigoCategoria = codigoCategoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public void setCodigoMarca(int codigoMarca) {
        this.codigoMarca = codigoMarca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setCodigoTalla(int codigoTalla) {
        this.codigoTalla = codigoTalla;
    }

    public void setTalla(String talla) {
        this.talla = talla;
    }

    public int getCodigoProducto() {
        return codigoProducto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public int getExistencia() {
        return existencia;
    }

    public double getPrecioUnitario() {
        return precioUnitario;
    }

    public double getPrecioPorDocena() {
        return precioPorDocena;
    }

    public double getPrecioPorMayor() {
        return precioPorMayor;
    }

    public String getImagen() {
        return imagen;
    }

    public int getCodigoCategoria() {
        return codigoCategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public int getCodigoMarca() {
        return codigoMarca;
    }

    public String getMarca() {
        return marca;
    }

    public int getCodigoTalla() {
        return codigoTalla;
    }

    public String getTalla() {
        return talla;
    }
    
    

    
}
