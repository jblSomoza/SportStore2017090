/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhonylopez.controller;

import java.sql.Statement;
import java.sql.PreparedStatement;//Permite ejecutar todos los procedimientos almacenados
import java.sql.ResultSet;//Devuelve el resultado de un exec 
import java.sql.SQLException;

import javax.swing.JOptionPane;

import org.jhonylopez.db.Conexion;
import org.jhonylopez.bean.Marca;
import org.jhonylopez.sistema.Principal;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.collections.ObservableList;//El tableview recibe un acceslist
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
import org.jhonylopez.reportes.GenerarReporte;

/**
 *
 * @author bryan
 */
public class MarcaController implements Initializable {
    private enum operaciones{NUEVO,GUARDAR,EDITAR,ELIMINAR,ACTUALIZAR,CANCELAR,NINGUNO}//operaciones cambio el tipo a enum
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList <Marca> listaMarca;
    private Principal escenarioPrincipal;
    
    @FXML private ComboBox cmbMarca;
    @FXML private TextField txtDescripcion;
    @FXML private TableView tblMarcas;
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
        cmbMarca.setItems(getMarcas());
    }
    
    public void activarControles()
    {
        txtDescripcion.setEditable(true);
        cmbMarca.setEditable(false);
    }
    
    public void desactivarControles()
    {
        txtDescripcion.setEditable(false);
        cmbMarca.setEditable(true);
    }
    
    public void cargarDatos()
    {
        tblMarcas.setItems(getMarcas());
        colCodigo.setCellValueFactory((new PropertyValueFactory <Marca, Integer>("codigoMarca")));
        colDescripcion.setCellValueFactory((new PropertyValueFactory <Marca, String>("descripcion")));
    }
    
    public ObservableList <Marca> getMarcas()
    {
        ArrayList <Marca> lista = new ArrayList <Marca>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMarcas}");//Conexion: llama a la clase, call es una palabra reservada
            ResultSet resultado = procedimiento.executeQuery();//executeQuery va a traer datos de la DB, execute solo ejecuta el procedimiento
                while(resultado.next()) //.next cada vez que encuentre un espacio, verifica que haya otro registro entonces se lo lleva 
                {
                    lista.add(new Marca(resultado.getInt("codigoMarca"),resultado.getString("descripcion")));
                }
        }catch(SQLException e)
        {
            e.getMessage();
        }
        return listaMarca = FXCollections.observableList(lista);//Retorna el arraylist
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
        Marca registro = new Marca();
        registro.setDescripcion(txtDescripcion.getText());
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarMarca(?)}");//El signo de interrogacion es el parametro
            procedimiento.setString(1,registro.getDescripcion());//1 se refiere a index 
            procedimiento.execute();
            listaMarca.add(registro);
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
                    if(tblMarcas.getSelectionModel().getSelectedItem()!= null){
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
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarMarca(?,?)}");
            Marca registro = (Marca)tblMarcas.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            procedimiento.setInt(1,registro.getCodigoMarca());
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
                if(tblMarcas.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro","Eliminar Marca",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                    {
                        try
                        {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarMarca(?)}");
                            procedimiento.setInt(1,((Marca)tblMarcas.getSelectionModel().getSelectedItem()).getCodigoMarca());
                            procedimiento.execute();
                        }catch(SQLException e)
                        {
                            e.printStackTrace();
                            e.getMessage();
                        }
                    }
                }cargarDatos();
        }
    }
    
    public void seleccionarElemento()
    {
        if(tblMarcas.getSelectionModel().getSelectedIndex()>-1)
        {
            cmbMarca.getSelectionModel().select(buscarMarca(((Marca)tblMarcas.getSelectionModel().getSelectedItem()).getCodigoMarca()));
            txtDescripcion.setText(((Marca)tblMarcas.getSelectionModel().getSelectedItem()).getDescripcion());
        }
    }
    
    public Marca buscarMarca (int codigoMarca)
    {
        Marca resultado = null;
        
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarMarca(?)}");
            procedimiento.setInt(1, codigoMarca);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next())
            {
                resultado = new Marca(registro.getInt("codigoMarca"),registro.getString("descripcion"));
            }        
        }catch(SQLException e)
        {
            e.printStackTrace();
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
        }else if(tblMarcas.getSelectionModel().getSelectedItem() == null)
        {
            JOptionPane.showMessageDialog(null, "Debe seleccionar en elemento");
        }else
        {
            Map parametro = new HashMap();
            int CodigoMarca = ((Marca)tblMarcas.getSelectionModel().getSelectedItem()).getCodigoMarca();
            parametro.put("codigoMarca",CodigoMarca);
            GenerarReporte.mostrarReporte("ReporteMarca.jasper", "Reporte de Marcas", parametro);
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
        cmbMarca.getSelectionModel().clearSelection();
        txtDescripcion.setText("");
    }
     
}
