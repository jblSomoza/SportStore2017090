/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhonylopez.reportes;

import org.jhonylopez.db.Conexion;

import org.jhonylopez.bean.Cliente;

import net.sf.jasperreports.engine.util.JRLoader; //Carga el reporte
import net.sf.jasperreports.engine.JasperReport; //Crea el informe
import net.sf.jasperreports.engine.JasperPrint; //Imprime
import net.sf.jasperreports.engine.JasperFillManager; //Rellena la informacion al archivo
import net.sf.jasperreports.view.JasperViewer; //Archivo Previo
import java.util.Map; //Es una conexion de objetos, se usa para enviar los parametros
import java.io.InputStream; //Lectura al archivo
import java.util.HashMap;


/**
 *
 * @author informatica
 */
public class GenerarReporte {
    public static void mostrarReporte(String nombreReporte,String titulo,Map parametros)
    {
        InputStream reporte = GenerarReporte.class.getResourceAsStream(nombreReporte); // 
        try
        {
            JasperReport reporteMaestro = (JasperReport) JRLoader.loadObject(reporte);
            JasperPrint reporteImpreso = JasperFillManager.fillReport(reporteMaestro, parametros,Conexion.getInstancia().getConexion());
            JasperViewer visor = new JasperViewer(reporteImpreso,false);
            visor.setTitle(titulo);
            visor.setVisible(true);
        }catch(Exception e)
        {
            e.printStackTrace();
            e.getMessage();
        }
    }
    
    public void generarReporteCategorias()
    {
        Map parametro = new HashMap();
        int CodigoCategoria = 0;
        parametro.put("codigoCategoria",CodigoCategoria);
        GenerarReporte.mostrarReporte("ReporteCategoria.jasper", "Reporte de Categorias", parametro);
    }
    
    public void generarReporteTallas()
    {
        Map parametro = new HashMap();
        int CodigoTalla = 0;
        parametro.put("codigoTalla",CodigoTalla);
        GenerarReporte.mostrarReporte("ReporteTalla.jasper", "Reporte de Tallas", parametro);
    }
    
    public void generarReporteMarcas()
    {
        Map parametro = new HashMap();
        int CodigoMarca = 0;
        parametro.put("codigoMarca",CodigoMarca);
        GenerarReporte.mostrarReporte("ReporteMarca.jasper", "Reporte de Marcas", parametro);
    }
    
    public void generarReporteProveedores()
    {
        Map parametros = new HashMap();
        parametros.put("_NumeroDocumento", null);
        GenerarReporte.mostrarReporte("ReporteProveedor.jasper", "Reporte de Productos", parametros);
    }
    
    public void generarReporteProductos()
    {
        Map parametros = new HashMap();
        parametros.put("_NumeroDocumento", null);
        GenerarReporte.mostrarReporte("ReporteProducto.jasper", "Reporte de Productos", parametros);
    }
    
    public void generarReporteClientes()
    {
        Map parametro = new HashMap();
        int CodigoCliente = 0;
        parametro.put("codigoCliente",CodigoCliente);
        GenerarReporte.mostrarReporte("ReporteCliente.jasper", "Reporte de Clientes", parametro);
    }
    
    public void generarReporteTelefonoClientes()
    {
        Map parametro = new HashMap();
        int CodigoCliente = 0;
        parametro.put("codigoTelefonoCliente",CodigoCliente);
        GenerarReporte.mostrarReporte("ReporteCliente_Telefono.jasper.jasper", "Reporte de Telefono Clientes", parametro);
    }
     
}
