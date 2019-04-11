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
import org.jhonylopez.bean.Proveedor;
import org.jhonylopez.sistema.Principal;
import org.jhonylopez.reportes.GenerarReporte;
import org.jhonylopez.bean.EmailProveedor;

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
 */
public class EmailProveedorController implements Initializable{
    
    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<EmailProveedor> listaEmailProveedor;
    private ObservableList<Proveedor> listaProveedor;
    
    @FXML private ComboBox cmbCodigoEmailProveedor;
    @FXML private ComboBox cmbCodigoProveedor;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtEmail;
    @FXML private TableView tblEmailProveedores;
    @FXML private TableColumn colCodigoEmailProveeodor;
    @FXML private TableColumn colCodigoProveedor;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colEmail;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
       cmbCodigoEmailProveedor.setItems(getEmailProveedor());
       cmbCodigoProveedor.setItems(getProveedor());
    }
    
    public void cargarDatos()
    {
        tblEmailProveedores.setItems(getEmailProveedor());
        colCodigoEmailProveeodor.setCellValueFactory(new PropertyValueFactory<EmailProveedor, Integer>("codigoEmailProveedor"));
        colCodigoProveedor.setCellValueFactory(new PropertyValueFactory<EmailProveedor, Integer>("codigoProveedor"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<EmailProveedor, String>("descripcion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<EmailProveedor, String>("email"));
    }
    
    public ObservableList<EmailProveedor> getEmailProveedor()
    {
        ArrayList<EmailProveedor> lista = new ArrayList<EmailProveedor>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEmailProveedores}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next())
            {
                lista.add(new EmailProveedor(resultado.getInt("codigoEmailProveedor"), resultado.getString("email"),
                    resultado.getString("descripcion"), resultado.getInt("codigoProveedor")));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return listaEmailProveedor = FXCollections.observableList(lista);
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
                tipoDeOperacion = operaciones.GUARDAR;
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
        EmailProveedor registro = new EmailProveedor();
        registro.setEmail(txtEmail.getText());
        registro.setDescripcion(txtDescripcion.getText());
        registro.setCodigoProveedor(((Proveedor)cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEmailProveedor(?,?,?)}");
            procedimiento.setString(1, registro.getEmail());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setInt(3, registro.getCodigoProveedor());
            procedimiento.execute();
            listaEmailProveedor.add(registro);
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
                if(tblEmailProveedores.getSelectionModel().getSelectedItem() != null)
                {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtDescripcion.setEditable(true);
                    txtEmail.setEditable(true);
                    cmbCodigoEmailProveedor.setDisable(false);
                    cmbCodigoProveedor.setDisable(false);
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
                txtEmail.setEditable(false);
                cmbCodigoEmailProveedor.setDisable(true);
                cmbCodigoProveedor.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar()
    {
        try
        {
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarEmailProveedor(?,?,?,?)}");
        EmailProveedor registro = (EmailProveedor)tblEmailProveedores.getSelectionModel().getSelectedItem();
        registro.setEmail(txtEmail.getText());
        registro.setDescripcion(txtDescripcion.getText());
        registro.setCodigoProveedor(((Proveedor)cmbCodigoProveedor.getSelectionModel().getSelectedItem()).getCodigoProveedor());
        procedimiento.setInt(1, registro.getCodigoEmailProveedor());
        procedimiento.setString(2, registro.getEmail());
        procedimiento.setString(3, registro.getDescripcion());
        procedimiento.setInt(4, registro.getCodigoProveedor());
        }catch(SQLException e)
        {
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
                if(tblEmailProveedores.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro","Eliminar Email Proveedor",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                    {
                        try{
                        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEmailProveedor(?)}");
                        procedimiento.setInt(1, ((EmailProveedor)tblEmailProveedores.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor());
                        procedimiento.execute();
                        }catch(SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }else
                    {
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un registro para eliminar");
                    }
                    cargarDatos();
                }
        }
    }
    
    public void seleccionarElemento()
    {
        cmbCodigoEmailProveedor.getSelectionModel().select(buscarEmailProveedor(((EmailProveedor)tblEmailProveedores.getSelectionModel().getSelectedItem()).getCodigoEmailProveedor()));
        txtEmail.setText(((EmailProveedor)tblEmailProveedores.getSelectionModel().getSelectedItem()).getEmail());
        txtDescripcion.setText(((EmailProveedor)tblEmailProveedores.getSelectionModel().getSelectedItem()).getDescripcion());
        cmbCodigoProveedor.getSelectionModel().select(buscarProveedor(((EmailProveedor)tblEmailProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
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
            e.getMessage();
        }
        return resultado;
    }
   
    public EmailProveedor buscarEmailProveedor(int codigoEmailProveedor)
    {
        EmailProveedor resultado = null;
        
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarEmailProveedor(?)}");
            procedimiento.setInt(1, codigoEmailProveedor);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next())
            {
                resultado = new EmailProveedor(registro.getInt("codigoEmailProveedor"), registro.getString("email"),
                    registro.getString("descripcion"), registro.getInt("codigoProveedor"));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
             e.getMessage();
        }
        return resultado;
    }
    
    public void desactivarControles()
    {
        cmbCodigoEmailProveedor.setDisable(true);
        txtDescripcion.setEditable(false);
        txtEmail.setEditable(false);
        cmbCodigoProveedor.setDisable(true);
    }
    
    public void activarControles()
    {
        cmbCodigoEmailProveedor.setDisable(false);
        txtDescripcion.setEditable(true);
        txtEmail.setEditable(true);
        cmbCodigoProveedor.setDisable(false);
    }
    
    public void limpiarControles()
    {
        cmbCodigoEmailProveedor.getSelectionModel().clearSelection();
        txtDescripcion.setText("");
        txtEmail.setText("");
        cmbCodigoProveedor.getSelectionModel().clearSelection();
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
