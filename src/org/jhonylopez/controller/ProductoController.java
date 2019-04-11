/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhonylopez.controller;

import org.jhonylopez.bean.Categoria;
import org.jhonylopez.bean.Marca;
import org.jhonylopez.bean.Talla;
import org.jhonylopez.bean.Producto;
import org.jhonylopez.db.Conexion;
import org.jhonylopez.sistema.Principal;

import java.net.URL;
import java.io.File;
import java.sql.PreparedStatement; 
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javax.swing.JOptionPane; 
import org.jhonylopez.reportes.GenerarReporte;

/**
 *
 * @author bryan
 */
public class ProductoController implements Initializable {
    private enum operaciones {NUEVO, GUARDAR, ELIMINAR, ACTUALIZAR, CANCELAR, NINGUNO};
    private operaciones tipoDeOperacion = operaciones.NINGUNO;
    private ObservableList<Producto> listaProducto;
    private ObservableList<Categoria> listaCategoria;
    private ObservableList<Marca> listaMarca;
    private ObservableList<Talla> listaTalla;
    private Principal escenarioPrincipal;
    
    @FXML private ComboBox cmbCategoria;
    @FXML private ComboBox cmbMarca;
    @FXML private ComboBox cmbTalla;
    
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtImagen;
    
    @FXML private ImageView imgProducto;
    
    @FXML private TableView tblProductos;
    @FXML private TableColumn colExistencia;
    @FXML private TableColumn colPrecioUnitario;
    @FXML private TableColumn colPrecioPorDocena;
    @FXML private TableColumn colPrecioPorMayor;
    
    @FXML private Button btnImagen;
    @FXML private Button btnNuevo;
    @FXML private Button btnEditar;
    @FXML private Button btnEliminar;
    @FXML private Button btnReporte;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cargarDatos();
        cmbCategoria.setItems(getCategorias());
        cmbMarca.setItems(getMarcas());
        cmbTalla.setItems(getTallas());
    }
    
    public void cargarDatos()
    {
        tblProductos.setItems(getProductos());
        colExistencia.setCellValueFactory(new PropertyValueFactory<Producto, Integer>("existencia"));
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioUnitario"));
        colPrecioPorDocena.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioPorDocena"));
        colPrecioPorMayor.setCellValueFactory(new PropertyValueFactory<Producto, Double>("precioPorMayor"));

    }
    
    public ObservableList<Producto> getProductos()
    {
        ArrayList<Producto> lista = new ArrayList<Producto>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarProductos}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next())
            {
                lista.add(new Producto(resultado.getInt("codigoProducto"),
                    resultado.getString("descripcion"),
                    resultado.getInt("existencia"),
                    resultado.getDouble("precioUnitario"),
                    resultado.getDouble("precioPorDocena"),
                    resultado.getDouble("precioPorMayor"),
                    resultado.getString("imagen"),
                    resultado.getInt("codigoCategoria"),
                    resultado.getString("categoria"),
                    resultado.getInt("codigoMarca"),
                    resultado.getString("marcas"),
                    resultado.getInt("codigoTalla"),
                    resultado.getString("tallas")));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return listaProducto = FXCollections.observableList(lista);
    }
    
    public ObservableList<Categoria> getCategorias()
    {
        ArrayList<Categoria> lista = new ArrayList<Categoria>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarCategorias}");
            ResultSet resultado = procedimiento.executeQuery();
            while(resultado.next())
            {
                lista.add(new Categoria(resultado.getInt("codigoCategoria"),
                    resultado.getString("descripcion")));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return listaCategoria = FXCollections.observableList(lista);
    }
    
    public ObservableList<Marca> getMarcas()
    {
        ArrayList<Marca> lista = new ArrayList<Marca>();
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ListarMarcas}");
            ResultSet resultado = procedimiento.executeQuery();
                while(resultado.next())
                {
                    lista.add(new Marca(resultado.getInt("codigoMarca"),resultado.getString("descripcion")));
                }
        }catch(SQLException e)
        {
            e.getMessage();
        }
        return listaMarca = FXCollections.observableList(lista);//Retorna el arraylist
    }
    
    public ObservableList<Talla> getTallas()
    {
        ArrayList<Talla> lista = new ArrayList<Talla>();
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
        Producto registro = new Producto();
        registro.setDescripcion(txtDescripcion.getText());
        registro.setCodigoCategoria(((Categoria)cmbCategoria.getSelectionModel().getSelectedItem()).getCodigoCategoria());
        registro.setCodigoMarca(((Marca)cmbMarca.getSelectionModel().getSelectedItem()).getCodigoMarca());
        registro.setCodigoTalla(((Talla)cmbTalla.getSelectionModel().getSelectedItem()).getCodigoTalla());
        registro.setImagen(txtImagen.getText());
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_AgregarProducto(?,?,?,?,?)}");
            procedimiento.setString(1,registro.getDescripcion());
            procedimiento.setInt(2, registro.getCodigoCategoria());
            procedimiento.setInt(3, registro.getCodigoMarca());
            procedimiento.setInt(4, registro.getCodigoTalla());
            procedimiento.setString(5, registro.getImagen());
            procedimiento.execute();
            listaProducto.add(registro);
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
                    if(tblProductos.getSelectionModel().getSelectedItem()!= null){
                        btnEditar.setText("Actualizar");
                        btnReporte.setText("Cancelar");
                        btnNuevo.setDisable(true);
                        btnEliminar.setDisable(true);
                        btnImagen.setDisable(false);
                        txtDescripcion.setEditable(true);
                        cmbCategoria.setDisable(false);
                        cmbMarca.setDisable(false);
                        cmbTalla.setDisable(false);
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
                    btnImagen.setDisable(true);
                    txtDescripcion.setEditable(false);
                    cmbCategoria.setDisable(true);
                    cmbMarca.setDisable(true);
                    cmbTalla.setDisable(true);
                    tipoDeOperacion = operaciones.NINGUNO;
                    cargarDatos();
                    break;
        }
    }
    public void actualizar()
    {
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_ActualizarProducto(?,?,?,?,?,?)}");
            Producto registro = (Producto)tblProductos.getSelectionModel().getSelectedItem();
            registro.setDescripcion(txtDescripcion.getText());
            registro.setCodigoCategoria(((Categoria)cmbCategoria.getSelectionModel().getSelectedItem()).getCodigoCategoria());
            registro.setCodigoMarca(((Marca)cmbMarca.getSelectionModel().getSelectedItem()).getCodigoMarca());
            registro.setCodigoTalla(((Talla)cmbTalla.getSelectionModel().getSelectedItem()).getCodigoTalla());
            registro.setImagen(txtImagen.getText());
            procedimiento.setInt(1, registro.getCodigoProducto());
            procedimiento.setString(2, registro.getDescripcion());
            procedimiento.setString(3, registro.getImagen());
            procedimiento.setInt(4, registro.getCodigoCategoria());
            procedimiento.setInt(5, registro.getCodigoMarca());
            procedimiento.setInt(6, registro.getCodigoTalla());
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
                if(tblProductos.getSelectionModel().getSelectedItem() != null)
                {
                    int respuesta = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el registro","Eliminar Categoria",JOptionPane.YES_NO_OPTION,JOptionPane.QUESTION_MESSAGE);
                    if(respuesta == JOptionPane.YES_OPTION)
                    {
                        try
                        {
                            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_EliminarProducto(?)}");
                            procedimiento.setInt(1,((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoProducto());
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
        txtDescripcion.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getDescripcion());
        cmbCategoria.getSelectionModel().select(buscarCategoria(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoCategoria()));
        cmbMarca.getSelectionModel().select(buscarMarca(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoMarca()));
        cmbTalla.getSelectionModel().select(buscarTalla(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getCodigoTalla()));
        txtImagen.setText(((Producto)tblProductos.getSelectionModel().getSelectedItem()).getImagen());
    }
    
    public Categoria buscarCategoria(int codigoCategoria)
    {
        Categoria resultado = null;
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarCategorias(?)}");
            procedimiento.setInt(1, codigoCategoria);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next())
            {
                resultado = new Categoria(registro.getInt("codigoCategoria"), registro.getString("descripcion"));
            }
        }catch(SQLException e)
        {
            e.printStackTrace();
        }
        return resultado;
    }
    
    public Marca buscarMarca(int codigoMarca)
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
        }
        return resultado;
    }
    
    public Talla buscarTalla(int codigoTalla)
    {
        Talla resultado = null;
        try
        {
            PreparedStatement procedimiento = Conexion.getInstancia().getConexion().prepareCall("{call sp_BuscarTalla(?)}");
            procedimiento.setInt(1, codigoTalla);
            ResultSet registro = procedimiento.executeQuery();
            while(registro.next())
            {
                resultado = new Talla(registro.getInt("codigoTalla"), registro.getString("descripcion"));
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
                btnImagen.setDisable(true);
                txtDescripcion.setEditable(false);
                cmbCategoria.setDisable(true);
                cmbMarca.setDisable(true);
                cmbTalla.setDisable(true);
                tipoDeOperacion = operaciones.NINGUNO;
                break;
        }
    }
    
    public void imprimirReporte()
    {
        Map parametros = new HashMap();
        parametros.put("_NumeroDocumento", null);
        GenerarReporte.mostrarReporte("ReporteProducto.jasper", "Reporte de Productos", parametros);
    }
    
    public void agregarImagen()
    {
        FileChooser seleccionarArchivo = new FileChooser();
        File archivo = seleccionarArchivo.showOpenDialog(null);
        if(archivo != null)
        {
            txtImagen.setText(archivo.getName());
        }
    }
    
    public void desactivarControles()
    {
        txtDescripcion.setEditable(false);
        cmbCategoria.setDisable(true);
        cmbMarca.setDisable(true);
        cmbTalla.setDisable(true);
        btnImagen.setDisable(true);
        imgProducto.setImage(null);
    }
    
    public void activarControles()
    {
        txtDescripcion.setEditable(true);
        cmbCategoria.setDisable(false);
        cmbMarca.setDisable(false);
        cmbTalla.setDisable(false);
        btnImagen.setDisable(false);
    }
    
    public void limpiarControles()
    {
        txtDescripcion.setText("");
        txtImagen.setText("");
        cmbCategoria.getSelectionModel().clearSelection();
        cmbMarca.getSelectionModel().clearSelection();
        cmbTalla.getSelectionModel().clearSelection();
    }

    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
    public void menuPrincipal()
    {
        escenarioPrincipal.menuPrincipal();
    }
}
