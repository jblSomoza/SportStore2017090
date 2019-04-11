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
 **/
public class ProveedorController implements Initializable{
    
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Proveedor> listaProveedor;
    private Principal escenarioPrincipal;
    private Proveedor escenarioProveedor;
    
    @FXML private ComboBox cmbProveedor;
    @FXML private TextField txtNit;
    @FXML private TextField txtRazonSocial;
    @FXML private TextField txtContactoPrincipal;
    @FXML private TextField txtDireccion;
    @FXML private TextField txtPaginaWeb;
    @FXML private TableView tblProveedores;
    @FXML private TableColumn colCodigoProveedor;
    @FXML private TableColumn colNit;
    @FXML private TableColumn colRazonSocial;
    @FXML private TableColumn colContactoPrincipal;
    @FXML private TableColumn colDireccion;
    @FXML private TableColumn colPaginaWeb;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
       cargarDatos();
       cmbProveedor.setItems(getProveedores());
    }
    
    public void cargarDatos()
    {
        tblProveedores.setItems(getProveedores());
        colCodigoProveedor.setCellValueFactory((new PropertyValueFactory <Proveedor, Integer>("codigoProveedor")));
        colNit.setCellValueFactory((new PropertyValueFactory <Proveedor, String>("nit")));
        colRazonSocial.setCellValueFactory((new PropertyValueFactory <Proveedor, String>("razonSocial")));
        colContactoPrincipal.setCellValueFactory((new PropertyValueFactory <Proveedor, String>("contactoPrincipal")));
        colDireccion.setCellValueFactory((new PropertyValueFactory <Proveedor, String>("direccion")));
        colPaginaWeb.setCellValueFactory((new PropertyValueFactory <Proveedor, String>("paginaWeb")));
    }
    
    public ObservableList<Proveedor> getProveedores()
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
        Proveedor registro = new Proveedor();
        registro.setNit(txtNit.getText());
        registro.setRazonSocial(txtRazonSocial.getText());
        registro.setContactoPrincipal(txtContactoPrincipal.getText());
        registro.setDireccion(txtDireccion.getText());
        registro.setPaginaWeb(txtPaginaWeb.getText());
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarProveedor(?,?,?,?,?)}");
            procedimiento.setString(1, registro.getNit());
            procedimiento.setString(2, registro.getRazonSocial());
            procedimiento.setString(3, registro.getContactoPrincipal());
            procedimiento.setString(4, registro.getDireccion());
            procedimiento.setString(5, registro.getPaginaWeb());
            procedimiento.execute();
            listaProveedor.add(registro);
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
                    if(tblProveedores.getSelectionModel().getSelectedItem()!= null){
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        txtNit.setEditable(true);
                        txtRazonSocial.setEditable(true);
                        txtContactoPrincipal.setEditable(true);
                        txtDireccion.setEditable(true);
                        txtPaginaWeb.setEditable(true);
                        cmbProveedor.setDisable(false);
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
                    txtNit.setEditable(false);
                    txtRazonSocial.setEditable(false);
                    txtContactoPrincipal.setEditable(false);
                    txtDireccion.setEditable(false);
                    txtPaginaWeb.setEditable(false);
                    cmbProveedor.setDisable(true);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    break;
        }
    }
    
    public void actualizar()
    {
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarProveedor(?,?,?,?,?,?)}");
            Proveedor registro = (Proveedor)tblProveedores.getSelectionModel().getSelectedItem();
            registro.setNit(txtNit.getText());
            registro.setRazonSocial(txtRazonSocial.getText());
            registro.setContactoPrincipal(txtContactoPrincipal.getText());
            registro.setDireccion(txtDireccion.getText());
            registro.setPaginaWeb(txtPaginaWeb.getText());
            procedimiento.setInt(1, registro.getCodigoProveedor());
            procedimiento.setString(2, registro.getNit());
            procedimiento.setString(3, registro.getRazonSocial());
            procedimiento.setString(4, registro.getContactoPrincipal());
            procedimiento.setString(5, registro.getDireccion());
            procedimiento.setString(6, registro.getPaginaWeb());
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
                if(tblProveedores.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro","Eliminar Categoria",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                    {
                        try
                        {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarProveedor(?)}");
                            procedimiento.setInt(1,((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor());
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
        txtNit.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getNit());
        txtRazonSocial.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getRazonSocial());
        txtContactoPrincipal.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getContactoPrincipal());
        txtDireccion.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getDireccion());
        txtPaginaWeb.setText(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getPaginaWeb());
        cmbProveedor.getSelectionModel().select(buscarProveedor(((Proveedor)tblProveedores.getSelectionModel().getSelectedItem()).getCodigoProveedor()));
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
    
    public void generarReporte()
    {
        switch(tipoDeOperacion)
        {
            case NINGUNO:
                imprimirReporte();
                tipoDeOperacion = operaciones.ACTUALIZAR;
                break;
                
            case ACTUALIZAR:
                btnEditar.setText("Editar");
                btnReporte.setText("Reporte");
                btnNuevo.setDisable(false);
                btnEliminar.setDisable(false);
                txtNit.setEditable(false);
                txtRazonSocial.setEditable(false);
                txtContactoPrincipal.setEditable(false);
                txtDireccion.setEditable(false);
                txtPaginaWeb.setEditable(false);
                cmbProveedor.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void imprimirReporte()
    {
        Map parametros = new HashMap();
        parametros.put("_NumeroDocumento", null);
        GenerarReporte.mostrarReporte("ReporteProveedor.jasper", "Reporte de Productos", parametros);
    }
    
    public void desactivarControles()
    {
        txtNit.setEditable(false);
        txtContactoPrincipal.setEditable(false);
        txtDireccion.setEditable(false);
        txtPaginaWeb.setEditable(false);
        txtRazonSocial.setEditable(false);
        cmbProveedor.setDisable(true);
    }
    
    public void activarControles()
    {
        txtNit.setEditable(true);
        txtContactoPrincipal.setEditable(true);
        txtDireccion.setEditable(true);
        txtPaginaWeb.setEditable(true);
        txtRazonSocial.setEditable(true);
        cmbProveedor.setDisable(false);
    }
    
    public void limpiarControles()
    {
        txtNit.setText("");
        txtContactoPrincipal.setText("");
        txtDireccion.setText("");
        txtPaginaWeb.setText("");
        txtRazonSocial.setText("");
        cmbProveedor.getSelectionModel().clearSelection();
    }
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void ventanaTelefonoProveedor()
    {
        escenarioPrincipal.ventanaTelefonoProveedores();
    }
    
    public void ventanaEmailProveedor()
    {
        escenarioPrincipal.ventanaEmailProveedores();
    }
    
    public void menuPrincipal()
    {
        escenarioPrincipal.menuPrincipal();
    }
}
