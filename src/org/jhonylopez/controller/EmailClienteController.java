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
import org.jhonylopez.bean.EmailCliente;

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
public class EmailClienteController implements Initializable{

    private enum operaciones{NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<EmailCliente> listaEmailCliente;
    private ObservableList<Cliente> listaCliente;
    
    @FXML private ComboBox cmbCodigoEmailCliente;
    @FXML private ComboBox cmbCodigoCliente;
    
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtEmail;
    
    @FXML private TableView tblEmailClientes;
    @FXML private TableColumn colCodigoEmailCliente;
    @FXML private TableColumn colCodigoCliente;
    @FXML private TableColumn colDescripcion;
    @FXML private TableColumn colEmail;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCodigoEmailCliente.setItems(getEmailCliente());
        cmbCodigoCliente.setItems(getCliente());
    }
    
    public void cargarDatos()
    {
        tblEmailClientes.setItems(getEmailCliente());
        colCodigoEmailCliente.setCellValueFactory(new PropertyValueFactory<EmailCliente, Integer>("codigoEmailCliente"));
        colCodigoCliente.setCellValueFactory(new PropertyValueFactory<EmailCliente, Integer>("codigoCliente"));
        colDescripcion.setCellValueFactory(new PropertyValueFactory<EmailCliente, String>("descripcion"));
        colEmail.setCellValueFactory(new PropertyValueFactory<EmailCliente, String>("email"));
    }
    
    public ObservableList<EmailCliente> getEmailCliente()
    {
        ArrayList<EmailCliente> lista = new ArrayList<EmailCliente>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarEmailClientes}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next())
            {
                lista.add(new EmailCliente(resultado.getInt("codigoEmailCliente"), resultado.getInt("codigoCliente"),
                    resultado.getString("email"), resultado.getString("descripcion")));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return listaEmailCliente = FXCollections.observableList(lista);
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
        EmailCliente registro = new EmailCliente();
        registro.setCodigoCliente(((Cliente)cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
        registro.setEmail(txtEmail.getText());
        registro.setDescripcion(txtDescripcion.getText());
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarEmailCliente(?,?,?)}");
            procedimiento.setInt(1, registro.getCodigoCliente());
            procedimiento.setString(2, registro.getEmail());
            procedimiento.setString(3, registro.getDescripcion());
            procedimiento.execute();
            listaEmailCliente.add(registro);
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
                if(tblEmailClientes.getSelectionModel().getSelectedItem() != null)
                {
                    btnEditar.setText("Actualizar");
                    btnReporte.setText("Cancelar");
                    btnNuevo.setDisable(true);
                    btnEliminar.setDisable(true);
                    txtDescripcion.setEditable(true);
                    txtEmail.setEditable(true);
                    cmbCodigoEmailCliente.setDisable(false);
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
                txtEmail.setEditable(false);
                cmbCodigoEmailCliente.setDisable(true);
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
        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarEmailCliente(?,?,?,?)}");
        EmailCliente registro = (EmailCliente)tblEmailClientes.getSelectionModel().getSelectedItem();
        registro.setCodigoCliente(((Cliente)cmbCodigoCliente.getSelectionModel().getSelectedItem()).getCodigoCliente());
        registro.setEmail(txtEmail.getText());
        registro.setDescripcion(txtDescripcion.getText());
        procedimiento.setInt(1, registro.getCodigoCliente());
        procedimiento.setInt(2, registro.getCodigoCliente());
        procedimiento.setString(3, registro.getEmail());
        procedimiento.setString(4, registro.getDescripcion());
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
                if(tblEmailClientes.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro","Eliminar Email Cliente",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                    {
                        try{
                        PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarEmailCliente(?)}");
                        procedimiento.setInt(1, ((EmailCliente)tblEmailClientes.getSelectionModel().getSelectedItem()).getCodigoEmailCliente());
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
    
    public EmailCliente buscarEmailCliente(int codigoEmailCliente)
    {
        EmailCliente resultado = null;
        
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarEmailCliente(?)}");
            procedimiento.setInt(1, codigoEmailCliente);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next())
            {
                resultado = new EmailCliente(registro.getInt("codigoEmailCliente"), registro.getInt("codigoCliente"),
                    registro.getString("email"), registro.getString("descripcion"));
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
        cmbCodigoEmailCliente.getSelectionModel().select(buscarEmailCliente(((EmailCliente)tblEmailClientes.getSelectionModel().getSelectedItem()).getCodigoEmailCliente()));
        txtEmail.setText(((EmailCliente)tblEmailClientes.getSelectionModel().getSelectedItem()).getEmail());
        txtDescripcion.setText(((EmailCliente)tblEmailClientes.getSelectionModel().getSelectedItem()).getDescripcion());
        cmbCodigoCliente.getSelectionModel().select(buscarCliente(((EmailCliente)tblEmailClientes.getSelectionModel().getSelectedItem()).getCodigoCliente()));
    }
     
    public void desactivarControles()
    {
        cmbCodigoEmailCliente.setDisable(true);
        txtDescripcion.setEditable(false);
        txtEmail.setEditable(false);
        cmbCodigoCliente.setDisable(true);
    }
    
    public void activarControles()
    {
        cmbCodigoEmailCliente.setDisable(false);
        txtDescripcion.setEditable(true);
        txtEmail.setEditable(true);
        cmbCodigoCliente.setDisable(false);
    }
    
    public void limpiarControles()
    {
        cmbCodigoEmailCliente.getSelectionModel().clearSelection();
        txtDescripcion.setText("");
        txtEmail.setText("");
        cmbCodigoCliente.getSelectionModel().clearSelection();
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
