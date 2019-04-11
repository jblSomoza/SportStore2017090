/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhonylopez.controller;

import java.awt.event.ActionEvent;
import org.jhonylopez.sistema.Principal;
import org.jhonylopez.bean.Usuario;
import org.jhonylopez.bean.TipoDeUsuario;
import org.jhonylopez.db.Conexion;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JOptionPane;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.PasswordField;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

/**
 *
 * @author informatica
 */
public class RegistroController implements Initializable{
    
    private enum operaciones {NUEVO, GUARDAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<String> listaTipoDeUsuario;
    private ObservableList<Usuario> listaUsuario;
    private Principal escenarioPrincipal;
    private Boolean salida = false;
    
    @FXML private TextField txtNombreUsuario;
    @FXML private TextField txtEmailUsuario;
    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;
    
    @FXML private ComboBox cmbTipoUsuario;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnCancelar;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbTipoUsuario.setItems(getTipoDeUsuarios());
    }
    
    public ObservableList<Usuario> getUsuarios()
    {
        ArrayList<Usuario> lista = new ArrayList<>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarUsuarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next())
            {
                lista.add(new Usuario(resultado.getInt("codigoUsuario"), resultado.getString("nombreUsuario"),
                    resultado.getString("email"), resultado.getString("usuario"), resultado.getString("contraseña"),
                    resultado.getString("tipoDeUsuario")));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
            e.getMessage();
        }
        return listaUsuario = FXCollections.observableList(lista);
    }
    
    public ObservableList<String> getTipoDeUsuarios()
    {
        ArrayList<String> lista = new ArrayList<>();
        
        lista.add("root");
        lista.add("admin");
        lista.add("sa");
        lista.add("invitado");
        
        return listaTipoDeUsuario = FXCollections.observableList(lista);
    }
    
    public void nuevo()
    {
        switch(tipoDeOperacion)
        {
            case NINGUNO:
                activarControles();
                limpiarControles();
                btnNuevo.setText("Guardar");
                btnCancelar.setText("Cancelar");
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                agregar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnCancelar.setText("Cancelar");
                tipoDeOperacion = operaciones.NINGUNO;
                limpiarControles();
                break;
        }
    }
    
    public void agregar()
    {
        Usuario registro = new Usuario();
        registro.setNombreUsuario(txtNombreUsuario.getText());
        registro.setEmail(txtEmailUsuario.getText());
        registro.setUsuario(txtUsuario.getText());
        registro.setContraseña(txtPassword.getText());
        registro.setTipoDeUsuario((String)cmbTipoUsuario.getSelectionModel().getSelectedItem());
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarUsuario(?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNombreUsuario());
            procedimiento.setString(2, registro.getEmail());
            procedimiento.setString(3, registro.getUsuario());
            procedimiento.setString(4, registro.getContraseña());
            procedimiento.setString(5, registro.getTipoDeUsuario());
            procedimiento.execute();
            JOptionPane.showMessageDialog(null, "Usuario añadido");
            limpiarControles();
            
        }catch(SQLException e)
        {
            e.printStackTrace();
            e.getMessage();
            JOptionPane.showMessageDialog(null, "Error al ingresar usuario, por favor intentarlo otra vez");
        }
    }
    
    public void cancelar()
    {
        switch(tipoDeOperacion)
        {
            case GUARDAR:
                desactivarControles();
                limpiarControles();
                btnNuevo.setText("Nuevo");
                tipoDeOperacion = operaciones.NINGUNO;
                break;
            default:            
        } 
    }
    
    /*public void salir(ActionEvent event){
        if (salida == true)
        {
        
        }
        if (JOptionPane.showConfirmDialog(null, 
            "¿Esta Seguro De Salir De La Aplicación?", 
            "Aviso", 
            JOptionPane.YES_NO_OPTION) 
            == JOptionPane.NO_OPTION){ 

        }else
        {
           System.exit(0);
        }
    }*/
    
    public Usuario buscarUsuarios(int codigoUsuario)
    {
        Usuario resultado = null;
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarUsuario(?)}");
            procedimiento.setInt(1, codigoUsuario);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next())
            {
                resultado = new Usuario(registro.getInt("codigoUsuario"), registro.getString("nombreUsuario"),
                        registro.getString("email"), registro.getString("usuario"), registro.getString("contraseña"), 
                        registro.getString("tipoDeUsuario"));
            }
            
        }catch(SQLException e)
        {
            e.printStackTrace();
            e.getMessage();
        }
        return resultado;
    }
    
    public void limpiarControles()
    {
        txtNombreUsuario.setText("");
        txtEmailUsuario.setText("");
        txtUsuario.setText("");
        txtPassword.setText("");
        
        cmbTipoUsuario.getSelectionModel().clearSelection();
    }
    
    public void desactivarControles()
    {
        txtNombreUsuario.setEditable(false);
        txtEmailUsuario.setEditable(false);
        txtUsuario.setEditable(false);
        txtPassword.setEditable(false);
        
        cmbTipoUsuario.setDisable(true);
    }
    
    public void activarControles()
    {
        txtNombreUsuario.setEditable(true);
        txtEmailUsuario.setEditable(true);
        txtUsuario.setEditable(true);
        txtPassword.setEditable(true);
        
        cmbTipoUsuario.setDisable(false);
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }  
    
    public void menuLogin()
    {
        escenarioPrincipal.ventanaLogin();
    }
    
}
