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
import org.jhonylopez.bean.Talla;
import org.jhonylopez.sistema.Principal;

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
import java.util.Map;
import org.jhonylopez.reportes.GenerarReporte;


/**
 *
 * @author bryan
 */
public class TallaController implements Initializable {
    private enum operaciones{NUEVO,GUARDAR,EDITAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private Principal escenarioPrincipal;
    private ObservableList <Talla> listaTalla;
    
    @FXML private ComboBox cmbTalla;
    
    @FXML private TextField txtDescripcion;
    
    @FXML private TableView tblTallas;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colDescripcion;
    
    @FXML private Button btnNuevo;
    @FXML private Button btnEliminar;
    @FXML private Button btnEditar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        cargarDatos();
        cmbTalla.setItems(getTallas());
    }
    
    public void cargarDatos()
    {
        tblTallas.setItems(getTallas());
        colCodigo.setCellValueFactory((new PropertyValueFactory <Talla, Integer>("codigoTalla")));
        colDescripcion.setCellValueFactory((new PropertyValueFactory <Talla, String>("descripcion")));
    }
    
    public ObservableList <Talla> getTallas()
    {
        ArrayList <Talla> lista = new ArrayList <Talla>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarTallas}");
            ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next())
                {
                    lista.add(new Talla(resultado.getInt("codigoTalla"),resultado.getString("descripcion")));
                }
        }catch(SQLException e)
        {
            e.getMessage();
        }
        return listaTalla = FXCollections.observableList(lista);
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
                tipoDeOperacion = operaciones.GUARDAR;
                break;
                
            case GUARDAR:
                agregar();
                desactivarControles();
                btnNuevo.setText("Nuevo");
                btnEliminar.setText("Eliminar");
                btnEditar.setDisable(false);
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                limpiarControles();
                break;
        }
    }
    
    public void agregar()
    {
        Talla registro = new Talla();
        registro.setDescripcion(txtDescripcion.getText());
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarTalla(?)}");
            procedimiento.setString(1,registro.getDescripcion());
            procedimiento.execute();
            listaTalla.add(registro);
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
                    if(tblTallas.getSelectionModel().getSelectedItem()!= null){
                        btnEditar.setText("Actualizar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        txtDescripcion.setEditable(true);
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarTalla(?,?)}");
            Talla registro = (Talla)tblTallas.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1,registro.getCodigoTalla());
            procedimiento.setString(2,registro.getDescripcion());
            procedimiento.execute();
         
        }catch(SQLException e)
        {
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
                tipoDeOperacion = operaciones.NUEVO;
                break;
            default:
                limpiarControles();
                if(tblTallas.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro","Eliminar Talla",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                    {
                        try
                        {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarTalla(?)}");
                            procedimiento.setInt(1,((Talla)tblTallas.getSelectionModel().getSelectedItem()).getCodigoTalla());
                            procedimiento.execute();
                        }catch(SQLException e)
                        {
                            e.getMessage();
                        }
                    }
                }cargarDatos();
        }
    }
    
    public void seleccionarElemento()
    {
        if(tblTallas.getSelectionModel().getSelectedIndex()>-1)
        {
            cmbTalla.getSelectionModel().select(buscarTalla(((Talla)tblTallas.getSelectionModel().getSelectedItem()).getCodigoTalla()));
            txtDescripcion.setText(((Talla)tblTallas.getSelectionModel().getSelectedItem()).getDescripcion());
        }
    }
    
    public Talla buscarTalla (int codigoTalla)
    {
        Talla resultado = null;
        
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTalla(?)}");
            procedimiento.setInt(1, codigoTalla);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next())
            {
                resultado = new Talla(registro.getInt("codigoTalla"),registro.getString("descripcion"));
            }        
        }catch(SQLException e)
        {
            e.getMessage();
        }
        return resultado;
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
        }else if(tblTallas.getSelectionModel().getSelectedItem() == null)
        {
            JOptionPane.showMessageDialog(null, "Debe seleccionar en elemento");
        }else
        {
            Map parametro = new HashMap();
            int CodigoTalla = ((Talla)tblTallas.getSelectionModel().getSelectedItem()).getCodigoTalla();
            parametro.put("codigoTalla",CodigoTalla);
            GenerarReporte.mostrarReporte("ReporteTalla.jasper", "Reporte de Tallas", parametro);
        }
    }
    
    private Principal getEscenarioPrincipal()
    {
        return escenarioPrincipal;
    }
        
    public  void setEscenarioPrincipal(Principal escenarioPrincipal)
    {
        this.escenarioPrincipal = escenarioPrincipal; 
    }
    
    public void menuPrincipal()
    {
        escenarioPrincipal.menuPrincipal();
    }
    
    public void limpiarControles()
    {
        cmbTalla.getSelectionModel().clearSelection();
        txtDescripcion.setText("");
    }
    
     public void activarControles()
    {
        txtDescripcion.setEditable(true);
    }
    
    public void desactivarControles()
    {
        txtDescripcion.setEditable(false);
    }
    
}
