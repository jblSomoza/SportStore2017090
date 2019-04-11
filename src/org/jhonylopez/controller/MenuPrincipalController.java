/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhonylopez.controller;

import org.jhonylopez.reportes.GenerarReporte;

import javafx.fxml.Initializable; 
import org.jhonylopez.sistema.Principal; 
import java.net.URL;
import java.util.ResourceBundle;

/**
 *
 * @author informatica
 */
public class MenuPrincipalController implements Initializable {
    private Principal escenarioPrincipal;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    } 

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaCategoria(){ 
        escenarioPrincipal.ventanaCategorias();
    }
    
    public void ventanaMarca()
    {
        escenarioPrincipal.ventanaMarcas();
    }
    
    public void ventanaTalla()
    {
        escenarioPrincipal.ventanaTallas();
    }
    
    public void ventanaCliente()
    {
        escenarioPrincipal.ventanaClientes();
    }
    
    public void ventanaProducto()
    {
        escenarioPrincipal.ventanaProductos();
    }
    
    public void ventanaProveedor()
    {
        escenarioPrincipal.ventanaProveedores();
    }
    
    public void ventanaCompra()
    {
        escenarioPrincipal.ventanaCompras();
    }
    
    
    public void reporteCliente()
    {
        GenerarReporte.mostrarReporte("ReporteCliente.jasper", "Reporte Cliente", null);
    }
    
    public void reporteCategoria()
    {
        GenerarReporte.mostrarReporte("ReporteCategoria.jasper", "Reporte Categoria", null);
    }
    
    public void reporteTalla()
    {
        GenerarReporte.mostrarReporte("ReporteTalla.jasper", "Reporte Talla", null);
    }
    
    public void reporteMarca()
    {
        GenerarReporte.mostrarReporte("ReporteMarca.jasper", "Reporte Marca", null);
    }
    
    public void reporteProveedor()
    {
        GenerarReporte.mostrarReporte("ReporteProveedor.jasper", "Reporte Proveedor", null);
    }
    
    public void reporteProducto()
    {
        GenerarReporte.mostrarReporte("ReporteProducto.jasper", "Reporte Producto", null);
    }
}

