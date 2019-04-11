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
import org.jhonylopez.bean.Categoria;
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
public class CategoriaController implements Initializable {
    
    private enum operaciones{NUEVO,GUARDAR,EDITAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO}
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Categoria> listaCategoria;
    private Principal escenarioPrincipal;
    
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblCategorias;
    @FXML private TableColumn colCodigo;
    @FXML private TableColumn colDescripcion;
    @FXML private ComboBox cmbCategoria;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        cargarDatos();
        cmbCategoria.setItems(getCategorias());
    }
    
    public void cargarDatos()
    {
        tblCategorias.setItems(getCategorias());
        colCodigo.setCellValueFactory((new PropertyValueFactory <Categoria, Integer>("codigoCategoria")));
        colDescripcion.setCellValueFactory((new PropertyValueFactory<Categoria, String>("descripcion")));
    }
    
    public ObservableList <Categoria> getCategorias()
    {
        ArrayList <Categoria> lista = new ArrayList <Categoria>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCategorias}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next())
            {
                lista.add(new Categoria(resultado.getInt("codigoCategoria"),resultado.getString("descripcion")));
            }
        }
        catch(SQLException e)
        {
            e.getMessage();
        }
        return listaCategoria = FXCollections.observableList(lista);
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
                tipoDeOperacion = operaciones.NINGUNO;
                cargarDatos();
                limpiarControles();
                cmbCategoria.setItems(getCategorias());
                break;
        }
    }
    
    public void agregar()
    {
        Categoria registro = new Categoria();
        registro.setDescripcion(txtDescripcion.getText());
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarCategoria(?)}");
            procedimiento.setString(1,registro.getDescripcion());
            procedimiento.execute();
            listaCategoria.add(registro);
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
                    if(tblCategorias.getSelectionModel().getSelectedItem()!= null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarCategoria(?,?)}");
            Categoria registro = (Categoria)tblCategorias.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1,registro.getCodigoCategoria());
            procedimiento.setString(2,registro.getDescripcion());
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
                if(tblCategorias.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro","Eliminar Categoria",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                    {
                        try
                        {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarCategoria(?)}");
                            procedimiento.setInt(1,((Categoria)tblCategorias.getSelectionModel().getSelectedItem()).getCodigoCategoria());
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
        if(tblCategorias.getSelectionModel().getSelectedIndex()>-1)
        {
            cmbCategoria.getSelectionModel().select(buscarCategoria(((Categoria)tblCategorias.getSelectionModel().getSelectedItem()).getCodigoCategoria()));
            txtDescripcion.setText(((Categoria)tblCategorias.getSelectionModel().getSelectedItem()).getDescripcion());
        }
    }
    
    public Categoria buscarCategoria(int codigoCategoria)
    {
        Categoria resultado = null;
        
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCategoria(?)}");
            procedimiento.setInt(1, codigoCategoria);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next())
            {
                resultado = new Categoria(registro.getInt("codigoCategoria"),registro.getString("descripcion"));
            }        
        }catch(SQLException e)
        {
          e.printStackTrace();
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
        }else if(tblCategorias.getSelectionModel().getSelectedItem() == null)
        {
            JOptionPane.showMessageDialog(null, "Debe seleccionar en elemento");
        }else
        {
            Map parametro = new HashMap();
            int CodigoCategoria = ((Categoria)tblCategorias.getSelectionModel().getSelectedItem()).getCodigoCategoria();
            parametro.put("codigoCategoria",CodigoCategoria);
            GenerarReporte.mostrarReporte("ReporteCategoria.jasper", "Reporte de Categorias", parametro);
        }
    }
    
    public void limpiarControles()
    {
        cmbCategoria.getSelectionModel().clearSelection();
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
}
    
    
