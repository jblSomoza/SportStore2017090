/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhonylopez.controller;

import org.jhonylopez.bean.Compra;
import org.jhonylopez.bean.Proveedor;
import org.jhonylopez.db.Conexion;
import org.jhonylopez.sistema.Principal;

import java.util.ArrayList;
import java.util.Date;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.control.cell.PropertyValueFactory;

import eu.schudt.javafx.controls.calendar.DatePicker;

import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat; //Muestra la fecha como tipo texto

import java.util.Locale;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JOptionPane;

/**
 *
 * @author bryan
 */
public class CompraController implements Initializable{
    
    private enum operaciones{NUEVO, GUARDAR, EDITAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<Compra> listaCompra;
    private ObservableList<Proveedor> listaProveedor;
    private DatePicker fecha;
    
    @FXML private ComboBox cmbProveedor;

    @FXML private TextField txtNumeroDocumento;
    @FXML private TextField txtDescripcion;
    
    @FXML private GridPane grpFecha;
    
    @FXML private TableView tblCompras;
    @FXML private TableColumn colNumeroDocumento;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colFecha;
    @FXML private TableColumn colTotal;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbProveedor.setItems(getProveedores());
        fecha = new DatePicker(Locale.ENGLISH);
        fecha.setDateFormat(new SimpleDateFormat("yyyy-mm-dd"));
        fecha.getCalendarView().todayButtonTextProperty().set("Today");
        fecha.getCalendarView().setShowWeeks(false);
        fecha.getStylesheets().add("/org/jhonylopez/resource/DatePicker.css");
        grpFecha.add(fecha, 0, 0);
    }
    
    public void cargarDatos()
    {
        tblCompras.setItems(getCompras());
        colNumeroDocumento.setCellValueFactory(new PropertyValueFactory<Compra, Integer>("numeroDocumentos"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<Compra, String>("descripcion"));
        colFecha.setCellValueFactory(new PropertyValueFactory<Compra, Date>("fecha"));
        colTotal.setCellValueFactory(new PropertyValueFactory<Compra, Double>("total"));
    }
    
    public ObservableList<Proveedor> getProveedores(){
        ArrayList<Proveedor> lista = new ArrayList<Proveedor>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarProveedores}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next())
            {
                 lista.add(new Proveedor(resultado.getInt("codigoProveedor"),
                    resultado.getString("nit"),
                    resultado.getString("razonSocial"),
                    resultado.getString("contactoPrincipal"),
                    resultado.getString("direccion"),
                    resultado.getString("paginaWeb")));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
            e.getMessage();
        }
        return listaProveedor = FXCollections.observableList(lista);
    }
    
    public ObservableList<Compra> getCompras()
    {
        ArrayList<Compra> lista = new ArrayList<Compra>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCompras}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next())
            {
                lista.add(new Compra(resultado.getInt("numeroDocumento"), resultado.getString("descripcion"),
                    resultado.getDate("fecha"), resultado.getDouble("total"), resultado.getInt("codigoProveedor"),
                    resultado.getString("nit"), resultado.getString("razonSocial"), resultado.getString("contactoPrincipal"),
                    resultado.getString("direccion"), resultado.getString("paginaWeb")));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
            e.getMessage();
        }
        return listaCompra = FXCollections.observableList(lista);
    }
    
    public void nuevo()
    {
        switch(tipoDeOperacion)
        {
            case NINGUNO:
                limpiarControles();
                txtNumeroDocumento.setEditable(false);
                txtDescripcion.setEditable(true);
                grpFecha.setDisable(false);
                cmbProveedor.setDisable(false);
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                btnReporte.setDisable(true);
                tipoDeOperacion = operaciones.GUARDAR;
                break;
            
            case GUARDAR:
                txtNumeroDocumento.setEditable(false);
                txtDescripcion.setEditable(false);
                grpFecha.setDisable(true);
                cmbProveedor.setDisable(true);
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                guardar();
                cargarDatos();
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void guardar()
    {
        try
        {
            Compra nuevaCompra = new Compra();
            nuevaCompra.setFecha(fecha.getSelectedDate());
            nuevaCompra.setDescripcion(txtDescripcion.getText());
            nuevaCompra.setTotal(0.00);
            Proveedor proveedor = (Proveedor) cmbProveedor.getSelectionModel().getSelectedItem();
            nuevaCompra.setCodigoProveedor(proveedor.getCodigoProveedor());
            nuevaCompra.setNit(proveedor.getNit());
            nuevaCompra.setRazonSocial(proveedor.getRazonSocial());
            nuevaCompra.setContactoPrincipal(proveedor.getContactoPrincipal());
            nuevaCompra.setDireccion(proveedor.getDireccion());
            nuevaCompra.setPaginaWeb(proveedor.getPaginaWeb());
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCompra(?,?,?,?)}");
            procedimiento.setString(1, nuevaCompra.getDescripcion());
            procedimiento.setDate(2, new java.sql.Date(nuevaCompra.getFecha().getTime()));
            procedimiento.setDouble(3, nuevaCompra.getTotal());
            procedimiento.setInt(4, nuevaCompra.getCodigoProveedor());
            procedimiento.execute(); //Se usa cuando son varios registro 
            listaCompra.add(nuevaCompra);
        }catch(SQLException e)
        {
            e.printStackTrace();
            e.getMessage();
        }
    }
    
    public void editar()
    {
    
    }
    
    public void eliminar()
    {
    
    }
    
    public void detalleCompra()
    {
        if(tblCompras.getSelectionModel().getSelectedItem() != null)
        {
            escenarioPrincipal.ventanaDetalleCompras((Compra)tblCompras.getSelectionModel().getSelectedItem());
        }else
        {
            JOptionPane.showMessageDialog(null, "Debe seleccionar un registro");
        }
    }
    
    public void limpiarControles()
    {
        txtNumeroDocumento.setText("");
        txtDescripcion.setText("");
    }    

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal()
    {
        escenarioPrincipal.menuPrincipal();
    }    
}
