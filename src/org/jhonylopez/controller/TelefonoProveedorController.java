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
import org.jhonylopez.bean.TelefonoProveedor;
import org.jhonylopez.bean.Proveedor;
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
 * @author informatica
 */
public class TelefonoProveedorController implements Initializable{
    
    
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<TelefonoProveedor> listaTelefonoProveedor;
    private ObservableList<Proveedor> listaProveedor;
    private Principal escenarioPrincipal;
    
    @FXML private ComboBox cmbTelefonoProveedor;
    @FXML private ComboBox cmbProveedor;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtNumero;
    
    @FXML private TableView tblTelefonoProveedores;
    @FXML private TableColumn colCodigoTelefono;
    @FXML private TableColumn  colCodigoProveedor;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colNumero;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
       cmbProveedor.setItems(getProveedor());
       cmbTelefonoProveedor.setItems(getTelefonoProveedor());
    }
    
   public void cargarDatos()
    {
        tblTelefonoProveedores.setItems(getTelefonoProveedor());
        colCodigoTelefono.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, Integer>("codigoTelefonoProveedor"));        
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, Integer>("codigoProveedor"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, String>("descripcion"));
        colNumero.setCellValueFactory(new PropertyValueFactory<TelefonoProveedor, String>("numero"));
    }
    
    public ObservableList<TelefonoProveedor> getTelefonoProveedor()
    {
        ArrayList<TelefonoProveedor> lista = new ArrayList<TelefonoProveedor>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTelefonosProveedores}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next())
            {
                lista.add(new TelefonoProveedor(resultado.getInt("codigoTelefonoProveedor"), resultado.getString("numero"),
                    resultado.getString("descripcion"), resultado.getInt("codigoProveedor")));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
            e.getMessage();
        }
        return listaTelefonoProveedor = FXCollections.observableList(lista);
    }
    
    public ObservableList<Proveedor> getProveedor()
    {
        ArrayList<Proveedor> lista = new ArrayList<Proveedor>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarProveedores}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next())
            {
                lista.add(new Proveedor(resultado.getInt("codigoProveedor"), resultado.getString("nit"),
                    resultado.getString("razonSocial"), resultado.getString("contactoPrincipal"),
                    resultado.getString("direccion"), resultado.getString("paginaWeb")));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return listaProveedor = FXCollections.observableList(lista);
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
                btnReporte.setDisable(true);
                tipoDeOperacion =  operaciones.GUARDAR;
                break;
            
            case GUARDAR:
                agregar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                limpiarControles();
                break;
        }
    }
    
    public void agregar()
    {
        TelefonoProveedor registro = new TelefonoProveedor();
        registro.setNumero(txtNumero.getText());
        registro.setDescripcion(txtDescripcion.getText());
        registro.setCodigoProveedor(((Proveedor)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonoProveedor(?,?,?)}");
            procedimiento.setString(1, registro.getNumero());
            procedimiento.setString(2,registro.getDescripcion());
            procedimiento.setInt(3, registro.getCodigoProveedor());
            procedimiento.execute();
            listaTelefonoProveedor.add(registro);
        }catch(SQLException e)
        {
            e.getMessage();
            e.printStackTrace();
        }      
    }
    
    public void editar()
    {
        switch(tipoDeOperacion)
        {
            case NINGUNO:
                    if(tblTelefonoProveedores.getSelectionModel().getSelectedItem()!= null){
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        txtDescripcion.setEditable(true);
                        txtNumero.setEditable(true);
                        cmbProveedor.setDisable(false);
                        cmbTelefonoProveedor.setDisable(false);
                        tipoDeOperacion = operaciones.ACTUALIZAR;
                    }else
                    {
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un dato a modificar");
                    }
                    break;
                    
                case ACTUALIZAR:
                    actualizar();
                    btnEditar.setText("Editar");
                    btnReporte.setText("Reporte");
                    btnNuevo.setDisable(false);
                    btnEliminar.setDisable(false);
                    txtDescripcion.setEditable(false);
                    txtNumero.setEditable(false);
                    cmbProveedor.setDisable(true);
                    cmbTelefonoProveedor.setDisable(true);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    break;
        }
    }
    
    public void actualizar()
    {
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarTelefonoProveedor(?,?,?,?)}");
            TelefonoProveedor registro = (TelefonoProveedor)tblTelefonoProveedores.getSelectionModel().getSelectedItem();
            registro.setNumero(txtNumero.getText());
            registro.setDescripcion(txtDescripcion.getText());
            registro.setCodigoProveedor(((Proveedor)cmbProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
            procedimiento.setInt(1, registro.getCodigoTelefonoProveedor());
            procedimiento.setString(2, registro.getNumero());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.setInt(4, registro.getCodigoProveedor());
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
                btnReporte.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:
                limpiarControles();
                if(tblTelefonoProveedores.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro","Eliminar Categoria",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                    {
                        try
                        {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoProveedor(?)}");
                            procedimiento.setInt(1,((TelefonoProveedor)tblTelefonoProveedores.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor());
                            procedimiento.execute();
                        }catch(SQLException e)
                        {
                            e.getMessage();
                        }
                    }else
                    {
                        JOptionPane.showMessageDialog(null, "Debe selecionar un registro para eliminar");
                    }
                }cargarDatos();
        }
    }
    
    public void seleccionarElemento()
    {
        txtNumero.setText(((TelefonoProveedor)tblTelefonoProveedores.getSelectionModel().getSelectedItem()).getNumero());
        txtDescripcion.setText(((TelefonoProveedor)tblTelefonoProveedores.getSelectionModel().getSelectedItem()).getDescripcion());
        cmbProveedor.getSelectionModel().select(buscarProveedor(((TelefonoProveedor)tblTelefonoProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
        cmbTelefonoProveedor.getSelectionModel().select(buscarTelefonoProveedor(((TelefonoProveedor)tblTelefonoProveedores.getSelectionModel().getSelectedItem()).getCodigoTelefonoProveedor()));
    }
    
    public Proveedor buscarProveedor(int codigoProveedor)
    {
        Proveedor resultado = null;
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarProveedor(?)}");
            procedimiento.setInt(1, codigoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next())
            {
                resultado = new Proveedor(registro.getInt("codigoProveedor"), registro.getString("nit"),
                registro.getString("razonSocial"), registro.getString("contactoPrincipal"),
                registro.getString("direccion"), registro.getString("paginaWeb"));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public TelefonoProveedor buscarTelefonoProveedor(int codigoTelefonoProveedor)
    {
        TelefonoProveedor resultado = null;
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTelefonoProveedor(?)}");
            procedimiento.setInt(1, codigoTelefonoProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next())
            {
                resultado = new TelefonoProveedor(registro.getInt("codigoTelefonoProveedor"), registro.getString("numero"),
                    registro.getString("descripcion"), registro.getInt("codigoProveedor"));
            }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public void desactivarControles()
    {
        txtDescripcion.setEditable(false);
        txtNumero.setEditable(false);
        cmbProveedor.setDisable(true);
        cmbTelefonoProveedor.setDisable(true);
    }
    
    public void activarControles()
    {
        txtDescripcion.setEditable(true);
        txtNumero.setEditable(true);
        cmbProveedor.setDisable(false);
        cmbTelefonoProveedor.setDisable(false);
    }
    
    public void limpiarControles()
    {
        txtDescripcion.setText("");
        txtNumero.setText("");
        cmbTelefonoProveedor.getSelectionModel().clearSelection();
        cmbProveedor.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuProveedor()
    {
        escenarioPrincipal.ventanaProveedores();
    }
    
}
