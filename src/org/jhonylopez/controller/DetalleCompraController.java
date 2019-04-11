/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jhonylopez.controller;

import org.jhonylopez.sistema.Principal;
import org.jhonylopez.bean.Compra;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;


/**
 *
 * @author bryan
 */
public class DetalleCompraController implements Initializable {
    
    private Principal escenarioPrincipal;
    private Compra compra;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
       
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }
    
    
    
    public Principal getEscenarioPrincipal() {
        return escenarioPrincipal;
    }

    public void setEscenarioPrincipal(Principal escenarioPrincipal) {
        this.escenarioPrincipal = escenarioPrincipal;
    }
    
}
