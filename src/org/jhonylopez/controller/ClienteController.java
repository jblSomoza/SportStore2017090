/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
*/ 
package org.jhonylopez.controller;

import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.jhonylopez.db.Conexion;
import org.jhonylopez.bean.Cliente;
import org.jhonylopez.sistema.Principal;
import org.jhonylopez.reportes.GenerarReporte;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import javafx.scene.control.TableView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author bryan
**/ 
public class ClienteController implements Initializable {
    private enum operaciones{NUEVO,GUARDAR,EDITAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private Cliente escenarioProveedor;
    
    @FXML private ComboBox cmbCliente;
    
    @FXML private TextField txtNombre;
    @FXML private TextField txtNit;
    @FXML private TextField txtDireccion;
    
    @FXML private TableView tblClientes;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colNombre;
    @FXML private TableColumn colNit;
    @FXML private TableColumn colDireccion;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    private ObservableList <Cliente> listaCliente;
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        cargarDatos();
        cmbCliente.setItems(getCliente());
    }
    
    public void cargarDatos()
    {
        tblClientes.setItems(getCliente());
        colCodigo.setCellValueFactory((new PropertyValueFactory <Cliente, Integer>("codigoCliente")));
        colNombre.setCellValueFactory((new PropertyValueFactory<Cliente, String>("nombre")));
        colNit.setCellValueFactory(new PropertyValueFactory <Cliente, String>("nit"));
        colDireccion.setCellValueFactory(new PropertyValueFactory<Cliente, String>("direccion"));
    }
    
    public ObservableList <Cliente> getCliente()
    {
        ArrayList <Cliente> lista = new ArrayList <Cliente>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarClientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next())
            {
                lista.add(new Cliente(resultado.getInt("codigoCliente"),resultado.getString("nombre"),resultado.getString("nit"),resultado.getString("direccion")));
            }
        }
        catch(SQLException e)
        {
            e.getMessage();
        }
        return listaCliente = FXCollections.observableList(lista);
    }
    
    public void nuevo()
    {
        switch(tipoDeOperacion)
        {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnNuevo.setText("Guardar");
                btnEliminar.setText("Cancelar");
                btnEditar.setDisable(true);
                tipoDeOperacion =  operaciones.GUARDAR;
                break;
            
            case GUARDAR:
                agregar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                cmbCliente.setItems(getCliente());
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                limpiarControles();
                break;
        }
    }
    
    public void agregar()
    {
        Cliente registro = new Cliente();
        registro.setNombre(txtNombre.getText());
        registro.setNit(txtNit.getText());
        registro.setDireccion(txtDireccion.getText());
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCliente(?,?,?)}");
            procedimiento.setString(1,registro.getNombre());
            procedimiento.setString(2, registro.getNit());
            procedimiento.setString(3,registro.getDireccion());
            procedimiento.execute();
            listaCliente.add(registro);
        }catch(SQLException e)
        {
            e.printStackTrace();
        }      
    }
    
    public void editar()
    {
        switch(tipoDeOperacion)
        {
            case NINGUNO:
                    if(tblClientes.getSelectionModel().getSelectedItem()!= null){
                        btnEditar.setText("Actualizar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        txtDireccion.setEditable(true);
                        txtNombre.setEditable(true);
                        txtNit.setEditable(true);
                        tipoDeOperacion = operaciones.ACTUALIZAR;
                    }else
                    {
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un dato a modificar");
                    }
                    break;
                    
                case ACTUALIZAR:
                    actualizar();
                    btnEditar.setText("Editar");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    break;
        }
    }
    
    public void actualizar()
    {
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarCliente(?,?,?,?)}");
            Cliente registro = (Cliente)tblClientes.getSelectionModel().getSelectedItem();
            registro.setNombre(txtNombre.getText());
            registro.setNit(txtNit.getText());
            registro.setDireccion(txtDireccion.getText());
            procedimiento.setInt(1,registro.getCodigoCliente());
            procedimiento.setString(2,registro.getNombre());
            procedimiento.setString(3, registro.getNit());
            procedimiento.setString(4, registro.getDireccion());
            procedimiento.execute();
         
        }catch(SQLException e)
        {
            e.getMessage();
            e.printStackTrace();
        }      
    }
    
    public void eliminar()
    {
        switch(tipoDeOperacion)
        {
            case GUARDAR:
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                limpiarControles();
                if(tblClientes.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro","Eliminar Categoria",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                    {
                        try
                        {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCliente(?)}");
                            procedimiento.setInt(1,((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente());
                            procedimiento.execute();
                        }catch(SQLException e)
                        {
                            e.getMessage();
                        }
                    }
                }cargarDatos();
        }
    }
    
    public Cliente buscarCliente(int codigoCliente)
    {
        Cliente resultado = null;
        
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCliente(?)}");
            procedimiento.setInt(1, codigoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next())
            {
                resultado = new Cliente(registro.getInt("codigoCliente"),registro.getString("nombre"),registro.getString("nit"),registro.getString("direccion"));
            }        
        }catch(SQLException e)
        {
          e.printStackTrace();
        }
        return resultado;
    }
    
    public void seleccionarElemento()
    {
        if(tblClientes.getSelectionModel().getSelectedIndex()>-1)
        {
            cmbCliente.getSelectionModel().select(buscarCliente(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente()));
            txtNombre.setText(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getNombre());
            txtNit.setText(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getNit());
            txtDireccion.setText(((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getDireccion());
        }
    }
    
    public void GenerarReporte()
    {
        if(tipoDeOperacion == operaciones.ACTUALIZAR)
        {
            desactivarControles();
            btnEditar.setText("Editar");
            btnReporte.setText("Reporte");
            btnNuevo.setDisable(false);
            btnEliminar.setDisable(false);
            tipoDeOperacion = operaciones.NINGUNO;
        }else if(tblClientes.getSelectionModel().getSelectedItem() == null)
        {
            JOptionPane.showMessageDialog(null, "Debe seleccionar en elemento");
        }else
        {
            Map parametro = new HashMap();
            int CodigoCliente = ((Cliente)tblClientes.getSelectionModel().getSelectedItem()).getCodigoCliente();
            parametro.put("codigoCliente",CodigoCliente);
            GenerarReporte.mostrarReporte("ReporteCliente.jasper", "Reporte de Clientes", parametro);
        }
    }
    
    public void activarControles()
    {
        txtDireccion.setEditable(true);
        txtNit.setEditable(true);
        txtNombre.setEditable(true);
    }
    
    public void desactivarControles()
    {
        txtDireccion.setEditable(false);
        txtNit.setEditable(false);
        txtNombre.setEditable(false);
    }
    
    public void limpiarControles()
    {
        cmbCliente.getSelectionModel().clearSelection();
        txtDireccion.setText("");
        txtNit.setText("");
        txtNombre.setText("");
    }
        
    private Principal getEscenarioPrincipal()
    {
        return escenarioPrincipal;
    }
        
    public  void setEscenarioPrincipal(Principal escenarioPrincipal)
    {
        this.escenarioPrincipal = escenarioPrincipal; 
    }
    
    public void ventanaTelefonoCliente()
    {
        escenarioPrincipal.ventanaTelefonoClientes();
    }
    
    public void ventanaEmailCliente()
    {
        escenarioPrincipal.ventanaEmailClientes();
    }
    
    public void menuPrincipal()
    {
        escenarioPrincipal.menuPrincipal();
    }
}