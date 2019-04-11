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
import org.jhonylopez.bean.TelefonoCliente;

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
public class TelefonoClienteController implements Initializable {

    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<TelefonoCliente> listaTelefonoCliente;
    private ObservableList<Cliente> listaCliente;
    
    @FXML private ComboBox cmbCodigoTelefonoCliente;
    @FXML private ComboBox cmbCodigoCliente;
    
    @FXML private TextField txtNumero;
    @FXML private TextField txtDescripcion;
    
    @FXML private TableView tblTelefonoClientes;
    @FXML private TableColumn colCodigoTelefonoCliente;
    @FXML private TableColumn colCodigoCliente;
    @FXML private TableColumn colNumero;
    @FXML private TableColumn colDescripcion;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoTelefonoCliente.setItems(getTelefonoCliente());
        cmbCodigoCliente.setItems(getCliente());
    }
    
    public void cargarDatos()
    {
        tblTelefonoClientes.setItems(getTelefonoCliente());
        colCodigoTelefonoCliente.setCellValueFactory(new PropertyValueFactory<TelefonoCliente, Integer>("codigoTelefonoCliente"));
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<TelefonoCliente, Integer>("codigoCliente"));
        colNumero.setCellValueFactory(new PropertyValueFactory<TelefonoCliente, String>("numero"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<TelefonoCliente, String>("descripcion"));
    }
    
    public ObservableList<TelefonoCliente> getTelefonoCliente()
    {
        ArrayList<TelefonoCliente> lista = new ArrayList<>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTelefonoClientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next())
            {
                lista.add(new TelefonoCliente(resultado.getInt("codigoTelefonoCliente"), resultado.getInt("codigoCliente"),
                    resultado.getString("numero"), resultado.getString("descripcion")));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
            e.getMessage();
        }
        return listaTelefonoCliente = FXCollections.observableList(lista);
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
        TelefonoCliente registro = new TelefonoCliente();
        registro.setCodigoCliente(((Cliente)cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
        registro.setNumero(txtNumero.getText());
        registro.setDescripcion(txtDescripcion.getText());
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTelefonoCliente(?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(2, registro.getNumero());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.execute();
            listaTelefonoCliente.add(registro);
        }catch(SQLException e)
        {
            e.printStackTrace();
            e.getMessage();
        }     
    }
    
    public void editar()
    {
        switch(tipoDeOperacion)
        {
            case NINGUNO:
                if(tblTelefonoClientes.getSelectionModel().getSelectedItem() != null)
                {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtDescripcion.setEditable(true);
                    txtNumero.setEditable(true);
                    cmbCodigoTelefonoCliente.setDisable(false);
                    cmbCodigoCliente.setDisable(false);
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
                cmbCodigoTelefonoCliente.setDisable(true);
                cmbCodigoCliente.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                break;
        }
    }
    
    public void actualizar()
    {
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarTelefonoCliente(?,?,?,?)}");
            TelefonoCliente registro = (TelefonoCliente)tblTelefonoClientes.getSelectionModel().getSelectedItem();
            registro.setCodigoCliente(((Cliente)cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
            registro.setNumero(txtNumero.getText());
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1, registro.getCodigoTelefonoCliente());
            procedimiento.setInt(2, registro.getCodigoCliente());
            procedimiento.setString(3, registro.getNumero());
            procedimiento.setString(4, registro.getDescripcion());
        }catch(SQLException e)
        {
            e.printStackTrace();
            e.getMessage();
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
                if(tblTelefonoClientes.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro","Eliminar Email Cliente"
                            + "",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                    {
                        try{
                        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTelefonoCliente(?)}");
                        procedimiento.setInt(1, ((TelefonoCliente)tblTelefonoClientes.getSelectionModel().getSelectedItem()).getCodigoTelefonoCliente());
                        procedimiento.execute();
                        }catch(SQLException e)
                        {
                            e.printStackTrace();
                            e.getMessage();
                        }
                    }else
                    {
                        JOptionPane.showMessageDialog(null, "Debe seleccionar un registro para eliminar");
                    }
                    cargarDatos();
                }
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
    
    public TelefonoCliente buscarTelefonoCliente(int codigoTelefonoCliente)
    {
        TelefonoCliente resultado = null;
        
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTelefonoCliente(?)}");
            procedimiento.setInt(1, codigoTelefonoCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next())
            {
                resultado = new TelefonoCliente(registro.getInt("codigoTelefonoCliente"), registro.getInt("codigoCliente"),
                    registro.getString("numero"), registro.getString("descripcion"));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
            e.getMessage();
        }
        return resultado; 
    }
    
    public void seleccionarElemento()
    {
        cmbCodigoTelefonoCliente.getSelectionModel().select(buscarTelefonoCliente(((TelefonoCliente)tblTelefonoClientes.getSelectionModel().getSelectedItem()).getCodigoTelefonoCliente()));
        txtNumero.setText(((TelefonoCliente)tblTelefonoClientes.getSelectionModel().getSelectedItem()).getNumero());
        txtDescripcion.setText(((TelefonoCliente)tblTelefonoClientes.getSelectionModel().getSelectedItem()).getDescripcion());
        cmbCodigoCliente.getSelectionModel().select(buscarCliente(((TelefonoCliente)tblTelefonoClientes.getSelectionModel().getSelectedItem()).getCodigoCliente()));
    }
    
    public void limpiarControles()
    {
        cmbCodigoTelefonoCliente.getSelectionModel().clearSelection();
        txtNumero.setText("");
        txtDescripcion.setText("");
        cmbCodigoCliente.getSelectionModel().clearSelection();
    }
    
    public void desactivarControles()
    {
        cmbCodigoTelefonoCliente.setDisable(true);
        txtNumero.setEditable(false);
        txtDescripcion.setEditable(false);
        cmbCodigoCliente.setDisable(true);
    }
    
    public void activarControles()
    {
        cmbCodigoTelefonoCliente.setDisable(false);
        txtNumero.setEditable(true);
        txtDescripcion.setEditable(true);
        cmbCodigoCliente.setDisable(false);
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuCliente()
    {
        escenarioPrincipal.ventanaClientes();
    }
}
