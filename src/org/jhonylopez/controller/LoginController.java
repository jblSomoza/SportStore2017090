/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhonylopez.controller;

import static com.sun.glass.ui.Cursor.setVisible;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;

import javax.swing.JOptionPane;

import org.jhonylopez.db.Conexion;
import org.jhonylopez.bean.Usuario;
import org.jhonylopez.sistema.Principal;
import org.jhonylopez.reportes.GenerarReporte;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;
import java.util.ArrayList;
import java.util.Locale;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javafx.fxml.Initializable;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;

/**
 *
 * @author bryan
 */
public class LoginController implements Initializable {
    
    private enum operaciones{LOGGIN, LOGGEARSE, ELIMINAR, EDITAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeoperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList<String> listaTipoDeUsuario;
    private ObservableList<Usuario> listaUsuario;
    private int c;
    private ArrayList<Usuario> lista = new ArrayList<>();
    RegistroController registro = new RegistroController();
    String ln = System.getProperty("line.separator");
    boolean salida = false;
    private int us = 0;
    
    @FXML private TextField txtUsuario;
    
    @FXML private PasswordField txtPassword;
    
    @FXML private ComboBox cmbTipoUsuario; 
    
    @FXML private Button btnLogin;
    @FXML private Button btnRegister;
    @FXML private Button btnCancelar;
    @FXML private Button btnReporte;
    @FXML private Button btnSalir;
            
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cmbTipoUsuario.setItems(getTipoDeUsuarios());
    }

    public ObservableList<Usuario> getUsuarios()
    {
        ArrayList<Usuario> lista = new ArrayList<Usuario>();
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
    
    public void login()
    {
        switch(tipoDeoperacion)
        {
            case NINGUNO:
                limpiarControles();
                activarControles();
                btnLogin.setText("Loggerse");
                btnCancelar.setText("Cancelar");
                btnRegister.setDisable(true);
                btnReporte.setDisable(true);
                break;
            case LOGGEARSE:
                ingresar();
                desactivarControles();
                btnLogin.setText("Login");
                btnCancelar.setText("Cancelar");
                btnRegister.setDisable(false);
                btnReporte.setDisable(false);
                limpiarControles();
                break;
        }
    }
    
    public void ingresar()
    {
        
        ArrayList <Usuario> lista = new ArrayList <Usuario>();
        try{
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarUsuarios}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next()){
                lista.add(new Usuario(resultado.getInt("codigoUsuario"),resultado.getString("nombreDeUsuario"),resultado.getString("email"),
                resultado.getString("usuario"),resultado.getString("contraseña"),resultado.getString("tipoDeUsuario")));
            }  
        }catch(SQLException e){
            e.getMessage();
        }
        
        for (int x = 0; x<lista.size();x++) 
        {
            if (txtUsuario.getText().equals(lista.get(x).getCodigoUsuario())) 
            {
                if (txtPassword.getText().equals(lista.get(x).getContraseña())) 
                {
                    if (((Usuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getTipoDeUsuario().equals(lista.get(x).getTipoDeUsuario())) 
                    {
                        JOptionPane.showMessageDialog(null, "Has ingresado Correctamente");
                        JOptionPane.showMessageDialog(null, "Tu cuenta es de tipo " +cmbTipoUsuario.getSelectionModel().getSelectedItem());
                        btnRegister.setDisable(false);
                        btnLogin.setDisable(false);
                        
                        if(cmbTipoUsuario.getSelectionModel().getSelectedItem().toString().equals("admin")|| cmbTipoUsuario.getSelectionModel().getSelectedItem().toString().equals("root"))
                        {
                            btnReporte.setDisable(false);
                        }
                    }
                    escenarioPrincipal.menuPrincipal();
                }
            }
            
        }
        for(int x=0;x<lista.size();x++) 
        {
            Usuario n= lista.get(x);    
            if(txtUsuario.getText().equals(n.getNombreUsuario())&&txtPassword.getText().equals(n.getContraseña()))
            {
                if(((Usuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getTipoDeUsuario().equals(lista.get(x).getTipoDeUsuario()))
                {
                    escenarioPrincipal.menuPrincipal(); 
        
                    c = 1;     
                } 
            }
        }
        
        if(c==0)
        {
            JOptionPane.showMessageDialog(null, "Si quieres abrir el programa debes acceder con una cuenta privilegiada vuelve a intentarlo","Datos Erroneos", 0);
        }
            c=0;
    }
    
    public void cancelar()
    {     
        switch(tipoDeoperacion)
        {
            case LOGGEARSE:
                desactivarControles();
                limpiarControles();
                tipoDeoperacion = operaciones.NINGUNO;
                break;
            default:
        }
    }
    
    public void activarBotonReporte()
    {

        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarUsuario}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next())
            {
                lista.add(new Usuario(resultado.getInt("codigoUsuario"),
                        resultado.getString("nombreUsuario"),
                        resultado.getString("email"),
                        resultado.getString("usuario"),
                        resultado.getString("contraseña"),
                        resultado.getString("tipoDeUsuario")));
            }
        }catch(SQLException e)
        {
            e.getMessage();
        } 
        for(int x=0;x<lista.size();x++) 
        {
            Usuario e = lista.get(x);
            if(txtUsuario.getText().equals(e.getUsuario())&&txtPassword.getText().equals(e.getContraseña()))
            {
               if(((Usuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getTipoDeUsuario().equals(e.getTipoDeUsuario()))
               {
                    if(((Usuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getTipoDeUsuario().equals("admin"))
                    {
                    btnReporte.setDisable(false);
                    } 
                    else if(((Usuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getTipoDeUsuario().equals("root"))
                    {
                    btnReporte.setDisable(false);
                    }       
                }
            }
            
            else if(((Usuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getTipoDeUsuario().equals("invitado"))
            {
                btnReporte.setDisable(true);
            }
            
            else if(((Usuario)cmbTipoUsuario.getSelectionModel().getSelectedItem()).getTipoDeUsuario().equals("sa"))
            {
                btnReporte.setDisable(true);
            }
            
        }
    } 
    
    public void GenerarReporte()
    {
        Map parametros = new HashMap();
        parametros.put("NumeroDocumento",null);
        GenerarReporte.mostrarReporte("ReporteUsuario.jasper","Reporte de Usuarios",parametros);
    }
    
    public void salir(ActionEvent event)
    {
        if (salida == true)
        {
       
        }
       if (JOptionPane.showConfirmDialog(null, 
            "¿Esta Seguro De Salir De La Aplicación?", 
            "Aviso", 
            JOptionPane.YES_NO_OPTION) 
            == JOptionPane.NO_OPTION)
       { 

        }else
        {
           System.exit(0);
        }
    }

    public void desactivarControles()
    {
        txtUsuario.setEditable(false);
        txtPassword.setEditable(false);
        
        cmbTipoUsuario.setDisable(true);
    }
    
    public void activarControles()
    {
        txtUsuario.setEditable(true);
        txtPassword.setEditable(true);
        
        cmbTipoUsuario.setDisable(false);
    }
    
    public void limpiarControles()
    {
        txtUsuario.setText("");
        txtPassword.setText("");
        
        cmbTipoUsuario.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaRegistro()
    {
        escenarioPrincipal.ventanaRegistros();
    }
}
