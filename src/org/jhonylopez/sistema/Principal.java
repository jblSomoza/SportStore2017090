/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhonylopez.sistema;

import org.jhonylopez.controller.MenuPrincipalController;
import org.jhonylopez.controller.LoginController;
import org.jhonylopez.controller.RegistroController;
import org.jhonylopez.controller.CategoriaController;
import org.jhonylopez.controller.MarcaController;
import org.jhonylopez.controller.TallaController;
import org.jhonylopez.controller.ClienteController;
import org.jhonylopez.controller.TelefonoClienteController;
import org.jhonylopez.controller.ProductoController;
import org.jhonylopez.controller.ProveedorController;
import org.jhonylopez.controller.TelefonoProveedorController;
import org.jhonylopez.controller.EmailProveedorController;
import org.jhonylopez.controller.EmailClienteController;
import org.jhonylopez.controller.CompraController;
import org.jhonylopez.controller.DetalleCompraController;

import org.jhonylopez.bean.Compra;

import javafx.application.Application;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader; 
import javafx.fxml.JavaFXBuilderFactory;
import javafx.scene.layout.AnchorPane;

import java.io.InputStream; 

import static javafx.application.Application.launch;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


/**
 *
 * @author informatica
 */
public class Principal extends Application { 
    private final String PAQUETE_VISTA = "/org/jhonylopez/view/";// Es una variable constante, esta se declara con FINAL 
    private Stage escenarioPrincipal; 
    private Scene escena; 
    
    @Override
    
    public void start(Stage escenarioPrincipal) { 
        this.escenarioPrincipal = escenarioPrincipal;  
        escenarioPrincipal.setTitle("Next Sport");
        menuPrincipal();
        escenarioPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }  
    
    public void menuPrincipal(){ 
    try{
        MenuPrincipalController menuPrincipal = (MenuPrincipalController)cambiarEscena("MenuPrincipalView.fxml", 700, 500); 
        menuPrincipal.setEscenarioPrincipal(this);
    }catch(Exception e) 
    { 
        e.printStackTrace();  
    }
}
    
    public void ventanaLogin()
    {
        try
        {
           LoginController loginController = (LoginController) cambiarEscena("LoginView.fxml", 600, 400);
           loginController.setEscenarioPrincipal(this);
        }catch(Exception e)
        {
            e.printStackTrace();
            e.getMessage();
        }
    }
    
    public void ventanaRegistros()
    {
        try
        {
            RegistroController registroController = (RegistroController) cambiarEscena("IngresoUsuarioView.fxml", 575, 500);
            registroController.setEscenarioPrincipal(this);
        }catch(Exception e)
        {
            e.printStackTrace();
            e.getMessage();
        }
    }
 
    public void ventanaCategorias()
    {  
        try{
            CategoriaController categoriaController = (CategoriaController) cambiarEscena("CategoriaView.fxml", 700, 500); //El cambiar escena es de tipo anchor pane
            categoriaController.setEscenarioPrincipal(this);
        }catch(Exception e)
        { 
            e.printStackTrace(); 
            e.getMessage();
        }
    }
    
    public void ventanaMarcas()
    {  
        try{
            MarcaController marcaController = (MarcaController) cambiarEscena("MarcaView.fxml", 700, 500); //(MarcaController) se hace referencia a si mismo
            marcaController.setEscenarioPrincipal(this);
        }catch(Exception e)
        { 
            e.getMessage();
        }
    }
    
    public void ventanaTallas()
    {
        try
        {
            TallaController tallaController = (TallaController) cambiarEscena("TallaView.fxml", 700, 500);
            tallaController.setEscenarioPrincipal(this);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void ventanaClientes()
    {
        try
        {
            ClienteController clienteController = (ClienteController) cambiarEscena("ClienteView.fxml",800,600);
            clienteController.setEscenarioPrincipal(this);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void ventanaProductos()
    {
        try
        {
            ProductoController productoController = (ProductoController) cambiarEscena("ProductoView.fxml",800,600);
            productoController.setEscenarioPrincipal(this);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void ventanaProveedores()
    {
        try
        {
            ProveedorController proveedorController = (ProveedorController) cambiarEscena("ProveedorView.fxml",800,600);
            proveedorController.setEscenarioPrincipal(this);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void ventanaTelefonoProveedores()
    {
        try
        {
            TelefonoProveedorController telefonoProveedorController = (TelefonoProveedorController) cambiarEscena("TelefonoProveedorView.fxml",800 ,500);
            telefonoProveedorController.setEscenarioPrincipal(this);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void ventanaTelefonoClientes()
    {
        try
        {
            TelefonoClienteController telefonoClienteController = (TelefonoClienteController) cambiarEscena("TelefonoClienteView.fxml", 800, 500);
            telefonoClienteController.setEscenarioPrincipal(this);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void ventanaEmailClientes()
    {
        try
        {
            EmailClienteController emailClienteController = (EmailClienteController) cambiarEscena("EmailClienteView.fxml", 800, 500);
            emailClienteController.setEscenarioPrincipal(this);
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
    
    public void ventanaEmailProveedores()
    {
        try
        {
            EmailProveedorController emailProveedorController = (EmailProveedorController) cambiarEscena("EmailProveedorView.fxml", 800, 500);
            emailProveedorController.setEscenarioPrincipal(this);
        }catch(Exception e)
        {
            e.printStackTrace();
            e.getMessage();
        }
    }
    
    public void ventanaCompras()
    {
        try
        {
            CompraController compraController = (CompraController) cambiarEscena("CompraView.fxml", 800, 600);
            compraController.setEscenarioPrincipal(this);
        }catch(Exception e)
        {
            e.printStackTrace();
            e.getMessage();
        }
    }
    
    public void ventanaDetalleCompras(Compra compra)
    {
        try
        {
            DetalleCompraController detalleCompraController = (DetalleCompraController) cambiarEscena("DetalleCompraView.fxml", 800, 600);
            detalleCompraController.setEscenarioPrincipal(this);
        }catch(Exception e)
        {
            e.printStackTrace();
            e.getMessage();
        }
    }
    
    public Initializable cambiarEscena(String fxml, int ancho, int alto) throws Exception{ //Es un metodo, initializable es una interfaz esta actua cuando le damos f6, throw Exception captura el error
        Initializable resultado = null;   
        FXMLLoader cargadorFXML = new FXMLLoader();  
        InputStream archivo = Principal.class.getResourceAsStream(PAQUETE_VISTA + fxml); //Centro de procesamiento de datos
        cargadorFXML.setBuilderFactory(new JavaFXBuilderFactory()); 
        cargadorFXML.setLocation(Principal.class.getResource(PAQUETE_VISTA)); //Donde esta
        escena = new Scene((AnchorPane)cargadorFXML.load(archivo),ancho,alto); 
        escenarioPrincipal.setScene(escena); 
        escenarioPrincipal.sizeToScene();         
        resultado = (Initializable) cargadorFXML.getController();
        return resultado; 
    }
    
}
